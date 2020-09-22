package ru.betterend.world.biome;

import java.io.File;
import java.nio.file.Path;

import com.google.gson.JsonObject;

import net.minecraft.util.Identifier;
import ru.betterend.config.Config;
import ru.betterend.config.ConfigWriter;

public class BiomeConfig extends Config {

	private final static Path BIOME_CONFIG_DIR = ConfigWriter.MOD_CONFIG_DIR.toPath().resolve("biomes");
	
	private EndBiome biome;
	private ConfigWriter configWriter;
	private File configFile;
	
	public BiomeConfig(EndBiome biome) {
		this.biome = biome;
		Identifier biomeId = biome.getID();
		this.configFile = new File(BIOME_CONFIG_DIR.toFile(), biomeId.getPath());
		this.configWriter = new ConfigWriter();
		this.registerEntries();
		JsonObject config = configWriter.loadConfig(configFile);
		if (config.size() > 0) {
			this.configKeeper.fromJson(config);
		} else {
			this.configKeeper.toJson(config);
			this.configWriter.saveConfig();
		}
	}
	
	private void registerEntries() {
		//TODO: Need to register config params in the Keeper
	}
	
	@Override
	public void saveChanges() {
		this.configKeeper.toJson(configWriter.getConfig());
		this.configWriter.saveConfig();
	}
	
	static {
		if (!BIOME_CONFIG_DIR.toFile().exists()) {
			BIOME_CONFIG_DIR.toFile().mkdir();
		}
	}
}
