package ru.betterend.client;

import ru.betterend.config.Configs;

public class ClientOptions {
	private static boolean customSky;
	private static boolean useFogDensity;
	private static boolean blendBiomeMusic;
	
	public static void init() {
		setCustomSky(Configs.CLENT_CONFIG.getBooleanRoot("customSky", true));
		setUseFogDensity(Configs.CLENT_CONFIG.getBooleanRoot("useFogDensity", true));
		setBlendBiomeMusic(Configs.CLENT_CONFIG.getBooleanRoot("blendBiomeMusic", true));
		Configs.CLENT_CONFIG.saveChanges();
	}

	public static boolean isCustomSky() {
		return customSky;
	}

	public static void setCustomSky(boolean customSky) {
		ClientOptions.customSky = customSky;
	}

	public static boolean useFogDensity() {
		return useFogDensity;
	}

	public static void setUseFogDensity(boolean useFogDensity) {
		ClientOptions.useFogDensity = useFogDensity;
	}

	public static boolean blendBiomeMusic() {
		return blendBiomeMusic;
	}

	public static void setBlendBiomeMusic(boolean blendBiomeMusic) {
		ClientOptions.blendBiomeMusic = blendBiomeMusic;
	}
}
