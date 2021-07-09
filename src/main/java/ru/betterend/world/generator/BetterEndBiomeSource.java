package ru.betterend.world.generator;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.TheEndBiomeSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import ru.bclib.api.BiomeAPI;
import ru.bclib.world.biomes.BCLBiome;
import ru.bclib.world.generator.BiomeMap;
import ru.betterend.BetterEnd;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndTags;
import ru.betterend.util.FeaturesHelper;
import ru.betterend.world.biome.EndBiome;

import java.util.List;

public class BetterEndBiomeSource extends BiomeSource {
	public static final Codec<BetterEndBiomeSource> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter((theEndBiomeSource) -> {
			return theEndBiomeSource.biomeRegistry;
		}), Codec.LONG.fieldOf("seed").stable().forGetter((theEndBiomeSource) -> {
			return theEndBiomeSource.seed;
		})).apply(instance, instance.stable(BetterEndBiomeSource::new));
	});
	private static final OpenSimplexNoise SMALL_NOISE = new OpenSimplexNoise(8324);
	private final Registry<Biome> biomeRegistry;
	private final SimplexNoise noise;
	private final Biome centerBiome;
	private final Biome barrens;
	private BiomeMap mapLand;
	private BiomeMap mapVoid;
	private final long seed;

	public BetterEndBiomeSource(Registry<Biome> biomeRegistry, long seed) {
		super(getBiomes(biomeRegistry));

		this.mapLand = new BiomeMap(seed, GeneratorOptions.getBiomeSizeLand(), EndBiomes.LAND_BIOMES);
		this.mapVoid = new BiomeMap(seed, GeneratorOptions.getBiomeSizeVoid(), EndBiomes.VOID_BIOMES);
		this.centerBiome = biomeRegistry.getOrThrow(Biomes.THE_END);
		this.barrens = biomeRegistry.getOrThrow(Biomes.END_BARRENS);
		this.biomeRegistry = biomeRegistry;
		this.seed = seed;

		WorldgenRandom chunkRandom = new WorldgenRandom(seed);
		chunkRandom.consumeCount(17292);
		this.noise = new SimplexNoise(chunkRandom);

		EndBiomes.mutateRegistry(biomeRegistry);
		EndTags.addTerrainTags(biomeRegistry);
		FeaturesHelper.addFeatures(biomeRegistry);
	}

	private static List<Biome> getBiomes(Registry<Biome> biomeRegistry) {
		List<Biome> list = Lists.newArrayList();
		biomeRegistry.forEach((biome) -> {
			BCLBiome bclBiome = BiomeAPI.getBiome(biomeRegistry.getKey(biome));
			if (bclBiome instanceof EndBiome) {
				list.add(biome);
			}
		});
		return list;
	}

	@Override
	public Biome getNoiseBiome(int biomeX, int biomeY, int biomeZ) {
		boolean hasVoid = !GeneratorOptions.useNewGenerator() || !GeneratorOptions.noRingVoid();
		long i = (long) biomeX * (long) biomeX;
		long j = (long) biomeZ * (long) biomeZ;

		long dist = i + j;
		if (hasVoid) {
			if (dist <= 65536L) return this.centerBiome;
		}
		else if (dist <= 625L) {
			dist += noise.getValue(i * 0.2, j * 0.2) * 10;
			if (dist <= 625L) {
				return this.centerBiome;
			}
		}

		if (biomeX == 0 && biomeZ == 0) {
			mapLand.clearCache();
			mapVoid.clearCache();
		}

		BCLBiome endBiome = null;
		if (GeneratorOptions.useNewGenerator()) {
			if (TerrainGenerator.isLand(biomeX, biomeZ)) {
				endBiome = (EndBiome) mapLand.getBiome(biomeX << 2, biomeZ << 2);
			}
			else {
				if (!GeneratorOptions.noRingVoid() && dist <= 65536L) {
					return barrens;
				}
				endBiome = mapVoid.getBiome(biomeX << 2, biomeZ << 2);
			}
		}
		else {
			float height = TheEndBiomeSource.getHeightValue(noise, (biomeX >> 1) + 1, (biomeZ >> 1) + 1) + (float) SMALL_NOISE.eval(biomeX, biomeZ) * 5;

			if (height > -20F && height < -5F) {
				return barrens;
			}

			endBiome = height < -10F ? mapVoid.getBiome(biomeX << 2, biomeZ << 2) : mapLand.getBiome(biomeX << 2, biomeZ << 2);
		}

		return BiomeAPI.getActualBiome(endBiome);
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
			dist += noise.getValue(i * 0.2, j * 0.2) * 10;
			if (dist <= 625L) {
				return this.centerBiome;
			}
		}
		return BiomeAPI.getActualBiome(mapLand.getBiome(biomeX << 2, biomeZ << 2));
	}

	@Override
	public BiomeSource withSeed(long seed) {
		return new BetterEndBiomeSource(biomeRegistry, seed);
	}

	@Override
	protected Codec<? extends BiomeSource> codec() {
		return CODEC;
	}

	public static void register() {
		Registry.register(Registry.BIOME_SOURCE, BetterEnd.makeID("better_end_biome_source"), CODEC);
	}
}
