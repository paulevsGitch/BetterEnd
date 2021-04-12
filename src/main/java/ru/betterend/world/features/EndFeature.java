package ru.betterend.world.features;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Features;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.ChanceDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.complex.StoneMaterial;
import ru.betterend.world.features.terrain.OreLayerFeature;

public class EndFeature {
	private Feature<?> feature;
	private ConfiguredFeature<?, ?> featureConfigured;
	private GenerationStep.Decoration featureStep;
	
	protected EndFeature() {}
	
	public EndFeature(Feature<?> feature, ConfiguredFeature<?, ?> configuredFeature, GenerationStep.Decoration featureStep) {
		this.featureStep = featureStep;
		this.feature = feature;
		this.featureConfigured = configuredFeature;
	}
	
	public EndFeature(String name, Feature<NoneFeatureConfiguration> feature, GenerationStep.Decoration featureStep, ConfiguredFeature<?, ?> configuredFeature) {
		ResourceLocation id = BetterEnd.makeID(name);
		this.featureStep = featureStep;
		this.feature = Registry.register(Registry.FEATURE, id, feature);
		this.featureConfigured = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, configuredFeature);
	}
	
	public EndFeature(String name, Feature<NoneFeatureConfiguration> feature) {
		ResourceLocation id = BetterEnd.makeID(name);
		this.featureStep = GenerationStep.Decoration.VEGETAL_DECORATION;
		this.feature = Registry.register(Registry.FEATURE, id, feature);
		this.featureConfigured = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, feature.configured(FeatureConfiguration.NONE).decorated(FeatureDecorator.CHANCE.configured(new ChanceDecoratorConfiguration(100))));
	}
	
	public EndFeature(String name, Feature<NoneFeatureConfiguration> feature, int density) {
		ResourceLocation id = BetterEnd.makeID(name);
		this.featureStep = GenerationStep.Decoration.VEGETAL_DECORATION;
		this.feature = Registry.register(Registry.FEATURE, id, feature);
		this.featureConfigured = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, feature.configured(FeatureConfiguration.NONE).decorated(Features.Decorators.HEIGHTMAP_SQUARE).countRandom(density));
	}
	
	public static EndFeature makeRawGenFeature(String name, Feature<NoneFeatureConfiguration> feature, int chance) {
		ConfiguredFeature<?, ?> configured = feature.configured(FeatureConfiguration.NONE).decorated(FeatureDecorator.CHANCE.configured(new ChanceDecoratorConfiguration(chance)));
		return new EndFeature(name, feature, GenerationStep.Decoration.RAW_GENERATION, configured);
	}
	
	public static EndFeature makeLakeFeature(String name, Feature<NoneFeatureConfiguration> feature, int chance) {
		ConfiguredFeature<?, ?> configured = feature.configured(FeatureConfiguration.NONE).decorated(FeatureDecorator.WATER_LAKE.configured(new ChanceDecoratorConfiguration(chance)));
		return new EndFeature(name, feature, GenerationStep.Decoration.LAKES, configured);
	}
	
	public static EndFeature makeOreFeature(String name, Block blockOre, int veins, int veinSize, int offset, int minY, int maxY) {
		EndFeature newFeature = new EndFeature();
		OreConfiguration featureConfig = new OreConfiguration(new BlockMatchTest(Blocks.END_STONE), blockOre.defaultBlockState(), veinSize);
		RangeDecoratorConfiguration rangeDecorator = new RangeDecoratorConfiguration(offset, minY, maxY);
		ConfiguredFeature<?, ?> oreFeature = Feature.ORE.configured(featureConfig)
				  .decorated(FeatureDecorator.RANGE.configured(rangeDecorator))
				  .squared()
				  .count(veins);
		newFeature.feature = Feature.ORE;
		newFeature.featureStep = GenerationStep.Decoration.UNDERGROUND_ORES;
		newFeature.featureConfigured = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, BetterEnd.makeID(name), oreFeature);
		
		return newFeature;
	}
	
	public static EndFeature makeLayerFeature(String name, BlockState state, float radius, int minY, int maxY, int count) {
		OreLayerFeature layer = new OreLayerFeature(state, radius, minY, maxY);
		ConfiguredFeature<?, ?> configured = layer.configured(FeatureConfiguration.NONE).decorated(FeatureDecorator.COUNT.configured(new CountConfiguration(count)));
		return new EndFeature(name, layer, GenerationStep.Decoration.UNDERGROUND_ORES, configured);
	}
	
	public static EndFeature makeLayerFeature(String name, Block block, float radius, int minY, int maxY, int count) {
		OreLayerFeature layer = new OreLayerFeature(block.defaultBlockState(), radius, minY, maxY);
		ConfiguredFeature<?, ?> configured = layer.configured(FeatureConfiguration.NONE).decorated(FeatureDecorator.COUNT.configured(new CountConfiguration(count)));
		return new EndFeature(name, layer, GenerationStep.Decoration.UNDERGROUND_ORES, configured);
	}
	
	public static EndFeature makeLayerFeature(String name, StoneMaterial material, float radius, int minY, int maxY, int count) {
		OreLayerFeature layer = new OreLayerFeature(material.stone.defaultBlockState(), radius, minY, maxY);
		ConfiguredFeature<?, ?> configured = layer.configured(FeatureConfiguration.NONE).decorated(FeatureDecorator.COUNT.configured(new CountConfiguration(count)));
		return new EndFeature(name, layer, GenerationStep.Decoration.UNDERGROUND_ORES, configured);
	}
	
	public static EndFeature makeChunkFeature(String name, Feature<NoneFeatureConfiguration> feature) {
		ConfiguredFeature<?, ?> configured = feature.configured(FeatureConfiguration.NONE).decorated(FeatureDecorator.COUNT.configured(new CountConfiguration(1)));
		return new EndFeature(name, feature, GenerationStep.Decoration.LOCAL_MODIFICATIONS, configured);
	}
	
	public static EndFeature makeChansedFeature(String name, Feature<NoneFeatureConfiguration> feature, int chance) {
		ConfiguredFeature<?, ?> configured = feature.configured(FeatureConfiguration.NONE).decorated(FeatureDecorator.CHANCE.configured(new ChanceDecoratorConfiguration(chance)));
		return new EndFeature(name, feature, GenerationStep.Decoration.SURFACE_STRUCTURES, configured);
	}
	
	public static EndFeature makeCountRawFeature(String name, Feature<NoneFeatureConfiguration> feature, int chance) {
		ConfiguredFeature<?, ?> configured = feature.configured(FeatureConfiguration.NONE).decorated(FeatureDecorator.COUNT.configured(new CountConfiguration(chance)));
		return new EndFeature(name, feature, GenerationStep.Decoration.RAW_GENERATION, configured);
	}
	
	public static EndFeature makeFeatureConfigured(String name, Feature<NoneFeatureConfiguration> feature) {
		ConfiguredFeature<?, ?> configured = feature.configured(FeatureConfiguration.NONE);
		return new EndFeature(name, feature, GenerationStep.Decoration.RAW_GENERATION, configured);
	}
	
	public Feature<?> getFeature() {
		return feature;
	}

	public ConfiguredFeature<?, ?> getFeatureConfigured() {
		return featureConfigured;
	}

	public GenerationStep.Decoration getFeatureStep() {
		return featureStep;
	}
}
