package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties.LumecornShape;
import ru.betterend.blocks.basis.BaseBlockNotFull;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.registry.EndTags;

public class LumecornBlock extends BaseBlockNotFull implements IRenderTypeable {
	public static final EnumProperty<LumecornShape> SHAPE = EnumProperty.of("shape", LumecornShape.class);
	private static final VoxelShape SHAPE_BIG = Block.createCuboidShape(5, 0, 5, 11, 16, 11);
	private static final VoxelShape SHAPE_MEDIUM = Block.createCuboidShape(6, 0, 6, 10, 16, 10);
	private static final VoxelShape SHAPE_SMALL = Block.createCuboidShape(7, 0, 7, 9, 16, 9);
	
	public LumecornBlock() {
		super(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).hardness(0.5F).luminance((state) -> {
			return state.get(SHAPE).getLight();
		}));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		LumecornShape shape = state.get(SHAPE);
		if (shape == LumecornShape.LIGHT_MIDDLE) {
			return SHAPE_SMALL;
		}
		else if (shape == LumecornShape.LIGHT_TOP_MIDDLE) {
			return SHAPE_MEDIUM;
		}
		else {
			return SHAPE_BIG;
		}
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		LumecornShape shape = state.get(SHAPE);
		if (shape == LumecornShape.BOTTOM_BIG || shape == LumecornShape.BOTTOM_SMALL) {
			return world.getBlockState(pos.down()).isIn(EndTags.END_GROUND);
		}
		else {
			return world.getBlockState(pos.down()).isOf(this);
		}
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!canPlaceAt(state, world, pos)) {
			return Blocks.AIR.getDefaultState();
		}
		else {
			return state;
		}
	}
}
