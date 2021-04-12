package ru.betterend.integration.byg;

import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.behavior.WeightedList;
import net.minecraft.world.level.biome.Biome;
import ru.betterend.integration.Integrations;
import ru.betterend.integration.ModIntegration;
import ru.betterend.integration.byg.biomes.BYGBiomes;
import ru.betterend.integration.byg.features.BYGFeatures;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndTags;
import ru.betterend.util.TagHelper;
import ru.betterend.world.biome.EndBiome;

public class BYGIntegration extends ModIntegration {
	public BYGIntegration() {
		super("byg");
	}

	@Override
	public void register() {
		TagHelper.addTags(Integrations.BYG.getBlock("ivis_phylium"), EndTags.END_GROUND, EndTags.GEN_TERRAIN);
		BYGBlocks.register();
		BYGFeatures.register();
		BYGBiomes.register();
	}

	@Override
	public void addBiomes() {
		BYGBiomes.addBiomes();
		
		Class<?> biomeClass = this.getClass("corgiaoc.byg.common.world.biome.BYGEndBiome");
		List<Object> biomes = this.getStaticFieldValue(biomeClass, "BYG_END_BIOMES");
		
		if (biomes != null && biomeClass != null) {
			biomes.forEach((obj) -> {
				Biome biome = this.getAndExecuteRuntime(biomeClass, obj, "getBiome");
				if (biome != null) {
					ResourceLocation biomeID = BuiltinRegistries.BIOME.getKey(biome);
					EndBiome endBiome = EndBiomes.getBiome(biomeID);
					Biome edge = this.getAndExecuteRuntime(biomeClass, obj, "getEdge");
					if (edge != null) {
						ResourceLocation edgeID = BuiltinRegistries.BIOME.getKey(edge);
						EndBiomes.LAND_BIOMES.removeMutableBiome(edgeID);
						EndBiomes.VOID_BIOMES.removeMutableBiome(edgeID);
						EndBiome edgeBiome = EndBiomes.getBiome(edgeID);
						endBiome.setEdge(edgeBiome);
					}
					else {
						Boolean isVoid = this.getAndExecuteRuntime(biomeClass, obj, "isVoid");
						if (isVoid != null && isVoid.booleanValue()) {
							EndBiomes.LAND_BIOMES.removeMutableBiome(biomeID);
							EndBiomes.VOID_BIOMES.addBiomeMutable(endBiome);
						}
						WeightedList<ResourceLocation> subBiomes = this.getAndExecuteRuntime(biomeClass, obj, "getHills");
						if (subBiomes != null) {
							subBiomes.stream().collect(Collectors.toList()).forEach((id) -> {
								EndBiome subBiome = EndBiomes.getBiome(id);
								EndBiomes.LAND_BIOMES.removeMutableBiome(id);
								EndBiomes.VOID_BIOMES.removeMutableBiome(id);
								if (!endBiome.containsSubBiome(subBiome)) {
									EndBiomes.SUBBIOMES.add(subBiome);
									endBiome.addSubBiome(subBiome);
								}
							});
						}
					}
				}
			});
		}
	}
}
