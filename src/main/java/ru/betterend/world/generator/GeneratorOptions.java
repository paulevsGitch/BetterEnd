package ru.betterend.world.generator;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import ru.betterend.config.Configs;

public class GeneratorOptions {
	private static int biomeSizeCaves;
	private static boolean hasPortal;
	private static boolean hasPillars;
	private static boolean hasDragonFights;
	private static boolean swapOverworldToEnd;
	private static boolean changeChorusPlant;
	private static boolean removeChorusFromVanillaBiomes;
	private static boolean newGenerator;
	private static boolean generateCentralIsland;
	private static boolean generateObsidianPlatform;
	private static int endCityFailChance;
	public static LayerOptions bigOptions;
	public static LayerOptions mediumOptions;
	public static LayerOptions smallOptions;
	private static boolean changeSpawn;
	private static BlockPos spawn;
	private static BlockPos portal = BlockPos.ZERO;
	private static boolean replacePortal;
	private static boolean replacePillars;
	private static long islandDistBlock;
	private static int islandDistChunk;
	private static boolean directSpikeHeight;
	
	public static void init() {
		biomeSizeCaves = Configs.GENERATOR_CONFIG.getInt("biomeMap", "biomeSizeCaves", 32);
		hasPortal = Configs.GENERATOR_CONFIG.getBoolean("portal", "hasPortal", true);
		hasPillars = Configs.GENERATOR_CONFIG.getBoolean("spikes", "hasSpikes", true);
		hasDragonFights = Configs.GENERATOR_CONFIG.getBooleanRoot("hasDragonFights", true);
		swapOverworldToEnd = Configs.GENERATOR_CONFIG.getBooleanRoot("swapOverworldToEnd", false);
		changeChorusPlant = Configs.GENERATOR_CONFIG.getBoolean("chorusPlant", "changeChorusPlant", true);
		removeChorusFromVanillaBiomes = Configs.GENERATOR_CONFIG.getBoolean(
			"chorusPlant",
			"removeChorusFromVanillaBiomes",
			true
		);
		newGenerator = Configs.GENERATOR_CONFIG.getBoolean("customGenerator", "useNewGenerator", true);
		generateCentralIsland = Configs.GENERATOR_CONFIG.getBoolean("customGenerator", "generateCentralIsland", true);
		endCityFailChance = Configs.GENERATOR_CONFIG.getInt("customGenerator", "endCityFailChance", 5);
		generateObsidianPlatform = Configs.GENERATOR_CONFIG.getBooleanRoot("generateObsidianPlatform", true);
		bigOptions = new LayerOptions(
			"customGenerator.layers.bigIslands",
			Configs.GENERATOR_CONFIG,
			300,
			200,
			70,
			10,
			false
		);
		mediumOptions = new LayerOptions(
			"customGenerator.layers.mediumIslands",
			Configs.GENERATOR_CONFIG,
			150,
			100,
			70,
			20,
			true
		);
		smallOptions = new LayerOptions(
			"customGenerator.layers.smallIslands",
			Configs.GENERATOR_CONFIG,
			60,
			50,
			70,
			30,
			false
		);
		changeSpawn = Configs.GENERATOR_CONFIG.getBoolean("spawn", "changeSpawn", false);
		spawn = new BlockPos(
			Configs.GENERATOR_CONFIG.getInt("spawn.point", "x", 20),
			Configs.GENERATOR_CONFIG.getInt("spawn.point", "y", 65),
			Configs.GENERATOR_CONFIG.getInt("spawn.point", "z", 0)
		);
		replacePortal = Configs.GENERATOR_CONFIG.getBoolean("portal", "customEndPortal", true);
		replacePillars = Configs.GENERATOR_CONFIG.getBoolean("spikes", "customObsidianSpikes", true);
		int circleRadius = Configs.GENERATOR_CONFIG.getInt("customGenerator", "voidRingSize", 1000);
		islandDistBlock = (long) circleRadius * (long) circleRadius;
		islandDistChunk = (circleRadius >> 3); // Twice bigger than normal
	}
	
	public static int getBiomeSizeCaves() {
		return Mth.clamp(biomeSizeCaves, 1, 8192);
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
	
	public static boolean useNewGenerator() {
		return newGenerator;
	}
	
	public static boolean hasCentralIsland() {
		return generateCentralIsland;
	}
	
	public static boolean generateObsidianPlatform() {
		return generateObsidianPlatform;
	}
	
	public static int getEndCityFailChance() {
		return endCityFailChance;
	}
	
	public static boolean changeSpawn() {
		return changeSpawn;
	}
	
	public static BlockPos getSpawn() {
		return spawn;
	}
	
	public static BlockPos getPortalPos() {
		return portal;
	}
	
	public static void setPortalPos(BlockPos portal) {
		GeneratorOptions.portal = portal;
	}
	
	public static boolean replacePortal() {
		return replacePortal;
	}
	
	public static boolean replacePillars() {
		return replacePillars;
	}
	
	public static long getIslandDistBlock() {
		return islandDistBlock;
	}
	
	public static int getIslandDistChunk() {
		return islandDistChunk;
	}
	
	public static void setDirectSpikeHeight() {
		directSpikeHeight = true;
	}
	
	public static boolean isDirectSpikeHeight() {
		boolean height = directSpikeHeight;
		directSpikeHeight = false;
		return height;
	}
}
