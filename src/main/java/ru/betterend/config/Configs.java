package ru.betterend.config;

public class Configs {
	public static final PathConfig ENTITY_CONFIG = new PathConfig("entities");
	public static final PathConfig BLOCK_CONFIG = new PathConfig("blocks");
	public static final PathConfig ITEM_CONFIG = new PathConfig("items");
	public static final IdConfig BIOME_CONFIG = new EntryConfig("biomes");
	public static final PathConfig GENERATOR_CONFIG = new PathConfig("generator");
	
	public static void saveConfigs() {
		ENTITY_CONFIG.saveChanges();
		BLOCK_CONFIG.saveChanges();
		BIOME_CONFIG.saveChanges();
		ITEM_CONFIG.saveChanges();
		GENERATOR_CONFIG.saveChanges();
	}
}
