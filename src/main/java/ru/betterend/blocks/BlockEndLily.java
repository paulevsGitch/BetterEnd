package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.BlockUnderwaterPlant;

public class BlockEndLily extends BlockUnderwaterPlant {
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	private static final VoxelShape SHAPE_BOTTOM = Block.createCuboidShape(4, 0, 4, 12, 16, 12);
	private static final VoxelShape SHAPE_TOP = Block.createCuboidShape(2, 0, 2, 14, 6, 14);
	
	public BlockEndLily() {
		super(FabricBlockSettings.of(Material.UNDERWATER_PLANT)
				.breakByTool(FabricToolTags.SHEARS)
				.sounds(BlockSoundGroup.WET_GRASS)
				.breakByHand(true)
				.lightLevel((state) -> { return state.get(SHAPE) == TripleShape.TOP ? 13 : 0; })
				.noCollision());
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
	protected boolean isTerrain(BlockState state) {
		return super.isTerrain(state) || state.getBlock() == this;
	}
}
