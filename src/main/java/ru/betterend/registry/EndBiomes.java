package ru.betterend.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.world.biomes.BCLBiome;
import ru.bclib.world.generator.BiomePicker;
import ru.bclib.world.generator.map.hex.HexBiomeMap;
import ru.betterend.config.Configs;
import ru.betterend.world.biome.EndBiome;
import ru.betterend.world.biome.air.BiomeIceStarfield;
import ru.betterend.world.biome.cave.EmptyAuroraCaveBiome;
import ru.betterend.world.biome.cave.EmptyEndCaveBiome;
import ru.betterend.world.biome.cave.EmptySmaragdantCaveBiome;
import ru.betterend.world.biome.cave.EndCaveBiome;
import ru.betterend.world.biome.cave.JadeCaveBiome;
import ru.betterend.world.biome.cave.LushAuroraCaveBiome;
import ru.betterend.world.biome.cave.LushSmaragdantCaveBiome;
import ru.betterend.world.biome.land.AmberLandBiome;
import ru.betterend.world.biome.land.BlossomingSpiresBiome;
import ru.betterend.world.biome.land.ChorusForestBiome;
import ru.betterend.world.biome.land.CrystalMountainsBiome;
import ru.betterend.world.biome.land.DragonGraveyardsBiome;
import ru.betterend.world.biome.land.DryShrublandBiome;
import ru.betterend.world.biome.land.DustWastelandsBiome;
import ru.betterend.world.biome.land.FoggyMushroomlandBiome;
import ru.betterend.world.biome.land.GlowingGrasslandsBiome;
import ru.betterend.world.biome.land.LanternWoodsBiome;
import ru.betterend.world.biome.land.MegalakeBiome;
import ru.betterend.world.biome.land.MegalakeGroveBiome;
import ru.betterend.world.biome.land.NeonOasisBiome;
import ru.betterend.world.biome.land.PaintedMountainsBiome;
import ru.betterend.world.biome.land.ShadowForestBiome;
import ru.betterend.world.biome.land.SulphurSpringsBiome;
import ru.betterend.world.biome.land.UmbraValleyBiome;
import ru.betterend.world.biome.land.UmbrellaJungleBiome;
import ru.betterend.world.generator.BiomeType;
import ru.betterend.world.generator.GeneratorOptions;

public class EndBiomes {
	public static final BiomePicker CAVE_BIOMES = new BiomePicker();
	private static HexBiomeMap caveBiomeMap;
	private static long lastSeed;
	
	// Better End Land
	public static final EndBiome FOGGY_MUSHROOMLAND = registerBiome(new FoggyMushroomlandBiome(), BiomeType.LAND);
	public static final EndBiome CHORUS_FOREST = registerBiome(new ChorusForestBiome(), BiomeType.LAND);
	public static final EndBiome DUST_WASTELANDS = registerBiome(new DustWastelandsBiome(), BiomeType.LAND);
	public static final EndBiome MEGALAKE = registerBiome(new MegalakeBiome(), BiomeType.LAND);
	public static final EndBiome MEGALAKE_GROVE = registerSubBiome(new MegalakeGroveBiome(), MEGALAKE);
	public static final EndBiome CRYSTAL_MOUNTAINS = registerBiome(new CrystalMountainsBiome(), BiomeType.LAND);
	public static final EndBiome PAINTED_MOUNTAINS = registerSubBiome(new PaintedMountainsBiome(), DUST_WASTELANDS);
	public static final EndBiome SHADOW_FOREST = registerBiome(new ShadowForestBiome(), BiomeType.LAND);
	public static final EndBiome AMBER_LAND = registerBiome(new AmberLandBiome(), BiomeType.LAND);
	public static final EndBiome BLOSSOMING_SPIRES = registerBiome(new BlossomingSpiresBiome(), BiomeType.LAND);
	public static final EndBiome SULPHUR_SPRINGS = registerBiome(new SulphurSpringsBiome(), BiomeType.LAND);
	public static final EndBiome UMBRELLA_JUNGLE = registerBiome(new UmbrellaJungleBiome(), BiomeType.LAND);
	public static final EndBiome GLOWING_GRASSLANDS = registerBiome(new GlowingGrasslandsBiome(), BiomeType.LAND);
	public static final EndBiome DRAGON_GRAVEYARDS = registerBiome(new DragonGraveyardsBiome(), BiomeType.LAND);
	public static final EndBiome DRY_SHRUBLAND = registerBiome(new DryShrublandBiome(), BiomeType.LAND);
	public static final EndBiome LANTERN_WOODS = registerBiome(new LanternWoodsBiome(), BiomeType.LAND);
	public static final EndBiome NEON_OASIS = registerSubBiome(new NeonOasisBiome(), DUST_WASTELANDS);
	public static final EndBiome UMBRA_VALLEY = registerBiome(new UmbraValleyBiome(), BiomeType.LAND);
	
