package ru.betterend.world.biome;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects.Builder;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep.Carving;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndFeatures;
import ru.betterend.util.MHelper;
import ru.betterend.world.features.EndFeature;
import ru.betterend.world.structures.EndStructureFeature;
import ru.betterend.world.surface.DoubleBlockSurfaceBuilder;
import ru.betterend.world.surface.SurfaceBuilders;

public class BiomeDefinition {
	private static final int DEF_FOLIAGE = MHelper.color(197, 210, 112);
	
	private final List<ConfiguredStructureFeature<?, ?>> structures = Lists.newArrayList();
	private final List<FeatureInfo> features = Lists.newArrayList();
	private final List<CarverInfo> carvers = Lists.newArrayList();
	private final List<SpawnInfo> mobs = Lists.newArrayList();
	private final List<SpawnerData> spawns = Lists.newArrayList();

	private AmbientParticleSettings particleConfig;
	private AmbientAdditionsSettings additions;
	private AmbientMoodSettings mood;
	private SoundEvent music;
	private SoundEvent loop;

	private int waterFogColor = 329011;
	private int waterColor = 4159204;
	private int fogColor = 10518688;
	private int foliageColor = DEF_FOLIAGE;
	private int grassColor = DEF_FOLIAGE;
	private float fogDensity = 1F;
	private float depth = 0.1F;

	private final ResourceLocation id;
	private float genChance = 1F;
	private boolean hasCaves = true;
	private boolean isCaveBiome = false;
	
	private ConfiguredSurfaceBuilder<?> surface;

	public BiomeDefinition(String name) {
		this.id = BetterEnd.makeID(name);
	}
	
	public BiomeDefinition setCaveBiome() {
		isCaveBiome = true;
		return this;
	}
	
	public BiomeDefinition setSurface(Block block) {
		setSurface(SurfaceBuilder.DEFAULT.configured(new SurfaceBuilderBaseConfiguration(
				block.defaultBlockState(),
				Blocks.END_STONE.defaultBlockState(),
				Blocks.END_STONE.defaultBlockState()
		)));
		return this;
	}
	
	public BiomeDefinition setSurface(Block block1, Block block2) {
		setSurface(DoubleBlockSurfaceBuilder.register("be_" + id.getPath() + "_surface").setBlock1(block1).setBlock2(block2).configured());
		return this;
	}
	
	public BiomeDefinition setSurface(ConfiguredSurfaceBuilder<?> builder) {
		this.surface = builder;
		return this;
	}
	
	public BiomeDefinition setSurface(SurfaceBuilder<SurfaceBuilderBaseConfiguration> builder) {
		return setSurface(builder.configured(SurfaceBuilders.DEFAULT_END_CONFIG));
	}

	public BiomeDefinition setParticles(ParticleOptions particle, float probability) {
		this.particleConfig = new AmbientParticleSettings(particle, probability);
		return this;
	}
	
	public BiomeDefinition setGenChance(float genChance) {
		this.genChance = genChance;
		return this;
	}
	
	public BiomeDefinition setDepth(float depth) {
		this.depth = depth;
		return this;
	}

	public BiomeDefinition addMobSpawn(EntityType<?> type, int weight, int minGroupSize, int maxGroupSize) {
		ResourceLocation eID = Registry.ENTITY_TYPE.getKey(type);
		if (eID != Registry.ENTITY_TYPE.getDefaultKey()) {
			SpawnInfo info = new SpawnInfo();
			info.type = type;
			info.weight = weight;
			info.minGroupSize = minGroupSize;
			info.maxGroupSize = maxGroupSize;
			mobs.add(info);
		}
		return this;
	}
	
	public BiomeDefinition addMobSpawn(SpawnerData entry) {
		spawns.add(entry);
		return this;
	}

	public BiomeDefinition addStructureFeature(ConfiguredStructureFeature<?, ?> feature) {
		structures.add(feature);
		return this;
	}
	
	public BiomeDefinition addStructureFeature(EndStructureFeature feature) {
		structures.add(feature.getFeatureConfigured());
		return this;
	}
	
	public BiomeDefinition addFeature(EndFeature feature) {
		FeatureInfo info = new FeatureInfo();
		info.featureStep = feature.getFeatureStep();
		info.feature = feature.getFeatureConfigured();
		features.add(info);
		return this;
	}

	public BiomeDefinition addFeature(Decoration featureStep, ConfiguredFeature<?, ?> feature) {
		FeatureInfo info = new FeatureInfo();
		info.featureStep = featureStep;
		info.feature = feature;
		features.add(info);
		return this;
	}
	
