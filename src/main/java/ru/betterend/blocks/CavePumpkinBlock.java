package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import ru.betterend.blocks.basis.BlockBaseNotFull;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.registry.EndBlocks;

public class CavePumpkinBlock extends BlockBaseNotFull implements IRenderTypeable {
	public static final BooleanProperty SMALL = BlockProperties.SMALL;
	private static final VoxelShape SHAPE_SMALL;
	private static final VoxelShape SHAPE_BIG;
	
	public CavePumpkinBlock() {
		super(FabricBlockSettings.copyOf(Blocks.PUMPKIN).luminance((state) -> state.get(SMALL) ? 10 : 15));
		setDefaultState(getDefaultState().with(SMALL, false));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(SMALL);
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return state.get(SMALL) ? SHAPE_SMALL : SHAPE_BIG;
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return state.get(SMALL) ? Collections.singletonList(new ItemStack(EndBlocks.CAVE_PUMPKIN_SEED)) : Collections.singletonList(new ItemStack(this));
	}
	
	static {
		VoxelShape lantern = Block.createCuboidShape(1, 0, 1, 15, 13, 15);
		VoxelShape cap = Block.createCuboidShape(0, 12, 0, 16, 15, 16);
		VoxelShape top = Block.createCuboidShape(5, 15, 5, 11, 16, 11);
		SHAPE_BIG = VoxelShapes.union(lantern, cap, top);
		
		lantern = Block.createCuboidShape(1, 7, 1, 15, 13, 15);
		cap = Block.createCuboidShape(4, 12, 4, 12, 15, 12);
		top = Block.createCuboidShape(6, 15, 6, 10, 16, 10);
		SHAPE_SMALL = VoxelShapes.union(lantern, cap, top);
	}
}
