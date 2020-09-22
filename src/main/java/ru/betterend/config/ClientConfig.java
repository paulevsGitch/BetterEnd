package ru.betterend.config;

import com.google.gson.JsonObject;

public class ClientConfig extends Config {
	
	private static ClientConfig instance;
	
	public static ClientConfig get() {
		if (instance == null) {
			instance = new ClientConfig();
		}
		
		return instance;
	}
	
	private ClientConfig() {
		JsonObject config = ConfigWriter.load();
		if (config.size() > 0) {
			KEEPER.fromJson(config);
		} else {
			ConfigWriter.save(KEEPER.toJson());
		}
	}
	
	public void reloadFromDisk() {
		JsonObject config = ConfigWriter.load();
		if (config.size() > 0) {
			KEEPER.fromJson(config);
		}
	}

	@Override
	public void saveChanges() {
		ConfigWriter.save(KEEPER.toJson());
	}
}
