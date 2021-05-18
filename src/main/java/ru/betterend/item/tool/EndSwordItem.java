package ru.betterend.item.tool;

import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import ru.betterend.client.models.ModelProvider;
import ru.betterend.client.models.Patterns;

import java.util.Optional;

public class EndSwordItem extends SwordItem implements DynamicAttributeTool, ModelProvider {
	public EndSwordItem(Tier material, int attackDamage, float attackSpeed, Properties settings) {
		super(material, attackDamage, attackSpeed, settings);
	}
	
	@Override
	public Optional<String> getModelString(String name) {
		return Patterns.createJson(Patterns.ITEM_HANDHELD, name);
	}
}
