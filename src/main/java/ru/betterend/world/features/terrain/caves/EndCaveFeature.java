package ru.betterend.world.features.terrain.caves;

import java.util.Random;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import com.google.common.collect.Sets;
import ru.betterend.interfaces.IBiomeArray;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.world.biome.EndBiome;
import ru.betterend.world.biome.cave.EndCaveBiome;
import ru.betterend.world.features.DefaultFeature;
import ru.betterend.world.generator.GeneratorOptions;

public abstract class EndCaveFeature extends DefaultFeature {
	protected static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();
	protected static final BlockState END_STONE = Blocks.END_STONE.defaultBlockState();
	protected static final BlockState WATER = Blocks.WATER.defaultBlockState();

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, Random random, BlockPos pos,
			NoneFeatureConfiguration config) {
		if (!(GeneratorOptions.useNewGenerator() && GeneratorOptions.noRingVoid())
				|| pos.getX() * pos.getX() + pos.getZ() * pos.getZ() <= 22500) {
			return false;
		}

		if (biomeMissingCaves(world, pos)) {
			return false;
		}

		int radius = MHelper.randRange(10, 30, random);
		BlockPos center = findPos(world, pos, radius, random);

		if (center == null) {
			return false;
		}

		EndCaveBiome biome = EndBiomes.getCaveBiome(random);
		Set<BlockPos> caveBlocks = generate(world, center, radius, random);
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
				BlockState surfaceBlock = biome.getBiome().getGenerationSettings().getSurfaceBuilderConfig()
						.getTopMaterial();
				placeFloor(world, biome, floorPositions, random, surfaceBlock);
				placeCeil(world, biome, ceilPositions, random);
			}
			fixBlocks(world, caveBlocks);
		}

		return true;
	}

	protected abstract Set<BlockPos> generate(WorldGenLevel world, BlockPos center, int radius, Random random);

	protected void placeFloor(WorldGenLevel world, EndCaveBiome biome, Set<BlockPos> floorPositions, Random random,
			BlockState surfaceBlock) {
		float density = biome.getFloorDensity();
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

	protected void placeCeil(WorldGenLevel world, EndCaveBiome biome, Set<BlockPos> ceilPositions, Random random) {
		float density = biome.getCeilDensity();
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

	protected void setBiomes(WorldGenLevel world, EndCaveBiome biome, Set<BlockPos> blocks) {
		blocks.forEach((pos) -> setBiome(world, pos, biome));
	}

	private void setBiome(WorldGenLevel world, BlockPos pos, EndCaveBiome biome) {
		IBiomeArray array = (IBiomeArray) world.getChunk(pos).getBiomes();
		if (array != null) {
			array.setBiome(biome.getActualBiome(), pos);
		}
	}

	private BlockPos findPos(WorldGenLevel world, BlockPos pos, int radius, Random random) {
		int top = world.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
		MutableBlockPos bpos = new MutableBlockPos();
		bpos.setX(pos.getX());
		bpos.setZ(pos.getZ());
		bpos.setY(top - 1);

		BlockState state = world.getBlockState(bpos);
		while (!state.is(EndTags.GEN_TERRAIN) && bpos.getY() > 5) {
			bpos.setY(bpos.getY() - 1);
			state = world.getBlockState(bpos);
		}
		if (bpos.getY() < 10) {
			return null;
		}
		top = (int) (bpos.getY() - (radius * 1.3F + 5));

		while (state.is(EndTags.GEN_TERRAIN) || !state.getFluidState().isEmpty() && bpos.getY() > 5) {
			bpos.setY(bpos.getY() - 1);
			state = world.getBlockState(bpos);
		}
		int bottom = (int) (bpos.getY() + radius * 1.3F + 5);

		if (top <= bottom) {
			return null;
		}

		return new BlockPos(pos.getX(), MHelper.randRange(bottom, top, random), pos.getZ());
	}

	private void fixBlocks(WorldGenLevel world, Set<BlockPos> caveBlocks) {
		BlockPos pos = caveBlocks.iterator().next();
		MutableBlockPos start = new MutableBlockPos().set(pos);
		MutableBlockPos end = new MutableBlockPos().set(pos);
		caveBlocks.forEach((bpos) -> {
			if (bpos.getX() < start.getX()) {
				start.setX(bpos.getX());
			}
			if (bpos.getX() > end.getX()) {
				end.setX(bpos.getX());
			}

			if (bpos.getY() < start.getY()) {
				start.setY(bpos.getY());
			}
			if (bpos.getY() > end.getY()) {
				end.setY(bpos.getY());
			}

			if (bpos.getZ() < start.getZ()) {
				start.setZ(bpos.getZ());
			}
			if (bpos.getZ() > end.getZ()) {
				end.setZ(bpos.getZ());
			}
		});
		BlocksHelper.fixBlocks(world, start.offset(-5, -5, -5), end.offset(5, 5, 5));
	}

	protected boolean isWaterNear(WorldGenLevel world, BlockPos pos) {
		for (Direction dir : BlocksHelper.DIRECTIONS) {
			if (!world.getFluidState(pos.relative(dir, 5)).isEmpty()) {
				return true;
			}
		}
		return false;
	}

	protected boolean biomeMissingCaves(WorldGenLevel world, BlockPos pos) {
		for (int x = -2; x < 3; x++) {
			for (int z = -2; z < 3; z++) {
				Biome biome = world.getBiome(pos.offset(x << 4, 0, z << 4));
				EndBiome endBiome = EndBiomes.getFromBiome(biome);
				if (!endBiome.hasCaves()) {
					return true;
				}
			}
		}
		return false;
	}
}
