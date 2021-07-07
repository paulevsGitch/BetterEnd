package ru.betterend.world.structures;

import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.betterend.BetterEnd;

import java.util.Random;

public class EndStructureFeature {
	private static final Random RANDOM = new Random(354);
	private final StructureFeature<NoneFeatureConfiguration> structure;
	private final ConfiguredStructureFeature<?, ?> featureConfigured;
	private final GenerationStep.Decoration featureStep;

	public EndStructureFeature(String name, StructureFeature<NoneFeatureConfiguration> structure, GenerationStep.Decoration step, int spacing, int separation) {
		ResourceLocation id = BetterEnd.makeID(name);

		this.featureStep = step;
		this.structure = FabricStructureBuilder.create(id, structure)
				.step(step)
				.defaultConfig(spacing, separation, RANDOM.nextInt(8192))
				.register();

		this.featureConfigured = this.structure.configured(NoneFeatureConfiguration.NONE);

		BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, id, this.featureConfigured);
	}

	public StructureFeature<NoneFeatureConfiguration> getStructure() {
		return structure;
	}

	public ConfiguredStructureFeature<?, ?> getFeatureConfigured() {
		return featureConfigured;
	}

	public GenerationStep.Decoration getFeatureStep() {
		return featureStep;
	}
}
