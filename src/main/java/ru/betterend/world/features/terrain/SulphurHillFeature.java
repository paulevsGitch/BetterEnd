package ru.betterend.world.features.terrain;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.world.features.DefaultFeature;

public class SulphurHillFeature extends DefaultFeature {
	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, Random random, BlockPos pos,
			NoneFeatureConfiguration config) {
		pos = getPosOnSurfaceWG(world, pos);
		if (pos.getY() < 57 || pos.getY() > 70) {
			return false;
		}

		int count = MHelper.randRange(5, 13, random);
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
		for (int i = 0; i < count; i++) {
			int dist = count - i;
			int px = pos.getX() + MHelper.floor(random.nextGaussian() * dist * 0.6 + 0.5);
			int pz = pos.getZ() + MHelper.floor(random.nextGaussian() * dist * 0.6 + 0.5);
			int py = getYOnSurface(world, px, pz);
			if (py > 56 && py - pos.getY() <= count) {
				makeCircle(world, new BlockPos(px, py, pz), noise, random);
			}
		}
		return true;
	}

	private void makeCircle(WorldGenLevel world, BlockPos pos, OpenSimplexNoise noise, Random random) {
		int radius = MHelper.randRange(5, 9, random);
		int min = -radius - 3;
		int max = radius + 4;
		MutableBlockPos mut = new MutableBlockPos();
		BlockState rock = EndBlocks.SULPHURIC_ROCK.stone.defaultBlockState();
		BlockState brimstone = EndBlocks.BRIMSTONE.defaultBlockState().with(BlockProperties.ACTIVE, true);
		for (int x = min; x < max; x++) {
			int x2 = x * x;
			int px = pos.getX() + x;
			mut.setX(px);
			for (int z = min; z < max; z++) {
				int z2 = z * z;
				int pz = pos.getZ() + z;
				mut.setZ(pz);
				double r1 = radius * (noise.eval(px * 0.1, pz * 0.1) * 0.2 + 0.8);
				double r2 = r1 - 1.5;
				double r3 = r1 - 3;
				int d = x2 + z2;
				mut.setY(pos.getY());
				BlockState state = world.getBlockState(mut);
				if (state.getMaterial().isReplaceable() || state.is(EndBlocks.HYDROTHERMAL_VENT)) {
					if (d < r2 * r2) {
						BlocksHelper.setWithoutUpdate(world, mut, Blocks.WATER);
						mut.move(Direction.DOWN);
						if (d < r3 * r3) {
							BlocksHelper.setWithoutUpdate(world, mut, Blocks.WATER);
							mut.move(Direction.DOWN);
						}
						BlocksHelper.setWithoutUpdate(world, mut, brimstone);
						mut.move(Direction.DOWN);
						state = world.getBlockState(mut);
						int maxIt = MHelper.floor(10 - Math.sqrt(d)) + random.nextInt(1);
						for (int i = 0; i < maxIt && state.getMaterial().isReplaceable(); i++) {
							BlocksHelper.setWithoutUpdate(world, mut, rock);
							mut.move(Direction.DOWN);
						}
					} else if (d < r1 * r1) {
						BlocksHelper.setWithoutUpdate(world, mut, brimstone);
						mut.move(Direction.DOWN);
						state = world.getBlockState(mut);
						int maxIt = MHelper.floor(10 - Math.sqrt(d)) + random.nextInt(1);
						for (int i = 0; i < maxIt && state.getMaterial().isReplaceable(); i++) {
							BlocksHelper.setWithoutUpdate(world, mut, rock);
							mut.move(Direction.DOWN);
						}
					}
				}
			}
		}
	}
}
