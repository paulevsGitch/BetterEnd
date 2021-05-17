package ru.betterend.item.tool;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Tier;
import ru.betterend.client.models.ModelProvider;
import ru.betterend.client.models.Patterns;

public class EndHoeItem extends HoeItem implements ModelProvider {
	public EndHoeItem(Tier material, int attackDamage, float attackSpeed, Properties settings) {
		super(material, attackDamage, attackSpeed, settings);
	}
	
	@Override
	public String getModelString(String name) {
		return Patterns.createJson(Patterns.ITEM_HANDHELD, name);
	}
}
