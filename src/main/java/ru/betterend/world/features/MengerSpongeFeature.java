package ru.betterend.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.util.BlocksHelper;
import ru.betterend.registry.EndBlocks;

import java.util.Random;
import java.util.function.Function;

public class MengerSpongeFeature extends UnderwaterPlantScatter {
	private static final Function<BlockState, Boolean> REPLACE;
	
	public MengerSpongeFeature(int radius) {
		super(radius);
	}
	
	@Override
	public void generate(WorldGenLevel world, Random random, BlockPos blockPos) {
		BlocksHelper.setWithoutUpdate(world, blockPos, EndBlocks.MENGER_SPONGE_WET);
		if (random.nextBoolean()) {
			for (Direction dir : BlocksHelper.DIRECTIONS) {
				BlockPos pos = blockPos.relative(dir);
				if (REPLACE.apply(world.getBlockState(pos))) {
					BlocksHelper.setWithoutUpdate(world, pos, EndBlocks.MENGER_SPONGE_WET);
				}
			}
		}
	}
	
	static {
		REPLACE = (state) -> {
			if (state.is(EndBlocks.END_LOTUS_STEM)) {
				return false;
			}
			return !state.getFluidState().isEmpty() || state.getMaterial().isReplaceable();
		};
	}
}
