package ru.betterend.util;

import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.RandomValueBounds;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import ru.betterend.registry.EndItems;

public class LootTableUtil {
	private static final ResourceLocation END_CITY_TREASURE_ID = new ResourceLocation("chests/end_city_treasure");

	public static void init() {
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
			if (END_CITY_TREASURE_ID.equals(id)) {
				FabricLootPoolBuilder builder = FabricLootPoolBuilder.builder();
				builder.setRolls(RandomValueBounds.between(0, 5));
				builder.withCondition(LootItemRandomChanceCondition.randomChance(0.5f).build());
				builder.withEntry(LootItem.lootTableItem(Items.GHAST_TEAR).build());
				supplier.withPool(builder);
				
				builder = FabricLootPoolBuilder.builder();
				builder.setRolls(RandomValueBounds.between(0, 5));
				builder.withCondition(LootItemRandomChanceCondition.randomChance(0.05f).build());
				builder.withEntry(LootItem.lootTableItem(EndItems.MUSIC_DISC_STRANGE_AND_ALIEN).build());
				builder.withEntry(LootItem.lootTableItem(EndItems.MUSIC_DISC_GRASPING_AT_STARS).build());
				builder.withEntry(LootItem.lootTableItem(EndItems.MUSIC_DISC_ENDSEEKER).build());
				builder.withEntry(LootItem.lootTableItem(EndItems.MUSIC_DISC_EO_DRACONA).build());
				supplier.withPool(builder);
			}
		});
	}
}
