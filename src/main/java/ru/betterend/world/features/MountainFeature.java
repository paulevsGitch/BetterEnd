package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.registry.BiomeRegistry;
import ru.betterend.util.BlocksHelper;

public class MountainFeature extends DefaultFeature {
	private static final Mutable POS = new Mutable();
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig featureConfig) {
		int px = (blockPos.getX() >> 4) << 4;
		int pz = (blockPos.getZ() >> 4) << 4;
		for (int x = 0; x < 16; x++) {
			POS.setX(px | x);
			for (int z = 0; z < 16; z++) {
				POS.setZ(pz | z);
				int min = this.getPosOnSurfaceWG(world, POS).getY();
				if (min > 50) {
					double max = getHeightSmooth(world, 7) * 10 + min;
					for (int y = min; y < max; y++) {
						POS.setY(y);
						BlocksHelper.setWithoutUpdate(world, POS, Blocks.END_STONE);
					}
				}
			}
		}
		
		return true;
	}
	
	private int getHeight(StructureWorldAccess world) {
		if (BiomeRegistry.getFromBiome(world.getBiome(POS)) != BiomeRegistry.END_HIGHLANDS) {
			return -4;
		}
		int h = this.getPosOnSurfaceWG(world, POS).getY();
		if (h < 57) {
			return 0;
		}
		return h - 57;
	}
	
	private double getHeightSmooth(StructureWorldAccess world, int radius) {
		int sx = POS.getX();
		int sz = POS.getZ();
		int r2 = radius * radius;
		double height = 0;
		double max = 0;
		for (int x = -radius; x <= radius; x++) {
			POS.setX(sx + x);
			int x2 = x * x;
			for (int z = -radius; z <= radius; z++) {
				POS.setZ(sz + z);
				int z2 = z * z;
				if (x2 + z2 < r2) {
					double mult = 1 - Math.sqrt(x2 + z2) / radius;
					max += mult;
					height += getHeight(world) * mult;
				}
			}
		}
		POS.setX(sx);
		POS.setZ(sz);
		return height / max;
	}
}
