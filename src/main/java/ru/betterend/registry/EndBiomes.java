package ru.betterend.registry;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.biome.InternalBiomeData;
import net.fabricmc.fabric.impl.biome.WeightedBiomePicker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import ru.betterend.BetterEnd;
import ru.betterend.config.Configs;
import ru.betterend.integration.Integrations;
import ru.betterend.util.JsonFactory;
import ru.betterend.world.biome.air.BiomeIceStarfield;
import ru.betterend.world.biome.cave.EmptyAuroraCaveBiome;
import ru.betterend.world.biome.cave.EmptyEndCaveBiome;
import ru.betterend.world.biome.cave.EmptySmaragdantCaveBiome;
import ru.betterend.world.biome.cave.EndCaveBiome;
import ru.betterend.world.biome.cave.LushAuroraCaveBiome;
import ru.betterend.world.biome.cave.LushSmaragdantCaveBiome;
import ru.betterend.world.biome.land.BiomeAmberLand;
import ru.betterend.world.biome.land.BiomeBlossomingSpires;
import ru.betterend.world.biome.land.BiomeChorusForest;
import ru.betterend.world.biome.land.BiomeCrystalMountains;
import ru.betterend.world.biome.land.BiomeDustWastelands;
import ru.betterend.world.biome.land.BiomeFoggyMushroomland;
import ru.betterend.world.biome.land.BiomeMegalake;
import ru.betterend.world.biome.land.BiomeMegalakeGrove;
import ru.betterend.world.biome.land.BiomePaintedMountains;
import ru.betterend.world.biome.land.BiomeShadowForest;
import ru.betterend.world.biome.land.BiomeSulphurSprings;
import ru.betterend.world.biome.land.BiomeUmbrellaJungle;
import ru.betterend.world.biome.land.DragonGraveyardsBiome;
import ru.betterend.world.biome.land.DryShrublandBiome;
import ru.betterend.world.biome.land.EndBiome;
import ru.betterend.world.biome.land.GlowingGrasslandsBiome;
import ru.betterend.world.generator.BELayerRandomSource;
import ru.betterend.world.generator.BiomePicker;
import ru.betterend.world.generator.BiomeType;

public class EndBiomes {
	private static final HashMap<Identifier, EndBiome> ID_MAP = Maps.newHashMap();
	private static final HashMap<Biome, EndBiome> CLIENT = Maps.newHashMap();
	public static final Set<Identifier> FABRIC_VOID = Sets.newHashSet();
	private static final Set<Identifier> SUBBIOMES_UNMUTABLES = Sets.newHashSet();
	
	public static final BiomePicker LAND_BIOMES = new BiomePicker();
	public static final BiomePicker VOID_BIOMES = new BiomePicker();
	public static final BiomePicker CAVE_BIOMES = new BiomePicker();
	public static final List<EndBiome> SUBBIOMES = Lists.newArrayList();
	private static final JsonObject EMPTY_JSON = new JsonObject();
	
	private static Registry<Biome> biomeRegistry;
	
	// Vanilla Land
	public static final EndBiome END = registerBiome(BiomeKeys.THE_END, BiomeType.LAND, 1F);
	public static final EndBiome END_MIDLANDS = registerSubBiome(BiomeKeys.END_MIDLANDS, END, 0.5F);
	public static final EndBiome END_HIGHLANDS = registerSubBiome(BiomeKeys.END_HIGHLANDS, END, 0.5F);
	
	// Vanilla Void
	public static final EndBiome END_BARRENS = registerBiome(BiomeKeys.END_BARRENS, BiomeType.VOID, 1F);
	public static final EndBiome SMALL_END_ISLANDS = registerBiome(BiomeKeys.SMALL_END_ISLANDS, BiomeType.VOID, 1);
	
	// Better End Land
	public static final EndBiome FOGGY_MUSHROOMLAND = registerBiome(new BiomeFoggyMushroomland(), BiomeType.LAND);
	public static final EndBiome CHORUS_FOREST = registerBiome(new BiomeChorusForest(), BiomeType.LAND);
	public static final EndBiome DUST_WASTELANDS = registerBiome(new BiomeDustWastelands(), BiomeType.LAND);
	public static final EndBiome MEGALAKE = registerBiome(new BiomeMegalake(), BiomeType.LAND);
	public static final EndBiome MEGALAKE_GROVE = registerSubBiome(new BiomeMegalakeGrove(), MEGALAKE);
	public static final EndBiome CRYSTAL_MOUNTAINS = registerBiome(new BiomeCrystalMountains(), BiomeType.LAND);
	public static final EndBiome PAINTED_MOUNTAINS = registerSubBiome(new BiomePaintedMountains(), DUST_WASTELANDS);
	public static final EndBiome SHADOW_FOREST = registerBiome(new BiomeShadowForest(), BiomeType.LAND);
	public static final EndBiome AMBER_LAND = registerBiome(new BiomeAmberLand(), BiomeType.LAND);
	public static final EndBiome BLOSSOMING_SPIRES = registerBiome(new BiomeBlossomingSpires(), BiomeType.LAND);
	public static final EndBiome SULPHUR_SPRINGS = registerBiome(new BiomeSulphurSprings(), BiomeType.LAND);
	public static final EndBiome UMBRELLA_JUNGLE = registerBiome(new BiomeUmbrellaJungle(), BiomeType.LAND);
	public static final EndBiome GLOWING_GRASSLANDS = registerBiome(new GlowingGrasslandsBiome(), BiomeType.LAND);
	public static final EndBiome DRAGON_GRAVEYARDS = registerBiome(new DragonGraveyardsBiome(), BiomeType.LAND);
	public static final EndBiome DRY_SHRUBLAND = registerBiome(new DryShrublandBiome(), BiomeType.LAND);
	
