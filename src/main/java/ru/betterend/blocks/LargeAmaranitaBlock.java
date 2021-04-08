package ru.betterend.blocks;

import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.ShapeContext;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.EndPlantBlock;
import ru.betterend.registry.EndBlocks;

public class LargeAmaranitaBlock extends EndPlantBlock {
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	private static final VoxelShape SHAPE_BOTTOM = Block.createCuboidShape(4, 0, 4, 12, 14, 12);
	private static final VoxelShape SHAPE_TOP = VoxelShapes.union(Block.createCuboidShape(1, 3, 1, 15, 16, 15),
			SHAPE_BOTTOM);

	public LargeAmaranitaBlock() {
		super(FabricBlockSettings.of(Material.PLANT)
				.luminance((state) -> (state.getValue(SHAPE) == TripleShape.TOP) ? 15 : 0)
				.breakByTool(FabricToolTags.SHEARS).sounds(SoundType.GRASS).breakByHand(true));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return state.getValue(SHAPE) == TripleShape.TOP ? SHAPE_TOP : SHAPE_BOTTOM;
	}

	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(EndBlocks.SANGNUM) || state.is(EndBlocks.MOSSY_OBSIDIAN)
				|| state.is(EndBlocks.MOSSY_DRAGON_BONE);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		TripleShape shape = state.getValue(SHAPE);
		if (shape == TripleShape.BOTTOM) {
			return isTerrain(world.getBlockState(pos.below())) && world.getBlockState(pos.up()).is(this);
		} else if (shape == TripleShape.TOP) {
			return world.getBlockState(pos.below()).is(this);
		} else {
			return world.getBlockState(pos.below()).is(this) && world.getBlockState(pos.up()).is(this);
		}
	}

	@Override
	public OffsetType getOffsetType() {
		return OffsetType.NONE;
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
