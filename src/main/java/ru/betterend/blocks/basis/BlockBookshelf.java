package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.patterns.Patterns;

public class BlockBookshelf extends BlockBase {
	public BlockBookshelf(Block source) {
		super(FabricBlockSettings.copyOf(source));
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.get(LootContextParameters.TOOL);
		if (tool != null && tool.isEffectiveOn(state)) {
			int silk = EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, tool);
			if (silk > 0) {
				return Collections.singletonList(new ItemStack(this));
			}
		}
		return Collections.singletonList(new ItemStack(Items.BOOK, 3));
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_SIMPLE;
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterns.createJson(Patterns.BLOCK_BOOKSHELF, getName(blockId), blockId.getPath());
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterns.createJson(data, getName(blockId), blockId.getPath());
	}
	
	private String getName(Identifier blockId) {
		String name = blockId.getPath();
		return name.replace("_bookshelf", "");
	}
}
