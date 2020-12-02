package ru.betterend.world.features.terrain;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockSulphurCrystal;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.world.features.DefaultFeature;

public class SulphuricLakeFeature extends DefaultFeature {
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(15152);
	private static final Mutable POS = new Mutable();
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig featureConfig) {
		blockPos = getPosOnSurfaceWG(world, blockPos);
		
		if (blockPos.getY() < 57) {
			return false;
		}
		
		double radius = MHelper.randRange(10.0, 20.0, random);
		int dist2 = MHelper.floor(radius * 1.5);
		
		int minX = blockPos.getX() - dist2;
		int maxX = blockPos.getX() + dist2;
		int minZ = blockPos.getZ() - dist2;
		int maxZ = blockPos.getZ() + dist2;
		
		Set<BlockPos> brimstone = Sets.newHashSet();
		for (int x = minX; x <= maxX; x++) {
			POS.setX(x);
			int x2 = x - blockPos.getX();
			x2 *= x2;
			for (int z = minZ; z <= maxZ; z++) {
				POS.setZ(z);
				int z2 = z - blockPos.getZ();
				z2 *= z2;
				double r = radius * (NOISE.eval(x * 0.2, z * 0.2) * 0.25 + 0.75);
				double r2 = r * 1.5;
				r *= r;
				r2 *= r2;
				int dist = x2 + z2;
				if (dist <= r) {
					POS.setY(getYOnSurface(world, x, z) - 1);
					if (world.getBlockState(POS).isIn(EndTags.GEN_TERRAIN)) {
						if (isBorder(world, POS)) {
							if (random.nextInt(8) > 0) {
								brimstone.add(POS.toImmutable());
								if (random.nextBoolean()) {
									brimstone.add(POS.down());
									if (random.nextBoolean()) {
										brimstone.add(POS.down(2));
									}
								}
							}
							else {
								if (!isAbsoluteBorder(world, POS)) {
									BlocksHelper.setWithoutUpdate(world, POS, Blocks.WATER);
									world.getFluidTickScheduler().schedule(POS, Fluids.WATER, 0);
									brimstone.add(POS.down());
									if (random.nextBoolean()) {
										brimstone.add(POS.down(2));
										if (random.nextBoolean()) {
											brimstone.add(POS.down(3));
										}
									}
								}
								else {
									brimstone.add(POS.toImmutable());
									if (random.nextBoolean()) {
										brimstone.add(POS.down());
									}
								}
							}
						}
						else {
							BlocksHelper.setWithoutUpdate(world, POS, Blocks.WATER);
							if (isDeepWater(world, POS)) {
								BlocksHelper.setWithoutUpdate(world, POS.move(Direction.DOWN), Blocks.WATER);
							}
							brimstone.add(POS.down());
							if (random.nextBoolean()) {
								brimstone.add(POS.down(2));
								if (random.nextBoolean()) {
									brimstone.add(POS.down(3));
								}
							}
						}
					}
				}
				else if (dist < r2) {
					POS.setY(getYOnSurface(world, x, z) - 1);
					if (world.getBlockState(POS).isIn(EndTags.GEN_TERRAIN)) {
						brimstone.add(POS.toImmutable());
						if (random.nextBoolean()) {
							brimstone.add(POS.down());
							if (random.nextBoolean()) {
								brimstone.add(POS.down(2));
							}
						}
					}
				}
			}
		}
		
		brimstone.forEach((bpos) -> {
			placeBrimstone(world, bpos, random);
		});
		
		return true;
	}
	
	private boolean isBorder(StructureWorldAccess world, BlockPos pos) {
		int y = pos.getY() + 1;
		for (Direction dir: BlocksHelper.DIRECTIONS) {
			if (getYOnSurface(world, pos.getX() + dir.getOffsetX(), pos.getZ() + dir.getOffsetZ()) < y) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isAbsoluteBorder(StructureWorldAccess world, BlockPos pos) {
		int y = pos.getY() - 2;
		for (Direction dir: BlocksHelper.DIRECTIONS) {
			if (getYOnSurface(world, pos.getX() + dir.getOffsetX() * 3, pos.getZ() + dir.getOffsetZ() * 3) < y) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isDeepWater(StructureWorldAccess world, BlockPos pos) {
		int y = pos.getY() + 1;
		for (Direction dir: BlocksHelper.DIRECTIONS) {
			if (getYOnSurface(world, pos.getX() + dir.getOffsetX(), pos.getZ() + dir.getOffsetZ()) < y
					|| getYOnSurface(world, pos.getX() + dir.getOffsetX() * 2, pos.getZ() + dir.getOffsetZ() * 2) < y
					|| getYOnSurface(world, pos.getX() + dir.getOffsetX() * 3, pos.getZ() + dir.getOffsetZ() * 3) < y) {
				return false;
			}
		}
		return true;
	}
	
	private void placeBrimstone(StructureWorldAccess world, BlockPos pos, Random random) {
		BlockState state = getBrimstone(world, pos);
		BlocksHelper.setWithoutUpdate(world, pos, state);
		if (state.get(BlockProperties.ACTIVATED)) {
			makeShards(world, pos, random);
		}
	}
	
	private BlockState getBrimstone(StructureWorldAccess world, BlockPos pos) {
		for (Direction dir: BlocksHelper.DIRECTIONS) {
			if (world.getBlockState(pos.offset(dir)).isOf(Blocks.WATER)) {
				return EndBlocks.BRIMSTONE.getDefaultState().with(BlockProperties.ACTIVATED, true);
			}
		}
		return EndBlocks.BRIMSTONE.getDefaultState();
	}
	
	private void makeShards(StructureWorldAccess world, BlockPos pos, Random random) {
		for (Direction dir: BlocksHelper.DIRECTIONS) {
			BlockPos side;
			if (random.nextInt(4) == 0 && world.getBlockState((side = pos.offset(dir))).getMaterial().isReplaceable()) {
				BlockState state = EndBlocks.SULPHUR_CRYSTAL.getDefaultState()
						.with(BlockSulphurCrystal.WATERLOGGED, world.getBlockState(side).isOf(Blocks.WATER))
						.with(BlockSulphurCrystal.FACING, dir)
						.with(BlockSulphurCrystal.AGE, random.nextInt(3));
				BlocksHelper.setWithoutUpdate(world, side, state);
			}
		}
	}
}
