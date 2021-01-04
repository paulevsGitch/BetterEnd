package ru.betterend.blocks;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.AxisDirection;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.DimensionType;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.interfaces.TeleportingEntity;
import ru.betterend.registry.EndParticles;

public class EndPortalBlock extends NetherPortalBlock implements IRenderTypeable {
	public EndPortalBlock() {
		super(FabricBlockSettings.copyOf(Blocks.NETHER_PORTAL).resistance(Blocks.BEDROCK.getBlastResistance()).luminance(state -> {
			return 12;
		}));
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
			TeleportingEntity teleEntity = TeleportingEntity.class.cast(entity);
			if (teleEntity.hasCooldown()) return;
			boolean isOverworld = world.getRegistryKey().equals(World.OVERWORLD);
			ServerWorld destination = ((ServerWorld) world).getServer().getWorld(isOverworld ? World.END : World.OVERWORLD);
			BlockPos exitPos = this.findExitPos(destination, pos, entity);
			if (exitPos == null) return;
			if (entity instanceof ServerPlayerEntity) {
				ServerPlayerEntity player = (ServerPlayerEntity) entity;
				player.teleport(destination, exitPos.getX() + 0.5D, exitPos.getY(), exitPos.getZ() + 0.5D, entity.yaw, entity.pitch);
				teleEntity.beSetCooldown(player.isCreative() ? 50 : 300);
			} else {
				teleEntity.beSetExitPos(exitPos);
				entity.moveToWorld(destination);
				teleEntity.beSetCooldown(300);
			}
		}
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.TRANSLUCENT;
	}
	
	private BlockPos findExitPos(ServerWorld world, BlockPos pos, Entity entity) {
		Registry<DimensionType> registry = world.getRegistryManager().getDimensionTypes();
		double mult = registry.get(DimensionType.THE_END_ID).getCoordinateScale();
		BlockPos.Mutable basePos;
		if (world.getRegistryKey().equals(World.OVERWORLD)) {
			basePos = pos.mutableCopy().set(pos.getX() / mult, pos.getY(), pos.getZ() / mult);
		} else {
			basePos = pos.mutableCopy().set(pos.getX() * mult, pos.getY(), pos.getZ() * mult);
		}
		Direction direction = Direction.EAST;
		BlockPos.Mutable checkPos = basePos.mutableCopy();
		for (int step = 1; step < 64; step++) {
			for (int i = 0; i < (step >> 1); i++) {
				checkPos.setY(5);
				int ceil = world.getChunk(basePos).sampleHeightmap(Heightmap.Type.WORLD_SURFACE, checkPos.getX(), checkPos.getZ()) + 1;
				if (ceil < 5) continue;
				while(checkPos.getY() < ceil) {
					BlockState state = world.getBlockState(checkPos);
					if(state.isOf(this)) {
						int offStep;
						checkPos = this.findCenter(world, checkPos, state.get(AXIS));
						if (state.get(AXIS).equals(Direction.Axis.X)) {
							if (entity.getMovementDirection().getAxis() == Direction.Axis.X) {
								offStep = entity.getMovementDirection() == Direction.EAST ? 1 : -1;
								float rotation = entity.applyRotation(BlockRotation.CLOCKWISE_90);
								entity.yaw = rotation;
							} else {
								offStep = entity.getMovementDirection() == Direction.NORTH ? -1 : 1;
							}
							return checkPos.add(0, 0, offStep);
						} else {
							if (entity.getMovementDirection().getAxis() == Direction.Axis.Z) {
								offStep = entity.getMovementDirection() == Direction.SOUTH ? -1 : 1;
								float rotation = entity.applyRotation(BlockRotation.CLOCKWISE_90);
								entity.yaw = rotation;
							} else {
								offStep = entity.getMovementDirection() == Direction.EAST ? 1 : -1;
							}
							return checkPos.add(offStep, 0, 0);
						}
					}
					checkPos.move(Direction.UP);
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
		if (step > 21) return pos;
		
		BlockState right, left;
		Direction rightDir, leftDir;
		rightDir = Direction.from(axis, AxisDirection.POSITIVE);
		leftDir = rightDir.getOpposite();
		right = world.getBlockState(pos.offset(rightDir));
		left = world.getBlockState(pos.offset(leftDir));
		BlockState down = world.getBlockState(pos.down());
		if (down.isOf(this)) {
			return findCenter(world, pos.move(Direction.DOWN), axis, ++step);
		} else if (right.isOf(this) && left.isOf(this)) {
			return pos;
		} else if (right.isOf(this)) {
			return findCenter(world, pos.move(rightDir), axis, ++step);
		} else if (left.isOf(this)) {
			return findCenter(world, pos.move(leftDir), axis, ++step);
		}
		return pos;
	}
}
