package ru.betterend.config;

import ru.betterend.BetterEnd;
import ru.betterend.config.ConfigKeeper.*;

public abstract class Config {
	
	protected final static ConfigKeeper KEEPER = ConfigKeeper.getInstance();
	
	public abstract void saveChanges();
	
	public <E extends Entry<?>> E getEntry(String key) {
		return KEEPER.getEntry(key);
	}
	
	public <T> T getDefault(String key) {
		Entry<T> entry = KEEPER.getEntry(key);
		return entry != null ? entry.getDefault() : null;
	}
	
	public String getString(String key) {
		String str = KEEPER.getValue(key);
		return str != null ? str : "";
	}
	
	public boolean setString(String key, String value) {
		try {
			StringEntry entry = KEEPER.getEntry(key);
			entry.setValue(value);
			KEEPER.set(key, entry);
			
			return true;
		} catch (NullPointerException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		
		return false;
	}
	
	public int getInt(String key) {
		Integer val = KEEPER.getValue(key);		
		return val != null ? val : 0;
	}
	
	public boolean setInt(String key, int value) {
		try {
			IntegerEntry entry = KEEPER.getEntry(key);
			entry.setValue(value);
			KEEPER.set(key, entry);
			
			return true;
		} catch (NullPointerException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		
		return false;
	}
	
	public <T extends Comparable<T>> boolean setRanged(String key, T value) {
		try {
			RangeEntry<T> entry = KEEPER.getEntry(key);
			entry.setValue(value);
			KEEPER.set(key, entry);
			
			return true;
		} catch (NullPointerException | ClassCastException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		
		return false;
	}
	
	public float getFloat(String key) {
		Float val = KEEPER.getValue(key);		
		return val != null ? val : 0.0F;
	}
	
	public boolean setFloat(String key, float value) {
		try {
			FloatEntry entry = KEEPER.getEntry(key);
			entry.setValue(value);
			KEEPER.set(key, entry);
			
			return true;
		} catch (NullPointerException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		
		return false;
	}
	
	public boolean getBoolean(String key) {
		Boolean val = KEEPER.getValue(key);		
		return val != null ? val : false;
	}
	
	public boolean setBoolean(String key, boolean value) {
		try {
			BooleanEntry entry = KEEPER.getEntry(key);
			entry.setValue(value);
			KEEPER.set(key, entry);
			
			return true;
		} catch (NullPointerException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		
		return false;
	}
}
