package ru.betterend.config;

import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Identifier;
import ru.betterend.config.ConfigKeeper.Entry;

public class IdentifierConfig extends Config {
	public IdentifierConfig(String group) {
		super(group);
	}

	@Override
	protected void registerEntries() {}

	private ConfigKey createKey(Identifier id, String key) {
		Identifier groupId = new Identifier(group, id.getNamespace());
		Identifier categoryId = new Identifier(id.getPath(), key);
		return new ConfigKey(groupId, categoryId);
	}

	@Nullable
	public <E extends Entry<?>> E getEntry(Identifier id, String key) {
		return this.getEntry(createKey(id, key));
	}

	@Nullable
	public <T> T getDefault(Identifier id, String key) {
		return this.getDefault(createKey(id, key));
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

	public <T extends Comparable<T>> boolean setRanged(Identifier id, String key, T value) {
		return this.setRanged(createKey(id, key), value);
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
