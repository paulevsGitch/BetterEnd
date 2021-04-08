package ru.betterend.blocks.basis;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

public class BlockBaseNotFull extends BlockBase {

	public BlockBaseNotFull(Properties settings) {
		super(settings);
	}

	public boolean canSuffocate(BlockState state, BlockView view, BlockPos pos) {
		return false;
	}

	public boolean isSimpleFullBlock(BlockState state, BlockView view, BlockPos pos) {
		return false;
	}

	public boolean allowsSpawning(BlockState state, BlockView view, BlockPos pos, EntityType<?> type) {
		return false;
	}
}
