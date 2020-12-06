package ru.betterend.world.features;

import java.util.Random;
import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;

public class MengerSpongeFeature extends UnderwaterPlantScatter {
	private static final Function<BlockState, Boolean> REPLACE;
	
	public MengerSpongeFeature(int radius) {
		super(radius);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos blockPos) {
		BlocksHelper.setWithoutUpdate(world, blockPos, EndBlocks.MENGER_SPONGE_WET);
		if (random.nextBoolean()) {
			for (Direction dir: BlocksHelper.DIRECTIONS) {
				BlockPos pos = blockPos.offset(dir);
				if (REPLACE.apply(world.getBlockState(pos))) {
					BlocksHelper.setWithoutUpdate(world, pos, EndBlocks.MENGER_SPONGE_WET);
				}
			}
		}
	}
	
	static {
		REPLACE = (state) -> {
			if (state.isOf(EndBlocks.END_LOTUS_STEM)) {
				return false;
			}
			return !state.getFluidState().isEmpty() || state.getMaterial().isReplaceable();
		};
	}
}
