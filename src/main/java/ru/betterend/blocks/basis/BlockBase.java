package ru.betterend.blocks.basis;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.Patterns;

public class BlockBase extends Block implements BlockModelProvider {
	public BlockBase(Properties settings) {
		super(settings);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}

	@Override
	public Optional<String> getModelString(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		return Patterns.createBlockSimple(blockId.getPath());
	}

	@Override
	public BlockModel getModel(ResourceLocation blockId) {
		return getBlockModel(blockId, defaultBlockState());
	}

}