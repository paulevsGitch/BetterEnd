package ru.betterend.world.features.bushes;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
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
		
		int height = MHelper.randRange(3, 6, random);
		Mutable mut = new Mutable().set(pos);
		for (int i = 1; i < height; i++) {
			mut.move(Direction.UP);
			if (!world.isAir(mut)) {
				return false;
			}
		}
		mut.set(pos);
		if (height == 3) {
			BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.LUMECORN.getDefaultState().with(LumecornBlock.SHAPE, LumecornShape.BOTTOM_SMALL));
			BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), EndBlocks.LUMECORN.getDefaultState().with(LumecornBlock.SHAPE, LumecornShape.LIGHT2));
			BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), EndBlocks.LUMECORN.getDefaultState().with(LumecornBlock.SHAPE, LumecornShape.LIGHT3));
			return true;
		}
		boolean tall = random.nextBoolean();
		if (tall) {
			BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.LUMECORN.getDefaultState().with(LumecornBlock.SHAPE, LumecornShape.BOTTOM_BIG));
			BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), EndBlocks.LUMECORN.getDefaultState().with(LumecornBlock.SHAPE, LumecornShape.MIDDLE));
			height -= 2;
		}
		else {
			BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.LUMECORN.getDefaultState().with(LumecornBlock.SHAPE, LumecornShape.BOTTOM_SMALL));
			height --;
		}
		boolean smallBottom = height > 2 && random.nextBoolean();
		for (int i = 0; i < height; i++) {
			int size = i - height + 4;
			size = MathHelper.clamp(size, 1, 3);
			if (smallBottom && i == 0) {
				size ++;
			}
			if (size == 1) {
				BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), EndBlocks.LUMECORN.getDefaultState().with(LumecornBlock.SHAPE, LumecornShape.LIGHT1));
			}
			else if (size == 2) {
				BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), EndBlocks.LUMECORN.getDefaultState().with(LumecornBlock.SHAPE, LumecornShape.LIGHT2));
			}
			else {
				BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), EndBlocks.LUMECORN.getDefaultState().with(LumecornBlock.SHAPE, LumecornShape.LIGHT3));
			}
		}
		return false;
	}
}
