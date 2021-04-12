package ru.betterend.blocks;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.basis.UnderwaterPlantBlock;

public class CharniaBlock extends UnderwaterPlantBlock {
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return sideCoversSmallSquare(world, pos.below(), Direction.UP)
				&& world.getFluidState(pos).getFluid() == Fluids.WATER;
	}
}
