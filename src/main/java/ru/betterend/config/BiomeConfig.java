package ru.betterend.config;

import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Identifier;
import ru.betterend.config.ConfigKeeper.Entry;
import ru.betterend.world.biome.EndBiome;

public class BiomeConfig extends Config {

	protected BiomeConfig() {
		super("biomes");
	}
	
	@Override
	protected void registerEntries() {}
	
	@Override
	public void saveChanges() {
		this.writer.saveConfig();
	}
	
	private ConfigKey createKey(Identifier biome, String key) {
		Identifier groupId = new Identifier(group, biome.getNamespace());
		Identifier categoryId = new Identifier(biome.getPath(), key);
		return new ConfigKey(groupId, categoryId);
	}
	
	@Nullable
	public <E extends Entry<?>> E getEntry(EndBiome biome, String key) {
		return this.getEntry(createKey(biome.getID(), key));
	}
	
	@Nullable
	public <T> T getDefault(EndBiome biome, String key) {
		return this.getDefault(createKey(biome.getID(), key));
	}
	
	public String getString(EndBiome biome, String key, String defaultValue) {
		return this.getString(createKey(biome.getID(), key), defaultValue);
	}
	
	public String getString(EndBiome biome, String key) {
		return this.getString(createKey(biome.getID(), key));
	}
	
	public boolean setString(EndBiome biome, String key, String value) {
		return this.setString(createKey(biome.getID(), key), value);
	}
	
	public int getInt(EndBiome biome, String key, int defaultValue) {
		return this.getInt(createKey(biome.getID(), key), defaultValue);
	}
	
	public int getInt(EndBiome biome, String key) {
		return this.getInt(createKey(biome.getID(), key));
	}
	
	public boolean setInt(EndBiome biome, String key, int value) {
		return this.setInt(createKey(biome.getID(), key), value);
	}
	
	public <T extends Comparable<T>> boolean setRanged(EndBiome biome, String key, T value) {
		return this.setRanged(createKey(biome.getID(), key), value);
	}
	
	public float getFloat(EndBiome biome, String key, float defaultValue) {
		return this.getFloat(createKey(biome.getID(), key), defaultValue);
	}
	
	public float getFloat(EndBiome biome, String key) {
		return this.getFloat(createKey(biome.getID(), key));
	}
	
	public boolean setFloat(EndBiome biome, String key, float value) {
		return this.setFloat(createKey(biome.getID(), key), value);
	}
	
	public boolean getBoolean(EndBiome biome, String key, boolean defaultValue) {
		return this.getBoolean(createKey(biome.getID(), key), defaultValue);
	}
	
	public boolean getBoolean(EndBiome biome, String key) {
		return this.getBoolean(createKey(biome.getID(), key));
	}
	
	public boolean setBoolean(EndBiome biome, String key, boolean value) {
		return this.setBoolean(createKey(biome.getID(), key), value);
	}
}
