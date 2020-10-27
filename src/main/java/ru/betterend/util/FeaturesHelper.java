package ru.betterend.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import ru.betterend.mixin.common.GenerationSettingsAccessor;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndStructures;

public class FeaturesHelper {
	private static final Set<Biome> INJECTED = Sets.newHashSet();

	public static void addFeatures(Registry<Biome> biomeRegistry) {
		biomeRegistry.forEach((biome) -> {
			if (biome.getCategory() == Biome.Category.THEEND && !INJECTED.contains(biome)) {
				GenerationSettingsAccessor accessor = (GenerationSettingsAccessor) biome.getGenerationSettings();
				List<Supplier<ConfiguredStructureFeature<?, ?>>> structures = Lists.newArrayList(accessor.getStructures());
				List<List<Supplier<ConfiguredFeature<?, ?>>>> preFeatures = accessor.getFeatures();
				List<List<Supplier<ConfiguredFeature<?, ?>>>> features = new ArrayList<List<Supplier<ConfiguredFeature<?, ?>>>>(preFeatures.size());
				preFeatures.forEach((list) -> {
					features.add(Lists.newArrayList(list));
				});

				EndFeatures.registerBiomeFeatures(biomeRegistry.getId(biome), biome, features);
				EndStructures.registerBiomeStructures(biomeRegistry.getId(biome), biome, structures);

				accessor.setFeatures(features);
				accessor.setStructures(structures);
				INJECTED.add(biome);
			}
		}); 
	}
}