	// Better End Void
	public static final EndBiome ICE_STARFIELD = registerBiome(new BiomeIceStarfield(), BiomeType.VOID);
	
	// Better End Caves
	public static final EndCaveBiome EMPTY_END_CAVE = registerCaveBiome(new EmptyEndCaveBiome());
	public static final EndCaveBiome EMPTY_SMARAGDANT_CAVE = registerCaveBiome(new EmptySmaragdantCaveBiome());
	public static final EndCaveBiome LUSH_SMARAGDANT_CAVE = registerCaveBiome(new LushSmaragdantCaveBiome());
	public static final EndCaveBiome EMPTY_AURORA_CAVE = registerCaveBiome(new EmptyAuroraCaveBiome());
	public static final EndCaveBiome LUSH_AURORA_CAVE = registerCaveBiome(new LushAuroraCaveBiome());
	
	public static void register() {}
	
	public static void mutateRegistry(Registry<Biome> biomeRegistry) {
		EndBiomes.biomeRegistry = biomeRegistry;
		
		LAND_BIOMES.clearMutables();
		VOID_BIOMES.clearMutables();
		CAVE_BIOMES.clearMutables();
		
		if (FABRIC_VOID.isEmpty()) {
			loadFabricAPIBiomes();
		}
		
		Map<String, JsonObject> configs = Maps.newHashMap();
		
		biomeRegistry.forEach((biome) -> {
			if (biome.getCategory() == Category.THEEND) {
				Identifier id = biomeRegistry.getId(biome);
				if (Configs.BIOME_CONFIG.getBoolean(id, "enabled", true)) {
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

						if (isVoid) {
							VOID_BIOMES.addBiomeMutable(endBiome);
						}
						else {
							LAND_BIOMES.addBiomeMutable(endBiome);
						}
						ID_MAP.put(id, endBiome);
					}
				}
			}
		});
		Integrations.addBiomes();
		Configs.BIOME_CONFIG.saveChanges();
		
		rebuildPicker(LAND_BIOMES, biomeRegistry);
		rebuildPicker(VOID_BIOMES, biomeRegistry);
		rebuildPicker(CAVE_BIOMES, biomeRegistry);
		
		SUBBIOMES.forEach((endBiome) -> {
			endBiome.updateActualBiomes(biomeRegistry);
		});
		
