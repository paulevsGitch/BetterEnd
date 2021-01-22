package ru.betterend.client;

import net.fabricmc.loader.api.FabricLoader;
import ru.betterend.config.Configs;

public class ClientOptions {
	private static boolean customSky;
	private static boolean useFogDensity;
	private static boolean blendBiomeMusic;
	private static boolean sulfurWaterColor;
	
	public static void init() {
		customSky = Configs.CLENT_CONFIG.getBooleanRoot("customSky", true);
		useFogDensity = Configs.CLENT_CONFIG.getBooleanRoot("useFogDensity", true);
		blendBiomeMusic = Configs.CLENT_CONFIG.getBooleanRoot("blendBiomeMusic", true);
		sulfurWaterColor = Configs.CLENT_CONFIG.getBooleanRoot("sulfurWaterColor", true);
		if (sulfurWaterColor) {
			sulfurWaterColor = !FabricLoader.getInstance().isModLoaded("sodium");
		}
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
	
	public static boolean useSulfurWaterColor() {
		return sulfurWaterColor;
	}

	public static void setSulfurWaterColor(boolean sulfurWaterColor) {
		ClientOptions.sulfurWaterColor = sulfurWaterColor && !FabricLoader.getInstance().isModLoaded("sodium");
	}
}
