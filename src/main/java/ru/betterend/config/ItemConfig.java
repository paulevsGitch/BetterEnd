package ru.betterend.config;

import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Identifier;
import ru.betterend.config.ConfigKeeper.Entry;

public class ItemConfig extends Config {
	
	protected ItemConfig() {
		super("settings");
	}
	
	@Override
	protected void registerEntries() {}
	
	@Override
	public void saveChanges() {
		this.writer.save();
	}
	
	private ConfigKey createKey(Identifier item, String category) {
		Identifier groupId = new Identifier(group, item.getNamespace());
		Identifier categoryId = new Identifier(category, item.getPath());
		return new ConfigKey(groupId, categoryId);
	}
	
	@Nullable
	public <E extends Entry<?>> E getEntry(Identifier item, String category) {
		return this.getEntry(createKey(item, category));
	}
	
	@Nullable
	public <T> T getDefault(Identifier item, String category) {
		return this.getDefault(createKey(item, category));
	}
	
	public String getString(Identifier item, String category, String defaultValue) {
		return this.getString(createKey(item, category), defaultValue);
	}
	
	public String getString(Identifier item, String category) {
		return this.getString(createKey(item, category));
	}
	
	public boolean setString(Identifier item, String category, String value) {
		return this.setString(createKey(item, category), value);
	}
	
	public int getInt(Identifier item, String category, int defaultValue) {
		return this.getInt(createKey(item, category), defaultValue);
	}
	
	public int getInt(Identifier item, String category) {
		return this.getInt(createKey(item, category));
	}
	
	public boolean setInt(Identifier item, String category, int value) {
		return this.setInt(createKey(item, category), value);
	}
	
	public <T extends Comparable<T>> boolean setRanged(Identifier item, String category, T value) {
		return this.setRanged(createKey(item, category), value);
	}
	
	public float getFloat(Identifier item, String category, float defaultValue) {
		return this.getFloat(createKey(item, category), defaultValue);
	}
	
	public float getFloat(Identifier item, String category) {
		return this.getFloat(createKey(item, category));
	}
	
	public boolean setFloat(Identifier item, String category, float value) {
		return this.setFloat(createKey(item, category), value);
	}
	
	public boolean getBoolean(Identifier item, String category, boolean defaultValue) {
		return this.getBoolean(createKey(item, category), defaultValue);
	}
	
	public boolean getBoolean(Identifier item, String category) {
		return this.getBoolean(createKey(item, category));
	}
	
	public boolean setBoolean(Identifier item, String category, boolean value) {
		return this.setBoolean(createKey(item, category), value);
	}
}
