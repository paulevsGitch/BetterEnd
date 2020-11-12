package ru.betterend.registry;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.BiomeKeys;
import ru.betterend.BetterEnd;
import ru.betterend.util.JsonFactory;
import ru.betterend.world.biome.BiomeChorusForest;
import ru.betterend.world.biome.BiomeCrystalMountains;
import ru.betterend.world.biome.BiomeDustWastelands;
import ru.betterend.world.biome.BiomeFoggyMushroomland;
import ru.betterend.world.biome.BiomeMegalake;
import ru.betterend.world.biome.BiomeMegalakeGrove;
import ru.betterend.world.biome.BiomePaintedMountains;
import ru.betterend.world.biome.BiomeShadowForest;
import ru.betterend.world.biome.EndBiome;
import ru.betterend.world.generator.BiomePicker;
import ru.betterend.world.generator.BiomeType;

public class EndBiomes {
	private static final Map<EndBiome, RegistryKey<Biome>> KEYS = Maps.newHashMap();
	private static final HashMap<Identifier, EndBiome> ID_MAP = Maps.newHashMap();
	private static final HashMap<Biome, EndBiome> MUTABLE = Maps.newHashMap();
	private static final HashMap<Biome, EndBiome> CLIENT = Maps.newHashMap();
	
	public static final BiomePicker LAND_BIOMES = new BiomePicker();
	public static final BiomePicker VOID_BIOMES = new BiomePicker();
	public static final List<EndBiome> SUBBIOMES = Lists.newArrayList();
	private static final JsonObject EMPTY_JSON = new JsonObject();
	
	private static Registry<Biome> biomeRegistry;
	private static Set<Integer> occupiedIDs = Sets.newHashSet();
	private static int incID = 2048;
	
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
	
	public static void register() {}
	
	public static void mutateRegistry(Registry<Biome> biomeRegistry) {
		EndBiomes.biomeRegistry = biomeRegistry;
		
		EndBiomes.MUTABLE.clear();
		LAND_BIOMES.clearMutables();
		VOID_BIOMES.clearMutables();
		
		for (EndBiome biome : EndBiomes.LAND_BIOMES.getBiomes())
			EndBiomes.MUTABLE.put(biomeRegistry.getOrThrow(EndBiomes.getBiomeKey(biome)), biome);
		for (EndBiome biome : EndBiomes.VOID_BIOMES.getBiomes())
			EndBiomes.MUTABLE.put(biomeRegistry.getOrThrow(EndBiomes.getBiomeKey(biome)), biome);
		
		Map<String, JsonObject> configs = Maps.newHashMap();
		biomeRegistry.forEach((biome) -> {
			if (biome.getCategory() == Category.THEEND) {
				Identifier id = biomeRegistry.getId(biome);
				if (!MUTABLE.containsKey(biome) && !ID_MAP.containsKey(id)) {
					JsonObject config = configs.get(id.getNamespace());
					if (config == null) {
						config = loadJsonConfig(id.getNamespace());
						configs.put(id.getNamespace(), config);
					}
					float fog = 1F;
					float chance = 1F;
					boolean isVoid = false;
					JsonElement element = config.get(id.getPath());
					if (element != null && element.isJsonObject()) {
						fog = JsonFactory.getFloat(element.getAsJsonObject(), "fogDensity", 1);
						chance = JsonFactory.getFloat(element.getAsJsonObject(), "genChance", 1);
						isVoid = JsonFactory.getString(element.getAsJsonObject(), "type", "land").equals("void");
					}
					EndBiome endBiome = new EndBiome(id, biome, fog, chance);
					if (isVoid) {
						VOID_BIOMES.addBiomeMutable(endBiome);
					}
					else {
						LAND_BIOMES.addBiomeMutable(endBiome);
					}
					KEYS.put(endBiome, biomeRegistry.getKey(biome).get());
					ID_MAP.put(id, endBiome);
				}
			}
		});
		
		CLIENT.clear();
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
	 * @param server
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
		EndBiome endBiome = new EndBiome(BuiltinRegistries.BIOME.getId(biome), biome, fogDensity, genChance);
		addToPicker(endBiome, type);
		makeLink(endBiome);
		return endBiome;
	}
	
	/**
	 * Registers new {@link EndBiome} from existed {@link Biome} and put as a sub-biome into selected parent.
	 * @param biome - {@link Biome} instance
	 * @param parent - {@link EndBiome} to be linked with
	 * @param genChance - generation chance [0.0F - Infinity]
	 * @return registered {@link EndBiome}
	 */
	public static EndBiome registerSubBiome(Biome biome, EndBiome parent, float genChance) {
		return registerSubBiome(biome, parent, 1, genChance);
	}
	
