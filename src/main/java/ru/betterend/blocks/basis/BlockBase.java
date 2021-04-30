package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class BlockBase extends Block implements BlockPatterned {
	public BlockBase(Properties settings) {
		super(settings);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}

	@Override
	public BlockModel getBlockModel(BlockState state) {
		return null;
	}

	@Override
	public String getStatesPattern(Reader data) {
		String block = Registry.BLOCK.getKey(this).getPath();
		return Patterns.createJson(data, block, block);
	}

	@Override
	public String getModelPattern(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		return Patterns.createJson(Patterns.BLOCK_BASE, blockId.getPath(), block);
	}

	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_SIMPLE;
	}
}