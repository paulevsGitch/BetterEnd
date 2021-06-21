package ru.betterend.registry;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fabricmc.fabric.impl.biome.InternalBiomeData;
import net.fabricmc.fabric.impl.biome.WeightedBiomePicker;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.biome.Biomes;
import ru.bclib.BCLib;
import ru.bclib.api.BiomeAPI;
import ru.bclib.api.ModIntegrationAPI;
import ru.bclib.util.JsonFactory;
import ru.bclib.world.biomes.BCLBiome;
import ru.bclib.world.generator.BiomeMap;
import ru.bclib.world.generator.BiomePicker;
import ru.betterend.config.Configs;
import ru.betterend.integration.EndBiomeIntegration;
import ru.betterend.interfaces.IBiomeList;
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
import ru.betterend.world.biome.land.UmbrellaJungleBiome;
import ru.betterend.world.generator.BiomeType;
import ru.betterend.world.generator.GeneratorOptions;

public class EndBiomes {
	public static final Set<ResourceLocation> FABRIC_VOID = Sets.newHashSet();
	private static final Set<ResourceLocation> SUBBIOMES_UNMUTABLES = Sets.newHashSet();
	
	public static final BiomePicker LAND_BIOMES = new BiomePicker();
	public static final BiomePicker VOID_BIOMES = new BiomePicker();
	public static final BiomePicker CAVE_BIOMES = new BiomePicker();
	public static final List<BCLBiome> SUBBIOMES = Lists.newArrayList();
	private static final JsonObject EMPTY_JSON = new JsonObject();
	private static BiomeMap caveBiomeMap;
	
	// Vanilla Land
	public static final EndBiome END = registerBiome(Biomes.THE_END, BiomeType.LAND, 1F);
	public static final EndBiome END_MIDLANDS = registerSubBiome(Biomes.END_MIDLANDS, END, 0.5F);
	public static final EndBiome END_HIGHLANDS = registerSubBiome(Biomes.END_HIGHLANDS, END, 0.5F);
	
	// Vanilla Void
	public static final EndBiome END_BARRENS = registerBiome(Biomes.END_BARRENS, BiomeType.VOID, 1F);
	public static final EndBiome SMALL_END_ISLANDS = registerBiome(Biomes.SMALL_END_ISLANDS, BiomeType.VOID, 1);
	
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
	
	// Better End Void
	public static final EndBiome ICE_STARFIELD = registerBiome(new BiomeIceStarfield(), BiomeType.VOID);
	
	// Better End Caves
	public static final EndCaveBiome EMPTY_END_CAVE = registerCaveBiome(new EmptyEndCaveBiome());
	public static final EndCaveBiome EMPTY_SMARAGDANT_CAVE = registerCaveBiome(new EmptySmaragdantCaveBiome());
	public static final EndCaveBiome LUSH_SMARAGDANT_CAVE = registerCaveBiome(new LushSmaragdantCaveBiome());
	public static final EndCaveBiome EMPTY_AURORA_CAVE = registerCaveBiome(new EmptyAuroraCaveBiome());
	public static final EndCaveBiome LUSH_AURORA_CAVE = registerCaveBiome(new LushAuroraCaveBiome());
	public static final EndCaveBiome JADE_CAVE = registerCaveBiome(new JadeCaveBiome());
	
	public static void register() {
		CAVE_BIOMES.rebuild();
	}
	
	public static void onWorldLoad(long seed) {
		if (caveBiomeMap == null || caveBiomeMap.getSeed() != seed) {
			caveBiomeMap = new BiomeMap(seed, GeneratorOptions.getBiomeSizeCaves(), CAVE_BIOMES);
		}
	}
	
