package ru.betterend.mixin.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import ru.betterend.registry.FeatureRegistry;
import ru.betterend.registry.StructureRegistry;

@Mixin(Biome.class)
public abstract class BiomeMixin {
	private static final Set<Biome> INJECTED = Sets.newHashSet();
	
	@Shadow
	private Biome.Category category;
	
	@Shadow
	private GenerationSettings generationSettings;
	
	@Inject(method = "generateFeatureStep", at = @At("HEAD"))
	public void generateFeatureStep(StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, ChunkRegion region, long populationSeed, ChunkRandom random, BlockPos pos, CallbackInfo cinfo) {
		Biome biome = (Biome) (Object) this;
		if (!INJECTED.contains(biome)) {
			if (category.equals(Biome.Category.THEEND)) {
				GenerationSettingsAccessor accessor = (GenerationSettingsAccessor) generationSettings;
				List<Supplier<ConfiguredStructureFeature<?, ?>>> structures = Lists.newArrayList(accessor.getStructures());
				List<List<Supplier<ConfiguredFeature<?, ?>>>> preFeatures = accessor.getFeatures();
				List<List<Supplier<ConfiguredFeature<?, ?>>>> features = new ArrayList<List<Supplier<ConfiguredFeature<?, ?>>>>(preFeatures.size());
				preFeatures.forEach((list) -> {
					features.add(Lists.newArrayList(list));
				});
				
				FeatureRegistry.registerGlobals(features);
				StructureRegistry.registerBiomeStructures(biome, structures);
				
				accessor.setFeatures(features);
				accessor.setStructures(structures);
			}
			INJECTED.add(biome);
		}
	}
}
