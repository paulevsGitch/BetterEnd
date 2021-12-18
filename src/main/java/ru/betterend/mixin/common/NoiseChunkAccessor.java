package ru.betterend.mixin.common;

import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NoiseChunk.class)
public interface NoiseChunkAccessor {
	@Accessor("noiseSettings")
	NoiseSettings bnv_getNoiseSettings();
	
	@Accessor("cellCountXZ")
	int bnv_getCellCountXZ();
	
	@Accessor("cellCountY")
	int bnv_getCellCountY();
	
	@Accessor("firstCellZ")
	int bnv_getFirstCellZ();
	
	@Accessor("cellNoiseMinY")
	int bnv_getCellNoiseMinY();
}
