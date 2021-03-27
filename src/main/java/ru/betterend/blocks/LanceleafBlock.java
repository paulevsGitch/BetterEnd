package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties.PentaShape;
import ru.betterend.blocks.basis.EndPlantBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.MHelper;

public class LanceleafBlock extends EndPlantBlock {
	public static final EnumProperty<PentaShape> SHAPE = BlockProperties.PENTA_SHAPE;
	public static final IntProperty ROTATION = BlockProperties.ROTATION;
	
	public LanceleafBlock() {
		super();
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE, ROTATION);
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		PentaShape shape = state.get(SHAPE);
		if (shape == PentaShape.TOP) {
			return world.getBlockState(pos.down()).isOf(this);
		}
		else if (shape == PentaShape.BOTTOM) {
			return world.getBlockState(pos.down()).isOf(EndBlocks.AMBER_MOSS) && world.getBlockState(pos.up()).isOf(this);
		}
		else {
			return world.getBlockState(pos.down()).isOf(this) && world.getBlockState(pos.up()).isOf(this);
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
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		if (state.get(SHAPE) == PentaShape.BOTTOM) {
			return Collections.singletonList(new ItemStack(EndBlocks.LANCELEAF_SEED));
		}
		return MHelper.RANDOM.nextBoolean() ? Collections.emptyList() : Collections.singletonList(new ItemStack(EndBlocks.LANCELEAF_SEED));
	}
}
