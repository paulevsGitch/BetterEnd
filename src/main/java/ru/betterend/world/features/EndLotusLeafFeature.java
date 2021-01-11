package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.EndLotusLeafBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;

public class EndLotusLeafFeature extends ScatterFeature {
	public EndLotusLeafFeature(int radius) {
		super(radius);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos blockPos) {
		if (canGenerate(world, blockPos)) {
			generateLeaf(world, blockPos);
		}
	}
	
	@Override
	protected int getChance() {
		return 15;
	}
	
	@Override
	protected BlockPos getCenterGround(StructureWorldAccess world, BlockPos pos) {
		return getPosOnSurface(world, pos);
	}
	
	private void generateLeaf(StructureWorldAccess world, BlockPos pos) {
		Mutable p = new Mutable();
		BlockState leaf = EndBlocks.END_LOTUS_LEAF.getDefaultState();
		BlocksHelper.setWithoutUpdate(world, pos, leaf.with(EndLotusLeafBlock.SHAPE, TripleShape.BOTTOM));
		for (Direction move: BlocksHelper.HORIZONTAL) {
			BlocksHelper.setWithoutUpdate(world, p.set(pos).move(move), leaf.with(EndLotusLeafBlock.HORIZONTAL_FACING, move).with(EndLotusLeafBlock.SHAPE, TripleShape.MIDDLE));
		}
		for (int i = 0; i < 4; i ++) {
			Direction d1 = BlocksHelper.HORIZONTAL[i];
			Direction d2 = BlocksHelper.HORIZONTAL[(i + 1) & 3];
			BlocksHelper.setWithoutUpdate(world, p.set(pos).move(d1).move(d2), leaf.with(EndLotusLeafBlock.HORIZONTAL_FACING, d1).with(EndLotusLeafBlock.SHAPE, TripleShape.TOP));
		}
	}
	
	private boolean canGenerate(StructureWorldAccess world, BlockPos pos) {
		Mutable p = new Mutable();
		p.setY(pos.getY());
		int count = 0;
		for (int x = -1; x < 2; x ++) {
			p.setX(pos.getX() + x);
			for (int z = -1; z < 2; z ++) {
				p.setZ(pos.getZ() + z);
				if (world.isAir(p) && world.getBlockState(p.down()).isOf(Blocks.WATER))
					count ++;
			}
		}
		return count == 9;
	}

	@Override
	public boolean canGenerate(StructureWorldAccess world, Random random, BlockPos center, BlockPos blockPos, float radius) {
		return world.isAir(blockPos) && world.getBlockState(blockPos.down()).isOf(Blocks.WATER);
	}
}
