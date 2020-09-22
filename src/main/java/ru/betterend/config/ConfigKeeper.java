package ru.betterend.config;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.util.JsonHelper;
import ru.betterend.BetterEnd;

public final class ConfigKeeper {
	
	private Map<String, Entry<?>> configEntries = new HashMap<>();
	
	public JsonElement toJson(JsonObject jsonObject) {
		for (String param : configEntries.keySet()) {
			jsonObject.addProperty(param, configEntries.get(param).asString());
		}
		
		return jsonObject;
	}
	
	public void fromJson(JsonObject jsonObject) {
		for (String param : configEntries.keySet()) {
			if (jsonObject.has(param)) {
				Entry<?> entry = configEntries.get(param);
				entry.fromString(JsonHelper.getString(jsonObject, param));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <E extends Entry<?>> E getEntry(String key) {
		Entry<?> entry = this.configEntries.get(key);
		if (entry == null) {
			BetterEnd.LOGGER.warning(String.format("Entry '%s' doesn't exists.", key));			
			return null;
		}
		return (E) entry;
	}
	
	public <T> T getValue(String key) {
		Entry<T> entry = this.getEntry(key);
		if (entry == null) {
			BetterEnd.LOGGER.warning("Empty value will be returned.");
			return null;
		}
		return entry.getValue();
	}
	
	public void set(String key, Entry<?> entry) {
		configEntries.put(key, entry);
	}
	
	public <T extends Entry<?>> void registerEntry(String key, T entry) {
		configEntries.put(key, entry);
	}
	
	public static class BooleanEntry extends Entry<Boolean> {

		public BooleanEntry(Boolean defaultValue, Consumer<Boolean> consumer, Supplier<Boolean> supplier) {
			super(defaultValue, consumer, supplier);
		}

		@Override
		public Boolean getValue() {
			return this.getter.get();
		}

		@Override
		public void setValue(Boolean value) {
			this.setter.accept(value);
		}

		@Override
		public Boolean getDefault() {
			return this.defaultValue;
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

		public FloatEntry(Float defaultValue, Consumer<Float> consumer, Supplier<Float> supplier) {
			super(defaultValue, consumer, supplier);
		}

		@Override
		public Float getValue() {
			return this.getter.get();
		}

		@Override
		public void setValue(Float value) {
			this.setter.accept(value);
		}

		@Override
		public Float getDefault() {
			return this.defaultValue;
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

		public FloatRange(Float defaultValue, Consumer<Float> consumer, Supplier<Float> supplier, Float minVal, Float maxVal) {
			super(defaultValue, consumer, supplier, minVal, maxVal);
		}

		@Override
		public Float getValue() {
			return this.getter.get();
		}

		@Override
		public Float getDefault() {
			return this.defaultValue;
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

		public IntegerEntry(Integer defaultValue, Consumer<Integer> consumer, Supplier<Integer> supplier) {
			super(defaultValue, consumer, supplier);
		}

		@Override
		public Integer getValue() {
			return this.getter.get();
		}

		@Override
		public void setValue(Integer value) {
			this.setter.accept(value);
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

		public IntegerRange(Integer defaultValue, Consumer<Integer> consumer, Supplier<Integer> supplier, Integer minVal, Integer maxVal) {
			super(defaultValue, consumer, supplier, minVal, maxVal);
		}

		@Override
		public Integer getValue() {
			return this.getter.get();
		}

		@Override
		public Integer getDefault() {
			return this.defaultValue;
		}

		@Override
		public void fromString(String value) {
			this.setValue(Integer.valueOf(value));
		}

		@Override
		public String asString() {
			return Integer.toString(getValue());
		}
		
	}
	
	public static class StringEntry extends Entry<String> {

		public StringEntry(String defaultValue, Consumer<String> consumer, Supplier<String> supplier) {
			super(defaultValue, consumer, supplier);
		}

		@Override
		public String getValue() {
			return this.getter.get();
		}

		@Override
		public void setValue(String value) {
			this.setter.accept(value);
		}

		@Override
		public String getDefault() {
			return this.defaultValue;
		}

		@Override
		public String asString() {
			return getValue();
		}

		@Override
		public void fromString(String value) {
			this.setValue(value);
		}

	}
	
	public static class EnumEntry<T extends Enum<T>> extends Entry<T> {

		public EnumEntry(T defaultValue, Consumer<T> consumer, Supplier<T> supplier) {
			super(defaultValue, consumer, supplier);
		}

		@Override
		public T getValue() {
			return this.getter.get();
		}

		@Override
		public void setValue(T value) {
			this.setter.accept(value);
		}
		
		@SuppressWarnings("unchecked")
		public boolean setValue(String name) {
			try {
				this.setter.accept((T) Enum.valueOf(this.defaultValue.getClass(), name));
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

		public RangeEntry(T defaultValue, Consumer<T> consumer, Supplier<T> supplier, T minVal, T maxVal) {
			super(defaultValue, consumer, supplier);
			
			this.min = minVal;
			this.max = maxVal;
		}

		@Override
		public void setValue(T value) {
			this.setter.accept(value.compareTo(min) < 0 ? min : value.compareTo(max) > 0 ? max : value);
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
		
		protected final Consumer<T> setter;
		protected final Supplier<T> getter;
		
		public Entry (T defaultValue, Consumer<T> consumer, Supplier<T> supplier) {
			this.defaultValue = defaultValue;
			this.setter = consumer;
			this.getter = supplier;
		}

		public abstract T getValue();
		public abstract void setValue(T value);	
		public abstract T getDefault();
		public abstract void fromString(String value);
		public abstract String asString();
		
		public void setDefault() {
			this.setter.accept(defaultValue);
		}
	}
}