	private int getColor(int r, int g, int b) {
		r = Mth.clamp(r, 0, 255);
		g = Mth.clamp(g, 0, 255);
		b = Mth.clamp(b, 0, 255);
		return MHelper.color(r, g, b);
	}

	public BiomeDefinition setFogColor(int r, int g, int b) {
		this.fogColor = getColor(r, g, b);
		return this;
	}

	public BiomeDefinition setFogDensity(float density) {
		this.fogDensity = density;
		return this;
	}

	public BiomeDefinition setWaterColor(int r, int g, int b) {
		this.waterColor = getColor(r, g, b);
		return this;
	}

	public BiomeDefinition setWaterFogColor(int r, int g, int b) {
		this.waterFogColor = getColor(r, g, b);
		return this;
	}
	
	public BiomeDefinition setWaterAndFogColor(int r, int g, int b) {
		return setWaterColor(r, g, b).setWaterFogColor(r, g, b);
	}
	
	public BiomeDefinition setFoliageColor(int r, int g, int b) {
		this.foliageColor = getColor(r, g, b);
		return this;
	}
	
	public BiomeDefinition setGrassColor(int r, int g, int b) {
		this.grassColor = getColor(r, g, b);
		return this;
	}
	
	public BiomeDefinition setPlantsColor(int r, int g, int b) {
		return this.setFoliageColor(r, g, b).setGrassColor(r, g, b);
	}

	public BiomeDefinition setLoop(SoundEvent loop) {
		this.loop = loop;
		return this;
	}

	public BiomeDefinition setMood(SoundEvent mood) {
		this.mood = new AmbientMoodSettings(mood, 6000, 8, 2.0D);
		return this;
	}

	public BiomeDefinition setAdditions(SoundEvent additions) {
		this.additions = new AmbientAdditionsSettings(additions, 0.0111);
		return this;
	}

	public BiomeDefinition setMusic(SoundEvent music) {
		this.music = music;
		return this;
	}
	
	public BiomeDefinition setCaves(boolean hasCaves) {
		this.hasCaves = hasCaves;
		return this;
	}

	public Biome build() {
		MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
		BiomeGenerationSettings.Builder generationSettings = new BiomeGenerationSettings.Builder();
		Builder effects = new Builder();

		mobs.forEach((spawn) -> {
			spawnSettings.addSpawn(spawn.type.getCategory(), new MobSpawnSettings.SpawnerData(spawn.type, spawn.weight, spawn.minGroupSize, spawn.maxGroupSize));
		});
		
		spawns.forEach((entry) -> {
			spawnSettings.addSpawn(entry.type.getCategory(), entry);
		});

		EndFeatures.addDefaultFeatures(this);
		generationSettings.surfaceBuilder(surface == null ? net.minecraft.data.worldgen.SurfaceBuilders.END : surface);
		structures.forEach((structure) -> generationSettings.addStructureStart(structure));
		features.forEach((info) -> generationSettings.addFeature(info.featureStep, info.feature));
		carvers.forEach((info) -> generationSettings.addCarver(info.carverStep, info.carver));

		effects.skyColor(0).waterColor(waterColor).waterFogColor(waterFogColor).fogColor(fogColor).foliageColorOverride(foliageColor).grassColorOverride(grassColor);
		if (loop != null) effects.ambientLoopSound(loop);
		if (mood != null) effects.ambientMoodSound(mood);
		if (additions != null) effects.ambientAdditionsSound(additions);
		if (particleConfig != null) effects.ambientParticle(particleConfig);
		effects.backgroundMusic(music != null ? new Music(music, 600, 2400, true) : Musics.END);

		return new Biome.BiomeBuilder()
				.precipitation(Precipitation.NONE)
				.biomeCategory(isCaveBiome ? BiomeCategory.NONE : BiomeCategory.THEEND)
				.depth(depth)
				.scale(0.2F)
				.temperature(2.0F)
				.downfall(0.0F)
				.specialEffects(effects.build())
				.mobSpawnSettings(spawnSettings.build())
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
		Decoration featureStep;
		ConfiguredFeature<?, ?> feature;
	}
	
	private static final class CarverInfo {
		Carving carverStep;
		ConfiguredWorldCarver<ProbabilityFeatureConfiguration> carver;
	}

	public ResourceLocation getID() {
		return id;
	}
	
	public float getFodDensity() {
		return fogDensity;
	}

	public float getGenChance() {
		return genChance;
	}

	public boolean hasCaves() {
		return hasCaves;
	}

	public BiomeDefinition addCarver(Carving carverStep, ConfiguredWorldCarver<ProbabilityFeatureConfiguration> carver) {
		CarverInfo info = new CarverInfo();
		info.carverStep = carverStep;
		info.carver = carver;
		carvers.add(info);
		return this;
	}
}