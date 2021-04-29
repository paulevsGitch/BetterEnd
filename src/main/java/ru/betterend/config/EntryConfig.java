package ru.betterend.config;

public class EntryConfig extends IdConfig {

	public EntryConfig(String group) {
		super(group, (id, entry) -> {
			return new ConfigKey(entry, id);
		});
	}
}
