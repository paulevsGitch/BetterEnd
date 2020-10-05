package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public abstract class InvertedScatterFeature extends DefaultFeature {
	private static final Mutable POS = new Mutable();
	private final int radius;
	
	public InvertedScatterFeature(int radius) {
		this.radius = radius;
	}
	
	public abstract boolean canGenerate(StructureWorldAccess world, Random random, BlockPos center, BlockPos blockPos, float radius);
	
	public abstract void generate(StructureWorldAccess world, Random random, BlockPos blockPos);
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos center, DefaultFeatureConfig featureConfig) {
		for (int y = 128; y > 0; y--) {
			POS.set(center.getX(), y, center.getZ());
			if (world.getBlockState(POS).isAir() && !world.getBlockState(POS.up()).isAir()) {
				float r = MHelper.randRange(radius * 0.5F, radius, random);
				int count = MHelper.floor(r * r * MHelper.randRange(1.5F, 3F, random));
				for (int i = 0; i < count; i++) {
					float pr = r * (float) Math.sqrt(random.nextFloat());
					float theta = random.nextFloat() * MHelper.PI2;
					float x = pr * (float) Math.cos(theta);
					float z = pr * (float) Math.sin(theta);
					
					POS.set(center.getX() + x, center.getY() - 5, center.getZ() + z);
					int up = BlocksHelper.upRay(world, POS, 16);
					if (up > 10) continue;
					POS.setY(POS.getY() + up);
					
					if (canGenerate(world, random, center, POS, r)) {
						generate(world, random, POS);
					}
				}
			}
		}
		return true;
	}
}
