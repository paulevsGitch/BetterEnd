package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import ru.bclib.util.BlocksHelper;
import ru.betterend.blocks.basis.AttachedBlock;
import ru.betterend.blocks.basis.EndWallPlantBlock;

public class WallPlantFeature extends WallScatterFeature {
	private final Block block;
	
	public WallPlantFeature(Block block, int radius) {
		super(radius);
		this.block = block;
	}

	@Override
	public boolean canGenerate(WorldGenLevel world, Random random, BlockPos pos, Direction dir) {
		if (block instanceof EndWallPlantBlock) {
			BlockState state = block.defaultBlockState().setValue(EndWallPlantBlock.FACING, dir);
			return block.canSurvive(state, world, pos);
		}
		else if (block instanceof AttachedBlock) {
			BlockState state = block.defaultBlockState().setValue(BlockStateProperties.FACING, dir);
			return block.canSurvive(state, world, pos);
		}
		return block.canSurvive(block.defaultBlockState(), world, pos);
	}

	@Override
	public void generate(WorldGenLevel world, Random random, BlockPos pos, Direction dir) {
		BlockState state = block.defaultBlockState();
		if (block instanceof EndWallPlantBlock) {
			state = state.setValue(EndWallPlantBlock.FACING, dir);
		}
		else if (block instanceof AttachedBlock) {
			state = state.setValue(BlockStateProperties.FACING, dir);
		}
		BlocksHelper.setWithoutUpdate(world, pos, state);
	}
}
