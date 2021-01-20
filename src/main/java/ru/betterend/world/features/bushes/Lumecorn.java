package ru.betterend.world.features.bushes;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.blocks.BlockProperties.LumecornShape;
import ru.betterend.blocks.LumecornBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.world.features.DefaultFeature;

public class Lumecorn extends DefaultFeature {
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!world.getBlockState(pos.down()).getBlock().isIn(EndTags.END_GROUND)) return false;
		
		int height = MHelper.randRange(4, 7, random);
		Mutable mut = new Mutable().set(pos);
		for (int i = 1; i < height; i++) {
			mut.move(Direction.UP);
			if (!world.isAir(mut)) {
				return false;
			}
		}
		mut.set(pos);
		BlockState topMiddle = EndBlocks.LUMECORN.getDefaultState().with(LumecornBlock.SHAPE, LumecornShape.LIGHT_TOP_MIDDLE);
		BlockState middle = EndBlocks.LUMECORN.getDefaultState().with(LumecornBlock.SHAPE, LumecornShape.LIGHT_MIDDLE);
		BlockState bottom = EndBlocks.LUMECORN.getDefaultState().with(LumecornBlock.SHAPE, LumecornShape.LIGHT_BOTTOM);
		BlockState top = EndBlocks.LUMECORN.getDefaultState().with(LumecornBlock.SHAPE, LumecornShape.LIGHT_TOP);
		if (height == 4) {
			BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.LUMECORN.getDefaultState().with(LumecornBlock.SHAPE, LumecornShape.BOTTOM_SMALL));
			BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), bottom);
			BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), topMiddle);
			BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), top);
			return true;
		}
		if (random.nextBoolean()) {
			BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.LUMECORN.getDefaultState().with(LumecornBlock.SHAPE, LumecornShape.BOTTOM_SMALL));
		}
		else {
			BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.LUMECORN.getDefaultState().with(LumecornBlock.SHAPE, LumecornShape.BOTTOM_BIG));
			BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), EndBlocks.LUMECORN.getDefaultState().with(LumecornBlock.SHAPE, LumecornShape.MIDDLE));
			height --;
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