	public static void mutateRegistry(Registry<Biome> biomeRegistry) {
		LAND_BIOMES.clearMutables();
		VOID_BIOMES.clearMutables();
		CAVE_BIOMES.clearMutables();
		
		if (FABRIC_VOID.isEmpty()) {
			loadFabricAPIBiomes();
		}
		
		Map<String, JsonObject> configs = Maps.newHashMap();
		
		biomeRegistry.forEach((biome) -> {
			if (biome.getBiomeCategory() == BiomeCategory.THEEND) {
				ResourceLocation id = biomeRegistry.getKey(biome);
				if (!id.getNamespace().equals("ultra_amplified_dimension") && Configs.BIOME_CONFIG.getBoolean(id, "enabled", true)) {
					if (!LAND_BIOMES.containsImmutable(id) && !VOID_BIOMES.containsImmutable(id) && !SUBBIOMES_UNMUTABLES.contains(id)) {
						JsonObject config = configs.get(id.getNamespace());
						if (config == null) {
							config = loadJsonConfig(id.getNamespace());
							configs.put(id.getNamespace(), config);
						}
						float fog = 1F;
						float chance = 1F;
						boolean isVoid = FABRIC_VOID.contains(id);
						boolean hasCaves = true;
						JsonElement element = config.get(id.getPath());
						if (element != null && element.isJsonObject()) {
							fog = JsonFactory.getFloat(element.getAsJsonObject(), "fog_density", 1);
							chance = JsonFactory.getFloat(element.getAsJsonObject(), "generation_chance", 1);
							isVoid = JsonFactory.getString(element.getAsJsonObject(), "type", "land").equals("void");
							hasCaves = JsonFactory.getBoolean(element.getAsJsonObject(), "has_caves", true);
						}
						EndBiome endBiome = new EndBiome(id, biome, fog, chance, hasCaves);
						System.out.println("Added biome: " + endBiome);

						if (isVoid) {
							VOID_BIOMES.addBiomeMutable(endBiome);
						}
						else {
							LAND_BIOMES.addBiomeMutable(endBiome);
						}
						BiomeAPI.registerBiome(endBiome);
					}
				}
			}
		});
		ModIntegrationAPI.getIntegrations().forEach(integration -> {
			if (integration instanceof EndBiomeIntegration && integration.modIsInstalled()) {
				((EndBiomeIntegration) integration).addBiomes();
			}
		});
		Configs.BIOME_CONFIG.saveChanges();
		
		rebuildPicker(LAND_BIOMES, biomeRegistry);
		rebuildPicker(VOID_BIOMES, biomeRegistry);
		rebuildPicker(CAVE_BIOMES, biomeRegistry);
		
		SUBBIOMES.forEach((endBiome) -> {
			endBiome.updateActualBiomes(biomeRegistry);
		});
	}
	
	private static void rebuildPicker(BiomePicker picker, Registry<Biome> biomeRegistry) {
		picker.rebuild();
		picker.getBiomes().forEach((endBiome) -> {
			endBiome.updateActualBiomes(biomeRegistry);
		});
	}
	
	private static void loadFabricAPIBiomes() {
		List<ResourceKey<Biome>> biomes = Lists.newArrayList();
		biomes.addAll(getBiomes(InternalBiomeData.getEndBiomesMap().get(Biomes.SMALL_END_ISLANDS)));
		biomes.addAll(getBiomes(InternalBiomeData.getEndBarrensMap().get(Biomes.END_BARRENS)));
		biomes.forEach((key) -> FABRIC_VOID.add(key.location()));
		FABRIC_VOID.removeIf(id -> id.getNamespace().equals("endplus"));
		
		if (BCLib.isDevEnvironment()) {
			System.out.println("==================================");
			System.out.println("Added void biomes from Fabric API:");
			FABRIC_VOID.forEach((id) -> {
				System.out.println(id);
			});
			System.out.println("==================================");
		}
	}
	
	private static List<ResourceKey<Biome>> getBiomes(WeightedBiomePicker picker) {
		IBiomeList biomeList = (IBiomeList) (Object) picker;
		return biomeList == null ? Collections.emptyList() : biomeList.getBiomes();
	}
	
	private static JsonObject loadJsonConfig(String namespace) {
		InputStream inputstream = EndBiomes.class.getResourceAsStream("/data/" + namespace + "/end_biome_properties.json");
		if (inputstream != null) {
			return JsonFactory.getJsonObject(inputstream);
		}
		else {
			return EMPTY_JSON;
		}
	}
	
	/**
	 * Registers new {@link EndBiome} and adds it to picker, can be used to add existing mod biomes into the End.
	 * @param biome - {@link Biome} instance
	 * @param type - {@link BiomeType}
	 * @param genChance - generation chance [0.0F - Infinity]
	 * @return registered {@link EndBiome}
	 */
	public static EndBiome registerBiome(Biome biome, BiomeType type, float genChance) {
		return registerBiome(biome, type, 1, genChance);
	}
	
	/**
	 * Registers new {@link EndBiome} and adds it to picker, can be used to add existing mod biomes into the End.
	 * @param biome - {@link Biome} instance
	 * @param type - {@link BiomeType}
	 * @param fogDensity - density of fog (def: 1F) [0.0F - Infinity]
	 * @param genChance - generation chance [0.0F - Infinity]
	 * @return registered {@link EndBiome}
	 */
	public static EndBiome registerBiome(Biome biome, BiomeType type, float fogDensity, float genChance) {
		EndBiome endBiome = new EndBiome(BuiltinRegistries.BIOME.getKey(biome), biome, fogDensity, genChance, true);
		if (Configs.BIOME_CONFIG.getBoolean(endBiome.getID(), "enabled", true)) {
			addToPicker(endBiome, type);
		}
		return endBiome;
	}
	
