package ru.betterend.mixin.common;

import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.function.Supplier;

@Mixin(BiomeGenerationSettings.class)
public interface BiomeGenerationSettingsAccessor {
	@Accessor("features")
	List<List<Supplier<ConfiguredFeature<?, ?>>>> be_getFeatures();

	@Accessor("features")
	void be_setFeatures(List<List<Supplier<ConfiguredFeature<?, ?>>>> features);

	@Accessor("structureStarts")
	List<Supplier<ConfiguredStructureFeature<?, ?>>> be_getStructures();

	@Accessor("structureStarts")
	void be_setStructures(List<Supplier<ConfiguredStructureFeature<?, ?>>> structures);
}
