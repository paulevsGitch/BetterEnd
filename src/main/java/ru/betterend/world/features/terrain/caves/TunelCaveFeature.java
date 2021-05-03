package ru.betterend.world.features.terrain.caves;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.world.biome.cave.EndCaveBiome;

public class TunelCaveFeature extends EndCaveFeature {
	private static final OpenSimplexNoise BIOME_NOISE_X = new OpenSimplexNoise("biome_noise_x".hashCode());
	private static final OpenSimplexNoise BIOME_NOISE_Z = new OpenSimplexNoise("biome_noise_z".hashCode());
	
	private Set<BlockPos> generate(WorldGenLevel world, BlockPos center, Random random) {
		int cx = center.getX() >> 4;
		int cz = center.getZ() >> 4;
		if ((long) cx * (long) cx + (long) cz + (long) cz < 256) {
			return Sets.newHashSet();
		}
		int x1 = cx << 4;
		int z1 = cz << 4;
		int x2 = x1 + 16;
		int z2 = z1 + 16;
		int y2 = world.getHeight();
		Random rand = new Random(world.getSeed());
		OpenSimplexNoise noiseH = new OpenSimplexNoise(rand.nextInt());
		OpenSimplexNoise noiseV = new OpenSimplexNoise(rand.nextInt());
		OpenSimplexNoise noiseD = new OpenSimplexNoise(rand.nextInt());
		
		Set<BlockPos> positions = Sets.newHashSet();
		MutableBlockPos pos = new MutableBlockPos();
		for (int x = x1; x < x2; x++) {
			pos.setX(x);
			for (int z = z1; z < z2; z++) {
				pos.setZ(z);
				for (int y = 0; y < y2; y++) {
					pos.setY(y);
					float val = Mth.abs((float) noiseH.eval(x * 0.02, y * 0.01, z * 0.02));
					float vert = Mth.sin((y + (float) noiseV.eval(x * 0.01, z * 0.01) * 20) * 0.1F) * 0.9F;
					float dist = (float) noiseD.eval(x * 0.1, y * 0.1, z * 0.1) * 0.12F;
					vert *= vert;
					if (val + vert + dist < 0.15 && world.getBlockState(pos).is(EndTags.GEN_TERRAIN)) {
						BlocksHelper.setWithoutUpdate(world, pos, AIR);
						positions.add(pos.immutable());
						int height = world.getHeight(Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
						if (height < pos.getY() + 4) {
							while (pos.getY() < height) {
								pos.setY(pos.getY() + 1);
								BlocksHelper.setWithoutUpdate(world, pos, AIR);
							}
						}
					}
				}
			}
		}
		return positions;
	}
	
	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, NoneFeatureConfiguration config) {
		if (pos.getX() * pos.getX() + pos.getZ() * pos.getZ() <= 2500) {
			return false;
		}

		if (biomeMissingCaves(world, pos)) {
			return false;
		}

		EndCaveBiome biome = EndBiomes.getCaveBiome(random);
		Set<BlockPos> preCaveBlocks = generate(world, pos, random);
		Set<BlockPos> caveBlocks = mutateBlocks(preCaveBlocks);
		if (!caveBlocks.isEmpty()) {
			if (biome != null) {
				setBiomes(world, biome, caveBlocks);
				Set<BlockPos> floorPositions = Sets.newHashSet();
				Set<BlockPos> ceilPositions = Sets.newHashSet();
				MutableBlockPos mut = new MutableBlockPos();
				Set<BlockPos> remove = Sets.newHashSet();
				caveBlocks.forEach((bpos) -> {
					mut.set(bpos);
					int height = world.getHeight(Types.WORLD_SURFACE, bpos.getX(), bpos.getZ());
					if (mut.getY() >= height) {
						remove.add(bpos);
					}
					else if (world.getBlockState(mut).getMaterial().isReplaceable()) {
						mut.setY(bpos.getY() - 1);
						if (world.getBlockState(mut).is(EndTags.GEN_TERRAIN)) {
							floorPositions.add(mut.immutable());
						}
						mut.setY(bpos.getY() + 1);
						if (world.getBlockState(mut).is(EndTags.GEN_TERRAIN)) {
							ceilPositions.add(mut.immutable());
						}
					}
				});
				BlockState surfaceBlock = biome.getBiome().getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
				placeFloor(world, biome, floorPositions, random, surfaceBlock);
				placeCeil(world, biome, ceilPositions, random);
				caveBlocks.removeAll(remove);
				placeWalls(world, biome, caveBlocks, random);
			}
			fixBlocks(world, preCaveBlocks);
		}

		return true;
	}
	
	private Set<BlockPos> mutateBlocks(Set<BlockPos> caveBlocks) {
		Set<BlockPos> result = Sets.newHashSet();
		caveBlocks.forEach(pos -> {
			int dx = pos.getX() + (int) (BIOME_NOISE_X.eval(pos.getX() * 0.3, pos.getZ() * 0.3) * 10);
			int dz = pos.getZ() + (int) (BIOME_NOISE_Z.eval(pos.getX() * 0.3, pos.getZ() * 0.3) * 10);
			if ((dx >> 4) == (pos.getX() >> 4) && (dz >> 4) == (pos.getZ() >> 4)) {
				result.add(pos);
			}
		});
		return result;
	}

	@Override
	protected Set<BlockPos> generate(WorldGenLevel world, BlockPos center, int radius, Random random) {
		return null;
	}
	
	@Override
	protected void placeFloor(WorldGenLevel world, EndCaveBiome biome, Set<BlockPos> floorPositions, Random random, BlockState surfaceBlock) {
		float density = biome.getFloorDensity() * 0.2F;
		floorPositions.forEach((pos) -> {
			BlocksHelper.setWithoutUpdate(world, pos, surfaceBlock);
			if (density > 0 && random.nextFloat() <= density) {
				Feature<?> feature = biome.getFloorFeature(random);
				if (feature != null) {
					feature.place(world, null, random, pos.above(), null);
				}
			}
		});
	}

	@Override
	protected void placeCeil(WorldGenLevel world, EndCaveBiome biome, Set<BlockPos> ceilPositions, Random random) {
		float density = biome.getCeilDensity() * 0.2F;
		ceilPositions.forEach((pos) -> {
			BlockState ceilBlock = biome.getCeil(pos);
			if (ceilBlock != null) {
				BlocksHelper.setWithoutUpdate(world, pos, ceilBlock);
			}
			if (density > 0 && random.nextFloat() <= density) {
				Feature<?> feature = biome.getCeilFeature(random);
				if (feature != null) {
					feature.place(world, null, random, pos.below(), null);
				}
			}
		});
	}
}
