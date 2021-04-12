package ru.betterend.blocks.basis;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.FluidFillable;
import net.minecraft.world.level.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.WorldView;

public class EndUnderwaterWallPlantBlock extends EndWallPlantBlock implements FluidFillable {

	public EndUnderwaterWallPlantBlock() {
		super(FabricBlockSettings.of(Material.UNDERWATER_PLANT).breakByTool(FabricToolTags.SHEARS)
				.sounds(SoundType.WET_GRASS).breakByHand(true).noCollision());
	}

	public EndUnderwaterWallPlantBlock(int light) {
		super(FabricBlockSettings.of(Material.UNDERWATER_PLANT).breakByTool(FabricToolTags.SHEARS)
				.sounds(SoundType.WET_GRASS).luminance(light).breakByHand(true).noCollision());
	}

	public EndUnderwaterWallPlantBlock(Properties settings) {
		super(settings);
	}

	@Override
	public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	public boolean tryFillWithFluid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
		return false;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return Fluids.WATER.getStill(false);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getFluidState(pos).getFluid() == Fluids.WATER && super.canPlaceAt(state, world, pos);
	}
}
