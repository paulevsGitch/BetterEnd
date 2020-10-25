package ru.betterend.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.BiomeKeys;
import ru.betterend.world.biome.BiomeChorusForest;
import ru.betterend.world.biome.BiomeCrystalMountains;
import ru.betterend.world.biome.BiomeDustWastelands;
import ru.betterend.world.biome.BiomeFoggyMushroomland;
import ru.betterend.world.biome.BiomeMegalake;
import ru.betterend.world.biome.EndBiome;
import ru.betterend.world.generator.BiomePicker;
import ru.betterend.world.generator.BiomeType;

public class BiomeRegistry {
	private static final Map<EndBiome, RegistryKey<Biome>> KEYS = Maps.newHashMap();
	private static final HashMap<Identifier, EndBiome> ID_MAP = Maps.newHashMap();
	private static final HashMap<Biome, EndBiome> MUTABLE = Maps.newHashMap();
	private static final HashMap<Biome, EndBiome> CLIENT = Maps.newHashMap();
	
	public static final BiomePicker LAND_BIOMES = new BiomePicker();
	public static final BiomePicker VOID_BIOMES = new BiomePicker();
	
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
	public static final EndBiome CRYSTAL_MOUNTAINS = registerBiome(new BiomeCrystalMountains(), BiomeType.LAND);
	
	public static void register() {}
	
	public static void mutateRegistry(Registry<Biome> biomeRegistry) {
		BiomeRegistry.biomeRegistry = biomeRegistry;
		
		BiomeRegistry.MUTABLE.clear();
		LAND_BIOMES.clearMutables();
		
		for (EndBiome biome : BiomeRegistry.LAND_BIOMES.getBiomes())
			BiomeRegistry.MUTABLE.put(biomeRegistry.getOrThrow(BiomeRegistry.getBiomeKey(biome)), biome);
		for (EndBiome biome : BiomeRegistry.VOID_BIOMES.getBiomes())
			BiomeRegistry.MUTABLE.put(biomeRegistry.getOrThrow(BiomeRegistry.getBiomeKey(biome)), biome);
		
		biomeRegistry.forEach((biome) -> {
			if (biome.getCategory() == Category.THEEND) {
				if (!MUTABLE.containsKey(biome) && !biomeRegistry.getId(biome).getNamespace().equals("minecraft")) {
					EndBiome endBiome = new EndBiome(biome, 1);
					LAND_BIOMES.addBiomeMutable(endBiome);
					KEYS.put(endBiome, biomeRegistry.getKey(biome).get());
				}
			}
		});
		
		CLIENT.clear();
	}
	
	public static EndBiome registerBiome(RegistryKey<Biome> key, BiomeType type, float genChance) {
		return registerBiome(BuiltinRegistries.BIOME.get(key), type, genChance);
	}
	
	public static EndBiome registerBiome(Biome biome, BiomeType type, float genChance) {
		EndBiome endBiome = new EndBiome(biome, genChance);
		addToPicker(endBiome, type);
		makeLink(endBiome);
		return endBiome;
	}
	
	public static EndBiome registerSubBiome(RegistryKey<Biome> key, EndBiome parent, float genChance) {
		return registerSubBiome(BuiltinRegistries.BIOME.get(key), parent, genChance);
	}
	
	public static EndBiome registerSubBiome(Biome biome, EndBiome parent, float genChance) {
		EndBiome endBiome = new EndBiome(biome, genChance);
		parent.addSubBiome(endBiome);
		makeLink(endBiome);
		return endBiome;
	}
	
	public static EndBiome registerSubBiome(EndBiome biome, EndBiome parent, float genChance) {
		parent.addSubBiome(biome);
		makeLink(biome);
		return biome;
	}
	
	public static EndBiome registerBiome(EndBiome biome, BiomeType type) {
		registerBiomeDirect(biome);
		addToPicker(biome, type);
		ID_MAP.put(biome.getID(), biome);
		return biome;
	}
	
	private static void addToPicker(EndBiome biome, BiomeType type) {
		if (type == BiomeType.LAND)
			LAND_BIOMES.addBiome(biome);
		else
			VOID_BIOMES.addBiome(biome);
	}

	private static void registerBiomeDirect(EndBiome biome) {
		Registry.register(BuiltinRegistries.BIOME, biome.getID(), biome.getBiome());
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
}
