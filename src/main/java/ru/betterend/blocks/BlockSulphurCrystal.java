package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.basis.BlockAttached;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;

public class BlockSulphurCrystal extends BlockAttached implements IRenderTypeable, Waterloggable, FluidFillable {
	public static final IntProperty AGE = IntProperty.of("age", 0, 2);
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	
	public BlockSulphurCrystal() {
		super(FabricBlockSettings.of(Material.STONE)
				.materialColor(MaterialColor.YELLOW)
				.breakByTool(FabricToolTags.PICKAXES)
				.sounds(BlockSoundGroup.GLASS)
				.requiresTool()
				.noCollision());
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		super.appendProperties(stateManager);
		stateManager.add(AGE, WATERLOGGED);
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return state.get(AGE) < 2 ? Collections.emptyList() : Lists.newArrayList(new ItemStack(this));
	}
	
	@Override
	public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return !state.get(WATERLOGGED);
	}

	@Override
	public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		return !state.get(WATERLOGGED);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		WorldView worldView = ctx.getWorld();
		BlockPos blockPos = ctx.getBlockPos();
		boolean water = worldView.getFluidState(blockPos).getFluid() == Fluids.WATER;
		return super.getPlacementState(ctx).with(WATERLOGGED, water);
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : Fluids.EMPTY.getDefaultState();
	}
}
