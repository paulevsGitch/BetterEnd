package ru.betterend.registry;

import net.minecraft.world.gen.GenerationStep.Feature;
import ru.betterend.world.structures.EndStructureFeature;
import ru.betterend.world.structures.features.StructureMountain;

public class DefaultBiomeFeatureRegistry {
	public static final EndStructureFeature MOUNTAINS = new EndStructureFeature("mountains", new StructureMountain(), Feature.RAW_GENERATION, 3, 2);
}
