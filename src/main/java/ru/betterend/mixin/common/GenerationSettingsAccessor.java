package ru.betterend.mixin.common;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BiomeGenerationSettings.class)
public interface GenerationSettingsAccessor {
	@Accessor("features")
	List<List<Supplier<ConfiguredFeature<?, ?>>>> beGetFeatures();
	
	@Accessor("features")
	void beSetFeatures(List<List<Supplier<ConfiguredFeature<?, ?>>>> features);
	
	@Accessor("structureFeatures")
	List<Supplier<ConfiguredStructureFeature<?, ?>>> beGetStructures();
	
	@Accessor("structureFeatures")
	void beSetStructures(List<Supplier<ConfiguredStructureFeature<?, ?>>> structures);
}
