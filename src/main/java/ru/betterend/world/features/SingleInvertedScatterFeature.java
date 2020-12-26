package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.basis.BlockAttached;
import ru.betterend.util.BlocksHelper;

public class SingleInvertedScatterFeature extends InvertedScatterFeature {
	private final Block block;
	
	public SingleInvertedScatterFeature(Block block, int radius) {
		super(radius);
		this.block = block;
	}

	@Override
	public boolean canGenerate(StructureWorldAccess world, Random random, BlockPos center, BlockPos blockPos, float radius) {
		if (!world.isAir(blockPos)) {
			return false;
		}
		BlockState state = block.getDefaultState();
		if (block instanceof BlockAttached) {
			state = state.with(Properties.FACING, Direction.DOWN);
		}
		return state.canPlaceAt(world, blockPos);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos blockPos) {
		BlockState state = block.getDefaultState();
		if (block instanceof BlockAttached) {
			state = state.with(Properties.FACING, Direction.DOWN);
		}
		BlocksHelper.setWithoutUpdate(world, blockPos, state);
	}
}
