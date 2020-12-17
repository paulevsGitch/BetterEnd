package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;

public class BlockSilkMothNest extends BlockBase implements IRenderTypeable {
	public static final BooleanProperty ACTIVE = BlockProperties.ACTIVE;
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	private static final VoxelShape TOP = createCuboidShape(6, 0, 6, 10, 16, 10);
	private static final VoxelShape BOTTOM = createCuboidShape(0, 0, 0, 16, 16, 16);
	
	public BlockSilkMothNest() {
		super(FabricBlockSettings.of(Material.WOOL).hardness(0.5F).resistance(0.1F).nonOpaque());
		this.setDefaultState(getDefaultState().with(ACTIVE, true));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(ACTIVE, FACING);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return state.get(ACTIVE) ? BOTTOM : TOP;
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction dir = ctx.getPlayerFacing().getOpposite();
		return this.getDefaultState().with(FACING, dir);
	}
}
