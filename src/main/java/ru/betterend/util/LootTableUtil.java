package ru.betterend.util;

import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.world.item.Items;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.resources.ResourceLocation;
import ru.betterend.registry.EndItems;

public class LootTableUtil {
	private static final ResourceLocation END_CITY_TREASURE_ID = new ResourceLocation("chests/end_city_treasure");

	public static void init() {
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
			if (END_CITY_TREASURE_ID.equals(id)) {
				FabricLootPoolBuilder builder = FabricLootPoolBuilder.builder();
				builder.rolls(UniformLootTableRange.between(0, 5));
				builder.withCondition(RandomChanceLootCondition.builder(0.5f).build());
				builder.withEntry(ItemEntry.builder(Items.GHAST_TEAR).build());
				builder.withEntry(ItemEntry.builder(EndItems.MUSIC_DISC_STRANGE_AND_ALIEN).build());
				supplier.pool(builder);
			}
		});
	}
}
