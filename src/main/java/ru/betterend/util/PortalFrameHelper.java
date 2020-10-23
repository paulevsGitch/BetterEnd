package ru.betterend.util;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import ru.betterend.registry.FeatureRegistry;

public class PortalFrameHelper {
	
	public static boolean checkPortalFrame(World world, BlockPos pos, Block frameBlock) {
		if (world == null || pos == null) return false;
		if (!world.getBlockState(pos).isOf(frameBlock)) return false;
		BlockPos bottomCorner = findBottomCorner(world, pos, frameBlock);
		if (bottomCorner == null) return false;
		boolean valid = true;
		BlockPos checkPos = bottomCorner.up();
		Direction moveDir = Direction.UP;
		while(!checkPos.equals(bottomCorner)) {
			while(valid && !checkPos.equals(bottomCorner)) {
				valid = world.getBlockState(checkPos).isOf(frameBlock);
				if (!valid) {
					switch(moveDir) {
						case UP: {
							if (world.getBlockState(checkPos.east()).isOf(frameBlock)) {
								checkPos = checkPos.east();
								moveDir = Direction.EAST;
								valid = true;
							} else if (world.getBlockState(checkPos.north()).isOf(frameBlock)) {
								checkPos = checkPos.north();
								moveDir = Direction.NORTH;
								valid = true;
							}
							break;
						}
						case DOWN: {
							if (world.getBlockState(checkPos.west()).isOf(frameBlock)) {
								checkPos = checkPos.west();
								moveDir = Direction.WEST;
								valid = true;
							} else if (world.getBlockState(checkPos.south()).isOf(frameBlock)) {
								checkPos = checkPos.south();
								moveDir = Direction.SOUTH;
								valid = true;
							}
							break;
						}
						case NORTH:
						case EAST: {
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
							checkPos = checkPos.south();
							break;
						}
						case EAST: {
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
		}
		return valid;
	}
	
	private static BlockPos findBottomCorner(World world, BlockPos pos, Block frameBlock) {
		BlockState up = world.getBlockState(pos.up());
		BlockState down = world.getBlockState(pos.down());
		BlockState north = world.getBlockState(pos.north());
		BlockState south = world.getBlockState(pos.south());
		BlockState west = world.getBlockState(pos.west());
		BlockState east = world.getBlockState(pos.east());
		if (up.isOf(frameBlock) && !down.isOf(frameBlock)) {
			if (north.isOf(frameBlock) || east.isOf(frameBlock)) {
				return pos;
			} else if (west.isOf(frameBlock)) {
				return findBottomCorner(world, pos.west(), frameBlock);
			} else if (south.isOf(frameBlock)){
				return findBottomCorner(world, pos.south(), frameBlock);
			}
			return null;
		} else if (down.isOf(frameBlock)) {
			if (west.isOf(frameBlock)) {
				return findBottomCorner(world, pos.west(), frameBlock);
			} else if (south.isOf(frameBlock)) {
				return findBottomCorner(world, pos.south(), frameBlock);
			} else {
				return findBottomCorner(world, pos.down(), frameBlock);
			}
		} else if (west.isOf(frameBlock)) {
			return findBottomCorner(world, pos.west(), frameBlock);
		} else if (south.isOf(frameBlock)) {
			return findBottomCorner(world, pos.south(), frameBlock);
		}
		return null;
	}

	public static void generatePortalFrame(ServerWorld world, BlockPos pos, Direction.Axis axis, boolean active) {
		FeatureRegistry.END_PORTAL.configure(axis, active).getFeatureConfigured().generate(world, world.getChunkManager().getChunkGenerator(), new Random(), pos);
	}
	
	public static void generateEternalPortalFrame(ServerWorld world, BlockPos pos, Direction.Axis axis, boolean active) {
		FeatureRegistry.END_PORTAL_ETERNAL.configure(axis, active).getFeatureConfigured().generate(world, world.getChunkManager().getChunkGenerator(), new Random(), pos);
	}
}
