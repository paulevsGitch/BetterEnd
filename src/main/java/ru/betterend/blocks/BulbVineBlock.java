package ru.betterend.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.bclib.blocks.BaseVineBlock;
import ru.bclib.blocks.BlockProperties.TripleShape;
import ru.bclib.util.MHelper;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class BulbVineBlock extends BaseVineBlock {
	public BulbVineBlock() {
		super(15, true);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (state.getValue(SHAPE) == TripleShape.BOTTOM) {
			return Lists.newArrayList(new ItemStack(EndItems.GLOWING_BULB));
		}
		else if (MHelper.RANDOM.nextInt(8) == 0) {
			return Lists.newArrayList(new ItemStack(EndBlocks.BULB_VINE_SEED));
		}
		else {
			return Lists.newArrayList();
		}
	}
	
	@Override
	public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
		return false;
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		boolean canPlace = super.canSurvive(state, world, pos);
		return (state.is(this) && state.getValue(SHAPE) == TripleShape.BOTTOM) ? canPlace : canPlace && world.getBlockState(pos.below()).is(this);
	}
}
