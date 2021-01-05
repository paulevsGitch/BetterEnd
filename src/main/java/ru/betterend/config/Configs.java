package ru.betterend.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.betterend.BetterEnd;

public class Configs {
	public static final PathConfig ENTITY_CONFIG = new PathConfig("entities");
	public static final PathConfig BLOCK_CONFIG = new PathConfig("blocks");
	public static final PathConfig ITEM_CONFIG = new PathConfig("items");
	public static final IdConfig BIOME_CONFIG = new EntryConfig("biomes");
	public static final PathConfig GENERATOR_CONFIG = new PathConfig("generator");
	public static final PathConfig RECIPE_CONFIG = new PathConfig("recipes");
	
	@Environment(value = EnvType.CLIENT)
	public static final PathConfig CLENT_CONFIG = new PathConfig("client");
	
	public static void saveConfigs() {
		ENTITY_CONFIG.saveChanges();
		BLOCK_CONFIG.saveChanges();
		BIOME_CONFIG.saveChanges();
		ITEM_CONFIG.saveChanges();
		GENERATOR_CONFIG.saveChanges();
		RECIPE_CONFIG.saveChanges();
		
		if (BetterEnd.isClient()) {
			CLENT_CONFIG.saveChanges();
		}
	}
}