	// Better End Void
	public static final EndBiome ICE_STARFIELD = registerBiome(new BiomeIceStarfield(), BiomeType.VOID);
	
	// Better End Caves
	public static final EndCaveBiome EMPTY_END_CAVE = registerCaveBiome(new EmptyEndCaveBiome());
	public static final EndCaveBiome EMPTY_SMARAGDANT_CAVE = registerCaveBiome(new EmptySmaragdantCaveBiome());
	public static final EndCaveBiome LUSH_SMARAGDANT_CAVE = registerCaveBiome(new LushSmaragdantCaveBiome());
	public static final EndCaveBiome EMPTY_AURORA_CAVE = registerCaveBiome(new EmptyAuroraCaveBiome());
	public static final EndCaveBiome LUSH_AURORA_CAVE = registerCaveBiome(new LushAuroraCaveBiome());
	public static final EndCaveBiome JADE_CAVE = registerCaveBiome(new JadeCaveBiome());
	
	public static void register() {}
	
	public static void onWorldLoad(long seed, Registry<Biome> registry) {
		CAVE_BIOMES.getBiomes().forEach(biome -> biome.updateActualBiomes(registry));
		CAVE_BIOMES.rebuild();
		if (caveBiomeMap == null || lastSeed != seed) {
			caveBiomeMap = new HexBiomeMap(seed, GeneratorOptions.getBiomeSizeCaves(), CAVE_BIOMES);
			lastSeed = seed;
		}
	}
	
	/**
	 * Put existing {@link EndBiome} as a sub-biome into selected parent.
	 *
	 * @param biomeConfig  - {@link EndBiome.Config} instance
	 * @param parent - {@link EndBiome} to be linked with
	 * @return registered {@link EndBiome}
	 */
	public static EndBiome registerSubBiome(EndBiome.Config biomeConfig, EndBiome parent) {
		final EndBiome biome = EndBiome.create(biomeConfig);

		if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
			BiomeAPI.registerSubBiome(parent, biome);
		}
		return biome;
	}
	
	/**
	 * Registers {@link EndBiome} and adds it into worldgen.
	 *
	 * @param biomeConfig - {@link EndBiome.Config} instance
	 * @param type  - {@link BiomeType}
	 * @return registered {@link EndBiome}
	 */
	public static EndBiome registerBiome(EndBiome.Config biomeConfig, BiomeType type) {
		final EndBiome biome = EndBiome.create(biomeConfig);
		if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
			if (type == BiomeType.LAND) {
				BiomeAPI.registerEndLandBiome(biome);
			}
			else {
				BiomeAPI.registerEndVoidBiome(biome);
			}
		}
		return biome;
	}
	
	/**
	 * Put integration sub-biome {@link EndBiome} into subbiomes list and registers it.
	 *
	 * @param biomeConfig - {@link EndBiome.Config} instance
	 * @return registered {@link EndBiome}
	 */
	public static EndBiome registerSubBiomeIntegration(EndBiome.Config biomeConfig) {
		EndBiome biome = EndBiome.create(biomeConfig);
		if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
			BiomeAPI.registerBiome(biome);
		}
		return biome;
	}
	
	/**
	 * Link integration sub-biome with parent.
	 *
	 * @param biome  - {@link EndBiome} instance
	 * @param parent - {@link ResourceLocation} parent id
	 */
	public static void addSubBiomeIntegration(EndBiome biome, ResourceLocation parent) {
		if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
			BCLBiome parentBiome = BiomeAPI.getBiome(parent);
			if (parentBiome != null && !parentBiome.containsSubBiome(biome)) {
				parentBiome.addSubBiome(biome);
			}
		}
	}
	
	public static EndCaveBiome registerCaveBiome(EndCaveBiome.Config biomeConfig) {
		final EndCaveBiome biome = EndCaveBiome.create(biomeConfig);
		if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
			BiomeAPI.registerBiome(biome);
			CAVE_BIOMES.addBiome(biome);
		}
		return biome;
	}
	
	public static EndCaveBiome getCaveBiome(int x, int z) {
		return (EndCaveBiome) caveBiomeMap.getBiome(x, 5, z);
	}
}
