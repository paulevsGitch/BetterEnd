package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.basis.BlockAttached;
import ru.betterend.blocks.basis.BlockWallPlant;
import ru.betterend.util.BlocksHelper;

public class WallPlantFeature extends WallScatterFeature {
	private final Block block;
	
	public WallPlantFeature(Block block, int radius) {
		super(radius);
		this.block = block;
	}

	@Override
	public boolean canGenerate(StructureWorldAccess world, Random random, BlockPos pos, Direction dir) {
		if (block instanceof BlockWallPlant) {
			BlockState state = block.getDefaultState().with(BlockWallPlant.FACING, dir);
			return block.canPlaceAt(state, world, pos);
		}
		else if (block instanceof BlockAttached) {
			BlockState state = block.getDefaultState().with(Properties.FACING, dir);
			return block.canPlaceAt(state, world, pos);
		}
		return block.canPlaceAt(block.getDefaultState(), world, pos);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos pos, Direction dir) {
		BlockState state = block.getDefaultState();
		if (block instanceof BlockWallPlant) {
			state = state.with(BlockWallPlant.FACING, dir);
		}
		else if (block instanceof BlockAttached) {
			state = state.with(Properties.FACING, dir);
		}
		BlocksHelper.setWithoutUpdate(world, pos, state);
	}
}
