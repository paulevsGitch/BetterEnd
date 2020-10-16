package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import ru.betterend.BetterEnd;
import ru.betterend.registry.FeatureRegistry;

@Mixin(Biome.class)
public abstract class BiomeMixin {
	
	@Shadow
	private Biome.Category category;
	@Shadow
	private GenerationSettings generationSettings;
	private boolean injected = false;
	
	@Inject(method = "generateFeatureStep", at = @At("HEAD"))
	public void generateFeatureStep(StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, ChunkRegion region, long populationSeed, ChunkRandom random, BlockPos pos, CallbackInfo cinfo) {
		if (!injected) {
			Identifier biomeId = region.toServerWorld().getRegistryManager().get(Registry.BIOME_KEY).getId(Biome.class.cast(this));
			if (biomeId != null && !biomeId.getNamespace().equals(BetterEnd.MOD_ID) && category.equals(Biome.Category.THEEND)) {
				FeatureRegistry.registerGlobals(this.generationSettings.getFeatures());
			}
			this.injected = true;
		}
	}
}
