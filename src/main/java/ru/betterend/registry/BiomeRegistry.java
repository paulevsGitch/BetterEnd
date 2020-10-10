package ru.betterend.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.BiomeKeys;
import ru.betterend.world.biome.BiomeChorusForest;
import ru.betterend.world.biome.BiomeFoggyMushroomland;
import ru.betterend.world.biome.EndBiome;
import ru.betterend.world.generator.BiomePicker;
import ru.betterend.world.generator.BiomeType;

public class BiomeRegistry {
	private static final Map<EndBiome, RegistryKey<Biome>> KEYS = Maps.newHashMap();
	private static final HashMap<Biome, EndBiome> MUTABLE = Maps.newHashMap();
	private static final HashMap<Biome, EndBiome> CLIENT = Maps.newHashMap();
	public static final BiomePicker LAND_BIOMES = new BiomePicker();
	public static final BiomePicker VOID_BIOMES = new BiomePicker();
	
	public static final EndBiome END = registerBiome(BiomeKeys.THE_END, BiomeType.LAND, true);
	public static final EndBiome END_BARRENS = registerBiome(BiomeKeys.END_BARRENS, BiomeType.VOID, true);
	public static final EndBiome END_HIGHLANDS = registerBiome(BiomeKeys.END_HIGHLANDS, BiomeType.LAND, true);
	public static final EndBiome END_MIDLANDS = registerBiome(BiomeKeys.END_MIDLANDS, BiomeType.LAND, false);
	public static final EndBiome SMALL_END_ISLANDS = registerBiome(BiomeKeys.SMALL_END_ISLANDS, BiomeType.VOID, true);
	public static final EndBiome FOGGY_MUSHROOMLAND = registerBiome(new BiomeFoggyMushroomland(), BiomeType.LAND);
	public static final EndBiome CHORUS_FOREST = registerBiome(new BiomeChorusForest(), BiomeType.LAND);
	
	public static void register() {}
	
	public static void mutateRegistry(Registry<Biome> biomeRegistry) {
		BiomeRegistry.MUTABLE.clear();
		LAND_BIOMES.clearMutables();
		
		for (EndBiome biome : BiomeRegistry.LAND_BIOMES.getBiomes())
			BiomeRegistry.MUTABLE.put(biomeRegistry.getOrThrow(BiomeRegistry.getBiomeKey(biome)), biome);
		for (EndBiome biome : BiomeRegistry.VOID_BIOMES.getBiomes())
			BiomeRegistry.MUTABLE.put(biomeRegistry.getOrThrow(BiomeRegistry.getBiomeKey(biome)), biome);
		
		biomeRegistry.forEach((biome) -> {
			if (biome.getCategory() == Category.THEEND) {
				if (!MUTABLE.containsKey(biome) && !biomeRegistry.getId(biome).getNamespace().equals("minecraft")) {
					EndBiome endBiome = new EndBiome(biome);
					LAND_BIOMES.addBiomeMutable(endBiome);
					KEYS.put(endBiome, biomeRegistry.getKey(biome).get());
				}
			}
		});
	}
	
	public static EndBiome registerBiome(RegistryKey<Biome> key, BiomeType type, boolean addToGen) {
		EndBiome endBiome = new EndBiome(BuiltinRegistries.BIOME.get(key));
		if (addToGen) addToPicker(endBiome, type);
		makeLink(endBiome);
		return endBiome;
	}
	
	public static EndBiome registerBiome(Biome biome, BiomeType type) {
		EndBiome endBiome = new EndBiome(biome);
		addToPicker(endBiome, type);
		makeLink(endBiome);
		return endBiome;
	}
	
	public static EndBiome registerBiome(EndBiome biome, BiomeType type) {
		registerBiomeDirect(biome);
		addToPicker(biome, type);
		CLIENT.put(biome.getBiome(), biome);
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
	
	private static boolean equals(Biome biome1, Biome biome2) {
		return (biome1.getDepth() - biome2.getDepth() == 0) &&
		(biome1.getDownfall() - biome2.getDownfall() == 0) &&
		(biome1.getScale() - biome2.getScale() == 0) &&
		(biome1.getCategory() == biome2.getCategory()) &&
		(biome1.getFogColor() == biome2.getFogColor()) &&
		(biome1.getSkyColor() == biome2.getSkyColor()) &&
		(biome1.getWaterColor() == biome2.getWaterColor()) &&
		(biome1.getWaterFogColor() == biome2.getWaterFogColor()) &&
		(biome1.getPrecipitation().equals(biome2.getPrecipitation()));
	}
	
	public static EndBiome getFromBiome(Biome biome) {
		EndBiome endBiome = MUTABLE.get(biome);
		if (endBiome == null) {
			for (Biome key: CLIENT.keySet()) {
				if (equals(key, biome)) {
					endBiome = CLIENT.get(key);
					MUTABLE.put(biome, endBiome);
					return endBiome;
				}
			}
			MUTABLE.put(biome, END);
			return END;
		}
		return endBiome;
	}
}
