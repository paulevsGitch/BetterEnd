package ru.betterend.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.BlockVine;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;
import ru.betterend.util.MHelper;

public class BlockBulbVine extends BlockVine {
	public BlockBulbVine() {
		super(15, true);
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		if (state.get(SHAPE) == TripleShape.BOTTOM) {
			return Lists.newArrayList(new ItemStack(EndItems.GLOWING_BULB));
		}
		else if (MHelper.RANDOM.nextInt(8) == 0) {
			return Lists.newArrayList(new ItemStack(EndBlocks.BULB_VINE_SEED));
		}
		else {
			return Lists.newArrayList();
		}
	}
}
