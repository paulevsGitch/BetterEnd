package ru.betterend.world.generator;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import ru.betterend.BetterEnd;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndTags;
import ru.betterend.util.FeaturesHelper;
import ru.betterend.world.biome.EndBiome;

public class BetterEndBiomeSource extends BiomeSource {
	public static final Codec<BetterEndBiomeSource> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter((theEndBiomeSource) -> {
			return theEndBiomeSource.biomeRegistry;
		}), Codec.LONG.fieldOf("seed").stable().forGetter((theEndBiomeSource) -> {
			return theEndBiomeSource.seed;
		})).apply(instance, instance.stable(BetterEndBiomeSource::new));
	});
	private static final OpenSimplexNoise SMALL_NOISE = new OpenSimplexNoise(8324);
	private final Registry<Biome> biomeRegistry;
	private final SimplexNoiseSampler noise;
	private final Biome centerBiome;
	private final Biome barrens;
	private BiomeMap mapLand;
	private BiomeMap mapVoid;
	private final long seed;

	public BetterEndBiomeSource(Registry<Biome> biomeRegistry, long seed) {
		super(getBiomes(biomeRegistry));
		
		this.mapLand = new BiomeMap(seed, GeneratorOptions.getBiomeSizeLand(), EndBiomes.LAND_BIOMES);
		this.mapVoid = new BiomeMap(seed, GeneratorOptions.getBiomeSizeVoid(), EndBiomes.VOID_BIOMES);
		this.centerBiome = biomeRegistry.getOrThrow(BiomeKeys.THE_END);
		this.barrens = biomeRegistry.getOrThrow(BiomeKeys.END_BARRENS);
		this.biomeRegistry = biomeRegistry;
		this.seed = seed;
		
		ChunkRandom chunkRandom = new ChunkRandom(seed);
		chunkRandom.consume(17292);
		this.noise = new SimplexNoiseSampler(chunkRandom);

		EndBiomes.mutateRegistry(biomeRegistry);
		EndTags.addTerrainTags(biomeRegistry);
		FeaturesHelper.addFeatures(biomeRegistry);
	}
	
	private static List<Biome> getBiomes(Registry<Biome> biomeRegistry) {
		List<Biome> list = Lists.newArrayList();
		biomeRegistry.forEach((biome) -> {
			if (EndBiomes.hasBiome(biomeRegistry.getId(biome))) {
				list.add(biome);
			}
		});
		return list;
	}

	@Override
	public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
		boolean hasVoid = !GeneratorOptions.useNewGenerator() || !GeneratorOptions.noRingVoid();
		long i = (long) biomeX * (long) biomeX;
		long j = (long) biomeZ * (long) biomeZ;
		
		long dist = i + j;
		if (hasVoid) {
			if (dist <= 65536L) return this.centerBiome;
		}
		else if (dist <= 625L) {
			dist += noise.sample(i * 0.2, j * 0.2) * 10;
			if (dist <= 625L) {
				return this.centerBiome;
			}
		}
		
		if (biomeX == 0 && biomeZ == 0) {
			mapLand.clearCache();
			mapVoid.clearCache();
		}
		
		if (GeneratorOptions.useNewGenerator()) {
			if (TerrainGenerator.isLand(biomeX, biomeZ)) {
				return mapLand.getBiome(biomeX << 2, biomeZ << 2).getActualBiome();
			}
			else {
				return mapVoid.getBiome(biomeX << 2, biomeZ << 2).getActualBiome();
			}
		}
		else {
			float height = TheEndBiomeSource.getNoiseAt(noise, (biomeX >> 1) + 1, (biomeZ >> 1) + 1) + (float) SMALL_NOISE.eval(biomeX, biomeZ) * 5;
	
			if (height > -20F && height < -5F) {
				return barrens;
			}
			
			EndBiome endBiome = height < -10F ? mapVoid.getBiome(biomeX << 2, biomeZ << 2) : mapLand.getBiome(biomeX << 2, biomeZ << 2);
			return endBiome.getActualBiome();
		}
	}
	
	public Biome getLandBiome(int biomeX, int biomeY, int biomeZ) {
		boolean hasVoid = !GeneratorOptions.useNewGenerator() || !GeneratorOptions.noRingVoid();
		long i = (long) biomeX * (long) biomeX;
		long j = (long) biomeZ * (long) biomeZ;
		
		long dist = i + j;
		if (hasVoid) {
			if (dist <= 65536L) return this.centerBiome;
		}
		else if (dist <= 625L) {
			dist += noise.sample(i * 0.2, j * 0.2) * 10;
			if (dist <= 625L) {
				return this.centerBiome;
			}
		}
		return mapLand.getBiome(biomeX << 2, biomeZ << 2).getActualBiome();
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
		Registry.register(Registry.BIOME_SOURCE, BetterEnd.makeID("better_end_biome_source"), CODEC);
	}
}