		CLIENT.clear();
	}
	
	private static void rebuildPicker(BiomePicker picker, Registry<Biome> biomeRegistry) {
		picker.rebuild();
		picker.getBiomes().forEach((endBiome) -> {
			endBiome.updateActualBiomes(biomeRegistry);
		});
	}
	
	private static void loadFabricAPIBiomes() {
		WeightedBiomePicker picker = InternalBiomeData.getEndBiomesMap().get(BiomeKeys.SMALL_END_ISLANDS);
		LayerRandomnessSource random = new BELayerRandomSource();
		if (picker != null) {
			for (int i = 0; i < 1000; i++) {
				RegistryKey<Biome> key = picker.pickRandom(random);
				FABRIC_VOID.add(key.getValue());
			}
		}
		picker = InternalBiomeData.getEndBiomesMap().get(BiomeKeys.END_BARRENS);
		if (picker != null) {
			for (int i = 0; i < 1000; i++) {
				RegistryKey<Biome> key = picker.pickRandom(random);
				FABRIC_VOID.add(key.getValue());
			}
		}
		if (BetterEnd.isDevEnvironment()) {
			System.out.println("==================================");
			System.out.println("Added void biomes from Fabric API:");
			FABRIC_VOID.forEach((id) -> {
				System.out.println(id);
			});
			System.out.println("==================================");
		}
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
	 * Initialize registry if it was not initialized in world generation (when using mods/datapacks, that overrides the End generation)
	 * @param server - {@link MinecraftServer}
	 */
	public static void initRegistry(MinecraftServer server) {
		if (biomeRegistry == null) {
			biomeRegistry = server.getRegistryManager().get(Registry.BIOME_KEY);
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
		EndBiome endBiome = new EndBiome(BuiltinRegistries.BIOME.getId(biome), biome, fogDensity, genChance, true);
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
		EndBiome endBiome = new EndBiome(BuiltinRegistries.BIOME.getId(biome), biome, fogDensity, genChance, hasCaves);
		if (Configs.BIOME_CONFIG.getBoolean(endBiome.getID(), "enabled", true)) {
			parent.addSubBiome(endBiome);
			SUBBIOMES.add(endBiome);
			SUBBIOMES_UNMUTABLES.add(endBiome.getID());
			ID_MAP.put(endBiome.getID(), endBiome);
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
		registerBiomeDirectly(biome);
		if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
			parent.addSubBiome(biome);
			SUBBIOMES.add(biome);
			SUBBIOMES_UNMUTABLES.add(biome.getID());
			ID_MAP.put(biome.getID(), biome);
			addLandBiomeToFabricApi(biome);
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
		registerBiomeDirectly(biome);
		if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
			addToPicker(biome, type);
			ID_MAP.put(biome.getID(), biome);
			if (type == BiomeType.LAND) {
				addLandBiomeToFabricApi(biome);
			}
			else {
				addVoidBiomeToFabricApi(biome);
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
		registerBiomeDirectly(biome);
		if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
			SUBBIOMES.add(biome);
			SUBBIOMES_UNMUTABLES.add(biome.getID());
			ID_MAP.put(biome.getID(), biome);
			addLandBiomeToFabricApi(biome);
		}
		return biome;
	}
	
	/**
	 * Link integration sub-biome with parent.
	 * @param biome - {@link EndBiome} instance
	 * @param parent - {@link Identifier} parent id
	 */
	public static void addSubBiomeIntegration(EndBiome biome, Identifier parent) {
		if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
			EndBiome parentBiome = ID_MAP.get(parent);
			if (parentBiome != null && !parentBiome.containsSubBiome(biome)) {
				parentBiome.addSubBiome(biome);
			}
		}
	}
	
	public static EndBiome registerBiome(RegistryKey<Biome> key, BiomeType type, float genChance) {
		return registerBiome(BuiltinRegistries.BIOME.get(key), type, genChance);
	}
	
	public static EndBiome registerSubBiome(RegistryKey<Biome> key, EndBiome parent, float genChance) {
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

	private static void registerBiomeDirectly(EndBiome biome) {
		if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
			Registry.register(BuiltinRegistries.BIOME, biome.getID(), biome.getBiome());
		}
	}
	
	private static void addLandBiomeToFabricApi(EndBiome biome) {
		float weight = biome.getGenChanceImmutable();
		RegistryKey<Biome> key = BuiltinRegistries.BIOME.getKey(biome.getBiome()).get();
		InternalBiomeData.addEndBiomeReplacement(BiomeKeys.END_HIGHLANDS, key, weight);
		InternalBiomeData.addEndBiomeReplacement(BiomeKeys.END_MIDLANDS, key, weight);
	}
	
	private static void addVoidBiomeToFabricApi(EndBiome biome) {
		float weight = biome.getGenChanceImmutable();
		RegistryKey<Biome> key = BuiltinRegistries.BIOME.getKey(biome.getBiome()).get();
		InternalBiomeData.addEndBiomeReplacement(BiomeKeys.SMALL_END_ISLANDS, key, weight);
	}
	
	public static EndBiome getFromBiome(Biome biome) {
		return ID_MAP.getOrDefault(biomeRegistry.getId(biome), END);
	}
	
	@Environment(EnvType.CLIENT)
	public static EndBiome getRenderBiome(Biome biome) {
		EndBiome endBiome = CLIENT.get(biome);
		if (endBiome == null) {
			MinecraftClient minecraft = MinecraftClient.getInstance();
			Identifier id = minecraft.world.getRegistryManager().get(Registry.BIOME_KEY).getId(biome);
			endBiome = id == null ? END : ID_MAP.getOrDefault(id, END);
			CLIENT.put(biome, endBiome);
		}
		return endBiome;
	}
	
	public static Identifier getBiomeID(Biome biome) {
		Identifier id = biomeRegistry.getId(biome);
		return id == null ? END.getID() : id;
	}

	public static EndBiome getBiome(Identifier biomeID) {
		return ID_MAP.getOrDefault(biomeID, END);
	}

	public static List<EndBiome> getModBiomes() {
		List<EndBiome> result = Lists.newArrayList();
		result.addAll(EndBiomes.LAND_BIOMES.getBiomes());
		result.addAll(EndBiomes.VOID_BIOMES.getBiomes());
		result.addAll(SUBBIOMES);
		return result;
	}
	
	public static EndCaveBiome registerCaveBiome(EndCaveBiome biome) {
		registerBiomeDirectly(biome);
		if (Configs.BIOME_CONFIG.getBoolean(biome.getID(), "enabled", true)) {
			CAVE_BIOMES.addBiome(biome);
			ID_MAP.put(biome.getID(), biome);
		}
		return biome;
	}
	
	public static EndCaveBiome getCaveBiome(Random random) {
		return (EndCaveBiome) CAVE_BIOMES.getBiome(random);
	}
	
	public static boolean hasBiome(Identifier biomeID) {
		return ID_MAP.containsKey(biomeID);
	}
}
