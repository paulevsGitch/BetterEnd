package ru.betterend.mixin.common;

import java.util.List;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;

@Mixin(GenerationSettings.class)
public interface GenerationSettingsAccessor {
	@Accessor("features")
	List<List<Supplier<ConfiguredFeature<?, ?>>>> getFeatures();
	
	@Accessor("features")
	void setFeatures(List<List<Supplier<ConfiguredFeature<?, ?>>>> features);
	
	@Accessor("structureFeatures")
	List<Supplier<ConfiguredStructureFeature<?, ?>>> getStructures();
	
	@Accessor("structureFeatures")
	void setStructures(List<Supplier<ConfiguredStructureFeature<?, ?>>> structures);
}
