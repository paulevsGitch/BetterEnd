package ru.betterend.config;

import com.google.gson.JsonObject;

public class PortalsConfig {


	private final ConfigWriter writer;
	private final JsonObject configObject;

	public PortalsConfig(String path) {
		this.writer = new ConfigWriter(path);
		this.configObject = writer.load();
	}

	public void saveChanges() {
		writer.save();
	}
}
