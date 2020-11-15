package ru.betterend.world.biome;

import java.nio.file.Path;

import com.google.gson.JsonObject;

import net.minecraft.util.Identifier;

import ru.betterend.config.Config;
import ru.betterend.config.ConfigWriter;

public class BiomeConfig extends Config {

	private final static Path BIOME_CONFIG_DIR = ConfigWriter.MOD_CONFIG_DIR.toPath().resolve("biomes");
	
	public static BiomeConfig getConfig(EndBiome biome) {
		return new BiomeConfig(biome);
	}
	
	private final EndBiome biome;
	private final ConfigWriter writer;
	
	public BiomeConfig(EndBiome biome) {
		this.biome = biome;
		Identifier biomeId = biome.getID();
		String file = ConfigWriter.scrubFileName(biomeId.toString());
		this.writer = new ConfigWriter("biomes/" + file);
		this.settings = writer.load();
		this.registerEntries();
		if (settings.size() > 0) {
			this.configKeeper.fromJson(settings);
		} else {
			this.configKeeper.toJson(settings);
			this.writer.save();
		}
	}
	
	@Override
	protected void registerEntries() {
		//TODO: Need to register config params in the Keeper
	}
	
	@Override
	public void saveChanges() {
		this.configKeeper.toJson(settings);
		this.writer.saveConfig();
	}
	
	static {
		if (!BIOME_CONFIG_DIR.toFile().exists()) {
			BIOME_CONFIG_DIR.toFile().mkdir();
		}
	}
}
