package ru.betterend.blocks;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import ru.bclib.util.BlocksHelper;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.UnderwaterPlantWithAgeBlock;
import ru.betterend.registry.EndBlocks;

public class EndLotusSeedBlock extends UnderwaterPlantWithAgeBlock {
	@Override
	public void grow(WorldGenLevel world, Random random, BlockPos pos) {
		if (canGrow(world, pos)) {
			BlockState startLeaf = EndBlocks.END_LOTUS_STEM.defaultBlockState().setValue(EndLotusStemBlock.LEAF, true);
			BlockState roots = EndBlocks.END_LOTUS_STEM.defaultBlockState().setValue(EndLotusStemBlock.SHAPE, TripleShape.BOTTOM).setValue(EndLotusStemBlock.WATERLOGGED, true);
			BlockState stem = EndBlocks.END_LOTUS_STEM.defaultBlockState();
			BlockState flower = EndBlocks.END_LOTUS_FLOWER.defaultBlockState();
			
			BlocksHelper.setWithoutUpdate(world, pos, roots);
			MutableBlockPos bpos = new MutableBlockPos().set(pos);
			bpos.setY(bpos.getY() + 1);
			while (world.getFluidState(bpos).isSource()) {
				BlocksHelper.setWithoutUpdate(world, bpos, stem.setValue(EndLotusStemBlock.WATERLOGGED, true));
				bpos.setY(bpos.getY() + 1);
			}
			
			int height = random.nextBoolean() ? 0 : random.nextBoolean() ? 1 : random.nextBoolean() ? 1 : -1;
			TripleShape shape = (height == 0) ? TripleShape.TOP : TripleShape.MIDDLE;
			Direction dir = BlocksHelper.randomHorizontal(random);
			BlockPos leafCenter = bpos.immutable().relative(dir);
			if (hasLeaf(world, leafCenter)) {
				generateLeaf(world, leafCenter);
				BlocksHelper.setWithoutUpdate(world, bpos, startLeaf.setValue(EndLotusStemBlock.SHAPE, shape).setValue(EndLotusStemBlock.FACING, dir));
			}
			else {
				BlocksHelper.setWithoutUpdate(world, bpos, stem.setValue(EndLotusStemBlock.SHAPE, shape));
			}
			
			bpos.setY(bpos.getY() + 1);
			for (int i = 1; i <= height; i++) {
				if (!world.isEmptyBlock(bpos)) {
					bpos.setY(bpos.getY() - 1);
					BlocksHelper.setWithoutUpdate(world, bpos, flower);
					bpos.setY(bpos.getY() - 1);
					stem = world.getBlockState(bpos);
					BlocksHelper.setWithoutUpdate(world, bpos, stem.setValue(EndLotusStemBlock.SHAPE, TripleShape.TOP));
					return;
				}
				BlocksHelper.setWithoutUpdate(world, bpos, stem);
				bpos.setY(bpos.getY() + 1);
			}
			
			if (!world.isEmptyBlock(bpos) || height < 0) {
				bpos.setY(bpos.getY() - 1);
			}
			
			BlocksHelper.setWithoutUpdate(world, bpos, flower);
			bpos.setY(bpos.getY() - 1);
			stem = world.getBlockState(bpos);
			if (!stem.is(EndBlocks.END_LOTUS_STEM)) {
				stem = EndBlocks.END_LOTUS_STEM.defaultBlockState();
				if (!world.getBlockState(bpos.north()).getFluidState().isEmpty()) {
					stem = stem.setValue(EndLotusStemBlock.WATERLOGGED, true);
				}
			}
			
			if (world.getBlockState(bpos.relative(dir)).is(EndBlocks.END_LOTUS_LEAF)) {
				stem = stem.setValue(EndLotusStemBlock.LEAF, true).setValue(EndLotusStemBlock.FACING, dir);
			}
			
			BlocksHelper.setWithoutUpdate(world, bpos, stem.setValue(EndLotusStemBlock.SHAPE, TripleShape.TOP));
		}
	}
	
	private boolean canGrow(WorldGenLevel world, BlockPos pos) {
		MutableBlockPos bpos = new MutableBlockPos();
		bpos.set(pos);
		while (world.getBlockState(bpos).getFluidState().getType().equals(Fluids.WATER.getSource())) {
			bpos.setY(bpos.getY() + 1);
		}
		return world.isEmptyBlock(bpos) && world.isEmptyBlock(bpos.above());
	}
	
	private void generateLeaf(WorldGenLevel world, BlockPos pos) {
		MutableBlockPos p = new MutableBlockPos();
		BlockState leaf = EndBlocks.END_LOTUS_LEAF.defaultBlockState();
		BlocksHelper.setWithoutUpdate(world, pos, leaf.setValue(EndLotusLeafBlock.SHAPE, TripleShape.BOTTOM));
		for (Direction move: BlocksHelper.HORIZONTAL) {
			BlocksHelper.setWithoutUpdate(world, p.set(pos).move(move), leaf.setValue(EndLotusLeafBlock.HORIZONTAL_FACING, move).setValue(EndLotusLeafBlock.SHAPE, TripleShape.MIDDLE));
		}
		for (int i = 0; i < 4; i ++) {
			Direction d1 = BlocksHelper.HORIZONTAL[i];
			Direction d2 = BlocksHelper.HORIZONTAL[(i + 1) & 3];
			BlocksHelper.setWithoutUpdate(world, p.set(pos).move(d1).move(d2), leaf.setValue(EndLotusLeafBlock.HORIZONTAL_FACING, d1).setValue(EndLotusLeafBlock.SHAPE, TripleShape.TOP));
		}
	}
	
	private boolean hasLeaf(WorldGenLevel world, BlockPos pos) {
		MutableBlockPos p = new MutableBlockPos();
		p.setY(pos.getY());
		int count = 0;
		for (int x = -1; x < 2; x ++) {
			p.setX(pos.getX() + x);
			for (int z = -1; z < 2; z ++) {
				p.setZ(pos.getZ() + z);
				if (world.isEmptyBlock(p) && !world.getFluidState(p.below()).isEmpty())
					count ++;
			}
		}
		return count == 9;
	}
}
