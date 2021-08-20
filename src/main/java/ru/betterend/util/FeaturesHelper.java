package ru.betterend.util;

import com.google.common.collect.Lists;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import ru.bclib.api.BiomeAPI;
import ru.betterend.mixin.common.BiomeGenerationSettingsAccessor;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndStructures;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FeaturesHelper {
	public static void addFeatures(Registry<Biome> biomeRegistry) {
		biomeRegistry.forEach((biome) -> {
			ResourceLocation key = biomeRegistry.getKey(biome);
			if (BiomeAPI.isEndBiome(key)) {
				BiomeGenerationSettingsAccessor accessor = (BiomeGenerationSettingsAccessor) biome.getGenerationSettings();
				List<Supplier<ConfiguredStructureFeature<?, ?>>> structures = Lists.newArrayList(accessor.be_getStructures());
				List<List<Supplier<ConfiguredFeature<?, ?>>>> preFeatures = accessor.be_getFeatures();
				List<List<Supplier<ConfiguredFeature<?, ?>>>> features = new ArrayList<List<Supplier<ConfiguredFeature<?, ?>>>>(
					preFeatures.size());
				preFeatures.forEach((list) -> {
					features.add(Lists.newArrayList(list));
				});
				
				EndFeatures.registerBiomeFeatures(key, biome, features);
				EndStructures.registerBiomeStructures(key, biome, structures);
				
				accessor.be_setFeatures(features);
				accessor.be_setStructures(structures);
			}
		});
	}
}