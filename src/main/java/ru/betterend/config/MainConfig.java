package ru.betterend.config;

public class MainConfig {
	public static final ItemConfig ITEM_CONFIG = getItemConfig();
	public static final BiomeConfig BIOME_CONFIG = getBiomeConfig();
	
	private static ItemConfig itemConfig;
	private static BiomeConfig biomeConfig;
	
	public static ItemConfig getItemConfig() {
		if (itemConfig == null) {
			itemConfig = new ItemConfig();
		}
		return itemConfig;
	}
	
	public static BiomeConfig getBiomeConfig() {
		if (biomeConfig == null) {
			biomeConfig = new BiomeConfig();
		}
		return biomeConfig;
	}
	
	public static void saveConfig() {
		itemConfig.saveChanges();
		biomeConfig.saveChanges();
	}
}
