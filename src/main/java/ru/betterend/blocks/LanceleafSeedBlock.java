package ru.betterend.blocks;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.BlockProperties.PentaShape;
import ru.betterend.blocks.basis.EndPlantWithAgeBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class LanceleafSeedBlock extends EndPlantWithAgeBlock {
	@Override
	public void growAdult(StructureWorldAccess world, Random random, BlockPos pos) {
		int height = MHelper.randRange(4, 6, random);
		int h = BlocksHelper.upRay(world, pos, height + 2);
		if (h < height + 1) {
			return;
		}
		int rotation = random.nextInt(4);
		Mutable mut = new Mutable().set(pos);
		BlockState plant = EndBlocks.LANCELEAF.getDefaultState().with(BlockProperties.ROTATION, rotation);
		BlocksHelper.setWithoutUpdate(world, mut, plant.with(BlockProperties.PENTA_SHAPE, PentaShape.BOTTOM));
		BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), plant.with(BlockProperties.PENTA_SHAPE, PentaShape.PRE_BOTTOM));
		for (int i = 2; i < height - 2; i++) {
			BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), plant.with(BlockProperties.PENTA_SHAPE, PentaShape.MIDDLE));
		}
		BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), plant.with(BlockProperties.PENTA_SHAPE, PentaShape.PRE_TOP));
		BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), plant.with(BlockProperties.PENTA_SHAPE, PentaShape.TOP));
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.isOf(EndBlocks.AMBER_MOSS);
	}
}
