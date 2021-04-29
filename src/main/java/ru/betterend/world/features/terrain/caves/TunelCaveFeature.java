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
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.world.biome.cave.EndCaveBiome;

public class TunelCaveFeature extends EndCaveFeature {
	private Set<BlockPos> generate(WorldGenLevel world, BlockPos center, Random random) {
		/*Random rand = new Random(world.getSeed());
		OpenSimplexNoise noise1 = new OpenSimplexNoise(rand.nextInt());
		OpenSimplexNoise noise2 = new OpenSimplexNoise(rand.nextInt());
		OpenSimplexNoise noise3 = new OpenSimplexNoise(rand.nextInt());
		int x1 = (center.getX() >> 4) << 4;
		int z1 = (center.getZ() >> 4) << 4;
		int x2 = x1 + 16;
		int z2 = z1 + 16;
		int y2 = world.getHeight();
		Set<BlockPos> positions = Sets.newHashSet();
		MutableBlockPos pos = new MutableBlockPos();
		for (int x = x1; x < x2; x++) {
			pos.setX(x);
			for (int z = z1; z < z2; z++) {
				pos.setZ(z);
				for (int y = 0; y < y2; y++) {
					pos.setY(y);
					float v1 = Mth.abs((float) noise1.eval(x * 0.02, y * 0.02, z * 0.02));
					float v2 = Mth.abs((float) noise2.eval(x * 0.02, y * 0.02, z * 0.02));
					//float v3 = Mth.abs((float) noise3.eval(x * 0.02, y * 0.02, z * 0.02));
					if (MHelper.max(v1, v2) > 0.7 && world.getBlockState(pos).is(EndTags.GEN_TERRAIN)) {
						BlocksHelper.setWithoutUpdate(world, pos, AIR);
						positions.add(pos.immutable());
					}
				}
			}
		}
		return positions;*/
		
		int x1 = (center.getX() >> 4) << 4;
		int z1 = (center.getZ() >> 4) << 4;
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
					float vert = Mth.sin((y + (float) noiseV.eval(x * 0.01, z * 0.01) * 20) * 0.1F) * 0.9F;//Mth.abs(y - 50 + (float) noiseV.eval(x * 0.01, z * 0.01) * 20) * 0.1F;
					float dist = (float) noiseD.eval(x * 0.1, y * 0.1, z * 0.1) * 0.12F;
					vert *= vert;
					if (val + vert + dist < 0.15 && world.getBlockState(pos).is(EndTags.GEN_TERRAIN)) {
						BlocksHelper.setWithoutUpdate(world, pos, AIR);
						positions.add(pos.immutable());
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

		EndCaveBiome biome = EndBiomes.getCaveBiome(random);//EndBiomes.EMPTY_END_CAVE;
		Set<BlockPos> caveBlocks = generate(world, pos, random);
		if (!caveBlocks.isEmpty()) {
			if (biome != null) {
				setBiomes(world, biome, caveBlocks);
				Set<BlockPos> floorPositions = Sets.newHashSet();
				Set<BlockPos> ceilPositions = Sets.newHashSet();
				MutableBlockPos mut = new MutableBlockPos();
				caveBlocks.forEach((bpos) -> {
					mut.set(bpos);
					if (world.getBlockState(mut).getMaterial().isReplaceable()) {
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
				placeWalls(world, biome, caveBlocks, random);
			}
			fixBlocks(world, caveBlocks);
		}

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
