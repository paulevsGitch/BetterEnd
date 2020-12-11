package ru.betterend.config;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;

import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public final class ConfigKeeper {
	
	private Map<ConfigKey, Entry<?>> configEntries = Maps.newHashMap();
	private final JsonObject configObject;
	
	public ConfigKeeper(JsonObject config) {
		this.configObject = config;
	}
	
	private <T, E extends Entry<T>> void storeValue(ConfigKey key, E entry, T value) {
		if (configObject == null) return;
		
		Identifier categoryId = key.getCategory();
		Identifier paramId = key.getParameter();
		String group = categoryId.getPath();
		JsonObject jsonGroup;
		if (configObject.has(group)) {
			jsonGroup = JsonHelper.getObject(configObject, group);
		} else {
			jsonGroup = new JsonObject();
			configObject.add(group, jsonGroup);
		}
		String category = paramId.getNamespace();
		JsonObject jsonCategory;
		if (jsonGroup.has(category)) {
			jsonCategory = JsonHelper.getObject(jsonGroup, category);
		} else {
			jsonCategory = new JsonObject();
			jsonGroup.add(category, jsonCategory);
		}
		String paramKey = paramId.getPath();
		paramKey += " [default: " + entry.getDefault() + "]";
		if (value instanceof Boolean) {
			jsonCategory.addProperty(paramKey, (Boolean) value);
		} else if (value instanceof Number) {
			jsonCategory.addProperty(paramKey, (Number) value);
		} else {
			jsonCategory.addProperty(paramKey, entry.asString(value));
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T, E extends Entry<T>> T getValue(ConfigKey key, E entry) {
		T defaultVal = entry.getDefault();
		if (configObject == null) return defaultVal;
		
		Identifier categoryId = key.getCategory();
		Identifier paramId = key.getParameter();
		String group = categoryId.getPath();
		if (!configObject.has(group)) return defaultVal;
		
		JsonObject jsonGroup = JsonHelper.getObject(configObject, group);
		String category = paramId.getNamespace();
		if (!jsonGroup.has(category)) return defaultVal;
		
		JsonObject jsonCategory = JsonHelper.getObject(jsonGroup, category);
		String paramKey = paramId.getPath();
		paramKey += " [default: " + entry.getDefault() + "]";
		if (!jsonCategory.has(paramKey)) return defaultVal;
		
		
		if (defaultVal instanceof Boolean) {
			return (T) (Object) jsonCategory.get(paramKey).getAsBoolean();
		} else if (defaultVal instanceof Integer) {
			return (T) (Object) jsonCategory.get(paramKey).getAsInt();
		} else if (defaultVal instanceof Float) {
			return (T) (Object) jsonCategory.get(paramKey).getAsFloat();
		}
		return entry.fromString(JsonHelper.getString(jsonCategory, paramKey));
	}
	
	@Nullable
	@SuppressWarnings("unchecked")
	public <E extends Entry<?>> E getEntry(ConfigKey key) {
		return (E) this.configEntries.get(key);
	}
	
	@Nullable
	public <T> T getValue(ConfigKey key) {
		Entry<T> entry = this.getEntry(key);
		if (entry == null) {
			return null;
		}
		return entry.getValue();
	}

	public <T, E extends Entry<T>> E registerEntry(ConfigKey key, E entry) {
		entry.setWriter(value -> this.storeValue(key, entry, value));
		entry.setReader(() -> { return this.getValue(key, entry); });
		this.storeValue(key, entry, entry.getValue());
		this.configEntries.put(key, entry);
		return entry;
	}
	
	public static class BooleanEntry extends Entry<Boolean> {

		public BooleanEntry(Boolean defaultValue) {
			super(defaultValue);
		}

		@Override
		public String asString(Boolean value) {
			return value ? "true" : "false";
		}

		@Override
		public Boolean fromString(String value) {
			return value.equals("true") ? true : false;
		}
	}
	
	public static class FloatEntry extends Entry<Float> {

		public FloatEntry(Float defaultValue) {
			super(defaultValue);
		}

		@Override
		public Float fromString(String value) {
			return Float.valueOf(value);
		}

		@Override
		public String asString(Float value) {
			return Float.toString(value);
		}
	}
	
	public static class FloatRange extends RangeEntry<Float> {

		public FloatRange(Float defaultValue, float minVal, float maxVal) {
			super(defaultValue, minVal, maxVal);
		}

		@Override
		public Float fromString(String value) {
			return Float.valueOf(value);
		}

		@Override
		public String asString(Float value) {
			return Float.toString(value);
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
		public Integer fromString(String value) {
			return Integer.parseInt(value);
		}

		@Override
		public String asString(Integer value) {
			return Integer.toString(value);
		}
	}
	
	public static class IntegerRange extends RangeEntry<Integer> {

		public IntegerRange(Integer defaultValue, int minVal, int maxVal) {
			super(defaultValue, minVal, maxVal);
		}

		@Override
		public Integer fromString(String value) {
			return Integer.parseInt(value);
		}

		@Override
		public String asString(Integer value) {
			return Integer.toString(value);
		}
	}
	
	public static class StringEntry extends Entry<String> {

		public StringEntry(String defaultValue) {
			super(defaultValue);
		}

		@Override
		public String fromString(String value) {
			return value;
		}

		@Override
		public String asString(String value) {
			return value;
		}

	}
	
	public static class EnumEntry<T extends Enum<T>> extends Entry<T> {

		public EnumEntry(T defaultValue) {
			super(defaultValue);
		}

		@Override
		public T getDefault() {
			return this.defaultValue;
		}

		@Override
		@SuppressWarnings("unchecked")
		public T fromString(String value) {
			return (T) Enum.valueOf(defaultValue.getClass(), value);
		}

		@Override
		public String asString(T value) {
			return value.name();
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
		
		public abstract T fromString(String value);
		public abstract String asString(T value);
		
		public Entry (T defaultValue) {
			this.defaultValue = defaultValue;
		}
		
		protected void setWriter(Consumer<T> writer) {
			this.writer = writer;
		}
		
		protected void setReader(Supplier<T> reader) {
			this.reader = reader;
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
