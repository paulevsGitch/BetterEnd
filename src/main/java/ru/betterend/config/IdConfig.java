package ru.betterend.config;

import java.util.function.BiFunction;
import org.jetbrains.annotations.Nullable;
import net.minecraft.util.Identifier;
import ru.betterend.config.ConfigKeeper.Entry;
import ru.betterend.config.ConfigKeeper.RangeEntry;

public class IdConfig extends Config {
	
	private final BiFunction<Identifier, String, ConfigKey> keyFactory;
	
	public IdConfig(String group, BiFunction<Identifier, String, ConfigKey> keyFactory) {
		super(group);
		this.keyFactory = keyFactory;
	}

	@Override
	protected void registerEntries() {}

	private ConfigKey createKey(Identifier id, String key) {
		return this.keyFactory.apply(id, key);
	}

	@Nullable
	public <T, E extends Entry<T>> E getEntry(Identifier id, String key, Class<E> type) {
		return this.getEntry(createKey(id, key), type);
	}

	@Nullable
	public <T, E extends Entry<T>> T getDefault(Identifier id, String key, Class<E> type) {
		return this.getDefault(createKey(id, key), type);
	}

	public String getString(Identifier id, String key, String defaultValue) {
		return this.getString(createKey(id, key), defaultValue);
	}

	public String getString(Identifier id, String key) {
		return this.getString(createKey(id, key));
	}

	public boolean setString(Identifier id, String key, String value) {
		return this.setString(createKey(id, key), value);
	}

	public int getInt(Identifier id, String key, int defaultValue) {
		return this.getInt(createKey(id, key), defaultValue);
	}

	public int getInt(Identifier id, String key) {
		return this.getInt(createKey(id, key));
	}

	public boolean setInt(Identifier id, String key, int value) {
		return this.setInt(createKey(id, key), value);
	}

	public <T extends Comparable<T>, RE extends RangeEntry<T>> boolean setRanged(Identifier id, String key, T value, Class<RE> type) {
		return this.setRanged(createKey(id, key), value, type);
	}

	public float getFloat(Identifier id, String key, float defaultValue) {
		return this.getFloat(createKey(id, key), defaultValue);
	}

	public float getFloat(Identifier id, String key) {
		return this.getFloat(createKey(id, key));
	}

	public boolean setFloat(Identifier id, String key, float value) {
		return this.setFloat(createKey(id, key), value);
	}

	public boolean getBoolean(Identifier id, String key, boolean defaultValue) {
		return this.getBoolean(createKey(id, key), defaultValue);
	}

	public boolean getBoolean(Identifier id, String key) {
		return this.getBoolean(createKey(id, key));
	}

	public boolean setBoolean(Identifier id, String key, boolean value) {
		return this.setBoolean(createKey(id, key), value);
	}
}
