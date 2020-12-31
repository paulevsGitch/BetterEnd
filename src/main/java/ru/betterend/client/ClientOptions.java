package ru.betterend.client;

import ru.betterend.config.Configs;

public class ClientOptions {
	private static boolean customSky;
	
	public static void init() {
		setCustomSky(Configs.CLENT_CONFIG.getBooleanRoot("customSky", true));
		Configs.CLENT_CONFIG.saveChanges();
	}

	public static boolean isCustomSky() {
		return customSky;
	}

	public static void setCustomSky(boolean customSky) {
		ClientOptions.customSky = customSky;
	}
}
