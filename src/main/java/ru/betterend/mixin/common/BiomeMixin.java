package ru.betterend.mixin.common;

import java.util.List;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import ru.betterend.registry.FeatureRegistry;

@Mixin(Biome.class)
public abstract class BiomeMixin {
	
	@Shadow
	private Biome.Category category;
	
	@Shadow
	private GenerationSettings generationSettings;
	
	@Inject(method = "generateFeatureStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/GenerationSettings;getFeatures()Ljava/util/List;", shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	public void generateFeatureStep(StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, ChunkRegion region, long populationSeed, ChunkRandom random, BlockPos pos, CallbackInfo cinfo, List<List<Supplier<ConfiguredFeature<?, ?>>>> list) {
		if (category.equals(Biome.Category.THEEND)) {
			int index = FeatureRegistry.ENDER_ORE.getFeatureStep().ordinal();
			list.get(index).add(() -> {
				return FeatureRegistry.ENDER_ORE.getFeatureConfigured();
			});
		}
	}
}
