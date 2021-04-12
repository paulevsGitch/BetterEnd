package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import ru.betterend.blocks.basis.AttachedBlock;
import ru.betterend.blocks.basis.EndWallPlantBlock;
import ru.betterend.util.BlocksHelper;

public class WallPlantFeature extends WallScatterFeature {
	private final Block block;

	public WallPlantFeature(Block block, int radius) {
		super(radius);
		this.block = block;
	}

	@Override
	public boolean canGenerate(WorldGenLevel world, Random random, BlockPos pos, Direction dir) {
		if (block instanceof EndWallPlantBlock) {
			BlockState state = block.defaultBlockState().with(EndWallPlantBlock.FACING, dir);
			return block.canPlaceAt(state, world, pos);
		} else if (block instanceof AttachedBlock) {
			BlockState state = block.defaultBlockState().with(Properties.FACING, dir);
			return block.canPlaceAt(state, world, pos);
		}
		return block.canPlaceAt(block.defaultBlockState(), world, pos);
	}

	@Override
	public void place(WorldGenLevel world, Random random, BlockPos pos, Direction dir) {
		BlockState state = block.defaultBlockState();
		if (block instanceof EndWallPlantBlock) {
			state = state.with(EndWallPlantBlock.FACING, dir);
		} else if (block instanceof AttachedBlock) {
			state = state.with(Properties.FACING, dir);
		}
		BlocksHelper.setWithoutUpdate(world, pos, state);
	}
}
