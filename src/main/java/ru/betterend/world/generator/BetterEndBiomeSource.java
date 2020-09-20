package ru.betterend.world.generator;

import java.util.Collections;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import ru.betterend.BetterEnd;
import ru.betterend.registry.BiomeRegistry;
import ru.betterend.world.biome.EndBiome;

public class BetterEndBiomeSource extends BiomeSource {
	public static final Codec<BetterEndBiomeSource> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter((theEndBiomeSource) -> {
			return theEndBiomeSource.biomeRegistry;
		}), Codec.LONG.fieldOf("seed").stable().forGetter((theEndBiomeSource) -> {
			return theEndBiomeSource.seed;
		})).apply(instance, instance.stable(BetterEndBiomeSource::new));
	});
	private BiomeMap map;
	private final long seed;
	private final Registry<Biome> biomeRegistry;

	public BetterEndBiomeSource(Registry<Biome> biomeRegistry, long seed) {
		super(Collections.emptyList());
		this.seed = seed;
		this.map = new BiomeMap(seed, 50);
		this.biomeRegistry = biomeRegistry;

		BiomeRegistry.MUTABLE.clear();
		for (EndBiome biome : BiomePicker.getBiomes())
			BiomeRegistry.MUTABLE.put(biomeRegistry.getOrThrow(BiomeRegistry.getBiomeKey(biome)), biome);
	}

	@Override
	public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
		EndBiome netherBiome = map.getBiome(biomeX << 2, biomeZ << 2);
		if (biomeX == 0 && biomeZ == 0) {
			map.clearCache();
		}
		return biomeRegistry.getOrThrow(BiomeRegistry.getBiomeKey(netherBiome));
	}

	@Override
	public BiomeSource withSeed(long seed) {
		return new BetterEndBiomeSource(biomeRegistry, seed);
	}

	@Override
	protected Codec<? extends BiomeSource> getCodec() {
		return CODEC;
	}

	public static void register() {
		Registry.register(Registry.BIOME_SOURCE, new Identifier(BetterEnd.MOD_ID, "better_end_biome_source"), CODEC);
	}
}
