package ru.betterend.blocks;

import java.util.Random;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.BlockPlantWithStages;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class BlockBlueVineSeed extends BlockPlantWithStages {
	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos) {
		int height = MHelper.randRange(2, 5, random);
		int h = BlocksHelper.upRay(world, pos, height + 2);
		if (h < height + 1) {
			return;
		}
		BlocksHelper.setWithoutUpdate(world, pos, BlockRegistry.BLUE_VINE.getDefaultState().with(BlockProperties.TRIPLE_SHAPE, TripleShape.BOTTOM));
		for (int i = 1; i < height; i++) {
			BlocksHelper.setWithoutUpdate(world, pos.up(i), BlockRegistry.BLUE_VINE.getDefaultState().with(BlockProperties.TRIPLE_SHAPE, TripleShape.MIDDLE));
		}
		BlocksHelper.setWithoutUpdate(world, pos.up(height), BlockRegistry.BLUE_VINE.getDefaultState().with(BlockProperties.TRIPLE_SHAPE, TripleShape.TOP));
		placeLantern(world, pos.up(height + 1));
	}
	
	private void placeLantern(ServerWorld world, BlockPos pos) {
		BlocksHelper.setWithoutUpdate(world, pos, BlockRegistry.BLUE_VINE_LANTERN.getDefaultState().with(BlockBlueVineLantern.NATURAL, true));
		for (Direction dir: BlocksHelper.HORIZONTAL) {
			BlockPos p = pos.offset(dir);
			if (world.isAir(p)) {
				BlocksHelper.setWithoutUpdate(world, p, BlockRegistry.BLUE_VINE_FUR.getDefaultState().with(BlockGlowingFur.FACING, dir));
			}
		}
		if (world.isAir(pos.up())) {
			BlocksHelper.setWithoutUpdate(world, pos.up(), BlockRegistry.BLUE_VINE_FUR.getDefaultState().with(BlockGlowingFur.FACING, Direction.UP));
		}
	}
}
