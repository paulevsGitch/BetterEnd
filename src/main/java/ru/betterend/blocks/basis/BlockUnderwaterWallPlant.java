package ru.betterend.blocks.basis;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class BlockUnderwaterWallPlant extends BlockWallPlant implements FluidFillable {
	
	public BlockUnderwaterWallPlant() {
		super(FabricBlockSettings.of(Material.UNDERWATER_PLANT)
				.breakByTool(FabricToolTags.SHEARS)
				.sounds(BlockSoundGroup.WET_GRASS)
				.breakByHand(true)
				.noCollision());
	}
	
	public BlockUnderwaterWallPlant(int light) {
		super(FabricBlockSettings.of(Material.UNDERWATER_PLANT)
				.breakByTool(FabricToolTags.SHEARS)
				.sounds(BlockSoundGroup.WET_GRASS)
				.luminance(light)
				.breakByHand(true)
				.noCollision());
	}
	
	public BlockUnderwaterWallPlant(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
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
