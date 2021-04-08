package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.basis.AttachedBlock;
import ru.betterend.util.BlocksHelper;

public class SingleInvertedScatterFeature extends InvertedScatterFeature {
	private final Block block;

	public SingleInvertedScatterFeature(Block block, int radius) {
		super(radius);
		this.block = block;
	}

	@Override
	public boolean canGenerate(StructureWorldAccess world, Random random, BlockPos center, BlockPos blockPos,
			float radius) {
		if (!world.isAir(blockPos)) {
			return false;
		}
		BlockState state = block.defaultBlockState();
		if (block instanceof AttachedBlock) {
			state = state.with(Properties.FACING, Direction.DOWN);
		}
		return state.canPlaceAt(world, blockPos);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos blockPos) {
		BlockState state = block.defaultBlockState();
		if (block instanceof AttachedBlock) {
			state = state.with(Properties.FACING, Direction.DOWN);
		}
		BlocksHelper.setWithoutUpdate(world, blockPos, state);
	}
}
