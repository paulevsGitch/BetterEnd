package ru.betterend.world.features.terrain;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.Heightmap;
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

public class SulphuricCaveFeature extends DefaultFeature {
	private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
	private static final BlockState WATER = Blocks.WATER.getDefaultState();
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		int radius = MHelper.randRange(10, 30, random);
		int bottom = BlocksHelper.upRay(world, new BlockPos(pos.getX(), 0, pos.getZ()), 32) + radius + 5;
		int top = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, pos.getX(), pos.getZ()) - radius - 5;
		
		if (top <= bottom) {
			return false;
		}
		
		Mutable bpos = new Mutable();
		pos = new BlockPos(pos.getX(), MHelper.randRange(bottom, top, random), pos.getZ());
		
		OpenSimplexNoise noise = new OpenSimplexNoise(MHelper.getSeed(534, pos.getX(), pos.getZ()));
		
		int x1 = pos.getX() - radius - 5;
		int z1 = pos.getZ() - radius - 5;
		int x2 = pos.getX() + radius + 5;
		int z2 = pos.getZ() + radius + 5;
		int y1 = MHelper.floor(pos.getY() - (radius + 5) / 1.6);
		int y2 = MHelper.floor(pos.getY() + (radius + 5) / 1.6);
		
		double hr = radius * 0.75;
		double nr = radius * 0.25;
		
		Set<BlockPos> brimstone = Sets.newHashSet();
		BlockState rock = EndBlocks.SULPHURIC_ROCK.stone.getDefaultState();
		int waterLevel = pos.getY() + MHelper.randRange(MHelper.floor(radius * 0.8), radius, random);
		for (int x = x1; x <= x2; x++) {
			int xsq = x - pos.getX();
			xsq *= xsq;
			bpos.setX(x);
			for (int z = z1; z <= z2; z++) {
				int zsq = z - pos.getZ();
				zsq *= zsq;
				bpos.setZ(z);
				for (int y = y1; y <= y2; y++) {
					int ysq = y - pos.getY();
					ysq *= 1.6;
					ysq *= ysq;
					bpos.setY(y);
					double r = noise.eval(x * 0.1, y * 0.1, z * 0.1) * nr + hr;
					double r2 = r + 5;
					double dist = xsq + ysq + zsq;
					if (dist < r * r) {
						BlockState state = world.getBlockState(bpos);
						if (isReplaceable(state)) {
							BlocksHelper.setWithoutUpdate(world, bpos, y < waterLevel ? WATER : CAVE_AIR);
						}
					}
					else if (dist < r2 * r2) {
						if (world.getBlockState(bpos).isIn(EndTags.GEN_TERRAIN)) {
							double v = noise.eval(x * 0.1, y * 0.1, z * 0.1) + noise.eval(x * 0.03, y * 0.03, z * 0.03) * 0.5;
							if (v < 0) {
								brimstone.add(bpos.toImmutable());
							}
							else {
								BlocksHelper.setWithoutUpdate(world, bpos, rock);
							}
						}
					}
				}
			}
		}
		brimstone.forEach((blockPos) -> {
			placeBrimstone(world, blockPos, random);
		});
		
		BlocksHelper.fixBlocks(world, new BlockPos(x1, y1, z1), new BlockPos(x2, y2, z2));
		
		return true;
	}
	
	private boolean isReplaceable(BlockState state) {
		return state.isIn(EndTags.GEN_TERRAIN)
				|| state.isOf(EndBlocks.SULPHUR_CRYSTAL)
				|| state.getMaterial().isReplaceable()
				|| state.getMaterial().equals(Material.PLANT)
				|| state.getMaterial().equals(Material.LEAVES);
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
			if (random.nextInt(3) == 0 && world.getBlockState((side = pos.offset(dir))).isOf(Blocks.WATER)) {
				BlockState state = EndBlocks.SULPHUR_CRYSTAL.getDefaultState()
						.with(BlockSulphurCrystal.WATERLOGGED, true)
						.with(BlockSulphurCrystal.FACING, dir)
						.with(BlockSulphurCrystal.AGE, random.nextInt(3));
				BlocksHelper.setWithoutUpdate(world, side, state);
			}
		}
	}
}
