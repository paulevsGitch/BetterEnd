package ru.betterend.config;

import java.io.File;
import java.nio.file.Path;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fabricmc.loader.api.FabricLoader;

import ru.betterend.BetterEnd;
import ru.betterend.util.JsonFactory;

public class ConfigWriter {
	
	private final static FabricLoader fabricLoader = FabricLoader.getInstance();
	private final static Path GAME_CONFIG_DIR = fabricLoader.getConfigDir();
	private final static File MOD_CONFIG_DIR = new File(GAME_CONFIG_DIR.toFile(), BetterEnd.MOD_ID);
	private final static File CONFIG_FILE = new File(MOD_CONFIG_DIR, "settings.json");
	
	private static JsonObject configObject;
	
	private ConfigWriter() {}
	
	public static JsonObject load() {
		if (configObject == null) {
			configObject = JsonFactory.getJsonObject(CONFIG_FILE);
		}
		
		return configObject;
	}
	
	public static void save(JsonElement config) {
		JsonFactory.storeJson(CONFIG_FILE, config);
	}
}
