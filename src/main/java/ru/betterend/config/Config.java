package ru.betterend.config;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.util.JsonHelper;
import ru.betterend.BetterEnd;
import ru.betterend.config.ConfigKeeper.BooleanEntry;
import ru.betterend.config.ConfigKeeper.Entry;
import ru.betterend.config.ConfigKeeper.FloatEntry;
import ru.betterend.config.ConfigKeeper.IntegerEntry;
import ru.betterend.config.ConfigKeeper.RangeEntry;
import ru.betterend.config.ConfigKeeper.StringEntry;

public abstract class Config {
	
	protected final ConfigKeeper configKeeper = new ConfigKeeper();
	protected JsonObject settings;
	
	public abstract void saveChanges();
	protected abstract void registerEntries();
	
	@Nullable
	public <E extends Entry<?>> E getEntry(String category, String key) {
		return this.configKeeper.getEntry(category, key);
	}
	
	@Nullable
	public <T> T getDefault(String category, String key) {
		Entry<T> entry = configKeeper.getEntry(category, key);
		return entry != null ? entry.getDefault() : null;
	}
	
	public String getString(String category, String key, String defaultValue) {
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
	
	public String getString(String category, String key) {
		String str = configKeeper.getValue(category, key);
		return str != null ? str : "";
	}
	
	public boolean setString(String category, String key, String value) {
		try {
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
	
	public int getInt(String category, String key, int defaultValue) {
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
	
	public int getInt(String category, String key) {
		Integer val = configKeeper.getValue(category, key);		
		return val != null ? val : 0;
	}
	
	public boolean setInt(String category, String key, int value) {
		try {
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
	
	public <T extends Comparable<T>> boolean setRanged(String category, String key, T value) {
		try {
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
	
	public float getFloat(String category, String key, float defaultValue) {
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
	
	public float getFloat(String category, String key) {
		Float val = configKeeper.getValue(category, key);
		return val != null ? val : 0.0F;
	}
	
	public boolean setFloat(String category, String key, float value) {
		try {
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
	
	public boolean getBoolean(String category, String key, boolean defaultValue) {
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
	
	public boolean getBoolean(String category, String key) {
		Boolean val = configKeeper.getValue(category, key);
		return val != null ? val : false;
	}
	
	public boolean setBoolean(String category, String key, boolean value) {
		try {
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
