package ru.betterend.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.bclib.BCLib;
import ru.bclib.config.EntryConfig;
import ru.bclib.config.IdConfig;
import ru.bclib.config.PathConfig;
import ru.betterend.BetterEnd;

public class Configs {
	public static final PathConfig ENTITY_CONFIG = new PathConfig(BetterEnd.MOD_ID, "entities");
	public static final PathConfig BLOCK_CONFIG = new PathConfig(BetterEnd.MOD_ID, "blocks");
	public static final PathConfig ITEM_CONFIG = new PathConfig(BetterEnd.MOD_ID, "items");
	public static final IdConfig BIOME_CONFIG = new EntryConfig(BetterEnd.MOD_ID, "biomes");
	public static final PathConfig GENERATOR_CONFIG = new PathConfig(BetterEnd.MOD_ID, "generator");
	public static final PathConfig RECIPE_CONFIG = new PathConfig(BetterEnd.MOD_ID, "recipes");
	
	@Environment(value = EnvType.CLIENT)
	public static final PathConfig CLENT_CONFIG = new PathConfig(BetterEnd.MOD_ID, "client");
	
	public static void saveConfigs() {
		ENTITY_CONFIG.saveChanges();
		BLOCK_CONFIG.saveChanges();
		BIOME_CONFIG.saveChanges();
		ITEM_CONFIG.saveChanges();
		GENERATOR_CONFIG.saveChanges();
		RECIPE_CONFIG.saveChanges();
		
		if (BCLib.isClient()) {
			CLENT_CONFIG.saveChanges();
		}
	}
}
