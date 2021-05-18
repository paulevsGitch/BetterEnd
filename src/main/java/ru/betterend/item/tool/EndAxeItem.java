package ru.betterend.item.tool;

import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.state.BlockState;
import ru.betterend.client.models.ModelProvider;
import ru.betterend.client.models.Patterns;

import java.util.Optional;

public class EndAxeItem extends AxeItem implements DynamicAttributeTool, ModelProvider {
	public EndAxeItem(Tier material, float attackDamage, float attackSpeed, Properties settings) {
		super(material, attackDamage, attackSpeed, settings);
	}

	@Override
	public int getMiningLevel(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		if (tag.equals(FabricToolTags.AXES)) {
			return this.getTier().getLevel();
		}
		return 0;
	}
	
	@Override
	public Optional<String> getModelString(String name) {
		return Patterns.createJson(Patterns.ITEM_HANDHELD, name);
	}
}
