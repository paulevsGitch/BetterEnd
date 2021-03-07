package ru.betterend.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.Direction.AxisDirection;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IColorProvider;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.interfaces.TeleportingEntity;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndPortals;

import java.util.Objects;
import java.util.Random;

public class EndPortalBlock extends NetherPortalBlock implements IRenderTypeable, IColorProvider {
	public static final IntProperty PORTAL = BlockProperties.PORTAL;
	
	public EndPortalBlock() {
		super(FabricBlockSettings.copyOf(Blocks.NETHER_PORTAL).resistance(Blocks.BEDROCK.getBlastResistance()).luminance(state -> 12));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(PORTAL);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (random.nextInt(100) == 0) {
			world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
		}

		double x = pos.getX() + random.nextDouble();
		double y = pos.getY() + random.nextDouble();
		double z = pos.getZ() + random.nextDouble();
		int k = random.nextInt(2) * 2 - 1;
		if (!world.getBlockState(pos.west()).isOf(this) && !world.getBlockState(pos.east()).isOf(this)) {
			x = pos.getX() + 0.5D + 0.25D * k;
		} else {
			z = pos.getZ() + 0.5D + 0.25D * k;
		}

		world.addParticle(EndParticles.PORTAL_SPHERE, x, y, z, 0, 0, 0);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		return state;
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (world instanceof ServerWorld && !entity.hasVehicle() && !entity.hasPassengers() && entity.canUsePortals()) {
			if (entity.hasNetherPortalCooldown()) return;
			ServerWorld currentWorld = (ServerWorld) world;
			MinecraftServer server = currentWorld.getServer();
			ServerWorld targetWorld = EndPortals.getWorld(server, state.get(PORTAL));
			boolean isTarget = world.getRegistryKey().equals(targetWorld.getRegistryKey());
			ServerWorld destination = isTarget ? server.getWorld(World.END) : targetWorld;
			BlockPos exitPos = findExitPos(currentWorld, destination, pos, entity);
			if (exitPos == null) return;
			if (entity instanceof ServerPlayerEntity) {
				ServerPlayerEntity player = (ServerPlayerEntity) entity;
				this.teleportPlayer(player, destination, exitPos);
			} else {
				TeleportingEntity teleEntity = (TeleportingEntity) entity;
				teleEntity.beSetExitPos(exitPos);
				Entity teleported = entity.moveToWorld(destination);
				if (teleported != null) {
					teleported.resetNetherPortalCooldown();
				}
			}
		}
	}

	private void teleportPlayer(ServerPlayerEntity player, ServerWorld destination, BlockPos exitPos) {
		if (player.isCreative()) {
			player.teleport(destination, exitPos.getX() + 0.5, exitPos.getY(), exitPos.getZ() + 0.5, player.yaw, player.pitch);
		} else {
			TeleportingEntity teleEntity = (TeleportingEntity) player;
			teleEntity.beSetExitPos(exitPos);
			player.moveToWorld(destination);
		}
		player.resetNetherPortalCooldown();
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.TRANSLUCENT;
	}
	
	private BlockPos findExitPos(ServerWorld current, ServerWorld target, BlockPos pos, Entity entity) {
		if (target == null) return null;

		Registry<DimensionType> registry = target.getRegistryManager().getDimensionTypes();
		Identifier targetWorldId = target.getRegistryKey().getValue();
		Identifier currentWorldId = current.getRegistryKey().getValue();
		double targetMultiplier = Objects.requireNonNull(registry.get(targetWorldId)).getCoordinateScale();
		double currentMultiplier = Objects.requireNonNull(registry.get(currentWorldId)).getCoordinateScale();
		double multiplier = targetMultiplier > currentMultiplier ? currentMultiplier / targetMultiplier : currentMultiplier;
		BlockPos.Mutable basePos = pos.mutableCopy().set(pos.getX() * multiplier, pos.getY(), pos.getZ() * multiplier);
		System.out.println(basePos);
		Direction direction = Direction.EAST;
		BlockPos.Mutable checkPos = basePos.mutableCopy();
		for (int step = 1; step < 128; step++) {
			for (int i = 0; i < (step >> 1); i++) {
				Chunk chunk = target.getChunk(checkPos);
				if (chunk != null) {
					int surfaceY = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE, checkPos.getX() & 15, checkPos.getZ() & 15);
					int motionY = chunk.sampleHeightmap(Heightmap.Type.MOTION_BLOCKING, checkPos.getX() & 15, checkPos.getZ() & 15);
					int ceil = Math.max(surfaceY, motionY) + 1;
					if (ceil > 5) {
						checkPos.setY(ceil);
						while (checkPos.getY() > 5) {
							BlockState state = target.getBlockState(checkPos);
							if (state.isOf(this)) {
								Axis axis = state.get(AXIS);
								checkPos = findCenter(target, checkPos, axis);

								Direction frontDir = Direction.from(axis, AxisDirection.POSITIVE).rotateYClockwise();
								Direction entityDir = entity.getMovementDirection();
								if (entityDir.getAxis().isVertical()) {
									entityDir = frontDir;
								}

								if (frontDir != entityDir && frontDir.getOpposite() != entityDir) {
									entity.applyRotation(BlockRotation.CLOCKWISE_90);
									entityDir = entityDir.rotateYClockwise();
								}
								return checkPos.offset(entityDir);
							}
							checkPos.move(Direction.DOWN);
						}
					}
				}
				checkPos.move(direction);
			}
			direction = direction.rotateYClockwise();
		}
		return null;
	}
	
	private BlockPos.Mutable findCenter(World world, BlockPos.Mutable pos, Direction.Axis axis) {
		return this.findCenter(world, pos, axis, 1);
	}
	
	private BlockPos.Mutable findCenter(World world, BlockPos.Mutable pos, Direction.Axis axis, int step) {
		if (step > 8) return pos;
		BlockState right, left;
		Direction rightDir, leftDir;
		rightDir = Direction.from(axis, AxisDirection.POSITIVE);
		leftDir = rightDir.getOpposite();
		right = world.getBlockState(pos.offset(rightDir));
		left = world.getBlockState(pos.offset(leftDir));
		BlockState down = world.getBlockState(pos.down());
		if (down.isOf(this)) {
			return findCenter(world, pos.move(Direction.DOWN), axis, step);
		} else if (right.isOf(this) && left.isOf(this)) {
			return pos;
		} else if (right.isOf(this)) {
			return findCenter(world, pos.move(rightDir), axis, ++step);
		} else if (left.isOf(this)) {
			return findCenter(world, pos.move(leftDir), axis, ++step);
		}
		return pos;
	}

	@Override
	public BlockColorProvider getProvider() {
		return (state, world, pos, tintIndex) -> EndPortals.getColor(state.get(PORTAL));
	}

	@Override
	public ItemColorProvider getItemProvider() {
		return (stack, tintIndex) -> EndPortals.getColor(0);
	}
}
