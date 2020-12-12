package ru.betterend.config;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import ru.betterend.BetterEnd;
import ru.betterend.config.ConfigKeeper.BooleanEntry;
import ru.betterend.config.ConfigKeeper.Entry;
import ru.betterend.config.ConfigKeeper.FloatEntry;
import ru.betterend.config.ConfigKeeper.IntegerEntry;
import ru.betterend.config.ConfigKeeper.RangeEntry;
import ru.betterend.config.ConfigKeeper.StringEntry;

public abstract class Config {
	
	protected final ConfigKeeper configKeeper;
	protected final ConfigWriter writer;
	protected final String group;
	
	protected abstract void registerEntries();
	
	public Config(String group) {
		this.group = group;
		this.writer = new ConfigWriter(group);
		JsonObject settings = writer.load();
		this.configKeeper = new ConfigKeeper(settings);
		this.registerEntries();
		this.writer.save();
	}
	
	public void saveChanges() {
		this.writer.saveConfig();
	}
	
	@Nullable
	public <E extends Entry<?>> E getEntry(ConfigKey key) {
		return this.configKeeper.getEntry(key);
	}
	
	@Nullable
	public <T> T getDefault(ConfigKey key) {
		Entry<T> entry = configKeeper.getEntry(key);
		return entry != null ? entry.getDefault() : null;
	}
	
	public String getString(ConfigKey key, String defaultValue) {
		String str = configKeeper.getValue(key);
		if (str == null) {
			StringEntry entry = configKeeper.registerEntry(key, new StringEntry(defaultValue));
			return entry.getValue();
		}
		return str != null ? str : defaultValue;
	}
	
	public String getString(ConfigKey key) {
		String str = configKeeper.getValue(key);
		return str != null ? str : "";
	}
	
	public boolean setString(ConfigKey key, String value) {
		try {
			StringEntry entry = configKeeper.getEntry(key);
			if (entry == null) return false;
			entry.setValue(value);
			return true;
		} catch (NullPointerException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		return false;
	}
	
	public int getInt(ConfigKey key, int defaultValue) {
		Integer val = configKeeper.getValue(key);		
		if (val == null) {
			IntegerEntry entry = configKeeper.registerEntry(key, new IntegerEntry(defaultValue));
			return entry.getValue();
		}
		return val != null ? val : defaultValue;
	}
	
	public int getInt(ConfigKey key) {
		Integer val = configKeeper.getValue(key);		
		return val != null ? val : 0;
	}
	
	public boolean setInt(ConfigKey key, int value) {
		try {
			IntegerEntry entry = configKeeper.getEntry(key);
			if (entry == null) return false;
			entry.setValue(value);
			return true;
		} catch (NullPointerException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		return false;
	}
	
	public <T extends Comparable<T>> boolean setRanged(ConfigKey key, T value) {
		try {
			RangeEntry<T> entry = configKeeper.getEntry(key);
			if (entry == null) return false;
			entry.setValue(value);
			return true;
		} catch (NullPointerException | ClassCastException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		return false;
	}
	
	public float getFloat(ConfigKey key, float defaultValue) {
		Float val = configKeeper.getValue(key);
		if (val == null) {
			FloatEntry entry = configKeeper.registerEntry(key, new FloatEntry(defaultValue));
			return entry.getValue();
		}
		return val != null ? val : defaultValue;
	}
	
	public float getFloat(ConfigKey key) {
		Float val = configKeeper.getValue(key);
		return val != null ? val : 0.0F;
	}
	
	public boolean setFloat(ConfigKey key, float value) {
		try {
			FloatEntry entry = configKeeper.getEntry(key);
			if (entry == null) return false;
			entry.setValue(value);
			return true;
		} catch (NullPointerException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		return false;
	}
	
	public boolean getBoolean(ConfigKey key, boolean defaultValue) {
		Boolean val = configKeeper.getValue(key);
		if (val == null) {
			BooleanEntry entry = configKeeper.registerEntry(key, new BooleanEntry(defaultValue));
			return entry.getValue();
		}
		return val != null ? val : defaultValue;
	}
	
	public boolean getBoolean(ConfigKey key) {
		Boolean val = configKeeper.getValue(key);
		return val != null ? val : false;
	}
	
	public boolean setBoolean(ConfigKey key, boolean value) {
		try {
			BooleanEntry entry = configKeeper.getEntry(key);
			if (entry == null) return false;
			entry.setValue(value);
			return true;
		} catch (NullPointerException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		return false;
	}
}
