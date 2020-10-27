package ru.betterend.util;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;

public class PortalFrameHelper {
	
	public static boolean checkPortalFrame(ServerWorld world, BlockPos pos, Block frameBlock) {
		if (world == null || pos == null) return false;
		if (!world.getBlockState(pos).isOf(frameBlock)) return false;
		BlockPos bottomCorner = findBottomCorner(world, pos, frameBlock);
		BlockPos topCorner = findTopCorner(world, pos, frameBlock);
		if (bottomCorner == null || topCorner == null) return false;
		boolean valid = true;
		int width = 1, height = 1;
		Direction.Axis axis = Direction.Axis.X;
		BlockPos.Mutable checkPos = bottomCorner.up().mutableCopy();
		Direction moveDir = Direction.UP;
		while(valid && !checkPos.equals(bottomCorner)) {
			valid = world.getBlockState(checkPos).isOf(frameBlock);
			if (!valid) {
				checkPos = checkPos.move(moveDir.getOpposite());
				switch(moveDir) {
					case UP: {
						if (world.getBlockState(checkPos.east()).isOf(frameBlock)) {
							moveDir = Direction.EAST;
							valid = true;
						} else if (world.getBlockState(checkPos.south()).isOf(frameBlock)) {
							axis = Direction.Axis.Z;
							moveDir = Direction.SOUTH;
							valid = true;
						}
						break;
					}
					case DOWN: {
						if (world.getBlockState(checkPos.west()).isOf(frameBlock)) {
							moveDir = Direction.WEST;
							valid = true;
						} else if (world.getBlockState(checkPos.north()).isOf(frameBlock)) {
							moveDir = Direction.NORTH;
							valid = true;
						}
						break;
					}
					case SOUTH:
					case EAST: {
						if (world.getBlockState(checkPos.down()).isOf(frameBlock)) {
							moveDir = Direction.DOWN;
							valid = true;
						}
						break;
					}
					default:
						return false;
				}
				if (!valid) return false;
				checkPos = checkPos.move(moveDir);
			} else {
				if (moveDir.equals(Direction.EAST) || moveDir.equals(Direction.SOUTH)) {
					width++;
				} else if (moveDir.equals(Direction.UP)) {
					height++;
				}
				checkPos = checkPos.move(moveDir);
			}
		}
		if (width < 4 || height < 5 || width > 20 || height > 25) return false;
		if (axis.equals(Direction.Axis.X)) {
			if(!checkIsAreaEmpty(world, bottomCorner.add(1, 1, 0), topCorner.add(-1, -1, 0))) return false;
		} else {
			if(!checkIsAreaEmpty(world, bottomCorner.add(0, 1, 1), topCorner.add(0, -1, -1))) return false;
		}
		if (valid) {
			if (world.getBlockState(bottomCorner).isOf(EndBlocks.FLAVOLITE_RUNED)) {
				generatePortalFrame(world, bottomCorner, axis, width, height, true);
			} else {
				generateEternalPortalFrame(world, bottomCorner, axis, width, height, true);
			}
		}
		return valid;
	}
	
	public static BlockPos findBottomCorner(World world, BlockPos pos, Block frameBlock) {
		BlockState up = world.getBlockState(pos.up());
		BlockState down = world.getBlockState(pos.down());
		BlockState north = world.getBlockState(pos.north());
		BlockState south = world.getBlockState(pos.south());
		BlockState west = world.getBlockState(pos.west());
		BlockState east = world.getBlockState(pos.east());
		BlockPos.Mutable mutable = pos instanceof BlockPos.Mutable ? (BlockPos.Mutable) pos : pos.mutableCopy();
		if (up.isOf(frameBlock) && !down.isOf(frameBlock)) {
			if (south.isOf(frameBlock) || east.isOf(frameBlock)) {
				return pos.toImmutable();
			} else if (west.isOf(frameBlock)) {
				return findBottomCorner(world, mutable.move(Direction.WEST), frameBlock);
			} else if (north.isOf(frameBlock)){
				return findBottomCorner(world, mutable.move(Direction.NORTH), frameBlock);
			}
			return null;
		} else if (down.isOf(frameBlock)) {
			if (west.isOf(frameBlock)) {
				return findBottomCorner(world, mutable.move(Direction.WEST), frameBlock);
			} else if (north.isOf(frameBlock)) {
				return findBottomCorner(world, mutable.move(Direction.NORTH), frameBlock);
			} else {
				return findBottomCorner(world, mutable.move(Direction.DOWN), frameBlock);
			}
		} else if (west.isOf(frameBlock)) {
			return findBottomCorner(world, mutable.move(Direction.WEST), frameBlock);
		} else if (north.isOf(frameBlock)) {
			return findBottomCorner(world, mutable.move(Direction.NORTH), frameBlock);
		}
		return null;
	}
	
