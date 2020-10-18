package ru.betterend.mixin.common;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Lists;

import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;

@Mixin(GenerationSettings.class)
public abstract class GenerationSettingsMixin {
	/*@Shadow
	private List<List<Supplier<ConfiguredFeature<?, ?>>>> features;
	
	@Inject(method = "<init>", at = @At("TAIL"))
	void init(Supplier<ConfiguredSurfaceBuilder<?>> surfaceBuilder, Map<GenerationStep.Carver, List<Supplier<ConfiguredCarver<?>>>> carvers, List<List<Supplier<ConfiguredFeature<?, ?>>>> features, List<Supplier<ConfiguredStructureFeature<?, ?>>> structureFeatures, CallbackInfo cinfo) {
		List<List<Supplier<ConfiguredFeature<?, ?>>>> mutableFeatures = Lists.newArrayList(this.features);
		this.features.forEach(supplierList -> {
			mutableFeatures.add(Lists.newArrayList(supplierList));
		});
		this.features = mutableFeatures;
	}*/
}