	/**
	 * Registers new {@link EndBiome} from existed {@link Biome} and put as a sub-biome into selected parent.
	 * @param biome - {@link Biome} instance
	 * @param parent - {@link EndBiome} to be linked with
	 * @param fogDensity - density of fog (def: 1F) [0.0F - Infinity]
	 * @param genChance - generation chance [0.0F - Infinity]
	 * @return registered {@link EndBiome}
	 */
	public static EndBiome registerSubBiome(Biome biome, EndBiome parent, float fogDensity, float genChance) {
		EndBiome endBiome = new EndBiome(BuiltinRegistries.BIOME.getId(biome), biome, fogDensity, genChance);
		parent.addSubBiome(endBiome);
		makeLink(endBiome);
		SUBBIOMES.add(endBiome);
		ID_MAP.put(endBiome.getID(), endBiome);
		return endBiome;
	}
	
	/**
	 * Put existing {@link EndBiome} as a sub-biome into selected parent.
	 * @param biome - {@link EndBiome} instance
	 * @param parent - {@link EndBiome} to be linked with
	 * @return registered {@link EndBiome}
	 */
	public static EndBiome registerSubBiome(EndBiome biome, EndBiome parent) {
		registerBiomeDirect(biome);
		parent.addSubBiome(biome);
		makeLink(biome);
		SUBBIOMES.add(biome);
		ID_MAP.put(biome.getID(), biome);
		return biome;
	}
	
	/**
	 * Registers {@link EndBiome} and adds it into worldgen.
	 * @param biome - {@link EndBiome} instance
	 * @param type - {@link BiomeType}
	 * @return registered {@link EndBiome}
	 */
	public static EndBiome registerBiome(EndBiome biome, BiomeType type) {
		registerBiomeDirect(biome);
		addToPicker(biome, type);
		ID_MAP.put(biome.getID(), biome);
		return biome;
	}
	
	private static EndBiome registerBiome(RegistryKey<Biome> key, BiomeType type, float genChance) {
		return registerBiome(BuiltinRegistries.BIOME.get(key), type, genChance);
	}
	
	private static EndBiome registerSubBiome(RegistryKey<Biome> key, EndBiome parent, float genChance) {
		return registerSubBiome(BuiltinRegistries.BIOME.get(key), parent, genChance);
	}
	
	private static void addToPicker(EndBiome biome, BiomeType type) {
		if (type == BiomeType.LAND)
			LAND_BIOMES.addBiome(biome);
		else
			VOID_BIOMES.addBiome(biome);
	}
	
	private static void fillSet() {
		if (occupiedIDs.isEmpty()) {
			BuiltinRegistries.BIOME.getEntries().forEach((entry) -> {
				int id = BuiltinRegistries.BIOME.getRawId(entry.getValue());
				occupiedIDs.add(id);
			});
		}
	}

	private static void registerBiomeDirect(EndBiome biome) {
		fillSet();
		int possibleID = incID++;
		if (occupiedIDs.contains(possibleID)) {
			String message = "ID for biome " + biome.getID() + " is already occupied, changing biome ID from " + possibleID + " to ";
			while (occupiedIDs.contains(possibleID)) {
				possibleID ++;
			}
			BetterEnd.LOGGER.info(message + possibleID);
		}
		Registry.register(BuiltinRegistries.BIOME, possibleID, biome.getID().toString(), biome.getBiome());
		makeLink(biome);
	}
	
	private static void makeLink(EndBiome biome) {
		Optional<RegistryKey<Biome>> optional = BuiltinRegistries.BIOME.getKey(biome.getBiome());
		RegistryKey<Biome> key = optional.isPresent() ? optional.get() : RegistryKey.of(Registry.BIOME_KEY, biome.getID());
		KEYS.put(biome, key);
	}

	public static RegistryKey<Biome> getBiomeKey(EndBiome biome) {
		return KEYS.get(biome);
	}
	
	public static EndBiome getFromBiome(Biome biome) {
		return ID_MAP.getOrDefault(biomeRegistry.getId(biome), END);
	}
	
	@Environment(EnvType.CLIENT)
	public static EndBiome getRenderBiome(Biome biome) {
		EndBiome endBiome = CLIENT.get(biome);
		if (endBiome == null) {
			Identifier id = MinecraftClient.getInstance().world.getRegistryManager().get(Registry.BIOME_KEY).getId(biome);
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
}
