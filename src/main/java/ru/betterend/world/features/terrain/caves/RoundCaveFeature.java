package ru.betterend.world.features.terrain.caves;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class RoundCaveFeature extends EndCaveFeature {
	@Override
	protected Set<BlockPos> generate(StructureWorldAccess world, BlockPos center, int radius, Random random) {
		OpenSimplexNoise noise = new OpenSimplexNoise(MHelper.getSeed(534, center.getX(), center.getZ()));

		int x1 = center.getX() - radius - 5;
		int z1 = center.getZ() - radius - 5;
		int x2 = center.getX() + radius + 5;
		int z2 = center.getZ() + radius + 5;
		int y1 = MHelper.floor(center.getY() - (radius + 5) / 1.6);
		int y2 = MHelper.floor(center.getY() + (radius + 5) / 1.6);

		double hr = radius * 0.75;
		double nr = radius * 0.25;

		BlockState state;
		MutableBlockPos bpos = new MutableBlockPos();
		Set<BlockPos> blocks = Sets.newHashSet();
		for (int x = x1; x <= x2; x++) {
			int xsq = x - center.getX();
			xsq *= xsq;
			bpos.setX(x);
			for (int z = z1; z <= z2; z++) {
				int zsq = z - center.getZ();
				zsq *= zsq;
				bpos.setZ(z);
				for (int y = y1; y <= y2; y++) {
					int ysq = y - center.getY();
					ysq *= 1.6;
					ysq *= ysq;
					bpos.setY(y);
					double r = noise.eval(x * 0.1, y * 0.1, z * 0.1) * nr + hr;
					double dist = xsq + ysq + zsq;
					if (dist < r * r) {
						state = world.getBlockState(bpos);
						if (isReplaceable(state) && !isWaterNear(world, bpos)) {
							BlocksHelper.setWithoutUpdate(world, bpos, CAVE_AIR);
							blocks.add(bpos.toImmutable());

							while (state.getMaterial().equals(Material.LEAVES)) {
								BlocksHelper.setWithoutUpdate(world, bpos, CAVE_AIR);
								bpos.setY(bpos.getY() + 1);
								state = world.getBlockState(bpos);
							}

							bpos.setY(y - 1);
							while (state.getMaterial().equals(Material.LEAVES)) {
								BlocksHelper.setWithoutUpdate(world, bpos, CAVE_AIR);
								bpos.setY(bpos.getY() - 1);
								state = world.getBlockState(bpos);
							}
						}
					}
				}
			}
		}

		return blocks;
	}

	private boolean isReplaceable(BlockState state) {
		return state.isIn(EndTags.GEN_TERRAIN) || state.getMaterial().isReplaceable()
				|| state.getMaterial().equals(Material.PLANT) || state.getMaterial().equals(Material.LEAVES);
	}
}
