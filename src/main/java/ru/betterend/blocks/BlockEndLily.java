package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.BlockUnderwaterPlant;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;
import ru.betterend.util.MHelper;

public class BlockEndLily extends BlockUnderwaterPlant {
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	private static final VoxelShape SHAPE_BOTTOM = Block.createCuboidShape(4, 0, 4, 12, 16, 12);
	private static final VoxelShape SHAPE_TOP = Block.createCuboidShape(2, 0, 2, 14, 6, 14);
	
	public BlockEndLily() {
		super(FabricBlockSettings.of(Material.UNDERWATER_PLANT)
				.breakByTool(FabricToolTags.SHEARS)
				.sounds(BlockSoundGroup.WET_GRASS)
				.breakByHand(true)
				.luminance((state) -> { return state.get(SHAPE) == TripleShape.TOP ? 13 : 0; })
				.noCollision());
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!canPlaceAt(state, world, pos)) {
			return state.get(SHAPE) == TripleShape.TOP ? Blocks.AIR.getDefaultState() : Blocks.WATER.getDefaultState();
		}
		else {
			return state;
		}
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		Vec3d vec3d = state.getModelOffset(view, pos);
		VoxelShape shape = state.get(SHAPE) == TripleShape.TOP ? SHAPE_TOP : SHAPE_BOTTOM;
		return shape.offset(vec3d.x, vec3d.y, vec3d.z);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(SHAPE) == TripleShape.TOP ? Fluids.EMPTY.getDefaultState() : Fluids.WATER.getStill(false);
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		if (state.get(SHAPE) == TripleShape.TOP) {
			return world.getBlockState(pos.down()).getBlock() == this;
		}
		else if (state.get(SHAPE) == TripleShape.BOTTOM) {
			return isTerrain(world.getBlockState(pos.down()));
		}
		else {
			BlockState up = world.getBlockState(pos.up());
			BlockState down = world.getBlockState(pos.down());
			return up.getBlock() == this && down.getBlock() == this;
		}
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		if (state.get(SHAPE) == TripleShape.TOP) {
			return Lists.newArrayList(new ItemStack(EndItems.END_LILY_LEAF, MHelper.randRange(1, 2, MHelper.RANDOM)), new ItemStack(EndBlocks.END_LILY_SEED, MHelper.randRange(1, 2, MHelper.RANDOM)));
		}
		return Collections.emptyList();
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(EndBlocks.END_LILY_SEED);
	}
}
