package ru.betterend.integration;

import net.minecraft.world.item.Item;
import ru.bclib.api.tag.TagAPI;
import ru.bclib.api.tag.TagAPI.TagLocation;
import ru.bclib.integration.modmenu.ModIntegration;
import ru.betterend.registry.EndItems;

public class NourishIntegration extends ModIntegration {
	public NourishIntegration() {
		super("nourish");
	}
	
	@Override
	public void init() {
		TagLocation<Item> fats = TagLocation.of(getItemTag("fats"));
		TagLocation<Item> fruit = TagLocation.of(getItemTag("fruit"));
		TagLocation<Item> protein = TagLocation.of(getItemTag("protein"));
		TagLocation<Item> sweets = TagLocation.of(getItemTag("sweets"));
		
		TagAPI.addItemTag(fats, EndItems.END_FISH_RAW, EndItems.END_FISH_COOKED);
		TagAPI.addItemTag(
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
		TagAPI.addItemTag(
			protein,
			EndItems.END_FISH_RAW,
			EndItems.END_FISH_COOKED,
			EndItems.CHORUS_MUSHROOM_COOKED,
			EndItems.BOLUX_MUSHROOM_COOKED,
			EndItems.CAVE_PUMPKIN_PIE
		);
		TagAPI.addItemTag(
			sweets,
			EndItems.SHADOW_BERRY_JELLY,
			EndItems.SWEET_BERRY_JELLY,
			EndItems.BLOSSOM_BERRY_JELLY,
			EndItems.CAVE_PUMPKIN_PIE
		);
	}
}
