package ru.betterend.config;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonObject;

import net.minecraft.util.JsonHelper;
import ru.betterend.util.JsonFactory;

public final class ConfigKeeper {
	
	private Map<ConfigKey, Entry<?>> configEntries = Maps.newHashMap();
	
	private final JsonObject configObject;
	private final ConfigWriter writer;
	
	private boolean changed = false;
	
	public ConfigKeeper(String group) {
		this.writer = new ConfigWriter(group);
		this.configObject = writer.load();
	}
	
	public void save() {
		if (!changed) return;
		this.writer.save();
		this.changed = false;
	}
	
	private <T, E extends Entry<T>> void initializeEntry(ConfigKey key, E entry) {
		if (configObject == null) {
			return;
		}
		String group = key.getOwner();
		JsonObject jsonGroup;
		if (configObject.has(group)) {
			jsonGroup = JsonHelper.getObject(configObject, group);
		} else {
			jsonGroup = new JsonObject();
			configObject.add(group, jsonGroup);
		}
		String category = key.getCategory();
		JsonObject jsonCategory;
		if (jsonGroup.has(category)) {
			jsonCategory = JsonHelper.getObject(jsonGroup, category);
		} else {
			jsonCategory = new JsonObject();
			jsonGroup.add(category, jsonCategory);
		}
		String paramKey = key.getEntry();
		paramKey += " [default: " + entry.getDefault() + "]";
		this.changed = entry.setLocation(jsonCategory, paramKey);
	}
	
	private <T, E extends Entry<T>> void storeValue(E entry, T value) {
		if (configObject == null) {
			return;
		}
		T val = entry.getValue();
		if (value.equals(val)) return;
		entry.toJson(value);
		this.changed = true;
	}
	
	private <T, E extends Entry<T>> T getValue(E entry) {
		if (!entry.hasLocation()) {
			return entry.getDefault();
		}
		return entry.fromJson();
	}
	
	@Nullable
	public <T, E extends Entry<T>> E getEntry(ConfigKey key, Class<E> type) {
		Entry<?> entry = this.configEntries.get(key);
		if (type.isInstance(entry)) {
			return type.cast(entry);
		}
		return null;
	}
	
	@Nullable
	public <T, E extends Entry<T>> T getValue(ConfigKey key, Class<E> type) {
		Entry<T> entry = this.getEntry(key, type);
		if (entry == null) {
			return null;
		}
		return entry.getValue();
	}

	public <T, E extends Entry<T>> E registerEntry(ConfigKey key, E entry) {
		entry.setWriter(value -> this.storeValue(entry, value));
		entry.setReader(() -> { return this.getValue(entry); });
		this.initializeEntry(key, entry);
		this.configEntries.put(key, entry);
		return entry;
	}
	
	public static class BooleanEntry extends Entry<Boolean> {

		public BooleanEntry(Boolean defaultValue) {
			super(defaultValue);
		}

		@Override
		public Boolean fromJson() {
			return JsonHelper.getBoolean(location, key, defaultValue);
		}

		@Override
		public void toJson(Boolean value) {
			this.location.addProperty(key, value);
		}
	}
	
	public static class FloatEntry extends Entry<Float> {

		public FloatEntry(Float defaultValue) {
			super(defaultValue);
		}

		@Override
		public Float fromJson() {
			return JsonHelper.getFloat(location, key, defaultValue);
		}

		@Override
		public void toJson(Float value) {
			this.location.addProperty(key, value);
		}
	}
	
	public static class FloatRange extends RangeEntry<Float> {

		public FloatRange(Float defaultValue, float minVal, float maxVal) {
			super(defaultValue, minVal, maxVal);
		}

		@Override
		public Float fromJson() {
			return JsonHelper.getFloat(location, key, defaultValue);
		}

		@Override
		public void toJson(Float value) {
			this.location.addProperty(key, value);
		}
	}
	
	public static class IntegerEntry extends Entry<Integer> {

		public IntegerEntry(Integer defaultValue) {
			super(defaultValue);
		}

		@Override
		public Integer getDefault() {
			return this.defaultValue;
		}

		@Override
		public Integer fromJson() {
			return JsonHelper.getInt(location, key, defaultValue);
		}

		@Override
		public void toJson(Integer value) {
			this.location.addProperty(key, value);
		}
	}
	
	public static class IntegerRange extends RangeEntry<Integer> {

		public IntegerRange(Integer defaultValue, int minVal, int maxVal) {
			super(defaultValue, minVal, maxVal);
		}

		@Override
		public Integer fromJson() {
			return JsonHelper.getInt(location, key, defaultValue);
		}

		@Override
		public void toJson(Integer value) {
			this.location.addProperty(key, value);
		}
	}
	
	public static class StringEntry extends Entry<String> {

		public StringEntry(String defaultValue) {
			super(defaultValue);
		}

		@Override
		public String fromJson() {
			return JsonHelper.getString(location, key, defaultValue);
		}

		@Override
		public void toJson(String value) {
			this.location.addProperty(key, value);
		}

	}
	
	public static class EnumEntry<T extends Enum<T>> extends Entry<T> {

		private final Type type;
		
		public EnumEntry(T defaultValue) {
			super(defaultValue);
			TypeToken<T> token = new TypeToken<T>(){
				private static final long serialVersionUID = 1L;
			};
			this.type = token.getType();
		}

		@Override
		public T getDefault() {
			return this.defaultValue;
		}

		@Override
		public T fromJson() {
			return JsonFactory.GSON.fromJson(location.get(key), type);
		}

		@Override
		public void toJson(T value) {
			location.addProperty(key, JsonFactory.GSON.toJson(value, type));
		}
	}
	
	public static abstract class RangeEntry<T extends Comparable<T>> extends Entry<T> {

		private final T min, max;

		public RangeEntry(T defaultValue, T minVal, T maxVal) {
			super(defaultValue);
			this.min = minVal;
			this.max = maxVal;
		}

		@Override
		public void setValue(T value) {
			super.setValue(value.compareTo(min) < 0 ? min : value.compareTo(max) > 0 ? max : value);
		}
		
		public T minValue() {
			return this.min;
		}
		
		public T maxValue() {
			return this.max;
		}
	}
	
	public static abstract class Entry<T> {
		
		protected final T defaultValue;
		protected Consumer<T> writer;
		protected Supplier<T> reader;
		protected JsonObject location;
		protected String key;
		
		public abstract T fromJson();
		public abstract void toJson(T value);
		
		public Entry (T defaultValue) {
			this.defaultValue = defaultValue;
		}
		
		protected void setWriter(Consumer<T> writer) {
			this.writer = writer;
		}
		
		protected void setReader(Supplier<T> reader) {
			this.reader = reader;
		}
		
		protected boolean setLocation(JsonObject location, String key) {
			this.location = location;
			this.key = key;
			if (!location.has(key)) {
				this.toJson(defaultValue);
				return true;
			}
			return false;
		}
		
		protected boolean hasLocation() {
			return this.location != null &&
				   this.key != null;
		}
		
		public T getValue() {
			return this.reader.get();
		}
		
		public void setValue(T value) {
			this.writer.accept(value);
		}
		
		public T getDefault() {
			return this.defaultValue;
		}
		
		public void setDefault() {
			this.setValue(defaultValue);
		}
	}
}
