package ru.betterend.world.biome;

import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Identifier;

import ru.betterend.config.Config;
import ru.betterend.config.ConfigWriter;
import ru.betterend.config.ConfigKeeper.Entry;

public class BiomeConfig extends Config {

	private final ConfigWriter writer;
	
	public BiomeConfig() {
		this.writer = new ConfigWriter("biomes");
		this.settings = writer.load();
		this.registerEntries();
		if (settings.size() > 0) {
			this.configKeeper.fromJson(settings);
		} else {
			this.configKeeper.toJson(settings);
			this.writer.save();
		}
	}
	
	@Override
	protected void registerEntries() {}
	
	@Override
	public void saveChanges() {
		this.configKeeper.toJson(settings);
		this.writer.saveConfig();
	}
	
	public String getCategory(EndBiome biome) {
		Identifier biomeId = biome.getID();
		return biomeId.getPath();
	}
	
	@Nullable
	public <E extends Entry<?>> E getEntry(EndBiome biome, String key) {
		return this.getEntry(getCategory(biome), key);
	}
	
	@Nullable
	public <T> T getDefault(EndBiome biome, String key) {
		return this.getDefault(getCategory(biome), key);
	}
	
	public String getString(EndBiome biome, String key, String defaultValue) {
		return this.getString(getCategory(biome), key, defaultValue);
	}
	
	public String getString(EndBiome biome, String key) {
		return this.getString(getCategory(biome), key);
	}
	
	public boolean setString(EndBiome biome, String key, String value) {
		return this.setString(getCategory(biome), key, value);
	}
	
	public int getInt(EndBiome biome, String key, int defaultValue) {
		return this.getInt(getCategory(biome), key, defaultValue);
	}
	
	public int getInt(EndBiome biome, String key) {
		return this.getInt(getCategory(biome), key);
	}
	
	public boolean setInt(EndBiome biome, String key, int value) {
		return this.setInt(getCategory(biome), key, value);
	}
	
	public <T extends Comparable<T>> boolean setRanged(EndBiome biome, String key, T value) {
		return this.setRanged(getCategory(biome), key, value);
	}
	
	public float getFloat(EndBiome biome, String key, float defaultValue) {
		return this.getFloat(getCategory(biome), key, defaultValue);
	}
	
	public float getFloat(EndBiome biome, String key) {
		return this.getFloat(getCategory(biome), key);
	}
	
	public boolean setFloat(EndBiome biome, String key, float value) {
		return this.setFloat(getCategory(biome), key, value);
	}
	
	public boolean getBoolean(EndBiome biome, String key, boolean defaultValue) {
		return this.getBoolean(getCategory(biome), key, defaultValue);
	}
	
	public boolean getBoolean(EndBiome biome, String key) {
		return this.getBoolean(getCategory(biome), key);
	}
	
	public boolean setBoolean(EndBiome biome, String key, boolean value) {
		return this.setBoolean(getCategory(biome), key, value);
	}
}
