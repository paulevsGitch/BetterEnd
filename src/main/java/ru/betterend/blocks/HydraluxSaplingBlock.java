package ru.betterend.blocks;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.BlockProperties.HydraluxShape;
import ru.betterend.blocks.basis.UnderwaterPlantWithAgeBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class HydraluxSaplingBlock extends UnderwaterPlantWithAgeBlock {
	@Override
	public void grow(StructureWorldAccess world, Random random, BlockPos pos) {
		int h = MHelper.randRange(4, 8, random);
		Mutable mut = new Mutable().set(pos);
		
		for (int i = 1; i < h; i++) {
			mut.setY(pos.getY() + i);
			if (!world.getBlockState(mut).isOf(Blocks.WATER)) {
				return;
			}
		}
		
		mut.setY(pos.getY());
		BlockState state = EndBlocks.HYDRALUX.getDefaultState();
		BlocksHelper.setWithoutUpdate(world, pos, state.with(BlockProperties.HYDRALUX_SHAPE, HydraluxShape.ROOTS));
		for (int i = 1; i < h - 2; i++) {
			mut.setY(pos.getY() + i);
			BlocksHelper.setWithoutUpdate(world, mut, state.with(BlockProperties.HYDRALUX_SHAPE, HydraluxShape.VINE));
		}
		
		mut.setY(mut.getY() + 1);
		boolean big = random.nextBoolean();
		BlocksHelper.setWithoutUpdate(world, mut, state.with(BlockProperties.HYDRALUX_SHAPE, big ? HydraluxShape.FLOWER_BIG_BOTTOM : HydraluxShape.FLOWER_SMALL_BOTTOM));
		
		mut.setY(mut.getY() + 1);
		BlocksHelper.setWithoutUpdate(world, mut, state.with(BlockProperties.HYDRALUX_SHAPE, big ? HydraluxShape.FLOWER_BIG_TOP : HydraluxShape.FLOWER_SMALL_TOP));
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.isOf(EndBlocks.SULPHURIC_ROCK.stone);
	}
}
