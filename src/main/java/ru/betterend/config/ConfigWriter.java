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
	
	private final File configFile;
	private JsonObject configObject;
	
	public ConfigWriter(String configFile) {
		this.configFile = new File(MOD_CONFIG_DIR, configFile + ".json");
		this.load();
	}
	
	public JsonObject getConfig() {
		return configObject;
	}
	
	public void saveConfig() {
		if (configObject == null) {
			return;
		}
		save(configFile, configObject);
	}
	
	public JsonObject load() {
		if (configObject == null) {
			configObject = load(configFile);
		}
		return configObject;
	}
	
	public void save() {
		save(configFile, configObject);
	}
	
	public void save(JsonElement config) {
		this.configObject = config.getAsJsonObject();
		save(configFile, config);
	}
	
	public static JsonObject load(File configFile) {
		return JsonFactory.getJsonObject(configFile);
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
