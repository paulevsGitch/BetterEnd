package ru.betterend.integration;

import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import ru.betterend.registry.EndItems;
import ru.betterend.util.TagHelper;

public class NourishIntegration extends ModIntegration {
	public NourishIntegration() {
		super("nourish");
	}

	@Override
	public void register() {
		Tag.Named<Item> fats = getItemTag("fats");
		Tag.Named<Item> fruit = getItemTag("fruit");
		Tag.Named<Item> protein = getItemTag("protein");
		Tag.Named<Item> sweets = getItemTag("sweets");

		TagHelper.addTag(fats, EndItems.END_FISH_RAW, EndItems.END_FISH_COOKED);
		TagHelper.addTag(fruit, EndItems.SHADOW_BERRY_RAW, EndItems.SHADOW_BERRY_COOKED, EndItems.BLOSSOM_BERRY,
				EndItems.SHADOW_BERRY_JELLY, EndItems.SWEET_BERRY_JELLY);
		TagHelper.addTag(protein, EndItems.END_FISH_RAW, EndItems.END_FISH_COOKED);
		TagHelper.addTag(sweets, EndItems.SHADOW_BERRY_JELLY, EndItems.SWEET_BERRY_JELLY);
	}
}