	public static BlockPos findTopCorner(World world, BlockPos pos, Block frameBlock) {
		BlockState up = world.getBlockState(pos.up());
		BlockState down = world.getBlockState(pos.down());
		BlockState north = world.getBlockState(pos.north());
		BlockState south = world.getBlockState(pos.south());
		BlockState west = world.getBlockState(pos.west());
		BlockState east = world.getBlockState(pos.east());
		BlockPos.Mutable mutable = pos instanceof BlockPos.Mutable ? (BlockPos.Mutable) pos : pos.mutableCopy();
		if (!up.isOf(frameBlock) && down.isOf(frameBlock)) {
			if (north.isOf(frameBlock) || west.isOf(frameBlock)) {
				return pos.toImmutable();
			} else if (east.isOf(frameBlock)) {
				return findTopCorner(world, mutable.move(Direction.EAST), frameBlock);
			} else if (south.isOf(frameBlock)){
				return findTopCorner(world, mutable.move(Direction.SOUTH), frameBlock);
			}
			return null;
		} else if (up.isOf(frameBlock)) {
			if (east.isOf(frameBlock)) {
				return findTopCorner(world, mutable.move(Direction.EAST), frameBlock);
			} else if (south.isOf(frameBlock)){
				return findTopCorner(world, mutable.move(Direction.SOUTH), frameBlock);
			} else {
				return findTopCorner(world, mutable.move(Direction.UP), frameBlock);
			}
		} else if (east.isOf(frameBlock)) {
			return findTopCorner(world, mutable.move(Direction.EAST), frameBlock);
		} else if (south.isOf(frameBlock)){
			return findTopCorner(world, mutable.move(Direction.SOUTH), frameBlock);
		}
		return null;
	}
	
	private static boolean checkIsAreaEmpty(World world, BlockPos bottom, BlockPos top) {
		boolean valid = true;
		for (BlockPos current : BlockPos.iterate(bottom, top)) {
			BlockState state = world.getBlockState(current);
			valid &= state.isAir();
		}
		return valid;
	}

	public static void generatePortalFrame(ServerWorld world, BlockPos pos, Direction.Axis axis, int width, int height, boolean active) {
		EndFeatures.END_PORTAL.configure(axis, width, height, active).getFeatureConfigured().generate(world, world.getChunkManager().getChunkGenerator(), new Random(), pos);
	}
	
	public static void generateEternalPortalFrame(ServerWorld world, BlockPos pos, Direction.Axis axis, int width, int height, boolean active) {
		EndFeatures.END_PORTAL_ETERNAL.configure(axis, width, height, active).getFeatureConfigured().generate(world, world.getChunkManager().getChunkGenerator(), new Random(), pos);
	}
	
	public static void generatePortalFrame(ServerWorld world, BlockPos pos, Direction.Axis axis, boolean active) {
		EndFeatures.END_PORTAL.configure(axis, active).getFeatureConfigured().generate(world, world.getChunkManager().getChunkGenerator(), new Random(), pos);
	}
	
	public static void generateEternalPortalFrame(ServerWorld world, BlockPos pos, Direction.Axis axis, boolean active) {
		EndFeatures.END_PORTAL_ETERNAL.configure(axis, active).getFeatureConfigured().generate(world, world.getChunkManager().getChunkGenerator(), new Random(), pos);
	}
}
