package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
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
		BlockState state = block.getDefaultState().with(BlockWallPlant.FACING, dir);
		return block.canPlaceAt(state, world, pos);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos pos, Direction dir) {
		BlockState state = block.getDefaultState().with(BlockWallPlant.FACING, dir);
		BlocksHelper.setWithoutUpdate(world, pos, state);
	}
}
