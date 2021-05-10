package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import ru.betterend.patterns.Patterns;

public class EndBookshelfBlock extends BlockBase {
	public EndBookshelfBlock(Block source) {
		super(FabricBlockSettings.copyOf(source));
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.getParameter(LootContextParams.TOOL);
		if (tool != null && tool.isCorrectToolForDrops(state)) {
			int silk = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool);
			if (silk > 0) {
				return Collections.singletonList(new ItemStack(this));
			}
		}
		return Collections.singletonList(new ItemStack(Items.BOOK, 3));
	}
	
	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_SIMPLE;
	}
	
	@Override
	public String getModelString(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		return Patterns.createJson(Patterns.BLOCK_BOOKSHELF, getName(blockId), blockId.getPath());
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		return Patterns.createJson(data, getName(blockId), blockId.getPath());
	}
	
	private String getName(ResourceLocation blockId) {
		String name = blockId.getPath();
		return name.replace("_bookshelf", "");
	}
}