	/**
	 * Registers new {@link EndBiome} from existed {@link Biome} and put as a sub-biome into selected parent.
	 * @param biome - {@link Biome} instance
	 * @param parent - {@link EndBiome} to be linked with
	 * @param genChance - generation chance [0.0F - Infinity]
	 * @return registered {@link EndBiome}
	 */
	public static EndBiome registerSubBiome(Biome biome, EndBiome parent, float genChance, boolean hasCaves) {
		return registerSubBiome(biome, parent, 1, genChance, hasCaves);
	}
	
	/**
	 * Registers new {@link EndBiome} from existed {@link Biome} and put as a sub-biome into selected parent.
	 * @param biome - {@link Biome} instance
	 * @param parent - {@link EndBiome} to be linked with
	 * @param fogDensity - density of fog (def: 1F) [0.0F - Infinity]
	 * @param genChance - generation chance [0.0F - Infinity]
	 * @return registered {@link EndBiome}
	 */
	public static EndBiome registerSubBiome(Biome biome, EndBiome parent, float fogDensity, float genChance, boolean hasCaves) {
		EndBiome endBiome = new EndBiome(BuiltinRegistries.BIOME.getKey(biome), biome, fogDensity, genChance, hasCaves);
		if (Configs.BIOME_CONFIG.getBoolean(endBiome.getID(), "enabled", true)) {
			BiomeAPI.registerBiome(endBiome);
			parent.addSubBiome(endBiome);
			SUBBIOMES.add(endBiome);
			SUBBIOMES_UNMUTABLES.add(endBiome.getID());
			BiomeAPI.registerBiome(endBiome);
		}
		return endBiome;
	}
	
	/**
	 * Put existing {@link EndBiome} as a sub-biome into selected parent.
	 * @param biome - {@link EndBiome} instance
	 * @param parent - {@link EndBiome} to be linked with
	 * @return registered {@link EndBiome}
	 */
	public static EndBiome registerSubBiome(EndBiome biome, EndBiome parent) {
		if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
			BiomeAPI.registerBiome(biome);
			parent.addSubBiome(biome);
			SUBBIOMES.add(biome);
			SUBBIOMES_UNMUTABLES.add(biome.getID());
			BiomeAPI.addEndLandBiomeToFabricApi(biome);
		}
		return biome;
	}
	
	/**
	 * Registers {@link EndBiome} and adds it into worldgen.
	 * @param biome - {@link EndBiome} instance
	 * @param type - {@link BiomeType}
	 * @return registered {@link EndBiome}
	 */
	public static EndBiome registerBiome(EndBiome biome, BiomeType type) {
		if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
			BiomeAPI.registerBiome(biome);
			addToPicker(biome, type);
			if (type == BiomeType.LAND) {
				BiomeAPI.addEndLandBiomeToFabricApi(biome);
			}
			else {
				BiomeAPI.addEndVoidBiomeToFabricApi(biome);
			}
		}
		return biome;
	}
	
	/**
	 * Put integration sub-biome {@link EndBiome} into subbiomes list and registers it.
	 * @param biome - {@link EndBiome} instance
	 * @return registered {@link EndBiome}
	 */
	public static EndBiome registerSubBiomeIntegration(EndBiome biome) {
		if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
			BiomeAPI.registerBiome(biome);
			SUBBIOMES.add(biome);
			SUBBIOMES_UNMUTABLES.add(biome.getID());
			BiomeAPI.addEndLandBiomeToFabricApi(biome);
		}
		return biome;
	}
	
	/**
	 * Link integration sub-biome with parent.
	 * @param biome - {@link EndBiome} instance
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
	
	public static EndBiome registerBiome(ResourceKey<Biome> key, BiomeType type, float genChance) {
		return registerBiome(BuiltinRegistries.BIOME.get(key), type, genChance);
	}
	
	public static EndBiome registerSubBiome(ResourceKey<Biome> key, EndBiome parent, float genChance) {
		return registerSubBiome(BuiltinRegistries.BIOME.get(key), parent, genChance, true);
	}
	
	private static void addToPicker(EndBiome biome, BiomeType type) {
		if (type == BiomeType.LAND) {
			LAND_BIOMES.addBiome(biome);
		}
		else {
			VOID_BIOMES.addBiome(biome);
		}
	}
	
	public static EndCaveBiome registerCaveBiome(EndCaveBiome biome) {
		if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
			BiomeAPI.registerBiome(biome);
			CAVE_BIOMES.addBiome(biome);
		}
		return biome;
	}
	
	public static EndCaveBiome getCaveBiome(int x, int z) {
		return (EndCaveBiome) caveBiomeMap.getBiome(x, z);
	}
}
