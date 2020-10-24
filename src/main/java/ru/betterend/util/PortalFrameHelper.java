package ru.betterend.util;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.FeatureRegistry;

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
		BlockPos checkPos = bottomCorner.up();
		Direction moveDir = Direction.UP;
		while(valid && !checkPos.equals(bottomCorner)) {
			valid = world.getBlockState(checkPos).isOf(frameBlock);
			if (!valid) {
				switch(moveDir) {
					case UP: {
						checkPos = checkPos.down();
						if (world.getBlockState(checkPos.east()).isOf(frameBlock)) {
							checkPos = checkPos.east();
							moveDir = Direction.EAST;
							valid = true;
						} else if (world.getBlockState(checkPos.south()).isOf(frameBlock)) {
							axis = Direction.Axis.Z;
							checkPos = checkPos.south();
							moveDir = Direction.SOUTH;
							valid = true;
						}
						break;
					}
					case DOWN: {
						checkPos = checkPos.up();
						if (world.getBlockState(checkPos.west()).isOf(frameBlock)) {
							checkPos = checkPos.west();
							moveDir = Direction.WEST;
							valid = true;
						} else if (world.getBlockState(checkPos.north()).isOf(frameBlock)) {
							checkPos = checkPos.north();
							moveDir = Direction.NORTH;
							valid = true;
						}
						break;
					}
					case SOUTH:
					case EAST: {
						checkPos = moveDir.equals(Direction.SOUTH) ? checkPos.north() : checkPos.west();
						if (world.getBlockState(checkPos.down()).isOf(frameBlock)) {
							checkPos = checkPos.down();
							moveDir = Direction.DOWN;
							valid = true;
						}
						break;
					}
					default:
						return false;
				}
				if (!valid) return false;
			} else {
				switch(moveDir) {
					case UP: {
						height++;
						checkPos = checkPos.up();
						break;
					}
					case DOWN: {
						checkPos = checkPos.down();
						break;
					}
					case NORTH: {
						checkPos = checkPos.north();
						break;
					}
					case SOUTH: {
						width++;
						checkPos = checkPos.south();
						break;
					}
					case EAST: {
						width++;
						checkPos = checkPos.east();
						break;
					}
					case WEST: {
						checkPos = checkPos.west();
						break;
					}
				}
			}
		}
		if (width < 4 || height < 5 || width > 40 || height > 40) return false;
		if (axis.equals(Direction.Axis.X)) {
			if(!checkIsAreaEmpty(world, bottomCorner.add(1, 1, 0), topCorner.add(-1, -1, 0))) return false;
		} else {
			if(!checkIsAreaEmpty(world, bottomCorner.add(0, 1, 1), topCorner.add(0, -1, -1))) return false;
		}
		if (valid) {
			if (world.getBlockState(bottomCorner).isOf(BlockRegistry.FLAVOLITE_RUNED)) {
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
		if (up.isOf(frameBlock) && !down.isOf(frameBlock)) {
			if (south.isOf(frameBlock) || east.isOf(frameBlock)) {
				return pos;
			} else if (west.isOf(frameBlock)) {
				return findBottomCorner(world, pos.west(), frameBlock);
			} else if (north.isOf(frameBlock)){
				return findBottomCorner(world, pos.north(), frameBlock);
			}
			return null;
		} else if (down.isOf(frameBlock)) {
			if (west.isOf(frameBlock)) {
				return findBottomCorner(world, pos.west(), frameBlock);
			} else if (north.isOf(frameBlock)) {
				return findBottomCorner(world, pos.north(), frameBlock);
			} else {
				return findBottomCorner(world, pos.down(), frameBlock);
			}
		} else if (west.isOf(frameBlock)) {
			return findBottomCorner(world, pos.west(), frameBlock);
		} else if (north.isOf(frameBlock)) {
			return findBottomCorner(world, pos.north(), frameBlock);
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
		if (!up.isOf(frameBlock) && down.isOf(frameBlock)) {
			if (north.isOf(frameBlock) || west.isOf(frameBlock)) {
				return pos;
			} else if (east.isOf(frameBlock)) {
				return findTopCorner(world, pos.east(), frameBlock);
			} else if (south.isOf(frameBlock)){
				return findTopCorner(world, pos.south(), frameBlock);
			}
			return null;
		} else if (up.isOf(frameBlock)) {
			if (east.isOf(frameBlock)) {
				return findTopCorner(world, pos.east(), frameBlock);
			} else if (south.isOf(frameBlock)){
				return findTopCorner(world, pos.south(), frameBlock);
			} else {
				return findTopCorner(world, pos.up(), frameBlock);
			}
		} else if (east.isOf(frameBlock)) {
			return findTopCorner(world, pos.east(), frameBlock);
		} else if (south.isOf(frameBlock)){
			return findTopCorner(world, pos.south(), frameBlock);
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
		FeatureRegistry.END_PORTAL.configure(axis, width, height, active).getFeatureConfigured().generate(world, world.getChunkManager().getChunkGenerator(), new Random(), pos);
	}
	
	public static void generateEternalPortalFrame(ServerWorld world, BlockPos pos, Direction.Axis axis, int width, int height, boolean active) {
		FeatureRegistry.END_PORTAL_ETERNAL.configure(axis, width, height, active).getFeatureConfigured().generate(world, world.getChunkManager().getChunkGenerator(), new Random(), pos);
	}
	
	public static void generatePortalFrame(ServerWorld world, BlockPos pos, Direction.Axis axis, boolean active) {
		FeatureRegistry.END_PORTAL.configure(axis, active).getFeatureConfigured().generate(world, world.getChunkManager().getChunkGenerator(), new Random(), pos);
	}
	
	public static void generateEternalPortalFrame(ServerWorld world, BlockPos pos, Direction.Axis axis, boolean active) {
		FeatureRegistry.END_PORTAL_ETERNAL.configure(axis, active).getFeatureConfigured().generate(world, world.getChunkManager().getChunkGenerator(), new Random(), pos);
	}
}
