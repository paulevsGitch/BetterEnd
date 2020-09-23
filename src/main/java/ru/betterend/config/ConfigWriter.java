package ru.betterend.config;

import java.io.File;
import java.nio.file.Path;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fabricmc.loader.api.FabricLoader;
import ru.betterend.BetterEnd;
import ru.betterend.util.JsonFactory;

public class ConfigWriter {
	
	private final static Path GAME_CONFIG_DIR = FabricLoader.getInstance().getConfigDir();
	public final static File MOD_CONFIG_DIR = new File(GAME_CONFIG_DIR.toFile(), BetterEnd.MOD_ID);
	private final static File MAIN_CONFIG_FILE = new File(MOD_CONFIG_DIR, "settings.json");
	
	private static JsonObject mainConfig;
	
	private JsonObject configObject;
	private File configFile;
	
	public JsonObject getConfig() {
		return configObject;
	}
	
	public JsonObject loadConfig(File configFile) {
		this.configFile = configFile;
		if (configObject == null) {
			configObject = load(configFile);
		}
		
		return configObject;
	}
	
	public void saveConfig() {
		if (configFile == null || configObject == null) {
			return;
		}
		save(configFile, configObject);
	}
	
	public static JsonObject load() {
		if (mainConfig == null) {
			mainConfig = load(MAIN_CONFIG_FILE);
		}
		return mainConfig;
	}
	
	public static JsonObject load(File configFile) {
		return JsonFactory.getJsonObject(configFile);
	}
	
	public static void save() {
		save(MAIN_CONFIG_FILE, mainConfig);
	}
	
	public static void save(JsonElement config) {
		save(MAIN_CONFIG_FILE, config);
	}
	
	public static void save(File configFile, JsonElement config) {
		JsonFactory.storeJson(configFile, config);
	}

	public static String scrubFileName(String input) {
		input = input.replaceAll("[/\\ ]+", "_");
		input = input.replaceAll("[,:&\"\\|\\<\\>\\?\\*]", "_");

		return input;
	}
	
	static {
		if (!MOD_CONFIG_DIR.exists()) {
			MOD_CONFIG_DIR.mkdirs();
		}
	}
}
