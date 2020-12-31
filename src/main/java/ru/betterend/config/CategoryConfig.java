package ru.betterend.config;

public class CategoryConfig extends IdConfig {

	public CategoryConfig(String group) {
		super(group, (id, category) -> {
			return new ConfigKey(id.getPath(), id.getNamespace(), category);
		});
	}
}
