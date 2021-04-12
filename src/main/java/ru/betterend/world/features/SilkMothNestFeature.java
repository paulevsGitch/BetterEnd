package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;

public class SilkMothNestFeature extends DefaultFeature {
	private static final MutableBlockPos POS = new MutableBlockPos();

	private boolean canGenerate(WorldGenLevel world, BlockPos pos) {
		BlockState state = world.getBlockState(pos.up());
		if (state.isIn(BlockTags.LEAVES) || state.isIn(BlockTags.LOGS)) {
			state = world.getBlockState(pos);
			if ((state.isAir() || state.is(EndBlocks.TENANEA_OUTER_LEAVES)) && world.isAir(pos.below())) {
				for (Direction dir : BlocksHelper.HORIZONTAL) {
					if (world.getBlockState(pos.below().offset(dir)).getMaterial().blocksMovement()) {
						return false;
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, Random random, BlockPos center,
			NoneFeatureConfiguration featureConfig) {
		int maxY = world.getTopY(Heightmap.Type.WORLD_SURFACE, center.getX(), center.getZ());
		int minY = BlocksHelper.upRay(world, new BlockPos(center.getX(), 0, center.getZ()), maxY);
		POS.set(center);
		for (int y = maxY; y > minY; y--) {
			POS.setY(y);
			if (canGenerate(world, POS)) {
				Direction dir = BlocksHelper.randomHorizontal(random);
				BlocksHelper.setWithoutUpdate(world, POS, EndBlocks.SILK_MOTH_NEST.defaultBlockState()
						.with(Properties.HORIZONTAL_FACING, dir).with(BlockProperties.ACTIVE, false));
				POS.setY(y - 1);
				BlocksHelper.setWithoutUpdate(world, POS,
						EndBlocks.SILK_MOTH_NEST.defaultBlockState().with(Properties.HORIZONTAL_FACING, dir));
				return true;
			}
		}
		return false;
	}
}
