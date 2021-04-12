package ru.betterend.blocks;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import ru.betterend.blocks.BlockProperties.PentaShape;
import ru.betterend.blocks.basis.EndPlantWithAgeBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class LanceleafSeedBlock extends EndPlantWithAgeBlock {
	@Override
	public void growAdult(WorldGenLevel world, Random random, BlockPos pos) {
		int height = MHelper.randRange(4, 6, random);
		int h = BlocksHelper.upRay(world, pos, height + 2);
		if (h < height + 1) {
			return;
		}
		int rotation = random.nextInt(4);
		MutableBlockPos mut = new MutableBlockPos().set(pos);
		BlockState plant = EndBlocks.LANCELEAF.defaultBlockState().setValue(BlockProperties.ROTATION, rotation);
		BlocksHelper.setWithoutUpdate(world, mut, plant.setValue(BlockProperties.PENTA_SHAPE, PentaShape.BOTTOM));
		BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), plant.setValue(BlockProperties.PENTA_SHAPE, PentaShape.PRE_BOTTOM));
		for (int i = 2; i < height - 2; i++) {
			BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), plant.setValue(BlockProperties.PENTA_SHAPE, PentaShape.MIDDLE));
		}
		BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), plant.setValue(BlockProperties.PENTA_SHAPE, PentaShape.PRE_TOP));
		BlocksHelper.setWithoutUpdate(world, mut.move(Direction.UP), plant.setValue(BlockProperties.PENTA_SHAPE, PentaShape.TOP));
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(EndBlocks.AMBER_MOSS);
	}
	
	@Override
	public BlockBehaviour.OffsetType getOffsetType() {
		return BlockBehaviour.OffsetType.NONE;
	}
}
