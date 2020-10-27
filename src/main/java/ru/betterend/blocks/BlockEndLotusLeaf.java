package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.BlockBaseNotFull;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.util.BlocksHelper;

public class BlockEndLotusLeaf extends BlockBaseNotFull implements IRenderTypeable {
	public static final EnumProperty<Direction> HORIZONTAL_FACING = Properties.HORIZONTAL_FACING;
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	private static final VoxelShape VSHAPE = Block.createCuboidShape(0, 0, 0, 16, 1, 16);
	
	public BlockEndLotusLeaf() {
		super(FabricBlockSettings.of(Material.PLANT).nonOpaque().sounds(BlockSoundGroup.WET_GRASS));
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockState down = world.getBlockState(pos.down());
		return !down.getFluidState().isEmpty() && down.getFluidState().getFluid() instanceof WaterFluid;
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(SHAPE, HORIZONTAL_FACING);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return VSHAPE;
	}
	
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return BlocksHelper.rotateHorizontal(state, rotation, HORIZONTAL_FACING);
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return BlocksHelper.mirrorHorizontal(state, mirror, HORIZONTAL_FACING);
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
}
