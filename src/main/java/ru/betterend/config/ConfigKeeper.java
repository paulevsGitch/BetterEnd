package ru.betterend.config;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.util.JsonHelper;
import ru.betterend.util.JsonFactory;

public final class ConfigKeeper {
	
	private Map<ConfigKey, Entry<?>> configEntries = Maps.newHashMap();
	private final JsonObject configObject;
	
	public ConfigKeeper(JsonObject config) {
		this.configObject = config;
	}
	
	private <T, E extends Entry<T>> void storeValue(ConfigKey key, E entry, T value) {
		if (configObject == null) return;
		
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
		entry.toJson(jsonCategory, paramKey, value);
	}
	
	private <T, E extends Entry<T>> T getValue(ConfigKey key, E entry) {
		if (configObject == null) {
			return entry.getDefault();
		}
		
		String group = key.getOwner();
		if (!configObject.has(group)) {
			return entry.getDefault();
		}
		
		JsonObject jsonGroup = JsonHelper.getObject(configObject, group);
		String category = key.getCategory();
		if (!jsonGroup.has(category)) {
			return entry.getDefault();
		}
		
		JsonObject jsonCategory = JsonHelper.getObject(jsonGroup, category);
		String paramKey = key.getEntry();
		paramKey += " [default: " + entry.getDefault() + "]";
		if (!jsonCategory.has(paramKey)) {
			return entry.getDefault();
		}
		
		return entry.fromJson(jsonCategory.get(paramKey));
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
		public Boolean fromJson(JsonElement json) {
			return json.getAsBoolean();
		}

		@Override
		public void toJson(JsonObject json, String key, Boolean value) {
			json.addProperty(key, value);
		}
	}
	
	public static class FloatEntry extends Entry<Float> {

		public FloatEntry(Float defaultValue) {
			super(defaultValue);
		}

		@Override
		public Float fromJson(JsonElement json) {
			return json.getAsFloat();
		}

		@Override
		public void toJson(JsonObject json, String key, Float value) {
			json.addProperty(key, value);
		}
	}
	
	public static class FloatRange extends RangeEntry<Float> {

		public FloatRange(Float defaultValue, float minVal, float maxVal) {
			super(defaultValue, minVal, maxVal);
		}

		@Override
		public Float fromJson(JsonElement json) {
			return json.getAsFloat();
		}

		@Override
		public void toJson(JsonObject json, String key, Float value) {
			json.addProperty(key, value);
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
		public Integer fromJson(JsonElement json) {
			return json.getAsInt();
		}

		@Override
		public void toJson(JsonObject json, String key, Integer value) {
			json.addProperty(key, value);
		}
	}
	
	public static class IntegerRange extends RangeEntry<Integer> {

		public IntegerRange(Integer defaultValue, int minVal, int maxVal) {
			super(defaultValue, minVal, maxVal);
		}

		@Override
		public Integer fromJson(JsonElement json) {
			return json.getAsInt();
		}

		@Override
		public void toJson(JsonObject json, String key, Integer value) {
			json.addProperty(key, value);
		}
	}
	
	public static class StringEntry extends Entry<String> {

		public StringEntry(String defaultValue) {
			super(defaultValue);
		}

		@Override
		public String fromJson(JsonElement json) {
			return json.getAsString();
		}

		@Override
		public void toJson(JsonObject json, String key, String value) {
			json.addProperty(key, value);
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
		public T fromJson(JsonElement json) {
			return JsonFactory.GSON.fromJson(json, type);
		}

		@Override
		public void toJson(JsonObject json, String key, T value) {
			json.addProperty(key, JsonFactory.GSON.toJson(json, type));
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
		protected final Type type;
		protected Consumer<T> writer;
		protected Supplier<T> reader;
		
		public abstract T fromJson(JsonElement json);
		public abstract void toJson(JsonObject json, String key, T value);
		
		public Entry (T defaultValue) {
			this.defaultValue = defaultValue;
			this.type = new EntryType().getType();
		}
		
		protected void setWriter(Consumer<T> writer) {
			this.writer = writer;
		}
		
		protected void setReader(Supplier<T> reader) {
			this.reader = reader;
		}
		
		public Type getType() {
			return this.type;
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
		
		protected class EntryType extends TypeToken<T> {
			private static final long serialVersionUID = 1L;
		}
	}
}
