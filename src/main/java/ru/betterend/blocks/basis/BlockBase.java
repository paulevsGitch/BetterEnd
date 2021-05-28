package ru.betterend.blocks.basis;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.bclib.client.models.BlockModelProvider;

public class BlockBase extends Block implements BlockModelProvider {
	public BlockBase(Properties settings) {
		super(settings);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}

	@Override
	public BlockModel getItemModel(ResourceLocation blockId) {
		return getBlockModel(blockId, defaultBlockState());
	}
}