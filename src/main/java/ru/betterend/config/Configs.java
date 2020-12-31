package ru.betterend.config;

public class Configs {
	public static final IdConfig ENTITY_CONFIG = new CategoryConfig("entities");
	public static final IdConfig BLOCK_CONFIG = new CategoryConfig("blocks");
	public static final SimpleConfig GENERAL = new SimpleConfig("settings");
	public static final IdConfig ITEM_CONFIG = new CategoryConfig("items");
	public static final IdConfig BIOME_CONFIG = new EntryConfig("biomes");
	
	public static void saveConfigs() {
		ENTITY_CONFIG.saveChanges();
		BLOCK_CONFIG.saveChanges();
		BIOME_CONFIG.saveChanges();
		ITEM_CONFIG.saveChanges();
		GENERAL.saveChanges();
	}
}
