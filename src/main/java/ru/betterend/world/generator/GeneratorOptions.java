package ru.betterend.world.generator;

import ru.betterend.config.Configs;

public class GeneratorOptions {
	private static int biomeSizeLand;
	private static int biomeSizeVoid;
	
	public static void init() {
		biomeSizeLand = Configs.GENERATOR_CONFIG.getIntRoot("biomeSizeLand", 256);
		biomeSizeVoid = Configs.GENERATOR_CONFIG.getIntRoot("biomeSizeVoid", 256);
	}

	public static int getBiomeSizeLand() {
		return biomeSizeLand;
	}
	
	public static int getBiomeSizeVoid() {
		return biomeSizeVoid;
	}
}
