package ru.betterend.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.dimension.DimensionType;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.IColorProvider;
import ru.bclib.interfaces.IRenderTyped;
import ru.betterend.interfaces.TeleportingEntity;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndPortals;
import ru.betterend.rituals.EternalRitual;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class EndPortalBlock extends NetherPortalBlock implements IRenderTyped, IColorProvider {
	public static final IntegerProperty PORTAL = EndBlockProperties.PORTAL;

	public EndPortalBlock() {
		super(FabricBlockSettings.copyOf(Blocks.NETHER_PORTAL).resistance(Blocks.BEDROCK.getExplosionResistance()).luminance(15));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(PORTAL);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
		if (random.nextInt(100) == 0) {
			world.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
		}

		double x = pos.getX() + random.nextDouble();
		double y = pos.getY() + random.nextDouble();
		double z = pos.getZ() + random.nextDouble();
		int k = random.nextInt(2) * 2 - 1;
		if (!world.getBlockState(pos.west()).is(this) && !world.getBlockState(pos.east()).is(this)) {
			x = pos.getX() + 0.5D + 0.25D * k;
		}
		else {
			z = pos.getZ() + 0.5D + 0.25D * k;
		}

		world.addParticle(EndParticles.PORTAL_SPHERE, x, y, z, 0, 0, 0);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		return state;
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (world.isClientSide || !validate(entity)) return;
		entity.setPortalCooldown();
		ServerLevel currentWorld = (ServerLevel) world;
		MinecraftServer server = currentWorld.getServer();
		ServerLevel targetWorld = EndPortals.getWorld(server, state.getValue(PORTAL));
		boolean isInEnd = currentWorld.dimension().equals(Level.END);
		ServerLevel destination = isInEnd ? targetWorld : server.getLevel(Level.END);
		BlockPos exitPos = findExitPos(currentWorld, destination, pos, entity);
		if (exitPos == null) return;
		if (entity instanceof ServerPlayer && ((ServerPlayer) entity).isCreative()) {
			((ServerPlayer) entity).teleportTo(destination, exitPos.getX() + 0.5, exitPos.getY(),
					exitPos.getZ() + 0.5, entity.getYRot(), entity.getXRot());
		}
		else {
			((TeleportingEntity) entity).be_setExitPos(exitPos);
			Optional<Entity> teleported = Optional.ofNullable(entity.changeDimension(destination));
			teleported.ifPresent(Entity::setPortalCooldown);
		}
	}

	private boolean validate(Entity entity) {
		return !entity.isPassenger() && !entity.isVehicle() &&
				entity.canChangeDimensions() && !entity.isOnPortalCooldown();
	}

	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.TRANSLUCENT;
	}

	private BlockPos findExitPos(ServerLevel currentWorld, ServerLevel targetWorld, BlockPos currentPos, Entity entity) {
		if (targetWorld == null) return null;
		Registry<DimensionType> registry = targetWorld.registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
		ResourceLocation targetWorldId = targetWorld.dimension().location();
		ResourceLocation currentWorldId = currentWorld.dimension().location();
		double targetMultiplier = Objects.requireNonNull(registry.get(targetWorldId)).coordinateScale();
		double currentMultiplier = Objects.requireNonNull(registry.get(currentWorldId)).coordinateScale();
		double multiplier = targetMultiplier > currentMultiplier ? 1.0 / targetMultiplier : currentMultiplier;
		MutableBlockPos basePos = currentPos.mutable().set(currentPos.getX() * multiplier, currentPos.getY(), currentPos.getZ() * multiplier);
		MutableBlockPos checkPos = basePos.mutable();
		BlockState currentState = currentWorld.getBlockState(currentPos);
		int radius = (EternalRitual.SEARCH_RADIUS >> 4) + 1;
		checkPos = EternalRitual.findBlockPos(targetWorld, checkPos, radius, this, state -> state.is(this) &&
				state.getValue(PORTAL).equals(currentState.getValue(PORTAL)));
		if (checkPos != null) {
			BlockState checkState = targetWorld.getBlockState(checkPos);
			Axis axis = checkState.getValue(AXIS);
			checkPos = findCenter(targetWorld, checkPos, axis);
			Direction frontDir = Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE).getClockWise();
			Direction entityDir = entity.getMotionDirection();
			if (entityDir.getAxis().isVertical()) {
				entityDir = frontDir;
			}
			if (frontDir != entityDir && frontDir.getOpposite() != entityDir) {
				entity.rotate(Rotation.CLOCKWISE_90);
				entityDir = entityDir.getClockWise();
			}
			return checkPos.relative(entityDir);
		}
		return null;
	}

	private MutableBlockPos findCenter(Level world, MutableBlockPos pos, Direction.Axis axis) {
		return findCenter(world, pos, axis, 1);
	}

	private MutableBlockPos findCenter(Level world, MutableBlockPos pos, Direction.Axis axis, int step) {
		if (step > 8) return pos;
		BlockState right, left;
		Direction rightDir, leftDir;
		rightDir = Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE);
		leftDir = rightDir.getOpposite();
		right = world.getBlockState(pos.relative(rightDir));
		left = world.getBlockState(pos.relative(leftDir));
		BlockState down = world.getBlockState(pos.below());
		if (down.is(this)) {
			return findCenter(world, pos.move(Direction.DOWN), axis, step);
		}
		else if (right.is(this) && left.is(this)) {
			return pos;
		}
		else if (right.is(this)) {
			return findCenter(world, pos.move(rightDir), axis, ++step);
		}
		else if (left.is(this)) {
			return findCenter(world, pos.move(leftDir), axis, ++step);
		}
		return pos;
	}

	@Override
	public BlockColor getProvider() {
		return (state, world, pos, tintIndex) -> EndPortals.getColor(state.getValue(PORTAL));
	}

	@Override
	public ItemColor getItemProvider() {
		return (stack, tintIndex) -> EndPortals.getColor(0);
	}
}
