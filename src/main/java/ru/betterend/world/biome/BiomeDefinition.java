package ru.betterend.world.biome;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.sound.MusicType;
import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.BiomeAdditionsSound;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.Biome.Precipitation;
import net.minecraft.world.biome.BiomeEffects.Builder;
import net.minecraft.world.biome.BiomeParticleConfig;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import ru.betterend.BetterEnd;
import ru.betterend.util.MHelper;
import ru.betterend.world.features.EndFeature;
import ru.betterend.world.surface.DoubleBlockSurfaceBuilder;

public class BiomeDefinition {
	private final List<ConfiguredStructureFeature<?, ?>> structures = Lists.newArrayList();
	private final List<FeatureInfo> features = Lists.newArrayList();
	private final List<SpawnInfo> mobs = Lists.newArrayList();

	private BiomeParticleConfig particleConfig;
	private BiomeAdditionsSound additions;
	private BiomeMoodSound mood;
	private SoundEvent music;
	private SoundEvent loop;

	private int waterFogColor = 329011;
	private int waterColor = 4159204;
	private int fogColor = 10518688;
	private float fogDensity = 1F;

	private final Identifier id;
	private float genChance = 1F;
	
	private ConfiguredSurfaceBuilder<?> surface;

	public BiomeDefinition(String name) {
		this.id = new Identifier(BetterEnd.MOD_ID, name);
	}

	public BiomeDefinition setSurface(Block surfaceBlock) {
		this.surface = SurfaceBuilder.DEFAULT.method_30478(new TernarySurfaceConfig(
				surfaceBlock.getDefaultState(),
				Blocks.END_STONE.getDefaultState(),
				Blocks.END_STONE.getDefaultState()
		));
		return this;
	}
	
	public BiomeDefinition setSurface(Block surfaceBlock1, Block surfaceBlock2) {
		this.surface = DoubleBlockSurfaceBuilder.INSTANCE.setConfigUpper(new TernarySurfaceConfig(
				surfaceBlock1.getDefaultState(),
				Blocks.END_STONE.getDefaultState(),
				Blocks.END_STONE.getDefaultState()
		)).setConfigLower(new TernarySurfaceConfig(
				surfaceBlock2.getDefaultState(),
				Blocks.END_STONE.getDefaultState(),
				Blocks.END_STONE.getDefaultState()
		)).method_30478(new TernarySurfaceConfig(surfaceBlock1.getDefaultState(),
				Blocks.END_STONE.getDefaultState(),
				Blocks.END_STONE.getDefaultState()));
		return this;
	}

	public BiomeDefinition setParticles(ParticleEffect particle, float probability) {
		this.particleConfig = new BiomeParticleConfig(particle, probability);
		return this;
	}
	
	public BiomeDefinition setGenChance(float genChance) {
		this.genChance = genChance;
		return this;
	}

	public BiomeDefinition addMobSpawn(EntityType<?> type, int weight, int minGroupSize, int maxGroupSize) {
		Identifier eID = Registry.ENTITY_TYPE.getId(type);
		if (eID != Registry.ENTITY_TYPE.getDefaultId()) {
			SpawnInfo info = new SpawnInfo();
			info.type = type;
			info.weight = weight;
			info.minGroupSize = minGroupSize;
			info.maxGroupSize = maxGroupSize;
			mobs.add(info);
		}
		return this;
	}

	public BiomeDefinition addStructureFeature(ConfiguredStructureFeature<?, ?> feature) {
		structures.add(feature);
		return this;
	}
	
	public BiomeDefinition addFeature(EndFeature feature) {
		FeatureInfo info = new FeatureInfo();
		info.featureStep = feature.getFeatureStep();
		info.feature = feature.getFeatureConfigured();
		features.add(info);
		return this;
	}

	public BiomeDefinition addFeature(Feature featureStep, ConfiguredFeature<?, ?> feature) {
		FeatureInfo info = new FeatureInfo();
		info.featureStep = featureStep;
		info.feature = feature;
		features.add(info);
		return this;
	}

	public BiomeDefinition setFogColor(int r, int g, int b) {
		r = MathHelper.clamp(r, 0, 255);
		g = MathHelper.clamp(g, 0, 255);
		b = MathHelper.clamp(b, 0, 255);
		this.fogColor = MHelper.color(r, g, b);
		return this;
	}

	public BiomeDefinition setFogDensity(float density) {
		this.fogDensity = density;
		return this;
	}

	public BiomeDefinition setWaterColor(int r, int g, int b) {
		r = MathHelper.clamp(r, 0, 255);
		g = MathHelper.clamp(g, 0, 255);
		b = MathHelper.clamp(b, 0, 255);
		this.waterColor = MHelper.color(r, g, b);
		return this;
	}

	public BiomeDefinition setWaterFogColor(int r, int g, int b) {
		r = MathHelper.clamp(r, 0, 255);
		g = MathHelper.clamp(g, 0, 255);
		b = MathHelper.clamp(b, 0, 255);
		this.waterFogColor = MHelper.color(r, g, b);
		return this;
	}

	public BiomeDefinition setLoop(SoundEvent loop) {
		this.loop = loop;
		return this;
	}

	public BiomeDefinition setMood(SoundEvent mood) {
		this.mood = new BiomeMoodSound(mood, 6000, 8, 2.0D);
		return this;
	}

	public BiomeDefinition setAdditions(SoundEvent additions) {
		this.additions = new BiomeAdditionsSound(additions, 0.0111);
		return this;
	}

	public BiomeDefinition setMusic(SoundEvent music) {
		this.music = music;
		return this;
	}

	public Biome build() {
		SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
		GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
		Builder effects = new Builder();

		mobs.forEach((spawn) -> {
			spawnSettings.spawn(spawn.type.getSpawnGroup(),
					new SpawnSettings.SpawnEntry(spawn.type, spawn.weight, spawn.minGroupSize, spawn.maxGroupSize));
		});

		generationSettings.surfaceBuilder(surface == null ? ConfiguredSurfaceBuilders.END : surface);
		structures.forEach((structure) -> generationSettings.structureFeature(structure));
		features.forEach((info) -> generationSettings.feature(info.featureStep, info.feature));

		effects.skyColor(0).waterColor(waterColor).waterFogColor(waterFogColor).fogColor(fogColor);
		if (loop != null) effects.loopSound(loop);
		if (mood != null) effects.moodSound(mood);
		if (additions != null) effects.additionsSound(additions);
		if (particleConfig != null) effects.particleConfig(particleConfig);
		effects.music(MusicType.createIngameMusic(music != null ? music : SoundEvents.MUSIC_END));

		return new Biome.Builder()
				.precipitation(Precipitation.NONE)
				.category(Category.THEEND)
				.depth(0.1F)
				.scale(0.2F)
				.temperature(2.0F)
				.downfall(0.0F)
				.effects(effects.build())
				.spawnSettings(spawnSettings.build())
				.generationSettings(generationSettings.build())
				.build();
	}

	private static final class SpawnInfo {
		EntityType<?> type;
		int weight;
		int minGroupSize;
		int maxGroupSize;
	}

	private static final class FeatureInfo {
		Feature featureStep;
		ConfiguredFeature<?, ?> feature;
	}

	public Identifier getID() {
		return id;
	}
	
	public float getFodDensity() {
		return fogDensity;
	}

	public float getGenChance() {
		return genChance;
	}
}