package ru.betterend.config;

public class Configs {
	public static final IdConfig ITEM_CONFIG = new IdConfig("items", (item, category) -> {
		return new ConfigKey(item.getNamespace(), category, item.getPath());
	});
	public static final IdConfig BLOCK_CONFIG = new IdConfig("blocks", (block, category) -> {
		return new ConfigKey(block.getNamespace(), category, block.getPath());
	});
	public static final IdConfig BIOME_CONFIG = new IdConfig("biomes", (biome, entry) -> {
		return new ConfigKey(biome.getNamespace(), biome.getPath(), entry);
	});
	
	public static void saveConfigs() {
		ITEM_CONFIG.saveChanges();
		BLOCK_CONFIG.saveChanges();
		BIOME_CONFIG.saveChanges();
	}
}
