package ru.betterend.world.generator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
	private static boolean newGenerator;
	private static boolean noRingVoid;
	private static boolean generateCentralIsland;
	private static boolean generateObsidianPlatform;
	private static int endCityFailChance;
	public static LayerOptions bigOptions;
	public static LayerOptions mediumOptions;
	public static LayerOptions smallOptions;
	private static boolean changeSpawn;
	private static BlockPos spawn;
	private static BlockPos portal = BlockPos.ORIGIN;
	private static boolean replacePortal;
	
	public static void init() {
		biomeSizeLand = Configs.GENERATOR_CONFIG.getInt("biomeMap", "biomeSizeLand", 256);
		biomeSizeVoid = Configs.GENERATOR_CONFIG.getInt("biomeMap", "biomeSizeVoid", 256);
		hasPortal = Configs.GENERATOR_CONFIG.getBooleanRoot("hasPortal", true);
		hasPillars = Configs.GENERATOR_CONFIG.getBooleanRoot("hasPillars", true);
		hasDragonFights = Configs.GENERATOR_CONFIG.getBooleanRoot("hasDragonFights", true);
		swapOverworldToEnd = Configs.GENERATOR_CONFIG.getBooleanRoot("swapOverworldToEnd", false);
		changeChorusPlant = Configs.GENERATOR_CONFIG.getBoolean("chorusPlant", "changeChorusPlant", true);
		removeChorusFromVanillaBiomes = Configs.GENERATOR_CONFIG.getBoolean("chorusPlant", "removeChorusFromVanillaBiomes", true);
		newGenerator = Configs.GENERATOR_CONFIG.getBoolean("customGenerator", "useNewGenerator", true);
		noRingVoid = Configs.GENERATOR_CONFIG.getBoolean("customGenerator", "noRingVoid", false);
		generateCentralIsland = Configs.GENERATOR_CONFIG.getBoolean("customGenerator", "generateCentralIsland", false);
		endCityFailChance = Configs.GENERATOR_CONFIG.getInt("customGenerator", "endCityFailChance", 5);
		generateObsidianPlatform = Configs.GENERATOR_CONFIG.getBooleanRoot("generateObsidianPlatform", true);
		bigOptions = new LayerOptions("customGenerator.layers.bigIslands", Configs.GENERATOR_CONFIG, 300, 200, 70, 10, false);
		mediumOptions = new LayerOptions("customGenerator.layers.mediumIslands", Configs.GENERATOR_CONFIG, 150, 100, 70, 20, true);
		smallOptions = new LayerOptions("customGenerator.layers.smallIslands", Configs.GENERATOR_CONFIG, 60, 50, 70, 30, false);
		changeSpawn = Configs.GENERATOR_CONFIG.getBoolean("spawn", "changeSpawn", false);
		spawn = new BlockPos(
			Configs.GENERATOR_CONFIG.getInt("spawn.point", "x", 20),
			Configs.GENERATOR_CONFIG.getInt("spawn.point", "y", 65),
			Configs.GENERATOR_CONFIG.getInt("spawn.point", "z", 0)
		);
		replacePortal = Configs.GENERATOR_CONFIG.getBooleanRoot("customEndPortal", true);
	}

	public static int getBiomeSizeLand() {
		return MathHelper.clamp(biomeSizeLand, 1, 8192);
	}
	
	public static int getBiomeSizeVoid() {
		return MathHelper.clamp(biomeSizeVoid, 1, 8192);
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
	
	public static boolean noRingVoid() {
		return noRingVoid;
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
}
