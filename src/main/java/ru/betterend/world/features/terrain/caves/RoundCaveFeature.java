package ru.betterend.world.features.terrain.caves;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import ru.bclib.api.TagAPI;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.betterend.noise.OpenSimplexNoise;

import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

public class RoundCaveFeature extends EndCaveFeature {
	@Override
	protected Set<BlockPos> generate(WorldGenLevel world, BlockPos center, int radius, Random random) {
		OpenSimplexNoise noise = new OpenSimplexNoise(MHelper.getSeed(534, center.getX(), center.getZ()));
		
		int x1 = center.getX() - radius - 5;
		int z1 = center.getZ() - radius - 5;
		int x2 = center.getX() + radius + 5;
		int z2 = center.getZ() + radius + 5;
		int y1 = MHelper.floor(center.getY() - (radius + 5) / 1.6);
		int y2 = MHelper.floor(center.getY() + (radius + 5) / 1.6);
		
		double hr = radius * 0.75;
		double nr = radius * 0.25;
		
		int dx = x2 - x1 + 1;
		int dz = z2 - z1 + 1;
		int count = dx * dz;
		Set<BlockPos> blocks = Sets.newConcurrentHashSet();
		IntStream.range(0, count).parallel().forEach(index -> {
			MutableBlockPos bpos = new MutableBlockPos();
			int x = (index % dx) + x1;
			int z = (index / dx) + z1;
			bpos.setX(x);
			bpos.setZ(z);
			int xsq = MHelper.sqr(x - center.getX());
			int zsq = MHelper.sqr(z - center.getZ());
			int dxz = xsq + zsq;
			BlockState state;
			for (int y = y1; y <= y2; y++) {
				int ysq = (int) MHelper.sqr((y - center.getY()) * 1.6);
				double r = noise.eval(x * 0.1, y * 0.1, z * 0.1) * nr + hr;
				double dist = dxz + ysq;
				if (dist < r * r) {
					bpos.setY(y);
					state = world.getBlockState(bpos);
					if (isReplaceable(state) && !isWaterNear(world, bpos)) {
						blocks.add(bpos.immutable());
						
						while (state.getMaterial().equals(Material.LEAVES)) {
							bpos.setY(bpos.getY() + 1);
							state = world.getBlockState(bpos);
						}
						
						bpos.setY(y - 1);
						while (state.getMaterial().equals(Material.LEAVES)) {
							bpos.setY(bpos.getY() - 1);
							state = world.getBlockState(bpos);
						}
					}
				}
			}
		});
		blocks.forEach(bpos -> BlocksHelper.setWithoutUpdate(world, bpos, CAVE_AIR));
		
		return blocks;
	}
	
	private boolean isReplaceable(BlockState state) {
		return state.is(TagAPI.GEN_TERRAIN) || state.getMaterial().isReplaceable() || state.getMaterial()
																						   .equals(Material.PLANT) || state
			.getMaterial()
			.equals(Material.LEAVES);
	}
}
