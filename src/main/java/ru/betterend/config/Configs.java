package ru.betterend.config;

public class Configs {
	public static final IdConfig ITEM_CONFIG = new CategoryConfig("items");
	public static final IdConfig BLOCK_CONFIG = new CategoryConfig("blocks");
	public static final IdConfig ENTITY_CONFIG = new CategoryConfig("entities");
	public static final IdConfig BIOME_CONFIG = new EntryConfig("biomes");
	public static final SimpleConfig GENERATOR_CONFIG = new SimpleConfig("generator");
	
	public static void saveConfigs() {
		ITEM_CONFIG.saveChanges();
		BLOCK_CONFIG.saveChanges();
		BIOME_CONFIG.saveChanges();
		ENTITY_CONFIG.saveChanges();
		GENERATOR_CONFIG.save();
	}
}
