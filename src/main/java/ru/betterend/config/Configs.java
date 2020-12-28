package ru.betterend.config;

public class Configs {
	public static final IdConfig ITEM_CONFIG = new IdConfig("items", (itemId, category) -> {
		return new ConfigKey(itemId.getNamespace(), category, itemId.getPath());
	});
	public static final IdConfig BLOCK_CONFIG = new IdConfig("blocks", (blockId, category) -> {
		return new ConfigKey(blockId.getNamespace(), category, blockId.getPath());
	});
	public static final IdConfig BIOME_CONFIG = new IdConfig("biomes", (biomeId, entry) -> {
		return new ConfigKey(biomeId.getNamespace(), biomeId.getPath(), entry);
	});
	public static final IdConfig ENTITY_CONFIG = new IdConfig("entities", (entityId, category) -> {
		return new ConfigKey(entityId.getNamespace(), category, entityId.getPath());
	});
	public static final SimpleConfig GENERATOR_CONFIG = new SimpleConfig("generator");
	
	public static void saveConfigs() {
		ITEM_CONFIG.saveChanges();
		BLOCK_CONFIG.saveChanges();
		BIOME_CONFIG.saveChanges();
		ENTITY_CONFIG.saveChanges();
		GENERATOR_CONFIG.save();
	}
}
