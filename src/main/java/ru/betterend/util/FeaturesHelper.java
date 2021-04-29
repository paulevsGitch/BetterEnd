package ru.betterend.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import ru.betterend.mixin.common.BiomeGenerationSettingsAccessor;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndStructures;

public class FeaturesHelper {
	private static final Set<Biome> INJECTED = Sets.newHashSet();

	public static void addFeatures(Registry<Biome> biomeRegistry) {
		biomeRegistry.forEach((biome) -> {
			if (biome.getBiomeCategory() == Biome.BiomeCategory.THEEND && !INJECTED.contains(biome)) {
				BiomeGenerationSettingsAccessor accessor = (BiomeGenerationSettingsAccessor) biome.getGenerationSettings();
				List<Supplier<ConfiguredStructureFeature<?, ?>>> structures = Lists.newArrayList(accessor.be_getStructures());
				List<List<Supplier<ConfiguredFeature<?, ?>>>> preFeatures = accessor.be_getFeatures();
				List<List<Supplier<ConfiguredFeature<?, ?>>>> features = new ArrayList<List<Supplier<ConfiguredFeature<?, ?>>>>(preFeatures.size());
				preFeatures.forEach((list) -> {
					features.add(Lists.newArrayList(list));
				});

				EndFeatures.registerBiomeFeatures(biomeRegistry.getKey(biome), biome, features);
				EndStructures.registerBiomeStructures(biomeRegistry.getKey(biome), biome, structures);

				accessor.be_setFeatures(features);
				accessor.be_setStructures(structures);
				INJECTED.add(biome);
			}
		}); 
	}
}