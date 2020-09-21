package ru.betterend.world.generator;

import java.util.Collections;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.gen.ChunkRandom;
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
	private final Registry<Biome> biomeRegistry;
	private final SimplexNoiseSampler noise;
	private final Biome centerBiome;
	private BiomeMap mapLand;
	private BiomeMap mapVoid;
	private final long seed;

	public BetterEndBiomeSource(Registry<Biome> biomeRegistry, long seed) {
		super(Collections.emptyList());
		
		this.mapLand = new BiomeMap(seed, 50, BiomeRegistry.LAND_BIOMES);
		this.mapVoid = new BiomeMap(seed, 50, BiomeRegistry.VOID_BIOMES);
		this.centerBiome = biomeRegistry.getOrThrow(BiomeKeys.THE_END);
		this.biomeRegistry = biomeRegistry;
		this.seed = seed;
		
		ChunkRandom chunkRandom = new ChunkRandom(seed);
		chunkRandom.consume(17292);
		this.noise = new SimplexNoiseSampler(chunkRandom);

		BiomeRegistry.MUTABLE.clear();
		for (EndBiome biome : BiomeRegistry.LAND_BIOMES.getBiomes())
			BiomeRegistry.MUTABLE.put(biomeRegistry.getOrThrow(BiomeRegistry.getBiomeKey(biome)), biome);
		for (EndBiome biome : BiomeRegistry.VOID_BIOMES.getBiomes())
			BiomeRegistry.MUTABLE.put(biomeRegistry.getOrThrow(BiomeRegistry.getBiomeKey(biome)), biome);
	}

	@Override
	public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
		long i = biomeX >> 2;
		long j = biomeZ >> 2;
		if (i * i + j * j <= 4096L) return this.centerBiome;
		
		float height = TheEndBiomeSource.getNoiseAt(noise, (int) i * 2 + 1, (int) j * 2 + 1);
	         
		EndBiome netherBiome = height < 20.0F ? mapVoid.getBiome(biomeX << 2, biomeZ << 2) : mapLand.getBiome(biomeX << 2, biomeZ << 2);
		if (biomeX == 0 && biomeZ == 0) {
			mapLand.clearCache();
			mapVoid.clearCache();
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
