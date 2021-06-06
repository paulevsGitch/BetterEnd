package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;

public abstract class FullHeightScatterFeature extends DefaultFeature {
	private static final MutableBlockPos POS = new MutableBlockPos();
	private final int radius;

	public FullHeightScatterFeature(int radius) {
		this.radius = radius;
	}

	public abstract boolean canGenerate(WorldGenLevel world, Random random, BlockPos center, BlockPos blockPos,
			float radius);

	public abstract void generate(WorldGenLevel world, Random random, BlockPos blockPos);

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, Random random, BlockPos center,
			NoneFeatureConfiguration featureConfig) {
		int maxY = world.getHeight(Heightmap.Types.WORLD_SURFACE_WG, center.getX(), center.getZ());
		int minY = BlocksHelper.upRay(world, new BlockPos(center.getX(), 0, center.getZ()), maxY);
		for (int y = maxY; y > minY; y--) {
			POS.set(center.getX(), y, center.getZ());
			if (world.getBlockState(POS).isAir() && !world.getBlockState(POS.below()).isAir()) {
				float r = MHelper.randRange(radius * 0.5F, radius, random);
				int count = MHelper.floor(r * r * MHelper.randRange(1.5F, 3F, random));
				for (int i = 0; i < count; i++) {
					float pr = r * (float) Math.sqrt(random.nextFloat());
					float theta = random.nextFloat() * MHelper.PI2;
					float x = pr * (float) Math.cos(theta);
					float z = pr * (float) Math.sin(theta);

					POS.set(center.getX() + x, y + 5, center.getZ() + z);
					int down = BlocksHelper.downRay(world, POS, 16);
					if (down > 10)
						continue;
					POS.setY(POS.getY() - down);

					if (canGenerate(world, random, center, POS, r)) {
						generate(world, random, POS);
					}
				}
			}
		}
		return true;
	}
}
