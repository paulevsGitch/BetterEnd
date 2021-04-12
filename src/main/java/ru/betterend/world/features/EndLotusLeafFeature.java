package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.EndLotusLeafBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;

public class EndLotusLeafFeature extends ScatterFeature {
	public EndLotusLeafFeature(int radius) {
		super(radius);
	}

	@Override
	public void place(WorldGenLevel world, Random random, BlockPos blockPos) {
		if (canGenerate(world, blockPos)) {
			generateLeaf(world, blockPos);
		}
	}

	@Override
	protected int getChance() {
		return 15;
	}

	@Override
	protected BlockPos getCenterGround(WorldGenLevel world, BlockPos pos) {
		return getPosOnSurface(world, pos);
	}

	private void generateLeaf(WorldGenLevel world, BlockPos pos) {
		MutableBlockPos p = new MutableBlockPos();
		BlockState leaf = EndBlocks.END_LOTUS_LEAF.defaultBlockState();
		BlocksHelper.setWithoutUpdate(world, pos, leaf.with(EndLotusLeafBlock.SHAPE, TripleShape.BOTTOM));
		for (Direction move : BlocksHelper.HORIZONTAL) {
			BlocksHelper.setWithoutUpdate(world, p.set(pos).move(move), leaf
					.with(EndLotusLeafBlock.HORIZONTAL_FACING, move).with(EndLotusLeafBlock.SHAPE, TripleShape.MIDDLE));
		}
		for (int i = 0; i < 4; i++) {
			Direction d1 = BlocksHelper.HORIZONTAL[i];
			Direction d2 = BlocksHelper.HORIZONTAL[(i + 1) & 3];
			BlocksHelper.setWithoutUpdate(world, p.set(pos).move(d1).move(d2),
					leaf.with(EndLotusLeafBlock.HORIZONTAL_FACING, d1).with(EndLotusLeafBlock.SHAPE, TripleShape.TOP));
		}
	}

	private boolean canGenerate(WorldGenLevel world, BlockPos pos) {
		MutableBlockPos p = new MutableBlockPos();
		p.setY(pos.getY());
		int count = 0;
		for (int x = -1; x < 2; x++) {
			p.setX(pos.getX() + x);
			for (int z = -1; z < 2; z++) {
				p.setZ(pos.getZ() + z);
				if (world.isAir(p) && world.getBlockState(p.below()).is(Blocks.WATER))
					count++;
			}
		}
		return count == 9;
	}

	@Override
	public boolean canGenerate(WorldGenLevel world, Random random, BlockPos center, BlockPos blockPos, float radius) {
		return world.isAir(blockPos) && world.getBlockState(blockPos.below()).is(Blocks.WATER);
	}
}
