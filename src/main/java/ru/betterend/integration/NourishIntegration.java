package ru.betterend.integration;

import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import ru.bclib.integration.ModIntegration;
import ru.bclib.util.TagHelper;
import ru.betterend.registry.EndItems;

public class NourishIntegration extends ModIntegration {
	public NourishIntegration() {
		super("nourish");
	}
	
	@Override
	public void init() {
		Tag.Named<Item> fats = getItemTag("fats");
		Tag.Named<Item> fruit = getItemTag("fruit");
		Tag.Named<Item> protein = getItemTag("protein");
		Tag.Named<Item> sweets = getItemTag("sweets");
		
		TagHelper.addTag(fats, EndItems.END_FISH_RAW, EndItems.END_FISH_COOKED);
		TagHelper.addTag(
			fruit,
			EndItems.SHADOW_BERRY_RAW,
			EndItems.SHADOW_BERRY_COOKED,
			EndItems.BLOSSOM_BERRY,
			EndItems.SHADOW_BERRY_JELLY,
			EndItems.SWEET_BERRY_JELLY,
			EndItems.BLOSSOM_BERRY_JELLY,
			EndItems.AMBER_ROOT_RAW,
			EndItems.CHORUS_MUSHROOM_RAW,
			EndItems.CHORUS_MUSHROOM_COOKED,
			EndItems.BOLUX_MUSHROOM_COOKED
		);
		TagHelper.addTag(
			protein,
			EndItems.END_FISH_RAW,
			EndItems.END_FISH_COOKED,
			EndItems.CHORUS_MUSHROOM_COOKED,
			EndItems.BOLUX_MUSHROOM_COOKED,
			EndItems.CAVE_PUMPKIN_PIE
		);
		TagHelper.addTag(
			sweets,
			EndItems.SHADOW_BERRY_JELLY,
			EndItems.SWEET_BERRY_JELLY,
			EndItems.BLOSSOM_BERRY_JELLY,
			EndItems.CAVE_PUMPKIN_PIE
		);
	}
}
