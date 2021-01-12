package ru.betterend.world.generator;

import ru.betterend.config.Configs;

public class GeneratorOptions {
	private static int biomeSizeLand;
	private static int biomeSizeVoid;
	private static boolean hasPortal;
	private static boolean hasPillars;
	private static boolean hasDragonFights;
	private static boolean swapOverworldToEnd;
	private static boolean changeChorusPlant;
	private static boolean removeChorusFromVanillaBiomes;
	
	public static void init() {
		biomeSizeLand = Configs.GENERATOR_CONFIG.getInt("biomeMap", "biomeSizeLand", 256);
		biomeSizeVoid = Configs.GENERATOR_CONFIG.getInt("biomeMap", "biomeSizeVoid", 256);
		hasPortal = Configs.GENERATOR_CONFIG.getBooleanRoot("hasPortal", true);
		hasPillars = Configs.GENERATOR_CONFIG.getBooleanRoot("hasPillars", true);
		hasDragonFights = Configs.GENERATOR_CONFIG.getBooleanRoot("hasDragonFights", true);
		swapOverworldToEnd = Configs.GENERATOR_CONFIG.getBooleanRoot("swapOverworldToEnd", false);
		changeChorusPlant = Configs.GENERATOR_CONFIG.getBoolean("chorusPlant", "changeChorusPlant", true);
		removeChorusFromVanillaBiomes = Configs.GENERATOR_CONFIG.getBoolean("chorusPlant", "removeChorusFromVanillaBiomes", true);
	}

	public static int getBiomeSizeLand() {
		return biomeSizeLand;
	}
	
	public static int getBiomeSizeVoid() {
		return biomeSizeVoid;
	}

	public static boolean hasPortal() {
		return hasPortal;
	}
	
	public static boolean hasPillars() {
		return hasPillars;
	}
	
	public static boolean hasDragonFights() {
		return hasDragonFights;
	}
	
	public static boolean swapOverworldToEnd() {
		return swapOverworldToEnd;
	}

	public static boolean changeChorusPlant() {
		return changeChorusPlant;
	}

	public static boolean removeChorusFromVanillaBiomes() {
		return removeChorusFromVanillaBiomes;
	}
}
