package ru.betterend.config;

import java.util.function.BiFunction;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import ru.betterend.config.ConfigKeeper.Entry;
import ru.betterend.config.ConfigKeeper.FloatRange;
import ru.betterend.config.ConfigKeeper.IntegerRange;

public class IdConfig extends Config {
	
	protected final BiFunction<ResourceLocation, String, ConfigKey> keyFactory;
	
	public IdConfig(String group, BiFunction<ResourceLocation, String, ConfigKey> keyFactory) {
		super(group);
		this.keyFactory = keyFactory;
	}

	@Override
	protected void registerEntries() {}

	protected ConfigKey createKey(ResourceLocation id, String key) {
		return this.keyFactory.apply(id, key);
	}

	@Nullable
	public <T, E extends Entry<T>> E getEntry(ResourceLocation id, String key, Class<E> type) {
		return this.getEntry(createKey(id, key), type);
	}

	@Nullable
	public <T, E extends Entry<T>> T getDefault(ResourceLocation id, String key, Class<E> type) {
		return this.getDefault(createKey(id, key), type);
	}

	public String getString(ResourceLocation id, String key, String defaultValue) {
		return this.getString(createKey(id, key), defaultValue);
	}

	public String getString(ResourceLocation id, String key) {
		return this.getString(createKey(id, key));
	}

	public boolean setString(ResourceLocation id, String key, String value) {
		return this.setString(createKey(id, key), value);
	}

	public int getInt(ResourceLocation id, String key, int defaultValue) {
		return this.getInt(createKey(id, key), defaultValue);
	}

	public int getInt(ResourceLocation id, String key) {
		return this.getInt(createKey(id, key));
	}

	public boolean setInt(ResourceLocation id, String key, int value) {
		return this.setInt(createKey(id, key), value);
	}

	public boolean setRangedInt(ResourceLocation id, String key, int value) {
		return this.setRanged(createKey(id, key), value, IntegerRange.class);
	}
	
	public boolean setRangedFloat(ResourceLocation id, String key, float value) {
		return this.setRanged(createKey(id, key), value, FloatRange.class);
	}

	public float getFloat(ResourceLocation id, String key, float defaultValue) {
		return this.getFloat(createKey(id, key), defaultValue);
	}

	public float getFloat(ResourceLocation id, String key) {
		return this.getFloat(createKey(id, key));
	}

	public boolean setFloat(ResourceLocation id, String key, float value) {
		return this.setFloat(createKey(id, key), value);
	}

	public boolean getBoolean(ResourceLocation id, String key, boolean defaultValue) {
		return this.getBoolean(createKey(id, key), defaultValue);
	}

	public boolean getBoolean(ResourceLocation id, String key) {
		return this.getBoolean(createKey(id, key));
	}

	public boolean setBoolean(ResourceLocation id, String key, boolean value) {
		return this.setBoolean(createKey(id, key), value);
	}
}
