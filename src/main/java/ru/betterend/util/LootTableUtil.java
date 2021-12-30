package ru.betterend.util;

import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class LootTableUtil {
	private static final ResourceLocation END_CITY_TREASURE_ID = new ResourceLocation("chests/end_city_treasure");
	public static final ResourceLocation COMMON = BetterEnd.makeID("chests/common");
	
	public static void init() {
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, table, setter) -> {
			if (END_CITY_TREASURE_ID.equals(id)) {
				FabricLootPoolBuilder builder = FabricLootPoolBuilder.builder();
				builder.setRolls(ConstantValue.exactly(1));
				builder.withCondition(LootItemRandomChanceCondition.randomChance(0.2f).build());
				builder.withEntry(LootItem.lootTableItem(Items.GHAST_TEAR).build());
				table.withPool(builder);
				
				builder = FabricLootPoolBuilder.builder();
				builder.setRolls(UniformGenerator.between(0, 3));
				builder.withEntry(LootItem.lootTableItem(EndItems.MUSIC_DISC_STRANGE_AND_ALIEN).build());
				builder.withEntry(LootItem.lootTableItem(EndItems.MUSIC_DISC_GRASPING_AT_STARS).build());
				builder.withEntry(LootItem.lootTableItem(EndItems.MUSIC_DISC_ENDSEEKER).build());
				builder.withEntry(LootItem.lootTableItem(EndItems.MUSIC_DISC_EO_DRACONA).build());
				table.withPool(builder);
			}
			
			else if (COMMON.equals(id)) {
				addCommonItems(table);
			}
		});
	}
	
	private static void addCommonItems(FabricLootSupplierBuilder table) {
		FabricLootPoolBuilder builder = FabricLootPoolBuilder.builder();
		builder.setRolls(UniformGenerator.between(0, 2));
		builder.withEntry(LootItem.lootTableItem(EndItems.MUSIC_DISC_STRANGE_AND_ALIEN).build());
		builder.withEntry(LootItem.lootTableItem(EndItems.MUSIC_DISC_GRASPING_AT_STARS).build());
		builder.withEntry(LootItem.lootTableItem(EndItems.MUSIC_DISC_ENDSEEKER).build());
		builder.withEntry(LootItem.lootTableItem(EndItems.MUSIC_DISC_EO_DRACONA).build());
		table.withPool(builder);
		
		builder = FabricLootPoolBuilder.builder();
		builder.setRolls(UniformGenerator.between(4, 8));
		builder.withEntry(LootItem.lootTableItem(EndBlocks.THALLASIUM.ingot).build());
		builder.withEntry(LootItem.lootTableItem(EndBlocks.THALLASIUM.rawOre).build());
		builder.withEntry(LootItem.lootTableItem(Items.ENDER_PEARL).build());
		table.withPool(builder);
		
		builder = FabricLootPoolBuilder.builder();
		builder.setRolls(UniformGenerator.between(2, 4));
		builder.withEntry(LootItem.lootTableItem(EndBlocks.TERMINITE.ingot).build());
		builder.withEntry(LootItem.lootTableItem(EndItems.ENDER_SHARD).build());
		builder.withEntry(LootItem.lootTableItem(EndBlocks.AURORA_CRYSTAL).build());
		builder.withEntry(LootItem.lootTableItem(EndBlocks.THALLASIUM.axe).build());
		builder.withEntry(LootItem.lootTableItem(EndBlocks.THALLASIUM.pickaxe).build());
		builder.withEntry(LootItem.lootTableItem(EndBlocks.THALLASIUM.hoe).build());
		builder.withEntry(LootItem.lootTableItem(EndBlocks.THALLASIUM.sword).build());
		builder.withEntry(LootItem.lootTableItem(EndBlocks.THALLASIUM.shovel).build());
		builder.withEntry(LootItem.lootTableItem(Items.ENDER_EYE).build());
		builder.withEntry(LootItem.lootTableItem(Blocks.OBSIDIAN).build());
		table.withPool(builder);
		
		builder = FabricLootPoolBuilder.builder();
		builder.setRolls(UniformGenerator.between(0, 4));
		builder.withEntry(LootItem.lootTableItem(EndItems.AETERNIUM_INGOT).build());
		builder.withEntry(LootItem.lootTableItem(EndItems.AMBER_GEM).build());
		builder.withEntry(LootItem.lootTableItem(Items.END_CRYSTAL).build());
		builder.withEntry(LootItem.lootTableItem(Items.GHAST_TEAR).build());
		table.withPool(builder);
	}
}
