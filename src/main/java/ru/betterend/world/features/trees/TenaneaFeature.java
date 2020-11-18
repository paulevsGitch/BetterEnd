package ru.betterend.world.features.trees;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.world.features.DefaultFeature;

public class TenaneaFeature extends DefaultFeature {
	private static final Vec3i[] DIRECTIONS;
	private static final Vec3i UP;
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		branch(world, pos, UP, 100, random);
		return true;
	}
	
	private void branch(StructureWorldAccess world, BlockPos pos, Vec3i dir, int length, Random random) {
		Mutable mut = new Mutable().set(pos);
		int upCount = 0;
		for (int i = 0; i < length; i++) {
			MHelper.shuffle(DIRECTIONS, random);
			boolean up = false;
			
			BlocksHelper.setWithoutUpdate(world, mut, Blocks.DIAMOND_BLOCK);
			
			for (Direction hor: BlocksHelper.HORIZONTAL) {
				if (!isReplaceable(world.getBlockState(mut.offset(hor)))) {
					upCount = -1;
					up = true;
					break;
				}
			}
			if (up) {
				upCount ++;
				mut.move(Direction.UP);
				if (upCount > 8) {
					break;
				}
				continue;
			}
			else {
				up = true;
				for (Vec3i d: DIRECTIONS) {
					BlockPos offseted = mut.add(d.getX(), d.getY(), d.getZ());
					if (isReplaceable(world.getBlockState(offseted))) {
						int dist = BlocksHelper.raycastSqr(world, offseted, d.getX(), d.getY(), d.getZ(), 18);
						if (dist < 64) {
							mut.move(d.getX(), d.getY(), d.getZ());
							upCount = 0;
							up = false;
							break;
						}
					}
				}
				if (up) {
					upCount ++;
					mut.move(Direction.UP);
					if (upCount > 8) {
						break;
					}
				}
			}
			
			if (!world.getBlockState(mut).getMaterial().isReplaceable()) {
				boolean br = true;
				for (Direction hor: BlocksHelper.HORIZONTAL) {
					if (isReplaceable(world.getBlockState(mut.offset(hor)))) {
						mut.move(hor);
						br = false;
					}
				}
				if (br) {
					break;
				}
			}
		}
	}
	
	private boolean isReplaceable(BlockState state) {
		return state.getMaterial().isReplaceable();
	}
	
	static {
		DIRECTIONS = new Vec3i[] {
			new Vec3i(0, 0, 1),
			new Vec3i(0, 0, -1),
			new Vec3i(1, 0, 0),
			new Vec3i(-1, 0, 0),
			new Vec3i(1, 0, 1),
			new Vec3i(1, 0, -1),
			new Vec3i(-1, 0, 1),
			new Vec3i(-1, 0, -1),
			new Vec3i(1, 1, 1),
			new Vec3i(1, 1, -1),
			new Vec3i(-1, 1, 1),
			new Vec3i(-1, 1, -1)
		};
		
		UP = new Vec3i(0, 1, 0);
	}
}
