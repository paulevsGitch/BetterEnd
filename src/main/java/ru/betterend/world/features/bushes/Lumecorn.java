package ru.betterend.world.features.bushes;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.bclib.util.MHelper;
import ru.betterend.blocks.BlockProperties.LumecornShape;
import ru.betterend.blocks.LumecornBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.world.features.DefaultFeature;

public class Lumecorn extends DefaultFeature {
	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, Random random, BlockPos pos,
			NoneFeatureConfiguration config) {
		if (!world.getBlockState(pos.below()).getBlock().is(EndTags.END_GROUND))
			return false;

		int height = MHelper.randRange(4, 7, random);
		MutableBlockPos mut = new MutableBlockPos().set(pos);
		for (int i = 1; i < height; i++) {
			mut.move(Direction.UP);
			if (!world.isEmptyBlock(mut)) {
				return false;
			}
		}
		mut.set(pos);
		BlockState topMiddle = EndBlocks.LUMECORN.defaultBlockState().setValue(LumecornBlock.SHAPE,
				LumecornShape.LIGHT_TOP_MIDDLE);
		BlockState middle = EndBlocks.LUMECORN.defaultBlockState().setValue(LumecornBlock.SHAPE,
				LumecornShape.LIGHT_MIDDLE);
		BlockState bottom = EndBlocks.LUMECORN.defaultBlockState().setValue(LumecornBlock.SHAPE,
				LumecornShape.LIGHT_BOTTOM);
		BlockState top = EndBlocks.LUMECORN.defaultBlockState().setValue(LumecornBlock.SHAPE, LumecornShape.LIGHT_TOP);
		if (height == 4) {
			BlocksHelper.setWithoutUpdate(world, mut,
					EndBlocks.LUMECORN.defaultBlockState().setValue(LumecornBlock.SHAPE, LumecornShape.BOTTOM_SMALL));
			BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), bottom);
			BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), topMiddle);
			BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), top);
			return true;
		}
		if (random.nextBoolean()) {
			BlocksHelper.setWithoutUpdate(world, mut,
					EndBlocks.LUMECORN.defaultBlockState().setValue(LumecornBlock.SHAPE, LumecornShape.BOTTOM_SMALL));
		} else {
			BlocksHelper.setWithoutUpdate(world, mut,
					EndBlocks.LUMECORN.defaultBlockState().setValue(LumecornBlock.SHAPE, LumecornShape.BOTTOM_BIG));
			BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP),
					EndBlocks.LUMECORN.defaultBlockState().setValue(LumecornBlock.SHAPE, LumecornShape.MIDDLE));
			height--;
		}
		BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), bottom);
		for (int i = 4; i < height; i++) {
			BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), middle);
		}
		BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), topMiddle);
		BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), top);
		return false;
	}
}
