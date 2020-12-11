package ru.betterend.config;

public class Configs {
	public static final ItemConfig ITEM_CONFIG = new ItemConfig();
	public static final BiomeConfig BIOME_CONFIG = new BiomeConfig();
	
	public static void saveConfigs() {
		ITEM_CONFIG.saveChanges();
		BIOME_CONFIG.saveChanges();
	}
}
