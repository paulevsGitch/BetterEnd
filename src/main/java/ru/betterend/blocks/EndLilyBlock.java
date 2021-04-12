package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.ShapeContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.UnderwaterPlantBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;
import ru.betterend.util.MHelper;

public class EndLilyBlock extends UnderwaterPlantBlock {
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	private static final VoxelShape SHAPE_BOTTOM = Block.createCuboidShape(4, 0, 4, 12, 16, 12);
	private static final VoxelShape SHAPE_TOP = Block.createCuboidShape(2, 0, 2, 14, 6, 14);

	public EndLilyBlock() {
		super(FabricBlockSettings.of(Material.UNDERWATER_PLANT).breakByTool(FabricToolTags.SHEARS)
				.sounds(SoundType.WET_GRASS).breakByHand(true).luminance((state) -> {
					return state.getValue(SHAPE) == TripleShape.TOP ? 13 : 0;
				}).noCollision());
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world,
			BlockPos pos, BlockPos neighborPos) {
		if (!canPlaceAt(state, world, pos)) {
			return state.getValue(SHAPE) == TripleShape.TOP ? Blocks.AIR.defaultBlockState()
					: Blocks.WATER.defaultBlockState();
		} else {
			return state;
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		Vec3d vec3d = state.getModelOffset(view, pos);
		VoxelShape shape = state.getValue(SHAPE) == TripleShape.TOP ? SHAPE_TOP : SHAPE_BOTTOM;
		return shape.offset(vec3d.x, vec3d.y, vec3d.z);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(SHAPE) == TripleShape.TOP ? Fluids.EMPTY.defaultBlockState()
				: Fluids.WATER.getStill(false);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		if (state.getValue(SHAPE) == TripleShape.TOP) {
			return world.getBlockState(pos.below()).getBlock() == this;
		} else if (state.getValue(SHAPE) == TripleShape.BOTTOM) {
			return isTerrain(world.getBlockState(pos.below()));
		} else {
			BlockState up = world.getBlockState(pos.up());
			BlockState down = world.getBlockState(pos.below());
			return up.getBlock() == this && down.getBlock() == this;
		}
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (state.getValue(SHAPE) == TripleShape.TOP) {
			return Lists.newArrayList(new ItemStack(EndItems.END_LILY_LEAF, MHelper.randRange(1, 2, MHelper.RANDOM)),
					new ItemStack(EndBlocks.END_LILY_SEED, MHelper.randRange(1, 2, MHelper.RANDOM)));
		}
		return Collections.emptyList();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(EndBlocks.END_LILY_SEED);
	}

	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return false;
	}

	@Override
	public boolean canGrow(Level world, Random random, BlockPos pos, BlockState state) {
		return false;
	}
}
