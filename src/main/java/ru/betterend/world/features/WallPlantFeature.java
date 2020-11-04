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
		BlockPos blockPos = pos.offset(dir.getOpposite());
		BlockState blockState = world.getBlockState(blockPos);
		return ((BlockWallPlant) block).isSupport(world, blockPos, blockState, dir);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos pos, Direction dir) {
		BlocksHelper.setWithoutUpdate(world, pos, block.getDefaultState().with(BlockWallPlant.FACING, dir));
	}
}
