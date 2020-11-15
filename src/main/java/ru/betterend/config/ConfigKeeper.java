package ru.betterend.config;

import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.util.JsonHelper;
import ru.betterend.BetterEnd;

public final class ConfigKeeper {
	
	private Map<String, Map<String, Entry<?>>> configEntries = Maps.newHashMap();
	
	public JsonElement toJson(JsonObject jsonObject) {
		for (String category : configEntries.keySet()) {
			Map<String, Entry<?>> entryCategory = this.configEntries.get(category);
			JsonObject jsonCategory = new JsonObject();
			entryCategory.forEach((key, param) -> {
				key += " [default: " + param.getDefault() + "]";
				jsonCategory.addProperty(key, param.asString());
			});
			jsonObject.add(category, jsonCategory);
		}
		
		return jsonObject;
	}
	
	public void fromJson(JsonObject jsonObject) {
		if (jsonObject.size() == 0) return;
		for (String category : configEntries.keySet()) {
			Map<String, Entry<?>> entryCategory = this.configEntries.get(category);
			if (!jsonObject.has(category)) continue;
			JsonObject jsonCategory = jsonObject.getAsJsonObject(category);
			entryCategory.forEach((key, param) -> {
				key += " [default: " + param.getDefault() + "]";
				if (!jsonCategory.has(key)) return;
				param.fromString(JsonHelper.getString(jsonCategory, key));
			});
		}
	}
	
	@Nullable
	@SuppressWarnings("unchecked")
	public <E extends Entry<?>> E getEntry(String category, String key) {
		Map<String, Entry<?>> entryCategory = this.configEntries.get(category);
		if (entryCategory == null) {
			return null;
		}
		return (E) entryCategory.get(key);
	}
	
	@Nullable
	public <T> T getValue(String category, String key) {
		Entry<T> entry = this.getEntry(category, key);
		if (entry == null) {
			return null;
		}
		return entry.getValue();
	}
	
	public void set(String category, String key, Entry<?> entry) {
		Map<String, Entry<?>> entryCategory = this.configEntries.get(category);
		if (entryCategory != null) {
			entryCategory.put(key, entry);
		}
	}
	
	public <T extends Entry<?>> T registerEntry(String category, String key, T entry) {
		Map<String, Entry<?>> entryCategory = this.configEntries.get(category);
		if (entryCategory == null) {
			entryCategory = Maps.newHashMap();
			this.configEntries.put(category, entryCategory);
		}
		entryCategory.put(key, entry);
		return entry;
	}
	
	public static class BooleanEntry extends Entry<Boolean> {

		public BooleanEntry(Boolean defaultValue) {
			super(defaultValue);
		}

		@Override
		public String asString() {
			return this.getValue() ? "true" : "false";
		}

		@Override
		public void fromString(String value) {
			this.setValue(value.equals("true") ? true : false);
		}

	}
	
	public static class FloatEntry extends Entry<Float> {

		public FloatEntry(Float defaultValue) {
			super(defaultValue);
		}

		@Override
		public String asString() {
			return Float.toString(getValue());
		}

		@Override
		public void fromString(String value) {
			this.setValue(Float.valueOf(value));
		}

	}
	
	public static class FloatRange extends RangeEntry<Float> {

		public FloatRange(Float defaultValue, float minVal, float maxVal) {
			super(defaultValue, minVal, maxVal);
		}

		@Override
		public void fromString(String value) {
			this.setValue(Float.valueOf(value));
		}

		@Override
		public String asString() {
			return Float.toString(getValue());
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
		public String asString() {
			return Integer.toString(getValue());
		}

		@Override
		public void fromString(String value) {
			this.setValue(Integer.valueOf(value));
		}

	}
	
	public static class IntegerRange extends RangeEntry<Integer> {

		public IntegerRange(Integer defaultValue, int minVal, int maxVal) {
			super(defaultValue, minVal, maxVal);
		}

		@Override
		public String asString() {
			return Integer.toString(getValue());
		}

		@Override
		public void fromString(String value) {
			this.setValue(Integer.valueOf(value));
		}
		
	}
	
	public static class StringEntry extends Entry<String> {

		public StringEntry(String defaultValue) {
			super(defaultValue);
		}

		@Override
		public String asString() {
			return this.getValue();
		}

		@Override
		public void fromString(String value) {
			this.setValue(value);
		}

	}
	
	public static class EnumEntry<T extends Enum<T>> extends Entry<T> {

		public EnumEntry(T defaultValue) {
			super(defaultValue);
		}

		@SuppressWarnings("unchecked")
		public boolean setValue(String name) {
			try {
				this.setValue((T) Enum.valueOf(this.defaultValue.getClass(), name));
				return true;
			} catch(IllegalArgumentException ex) {
				BetterEnd.LOGGER.catching(ex);
			}
			
			return false;
		}
		
		@Override
		public T getDefault() {
			return this.defaultValue;
		}
		
		@Override
		public String asString() {
			return getValue().name();
		}

		@Override
		public void fromString(String value) {
			this.setValue(value);
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
			this.value = (value.compareTo(min) < 0 ? min : value.compareTo(max) > 0 ? max : value);
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
		protected T value;
		
		public abstract void fromString(String value);
		public abstract String asString();
		
		public Entry (T defaultValue) {
			this.defaultValue = defaultValue;
			this.value = defaultValue;
		}

		public T getValue() {
			return this.value;
		}
		
		public void setValue(T value) {
			this.value = value;
		}
		
		public T getDefault() {
			return this.defaultValue;
		}
		
		public void setDefault() {
			this.value = defaultValue;
		}
	}
}
