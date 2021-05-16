package ru.betterend.world.features.terrain.caves;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
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
					if (val + vert + dist < 0.15 && world.getBlockState(pos).is(EndTags.GEN_TERRAIN) && noWaterNear(world, pos)) {
						BlocksHelper.setWithoutUpdate(world, pos, AIR);
						positions.add(pos.immutable());
						int height = world.getHeight(Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
						if (height < pos.getY() + 4) {
							while (pos.getY() < height && noWaterNear(world, pos)) {
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
	
	private boolean noWaterNear(WorldGenLevel world, BlockPos pos) {
		BlockPos above1 = pos.above();
		BlockPos above2 = pos.above(2);
		if (!world.getFluidState(above1).isEmpty() || !world.getFluidState(above2).isEmpty()) {
			return false;
		}
		for (Direction dir: BlocksHelper.HORIZONTAL) {
			if (!world.getFluidState(above1.relative(dir)).isEmpty()) {
				return false;
			}
			if (!world.getFluidState(above2.relative(dir)).isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, NoneFeatureConfiguration config) {
		if (pos.getX() * pos.getX() + pos.getZ() * pos.getZ() <= 2500) {
			return false;
		}

		if (biomeMissingCaves(world, pos)) {
			return false;
		}
		
		Set<BlockPos> caveBlocks = generate(world, pos, random);
		if (caveBlocks.isEmpty()) {
			return false;
		}
		
		Map<EndCaveBiome, Set<BlockPos>> floorSets = Maps.newHashMap();
		Map<EndCaveBiome, Set<BlockPos>> ceilSets = Maps.newHashMap();
		MutableBlockPos mut = new MutableBlockPos();
		Set<BlockPos> remove = Sets.newHashSet();
		caveBlocks.forEach((bpos) -> {
			mut.set(bpos);
			EndCaveBiome bio = EndBiomes.getCaveBiome(bpos.getX(), bpos.getZ());
			int height = world.getHeight(Types.WORLD_SURFACE, bpos.getX(), bpos.getZ());
			if (mut.getY() >= height) {
				remove.add(bpos);
			}
			else if (world.getBlockState(mut).getMaterial().isReplaceable()) {
				mut.setY(bpos.getY() - 1);
				if (world.getBlockState(mut).is(EndTags.GEN_TERRAIN)) {
					Set<BlockPos> floorPositions = floorSets.get(bio);
					if (floorPositions == null) {
						floorPositions = Sets.newHashSet();
						floorSets.put(bio, floorPositions);
					}
					floorPositions.add(mut.immutable());
				}
				mut.setY(bpos.getY() + 1);
				if (world.getBlockState(mut).is(EndTags.GEN_TERRAIN)) {
					Set<BlockPos> ceilPositions = ceilSets.get(bio);
					if (ceilPositions == null) {
						ceilPositions = Sets.newHashSet();
						ceilSets.put(bio, ceilPositions);
					}
					ceilPositions.add(mut.immutable());
				}
				setBiome(world, bpos, bio);
			}
		});
		caveBlocks.removeAll(remove);
		
		if (caveBlocks.isEmpty()) {
			return true;
		}
		
		floorSets.forEach((biome, floorPositions) -> {
			BlockState surfaceBlock = biome.getBiome().getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
			placeFloor(world, biome, floorPositions, random, surfaceBlock);
		});
		ceilSets.forEach((biome, ceilPositions) -> {
			placeCeil(world, biome, ceilPositions, random);
		});
		EndCaveBiome biome = EndBiomes.getCaveBiome(pos.getX(), pos.getZ());
		placeWalls(world, biome, caveBlocks, random);
		fixBlocks(world, caveBlocks);

		return true;
	}

	@Override
	protected Set<BlockPos> generate(WorldGenLevel world, BlockPos center, int radius, Random random) {
		return null;
	}
	
	@Override
	protected void placeFloor(WorldGenLevel world, EndCaveBiome biome, Set<BlockPos> floorPositions, Random random, BlockState surfaceBlock) {
		float density = biome.getFloorDensity() * 0.2F;
		floorPositions.forEach((pos) -> {
			if (!surfaceBlock.is(Blocks.END_STONE)) {
				BlocksHelper.setWithoutUpdate(world, pos, surfaceBlock);
			}
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
