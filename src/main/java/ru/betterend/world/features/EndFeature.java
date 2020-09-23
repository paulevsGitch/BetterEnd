package ru.betterend.world.features;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.ConfiguredDecorator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import ru.betterend.BetterEnd;

public class EndFeature {
	private final Feature<DefaultFeatureConfig> feature;
	private final ConfiguredFeature<?, ?> featureConfigured;
	private final GenerationStep.Feature featureStep;
	
	public EndFeature(String name, Feature<DefaultFeatureConfig> feature, GenerationStep.Feature featureStep, ConfiguredDecorator<?> configuredDecorator) {
		Identifier id = new Identifier(BetterEnd.MOD_ID, name);
		this.featureStep = featureStep;
		this.feature = Registry.register(Registry.FEATURE, id, feature);
		this.featureConfigured = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, feature.configure(FeatureConfig.DEFAULT).decorate(configuredDecorator));
	}
	
	public EndFeature(String name, Feature<DefaultFeatureConfig> feature) {
		Identifier id = new Identifier(BetterEnd.MOD_ID, name);
		this.featureStep = GenerationStep.Feature.VEGETAL_DECORATION;
		this.feature = Registry.register(Registry.FEATURE, id, feature);
		this.featureConfigured = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, feature.configure(FeatureConfig.DEFAULT).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(100))));
	}
	
	public EndFeature(String name, Feature<DefaultFeatureConfig> feature, int density) {
		Identifier id = new Identifier(BetterEnd.MOD_ID, name);
		this.featureStep = GenerationStep.Feature.VEGETAL_DECORATION;
		this.feature = Registry.register(Registry.FEATURE, id, feature);
		this.featureConfigured = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, feature.configure(FeatureConfig.DEFAULT).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).repeatRandomly(density));
		//return new EndFeature(name, feature, GenerationStep.Feature.VEGETAL_DECORATION, feature.configure(FeatureConfig.DEFAULT).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).repeatRandomly(4));
	}

	public Feature<DefaultFeatureConfig> getFeature() {
		return feature;
	}

	public ConfiguredFeature<?, ?> getFeatureConfigured() {
		return featureConfigured;
	}

	public GenerationStep.Feature getFeatureStep() {
		return featureStep;
	}
}
