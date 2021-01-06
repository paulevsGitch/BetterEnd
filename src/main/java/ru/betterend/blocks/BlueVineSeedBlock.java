package ru.betterend.blocks;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.FurBlock;
import ru.betterend.blocks.basis.EndPlantWithAgeBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class BlueVineSeedBlock extends EndPlantWithAgeBlock {
	@Override
	public void growAdult(StructureWorldAccess world, Random random, BlockPos pos) {
		int height = MHelper.randRange(2, 5, random);
		int h = BlocksHelper.upRay(world, pos, height + 2);
		if (h < height + 1) {
			return;
		}
		BlocksHelper.setWithoutUpdate(world, pos, EndBlocks.BLUE_VINE.getDefaultState().with(BlockProperties.TRIPLE_SHAPE, TripleShape.BOTTOM));
		for (int i = 1; i < height; i++) {
			BlocksHelper.setWithoutUpdate(world, pos.up(i), EndBlocks.BLUE_VINE.getDefaultState().with(BlockProperties.TRIPLE_SHAPE, TripleShape.MIDDLE));
		}
		BlocksHelper.setWithoutUpdate(world, pos.up(height), EndBlocks.BLUE_VINE.getDefaultState().with(BlockProperties.TRIPLE_SHAPE, TripleShape.TOP));
		placeLantern(world, pos.up(height + 1));
	}
	
	private void placeLantern(StructureWorldAccess world, BlockPos pos) {
		BlocksHelper.setWithoutUpdate(world, pos, EndBlocks.BLUE_VINE_LANTERN.getDefaultState().with(BlueVineLanternBlock.NATURAL, true));
		for (Direction dir: BlocksHelper.HORIZONTAL) {
			BlockPos p = pos.offset(dir);
			if (world.isAir(p)) {
				BlocksHelper.setWithoutUpdate(world, p, EndBlocks.BLUE_VINE_FUR.getDefaultState().with(FurBlock.FACING, dir));
			}
		}
		if (world.isAir(pos.up())) {
			BlocksHelper.setWithoutUpdate(world, pos.up(), EndBlocks.BLUE_VINE_FUR.getDefaultState().with(FurBlock.FACING, Direction.UP));
		}
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.isOf(EndBlocks.END_MOSS) || state.isOf(EndBlocks.END_MYCELIUM);
	}
}
