package ru.betterend.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import ru.betterend.blocks.basis.UnderwaterPlantBlock;

public class CharniaBlock extends UnderwaterPlantBlock {
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return canSupportCenter(world, pos.below(), Direction.UP) && world.getFluidState(pos).getType() == Fluids.WATER;
	}
}
