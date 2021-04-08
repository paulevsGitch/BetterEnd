package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeArray;
import ru.betterend.interfaces.IBiomeArray;

@Mixin(BiomeArray.class)
public class BiomeArrayMixin implements IBiomeArray {
	@Final
	@Shadow
	private Biome[] data;

	@Final
	@Shadow
	private static int HORIZONTAL_SECTION_COUNT;

	@Final
	@Shadow
	public static int HORIZONTAL_BIT_MASK;

	@Final
	@Shadow
	public static int VERTICAL_BIT_MASK;

	@Override
	public void setBiome(Biome biome, BlockPos pos) {
		int biomeX = pos.getX() >> 2;
		int biomeY = pos.getY() >> 2;
		int biomeZ = pos.getZ() >> 2;
		int index = be_getArrayIndex(biomeX, biomeY, biomeZ);
		data[index] = biome;
	}

	private int be_getArrayIndex(int biomeX, int biomeY, int biomeZ) {
		int i = biomeX & HORIZONTAL_BIT_MASK;
		int j = Mth.clamp(biomeY, 0, VERTICAL_BIT_MASK);
		int k = biomeZ & HORIZONTAL_BIT_MASK;
		return j << HORIZONTAL_SECTION_COUNT + HORIZONTAL_SECTION_COUNT | k << HORIZONTAL_SECTION_COUNT | i;
	}
}
