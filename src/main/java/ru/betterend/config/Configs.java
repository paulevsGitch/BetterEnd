package ru.betterend.config;

public class Configs {
	public static final IdentifierConfig ITEM_CONFIG = new IdentifierConfig("items");
	public static final IdentifierConfig BLOCK_CONFIG = new IdentifierConfig("blocks");
	public static final IdentifierConfig BIOME_CONFIG = new IdentifierConfig("biomes");
	
	public static void saveConfigs() {
		ITEM_CONFIG.saveChanges();
		BLOCK_CONFIG.saveChanges();
		BIOME_CONFIG.saveChanges();
	}
}
