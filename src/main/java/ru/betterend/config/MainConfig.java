package ru.betterend.config;

import com.google.gson.JsonObject;

public class MainConfig extends Config {
	
	private static MainConfig instance;
	
	public static MainConfig getInstance() {
		if (instance == null) {
			instance = new MainConfig();
		}
		
		return instance;
	}
	
	private MainConfig() {
		//TODO: Need to register config params in the Keeper
		
		JsonObject config = ConfigWriter.load();
		if (config.size() > 0) {
			this.configKeeper.fromJson(config);
		} else {
			this.configKeeper.toJson(config);
			ConfigWriter.save();
		}
	}
	
	@Override
	public void saveChanges() {
		this.configKeeper.toJson(ConfigWriter.load());
		ConfigWriter.save();
	}
}
