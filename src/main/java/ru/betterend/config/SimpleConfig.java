package ru.betterend.config;

import org.jetbrains.annotations.Nullable;

import ru.betterend.BetterEnd;
import ru.betterend.config.ConfigKeeper.Entry;
import ru.betterend.config.ConfigKeeper.FloatRange;
import ru.betterend.config.ConfigKeeper.IntegerRange;

public class SimpleConfig extends Config {

	public SimpleConfig(String group) {
		super(group);
	}

	@Override
	protected void registerEntries() {}
	
	protected ConfigKey createKey(String category, String key) {
		return new ConfigKey(BetterEnd.MOD_ID, category, key);
	}
	
	@Nullable
	public <T, E extends Entry<T>> E getEntry(String category, String key, Class<E> type) {
		return this.getEntry(createKey(category, key), type);
	}

	@Nullable
	public <T, E extends Entry<T>> T getDefault(String category, String key, Class<E> type) {
		return this.getDefault(createKey(category, key), type);
	}

	public String getString(String category, String key, String defaultValue) {
		return this.getString(createKey(category, key), defaultValue);
	}

	public String getString(String category, String key) {
		return this.getString(createKey(category, key));
	}

	public boolean setString(String category, String key, String value) {
		return this.setString(createKey(category, key), value);
	}

	public int getInt(String category, String key, int defaultValue) {
		return this.getInt(createKey(category, key), defaultValue);
	}

	public int getInt(String category, String key) {
		return this.getInt(createKey(category, key));
	}

	public boolean setInt(String category, String key, int value) {
		return this.setInt(createKey(category, key), value);
	}

	public boolean setRangedInt(String category, String key, int value) {
		return this.setRanged(createKey(category, key), value, IntegerRange.class);
	}
	
	public boolean setRangedFloat(String category, String key, float value) {
		return this.setRanged(createKey(category, key), value, FloatRange.class);
	}

	public float getFloat(String category, String key, float defaultValue) {
		return this.getFloat(createKey(category, key), defaultValue);
	}

	public float getFloat(String category, String key) {
		return this.getFloat(createKey(category, key));
	}

	public boolean setFloat(String category, String key, float value) {
		return this.setFloat(createKey(category, key), value);
	}

	public boolean getBoolean(String category, String key, boolean defaultValue) {
		return this.getBoolean(createKey(category, key), defaultValue);
	}

	public boolean getBoolean(String category, String key) {
		return this.getBoolean(createKey(category, key));
	}

	public boolean setBoolean(String category, String key, boolean value) {
		return this.setBoolean(createKey(category, key), value);
	}
}
