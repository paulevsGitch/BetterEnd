package ru.betterend.util;

import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.complexmaterials.WoodenComplexMaterial;
import ru.bclib.world.biomes.BCLBiome;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class LootTableUtil {
	private static final ResourceLocation END_CITY_TREASURE_ID = new ResourceLocation("chests/end_city_treasure");
	private static final ResourceLocation COMMON = BetterEnd.makeID("chests/common");
	private static final ResourceLocation FOGGY_MUSHROOMLAND = BetterEnd.makeID("chests/foggy_mushroomland");
	private static final ResourceLocation CHORUS_FOREST = BetterEnd.makeID("chests/chorus_forest");
	private static final ResourceLocation SHADOW_FOREST = BetterEnd.makeID("chests/shadow_forest");
	private static final ResourceLocation LANTERN_WOODS = BetterEnd.makeID("chests/lantern_woods");
	private static final ResourceLocation UMBRELLA_JUNGLE = BetterEnd.makeID("chests/umbrella_jungle");
	
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
			else if (id.getNamespace().equals(BetterEnd.MOD_ID)) {
				addCommonItems(table);
				if (FOGGY_MUSHROOMLAND.equals(id)) {
					FabricLootPoolBuilder builder = FabricLootPoolBuilder.builder();
					builder.setRolls(UniformGenerator.between(4, 8));
					builder.withEntry(LootItem.lootTableItem(EndBlocks.MOSSY_GLOWSHROOM.getBlock(WoodenComplexMaterial.BLOCK_PLANKS)).build());
					builder.withEntry(LootItem.lootTableItem(EndBlocks.MOSSY_GLOWSHROOM_SAPLING).build());
					builder.withEntry(LootItem.lootTableItem(EndBlocks.BLUE_VINE_SEED).build());
					table.withPool(builder);
				}
				else if (CHORUS_FOREST.equals(id)) {
					FabricLootPoolBuilder builder = FabricLootPoolBuilder.builder();
					builder.setRolls(UniformGenerator.between(4, 8));
					builder.withEntry(LootItem.lootTableItem(EndBlocks.PYTHADENDRON.getBlock(WoodenComplexMaterial.BLOCK_PLANKS)).build());
					builder.withEntry(LootItem.lootTableItem(EndBlocks.PYTHADENDRON_SAPLING).build());
					builder.withEntry(LootItem.lootTableItem(EndBlocks.CHORUS_MUSHROOM).build());
					table.withPool(builder);
				}
				else if (SHADOW_FOREST.equals(id)) {
					FabricLootPoolBuilder builder = FabricLootPoolBuilder.builder();
					builder.setRolls(UniformGenerator.between(4, 8));
					builder.withEntry(LootItem.lootTableItem(EndBlocks.DRAGON_TREE.getBlock(WoodenComplexMaterial.BLOCK_PLANKS)).build());
					builder.withEntry(LootItem.lootTableItem(EndBlocks.DRAGON_TREE_SAPLING).build());
					builder.withEntry(LootItem.lootTableItem(EndBlocks.SHADOW_BERRY).build());
					builder.withEntry(LootItem.lootTableItem(EndItems.SHADOW_BERRY_RAW).build());
					table.withPool(builder);
				}
				else if (LANTERN_WOODS.equals(id)) {
					FabricLootPoolBuilder builder = FabricLootPoolBuilder.builder();
					builder.setRolls(UniformGenerator.between(4, 8));
					builder.withEntry(LootItem.lootTableItem(EndBlocks.LUCERNIA.getBlock(WoodenComplexMaterial.BLOCK_PLANKS)).build());
					builder.withEntry(LootItem.lootTableItem(EndBlocks.LUCERNIA_SAPLING).build());
					builder.withEntry(LootItem.lootTableItem(EndBlocks.BOLUX_MUSHROOM).build());
					table.withPool(builder);
				}
				else if (UMBRELLA_JUNGLE.equals(id)) {
					FabricLootPoolBuilder builder = FabricLootPoolBuilder.builder();
					builder.setRolls(UniformGenerator.between(4, 8));
					builder.withEntry(LootItem.lootTableItem(EndBlocks.UMBRELLA_TREE.getBlock(WoodenComplexMaterial.BLOCK_PLANKS)).build());
					builder.withEntry(LootItem.lootTableItem(EndBlocks.UMBRELLA_TREE_SAPLING).build());
					builder.withEntry(LootItem.lootTableItem(EndBlocks.SMALL_JELLYSHROOM).build());
					table.withPool(builder);
				}
			}
		});
	}
	
	public static ResourceLocation getTable(Holder<Biome> biome) {
		BCLBiome bclBiome = BiomeAPI.getBiome(biome.value());
		if (bclBiome == EndBiomes.FOGGY_MUSHROOMLAND) {
			return FOGGY_MUSHROOMLAND;
		}
		else if (bclBiome == EndBiomes.CHORUS_FOREST) {
			return CHORUS_FOREST;
		}
		else if (bclBiome == EndBiomes.SHADOW_FOREST) {
			return SHADOW_FOREST;
		}
		else if (bclBiome == EndBiomes.LANTERN_WOODS) {
			return LANTERN_WOODS;
		}
		else if (bclBiome == EndBiomes.UMBRELLA_JUNGLE) {
			return UMBRELLA_JUNGLE;
		}
		return COMMON;
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
		builder.withEntry(LootItem.lootTableItem(EndBlocks.FLAVOLITE_RUNED).build());
		builder.withEntry(LootItem.lootTableItem(EndItems.AETERNIUM_INGOT).build());
		builder.withEntry(LootItem.lootTableItem(EndItems.AMBER_GEM).build());
		builder.withEntry(LootItem.lootTableItem(Items.END_CRYSTAL).build());
		builder.withEntry(LootItem.lootTableItem(Items.GHAST_TEAR).build());
		table.withPool(builder);
	}
}
