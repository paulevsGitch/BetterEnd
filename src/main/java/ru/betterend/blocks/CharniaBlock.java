package ru.betterend.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.basis.UnderwaterPlantBlock;

public class CharniaBlock extends UnderwaterPlantBlock {
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return sideCoversSmallSquare(world, pos.down(), Direction.UP) && world.getFluidState(pos).getFluid() == Fluids.WATER;
	}
}
