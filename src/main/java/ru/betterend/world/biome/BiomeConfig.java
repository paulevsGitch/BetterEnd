package ru.betterend.world.biome;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.util.Identifier;

import net.minecraft.util.JsonHelper;

import ru.betterend.BetterEnd;
import ru.betterend.config.Config;
import ru.betterend.config.ConfigWriter;
import ru.betterend.config.ConfigKeeper.BooleanEntry;
import ru.betterend.config.ConfigKeeper.Entry;
import ru.betterend.config.ConfigKeeper.FloatEntry;
import ru.betterend.config.ConfigKeeper.IntegerEntry;
import ru.betterend.config.ConfigKeeper.RangeEntry;
import ru.betterend.config.ConfigKeeper.StringEntry;

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
	protected void registerEntries() {
		//TODO: Need to register config params in the Keeper
	}
	
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
		return this.configKeeper.getEntry(getCategory(biome), key);
	}
	
	@Nullable
	public <T> T getDefault(EndBiome biome, String key) {
		Entry<T> entry = configKeeper.getEntry(getCategory(biome), key);
		return entry != null ? entry.getDefault() : null;
	}
	
	public String getString(EndBiome biome, String key, String defaultValue) {
		String category = this.getCategory(biome);
		String str = configKeeper.getValue(category, key);
		if (str == null) {
			StringEntry entry = this.configKeeper.registerEntry(category, key, new StringEntry(defaultValue));
			if (settings != null && settings.has(category)) {
				JsonObject params = JsonHelper.getObject(settings, category);
				key += " [default: " + defaultValue + "]";
				if (params.has(key)) {
					entry.fromString(JsonHelper.getString(params, key));
					return entry.getValue();
				}
			}
		}
		return str != null ? str : defaultValue;
	}
	
	@Nullable
	public String getString(EndBiome biome, String key) {
		String str = configKeeper.getValue(getCategory(biome), key);
		return str != null ? str : null;
	}
	
	public boolean setString(EndBiome biome, String key, String value) {
		try {
			String category = this.getCategory(biome);
			StringEntry entry = configKeeper.getEntry(category, key);
			if (entry == null) return false;
			entry.setValue(value);
			this.configKeeper.set(category, key, entry);
			
			return true;
		} catch (NullPointerException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		
		return false;
	}
	
	public int getInt(EndBiome biome, String key, int defaultValue) {
		String category = this.getCategory(biome);
		Integer val = configKeeper.getValue(category, key);		
		if (val == null) {
			IntegerEntry entry = this.configKeeper.registerEntry(category, key, new IntegerEntry(defaultValue));
			if (settings != null && settings.has(category)) {
				JsonObject params = JsonHelper.getObject(settings, category);
				key += " [default: " + defaultValue + "]";
				if (params.has(key)) {
					entry.fromString(JsonHelper.getString(params, key));
					return entry.getValue();
				}
			}
		}
		return val != null ? val : defaultValue;
	}
	
	public int getInt(EndBiome biome, String key) {
		Integer val = configKeeper.getValue(getCategory(biome), key);		
		return val != null ? val : 0;
	}
	
	public boolean setInt(EndBiome biome, String key, int value) {
		try {
			String category = this.getCategory(biome);
			IntegerEntry entry = configKeeper.getEntry(category, key);
			if (entry == null) return false;
			entry.setValue(value);
			this.configKeeper.set(category, key, entry);
			
			return true;
		} catch (NullPointerException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		
		return false;
	}
	
	public <T extends Comparable<T>> boolean setRanged(EndBiome biome, String key, T value) {
		try {
			String category = this.getCategory(biome);
			RangeEntry<T> entry = configKeeper.getEntry(category, key);
			if (entry == null) return false;
			entry.setValue(value);
			this.configKeeper.set(category, key, entry);
			
			return true;
		} catch (NullPointerException | ClassCastException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		
		return false;
	}
	
	public float getFloat(EndBiome biome, String key, float defaultValue) {
		String category = this.getCategory(biome);
		Float val = configKeeper.getValue(category, key);
		if (val == null) {
			FloatEntry entry = this.configKeeper.registerEntry(category, key, new FloatEntry(defaultValue));
			if (settings != null && settings.has(category)) {
				JsonObject params = JsonHelper.getObject(settings, category);
				key += " [default: " + defaultValue + "]";
				if (params.has(key)) {
					entry.fromString(JsonHelper.getString(params, key));
					return entry.getValue();
				}
			}
		}
		return val != null ? val : defaultValue;
	}
	
	public float getFloat(EndBiome biome, String key) {
		Float val = configKeeper.getValue(getCategory(biome), key);
		return val != null ? val : 0.0F;
	}
	
	public boolean setFloat(EndBiome biome, String key, float value) {
		try {
			String category = this.getCategory(biome);
			FloatEntry entry = configKeeper.getEntry(category, key);
			if (entry == null) return false;
			entry.setValue(value);
			this.configKeeper.set(category, key, entry);
			
			return true;
		} catch (NullPointerException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		
		return false;
	}
	
	public boolean getBoolean(EndBiome biome, String key, boolean defaultValue) {
		String category = this.getCategory(biome);
		Boolean val = configKeeper.getValue(category, key);
		if (val == null) {
			BooleanEntry entry = this.configKeeper.registerEntry(category, key, new BooleanEntry(defaultValue));
			if (settings != null && settings.has(category)) {
				JsonObject params = JsonHelper.getObject(settings, category);
				key += " [default: " + defaultValue + "]";
				if (params.has(key)) {
					entry.fromString(JsonHelper.getString(params, key));
					return entry.getValue();
				}
			}
		}
		return val != null ? val : defaultValue;
	}
	
	public boolean getBoolean(EndBiome biome, String key) {
		Boolean val = configKeeper.getValue(getCategory(biome), key);
		return val != null ? val : false;
	}
	
	public boolean setBoolean(EndBiome biome, String key, boolean value) {
		try {
			String category = this.getCategory(biome);
			BooleanEntry entry = configKeeper.getEntry(category, key);
			if (entry == null) return false;
			entry.setValue(value);
			this.configKeeper.set(category, key, entry);
			
			return true;
		} catch (NullPointerException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		
		return false;
	}
}
