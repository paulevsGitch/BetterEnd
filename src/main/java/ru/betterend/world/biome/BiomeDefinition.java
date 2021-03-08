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
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
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
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;
import net.minecraft.world.gen.GenerationStep.Carver;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
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
	private final List<SpawnEntry> spawns = Lists.newArrayList();

	private BiomeParticleConfig particleConfig;
	private BiomeAdditionsSound additions;
	private BiomeMoodSound mood;
	private SoundEvent music;
	private SoundEvent loop;

	private int waterFogColor = 329011;
	private int waterColor = 4159204;
	private int fogColor = 10518688;
	private int foliageColor = DEF_FOLIAGE;
	private int grassColor = DEF_FOLIAGE;
	private float fogDensity = 1F;

	private final Identifier id;
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
		setSurface(SurfaceBuilder.DEFAULT.withConfig(new TernarySurfaceConfig(
				block.getDefaultState(),
				Blocks.END_STONE.getDefaultState(),
				Blocks.END_STONE.getDefaultState()
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
	
	public BiomeDefinition setSurface(SurfaceBuilder<TernarySurfaceConfig> builder) {
		return setSurface(builder.withConfig(SurfaceBuilders.DEFAULT_END_CONFIG));
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
	
	public BiomeDefinition addMobSpawn(SpawnEntry entry) {
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

	public BiomeDefinition addFeature(Feature featureStep, ConfiguredFeature<?, ?> feature) {
		FeatureInfo info = new FeatureInfo();
		info.featureStep = featureStep;
		info.feature = feature;
		features.add(info);
		return this;
	}
	
	private int getColor(int r, int g, int b) {
		r = MathHelper.clamp(r, 0, 255);
		g = MathHelper.clamp(g, 0, 255);
		b = MathHelper.clamp(b, 0, 255);
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
	
	public BiomeDefinition setCaves(boolean hasCaves) {
		this.hasCaves = hasCaves;
		return this;
	}

	public Biome build() {
		SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
		GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
		Builder effects = new Builder();

		mobs.forEach((spawn) -> {
			spawnSettings.spawn(spawn.type.getSpawnGroup(), new SpawnSettings.SpawnEntry(spawn.type, spawn.weight, spawn.minGroupSize, spawn.maxGroupSize));
		});
		
		spawns.forEach((entry) -> {
			spawnSettings.spawn(entry.type.getSpawnGroup(), entry);
		});

		EndFeatures.addDefaultFeatures(this);
		generationSettings.surfaceBuilder(surface == null ? ConfiguredSurfaceBuilders.END : surface);
		structures.forEach((structure) -> generationSettings.structureFeature(structure));
		features.forEach((info) -> generationSettings.feature(info.featureStep, info.feature));
		carvers.forEach((info) -> generationSettings.carver(info.carverStep, info.carver));

		effects.skyColor(0).waterColor(waterColor).waterFogColor(waterFogColor).fogColor(fogColor).foliageColor(foliageColor).grassColor(grassColor);
		if (loop != null) effects.loopSound(loop);
		if (mood != null) effects.moodSound(mood);
		if (additions != null) effects.additionsSound(additions);
		if (particleConfig != null) effects.particleConfig(particleConfig);
		effects.music(music != null ? new MusicSound(music, 600, 2400, true) : MusicType.END);

		return new Biome.Builder()
				.precipitation(Precipitation.NONE)
				.category(isCaveBiome ? Category.NONE : Category.THEEND)
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
	
	private static final class CarverInfo {
		Carver carverStep;
		ConfiguredCarver<ProbabilityConfig> carver;
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

	public boolean hasCaves() {
		return hasCaves;
	}

	public BiomeDefinition addCarver(Carver carverStep, ConfiguredCarver<ProbabilityConfig> carver) {
		CarverInfo info = new CarverInfo();
		info.carverStep = carverStep;
		info.carver = carver;
		carvers.add(info);
		return this;
	}
}