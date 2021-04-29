package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.betterend.blocks.BlockProperties.PentaShape;
import ru.betterend.blocks.basis.EndPlantBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.MHelper;

public class LanceleafBlock extends EndPlantBlock {
	public static final EnumProperty<PentaShape> SHAPE = BlockProperties.PENTA_SHAPE;
	public static final IntegerProperty ROTATION = BlockProperties.ROTATION;
	
	public LanceleafBlock() {
		super();
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE, ROTATION);
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		PentaShape shape = state.getValue(SHAPE);
		if (shape == PentaShape.TOP) {
			return world.getBlockState(pos.below()).is(this);
		}
		else if (shape == PentaShape.BOTTOM) {
			return world.getBlockState(pos.below()).is(EndBlocks.AMBER_MOSS) && world.getBlockState(pos.above()).is(this);
		}
		else {
			return world.getBlockState(pos.below()).is(this) && world.getBlockState(pos.above()).is(this);
		}
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (!canSurvive(state, world, pos)) {
			return Blocks.AIR.defaultBlockState();
		}
		else {
			return state;
		}
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (state.getValue(SHAPE) == PentaShape.BOTTOM) {
			return Collections.singletonList(new ItemStack(EndBlocks.LANCELEAF_SEED));
		}
		return MHelper.RANDOM.nextBoolean() ? Collections.emptyList() : Collections.singletonList(new ItemStack(EndBlocks.LANCELEAF_SEED));
	}
}
