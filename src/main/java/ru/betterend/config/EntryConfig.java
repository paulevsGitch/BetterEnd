package ru.betterend.config;

public class EntryConfig extends IdConfig {

	public EntryConfig(String group) {
		super(group, (id, entry) -> {
			return new ConfigKey(id.getNamespace(), id.getPath(), entry);
		});
	}
}
