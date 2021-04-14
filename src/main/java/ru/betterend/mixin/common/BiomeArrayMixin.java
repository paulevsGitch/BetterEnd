package ru.betterend.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkBiomeContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ru.betterend.interfaces.IBiomeArray;

@Mixin(ChunkBiomeContainer.class)
public class BiomeArrayMixin implements IBiomeArray {
	@Final
	@Shadow
	private Biome[] biomes;

	@Final
	@Shadow
	private static int WIDTH_BITS;

	@Final
	@Shadow
	public static int HORIZONTAL_MASK;

	@Final
	@Shadow
	public static int VERTICAL_MASK;

	@Override
	public void setBiome(Biome biome, BlockPos pos) {
		int biomeX = pos.getX() >> 2;
		int biomeY = pos.getY() >> 2;
		int biomeZ = pos.getZ() >> 2;
		int index = be_getArrayIndex(biomeX, biomeY, biomeZ);
		biomes[index] = biome;
	}

	private int be_getArrayIndex(int biomeX, int biomeY, int biomeZ) {
		int i = biomeX & HORIZONTAL_MASK;
		int j = Mth.clamp(biomeY, 0, VERTICAL_MASK);
		int k = biomeZ & HORIZONTAL_MASK;
		return j << WIDTH_BITS + WIDTH_BITS | k << WIDTH_BITS | i;
	}
}
