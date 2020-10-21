package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.BlockTagRegistry;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class RoundCave extends DefaultFeature {
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		int radius = MHelper.randRange(10, 30, random);
		int bottom = BlocksHelper.upRay(world, new BlockPos(pos.getX(), 0, pos.getZ()), 32) + radius;
		int top = world.getTopY(Heightmap.Type.WORLD_SURFACE, pos.getX(), pos.getZ()) - radius;
		if (top <= bottom) {
			return false;
		}
		pos = new BlockPos(pos.getX(), MHelper.randRange(bottom, top, random), pos.getZ());
		
		OpenSimplexNoise noise = new OpenSimplexNoise(MHelper.getSeed(534, pos.getX(), pos.getZ()));
		
		int x1 = pos.getX() - radius;
		int z1 = pos.getZ() - radius;
		int x2 = pos.getX() + radius;
		int z2 = pos.getZ() + radius;
		int y1 = MHelper.floor(pos.getY() - radius / 1.6);
		int y2 = MHelper.floor(pos.getY() + radius / 1.6);
		
		double hr = radius * 0.75;
		double nr = radius * 0.25;
		
		Mutable bpos = new Mutable();
		for (int x = x1; x <= x2; x++) {
			int xsq = x - pos.getX();
			xsq *= xsq;
			bpos.setX(x);
			for (int z = z1; z <= z2; z++) {
				int zsq = z - pos.getZ();
				zsq *= zsq;
				bpos.setZ(z);
				for (int y = y1; y <= y2; y++) {
					int ysq = y - pos.getY();
					ysq *= 1.6;
					ysq *= ysq;
					bpos.setY(y);
					double r = noise.eval(x * 0.1, y * 0.1, z * 0.1) * nr + hr;
					double dist = xsq + ysq + zsq;
					if (dist < r * r) {
						if (world.getBlockState(bpos).isIn(BlockTagRegistry.GEN_TERRAIN)) {
							BlocksHelper.setWithoutUpdate(world, bpos, AIR);
						}
					}
				}
			}
		}
		
		return true;
	}
}
