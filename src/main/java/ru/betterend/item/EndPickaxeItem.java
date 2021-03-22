package ru.betterend.item;

import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.fabricmc.fabric.impl.tool.attribute.ToolManagerImpl;
import net.fabricmc.fabric.impl.tool.attribute.ToolManagerImpl.Entry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.Tag;
import ru.betterend.patterns.Patterned;
import ru.betterend.patterns.Patterns;

public class EndPickaxeItem extends PickaxeItem implements DynamicAttributeTool, Patterned {
	public EndPickaxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
		super(material, attackDamage, attackSpeed, settings);
	}
	
	@Override
	public int getMiningLevel(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		if (tag.equals(FabricToolTags.PICKAXES)) {
			return this.getMaterial().getMiningLevel();
		}
		return 0;
	}
	
	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		if (this.getMaterial().getMiningLevel() > 2 && state.getMaterial().equals(Blocks.END_STONE.getDefaultState().getMaterial())) {
			return this.miningSpeed * 3;
		}
		Entry entry = ToolManagerImpl.entryNullable(state.getBlock());
		return (entry != null && entry.getMiningLevel(FabricToolTags.PICKAXES) >= 0) ? this.miningSpeed : super.getMiningSpeedMultiplier(stack, state);
	}
	
	@Override
	public String getModelPattern(String name) {
		return Patterns.createJson(Patterns.ITEM_HANDHELD, name);
	}
}
