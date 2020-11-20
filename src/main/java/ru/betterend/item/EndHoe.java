package ru.betterend.item;

import net.minecraft.item.HoeItem;
import net.minecraft.item.ToolMaterial;
import ru.betterend.patterns.Patterned;
import ru.betterend.patterns.Patterns;

public class EndHoe extends HoeItem implements Patterned {

	public EndHoe(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
		super(material, attackDamage, attackSpeed, settings);
	}
	
	@Override
	public String getModelPattern(String name) {
		return Patterns.createJson(Patterns.ITEM_HANDHELD, name);
	}
}
