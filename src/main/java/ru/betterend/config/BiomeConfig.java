package ru.betterend.config;

import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Identifier;
import ru.betterend.config.ConfigKeeper.Entry;

public class BiomeConfig extends Config {

	protected BiomeConfig() {
		super("biomes");
	}
	
	@Override
	protected void registerEntries() {}
	
	private ConfigKey createKey(Identifier biome, String key) {
		Identifier groupId = new Identifier(group, biome.getNamespace());
		Identifier categoryId = new Identifier(biome.getPath(), key);
		return new ConfigKey(groupId, categoryId);
	}
	
	@Nullable
	public <E extends Entry<?>> E getEntry(Identifier biome, String key) {
		return this.getEntry(createKey(biome, key));
	}
	
	@Nullable
	public <T> T getDefault(Identifier biome, String key) {
		return this.getDefault(createKey(biome, key));
	}
	
	public String getString(Identifier biome, String key, String defaultValue) {
		return this.getString(createKey(biome, key), defaultValue);
	}
	
	public String getString(Identifier biome, String key) {
		return this.getString(createKey(biome, key));
	}
	
	public boolean setString(Identifier biome, String key, String value) {
		return this.setString(createKey(biome, key), value);
	}
	
	public int getInt(Identifier biome, String key, int defaultValue) {
		return this.getInt(createKey(biome, key), defaultValue);
	}
	
	public int getInt(Identifier biome, String key) {
		return this.getInt(createKey(biome, key));
	}
	
	public boolean setInt(Identifier biome, String key, int value) {
		return this.setInt(createKey(biome, key), value);
	}
	
	public <T extends Comparable<T>> boolean setRanged(Identifier biome, String key, T value) {
		return this.setRanged(createKey(biome, key), value);
	}
	
	public float getFloat(Identifier biome, String key, float defaultValue) {
		return this.getFloat(createKey(biome, key), defaultValue);
	}
	
	public float getFloat(Identifier biome, String key) {
		return this.getFloat(createKey(biome, key));
	}
	
	public boolean setFloat(Identifier biome, String key, float value) {
		return this.setFloat(createKey(biome, key), value);
	}
	
	public boolean getBoolean(Identifier biome, String key, boolean defaultValue) {
		return this.getBoolean(createKey(biome, key), defaultValue);
	}
	
	public boolean getBoolean(Identifier biome, String key) {
		return this.getBoolean(createKey(biome, key));
	}
	
	public boolean setBoolean(Identifier biome, String key, boolean value) {
		return this.setBoolean(createKey(biome, key), value);
	}
}
