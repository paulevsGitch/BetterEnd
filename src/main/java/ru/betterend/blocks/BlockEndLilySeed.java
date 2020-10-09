package ru.betterend.blocks;

import java.util.Random;

import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.BlockUnderwaterPlantWithAge;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.util.BlocksHelper;

public class BlockEndLilySeed extends BlockUnderwaterPlantWithAge {
	@Override
	public void grow(StructureWorldAccess world, Random random, BlockPos pos) {
		if (canGrow(world, pos)) {
			world.setBlockState(pos, BlockRegistry.END_LILY.getDefaultState().with(BlockEndLily.SHAPE, TripleShape.BOTTOM), 0);
			BlockPos up = pos.up();
			while (world.getFluidState(up).isStill()) {
				BlocksHelper.setWithoutUpdate(world, up, BlockRegistry.END_LILY.getDefaultState().with(BlockEndLily.SHAPE, TripleShape.MIDDLE));
				up = up.up();
			}
			BlocksHelper.setWithoutUpdate(world, up, BlockRegistry.END_LILY.getDefaultState().with(BlockEndLily.SHAPE, TripleShape.TOP));
		}
	}
	
	private boolean canGrow(StructureWorldAccess world, BlockPos pos) {
		BlockPos up = pos.up();
		while (world.getBlockState(up).getFluidState().getFluid().equals(Fluids.WATER.getStill())) {
			up = up.up();
		}
		return world.isAir(up);
	}
}
