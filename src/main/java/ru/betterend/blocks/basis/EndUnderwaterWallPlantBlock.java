package ru.betterend.blocks.basis;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;

public class EndUnderwaterWallPlantBlock extends EndWallPlantBlock implements LiquidBlockContainer {
	
	public EndUnderwaterWallPlantBlock() {
		super(FabricBlockSettings.of(Material.WATER_PLANT)
				.breakByTool(FabricToolTags.SHEARS)
				.breakByHand(true)
				.sound(SoundType.WET_GRASS)
				.noCollission());
	}
	
	public EndUnderwaterWallPlantBlock(int light) {
		super(FabricBlockSettings.of(Material.WATER_PLANT)
				.breakByTool(FabricToolTags.SHEARS)
				.breakByHand(true)
				.luminance(light)
				.sound(SoundType.WET_GRASS)
				.noCollission());
	}
	
	public EndUnderwaterWallPlantBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public boolean canPlaceLiquid(BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
		return false;
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return Fluids.WATER.getSource(false);
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return world.getFluidState(pos).getType() == Fluids.WATER && super.canSurvive(state, world, pos);
	}
}
