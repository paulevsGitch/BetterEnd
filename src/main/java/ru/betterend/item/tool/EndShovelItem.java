package ru.betterend.item.tool;

import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.fabricmc.fabric.impl.tool.attribute.ToolManagerImpl;
import net.fabricmc.fabric.impl.tool.attribute.ToolManagerImpl.Entry;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.state.BlockState;
import ru.betterend.client.models.ModelProvider;
import ru.betterend.client.models.Patterns;

import java.util.Optional;

public class EndShovelItem extends ShovelItem implements DynamicAttributeTool, ModelProvider {
	public EndShovelItem(Tier material, float attackDamage, float attackSpeed, Properties settings) {
		super(material, attackDamage, attackSpeed, settings);
	}
	
	@Override
	public int getMiningLevel(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		if (tag.equals(FabricToolTags.SHOVELS)) {
			return this.getTier().getLevel();
		}
		return 0;
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Entry entry = ToolManagerImpl.entryNullable(state.getBlock());
		return (entry != null && entry.getMiningLevel(FabricToolTags.SHOVELS) >= 0) ? this.speed : super.getDestroySpeed(stack, state);
	}
	
	@Override
	public Optional<String> getModelString(String name) {
		return Patterns.createJson(Patterns.ITEM_HANDHELD, name);
	}
}
