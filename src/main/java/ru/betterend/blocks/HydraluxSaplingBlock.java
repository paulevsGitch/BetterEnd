package ru.betterend.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.blocks.UnderwaterPlantWithAgeBlock;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.betterend.blocks.EndBlockProperties.HydraluxShape;
import ru.betterend.registry.EndBlocks;

import java.util.Random;

public class HydraluxSaplingBlock extends UnderwaterPlantWithAgeBlock {
	
	@Override
	public void grow(WorldGenLevel world, Random random, BlockPos pos) {
		int h = MHelper.randRange(4, 8, random);
		MutableBlockPos mut = new MutableBlockPos().set(pos);
		
		for (int i = 1; i < h; i++) {
			mut.setY(pos.getY() + i);
			if (!world.getBlockState(mut).is(Blocks.WATER)) {
				return;
			}
		}
		
		mut.setY(pos.getY());
		BlockState state = EndBlocks.HYDRALUX.defaultBlockState();
		BlocksHelper.setWithoutUpdate(world, pos, state.setValue(EndBlockProperties.HYDRALUX_SHAPE, HydraluxShape.ROOTS));
		for (int i = 1; i < h - 2; i++) {
			mut.setY(pos.getY() + i);
			BlocksHelper.setWithoutUpdate(world, mut, state.setValue(EndBlockProperties.HYDRALUX_SHAPE, HydraluxShape.VINE));
		}
		
		mut.setY(mut.getY() + 1);
		boolean big = random.nextBoolean();
		BlocksHelper.setWithoutUpdate(world, mut, state.setValue(EndBlockProperties.HYDRALUX_SHAPE, big ? HydraluxShape.FLOWER_BIG_BOTTOM : HydraluxShape.FLOWER_SMALL_BOTTOM));
		
		mut.setY(mut.getY() + 1);
		BlocksHelper.setWithoutUpdate(world, mut, state.setValue(EndBlockProperties.HYDRALUX_SHAPE, big ? HydraluxShape.FLOWER_BIG_TOP : HydraluxShape.FLOWER_SMALL_TOP));
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(EndBlocks.SULPHURIC_ROCK.stone);
	}
}
