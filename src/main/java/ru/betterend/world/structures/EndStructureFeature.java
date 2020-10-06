package ru.betterend.world.structures;

import java.util.Random;

import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import ru.betterend.BetterEnd;

public class EndStructureFeature {
	private static final Random RANDOM = new Random(354);
	private final StructureFeature<DefaultFeatureConfig> structure;
	private final ConfiguredStructureFeature<?, ?> featureConfigured;
	private final GenerationStep.Feature featureStep;
	
	public EndStructureFeature(String name, StructureFeature<DefaultFeatureConfig> structure, GenerationStep.Feature step, int spacing, int separation) {
		Identifier id = BetterEnd.makeID(name);
		
		this.featureStep = step;
		this.structure = FabricStructureBuilder.create(id, structure)
		.step(step)
		.defaultConfig(spacing, separation, RANDOM.nextInt(8192))
		.register();

		this.featureConfigured = this.structure.configure(DefaultFeatureConfig.DEFAULT);
		
		BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, id, this.featureConfigured);
	}

	public StructureFeature<DefaultFeatureConfig> getStructure() {
		return structure;
	}

	public ConfiguredStructureFeature<?, ?> getFeatureConfigured() {
		return featureConfigured;
	}

	public GenerationStep.Feature getFeatureStep() {
		return featureStep;
	}
}
