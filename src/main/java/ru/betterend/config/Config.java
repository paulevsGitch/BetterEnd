package ru.betterend.config;

import ru.betterend.BetterEnd;
import ru.betterend.config.ConfigKeeper.BooleanEntry;
import ru.betterend.config.ConfigKeeper.Entry;
import ru.betterend.config.ConfigKeeper.FloatEntry;
import ru.betterend.config.ConfigKeeper.IntegerEntry;
import ru.betterend.config.ConfigKeeper.RangeEntry;
import ru.betterend.config.ConfigKeeper.StringEntry;

public abstract class Config {
	
	protected final ConfigKeeper configKeeper = new ConfigKeeper();
	
	public abstract void saveChanges();
	
	public <E extends Entry<?>> E getEntry(String key) {
		return this.configKeeper.getEntry(key);
	}
	
	public <T> T getDefault(String key) {
		Entry<T> entry = configKeeper.getEntry(key);
		return entry != null ? entry.getDefault() : null;
	}
	
	public String getString(String key) {
		String str = configKeeper.getValue(key);
		return str != null ? str : "";
	}
	
	public boolean setString(String key, String value) {
		try {
			StringEntry entry = configKeeper.getEntry(key);
			entry.setValue(value);
			this.configKeeper.set(key, entry);
			
			return true;
		} catch (NullPointerException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		
		return false;
	}
	
	public int getInt(String key) {
		Integer val = configKeeper.getValue(key);		
		return val != null ? val : 0;
	}
	
	public boolean setInt(String key, int value) {
		try {
			IntegerEntry entry = configKeeper.getEntry(key);
			entry.setValue(value);
			this.configKeeper.set(key, entry);
			
			return true;
		} catch (NullPointerException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		
		return false;
	}
	
	public <T extends Comparable<T>> boolean setRanged(String key, T value) {
		try {
			RangeEntry<T> entry = configKeeper.getEntry(key);
			entry.setValue(value);
			this.configKeeper.set(key, entry);
			
			return true;
		} catch (NullPointerException | ClassCastException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		
		return false;
	}
	
	public float getFloat(String key) {
		Float val = configKeeper.getValue(key);		
		return val != null ? val : 0.0F;
	}
	
	public boolean setFloat(String key, float value) {
		try {
			FloatEntry entry = configKeeper.getEntry(key);
			entry.setValue(value);
			this.configKeeper.set(key, entry);
			
			return true;
		} catch (NullPointerException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		
		return false;
	}
	
	public boolean getBoolean(String key) {
		Boolean val = configKeeper.getValue(key);		
		return val != null ? val : false;
	}
	
	public boolean setBoolean(String key, boolean value) {
		try {
			BooleanEntry entry = configKeeper.getEntry(key);
			entry.setValue(value);
			this.configKeeper.set(key, entry);
			
			return true;
		} catch (NullPointerException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		
		return false;
	}
}
