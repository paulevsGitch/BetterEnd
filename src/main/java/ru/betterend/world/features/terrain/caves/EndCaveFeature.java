package ru.betterend.world.features.terrain.caves;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.bclib.api.TagAPI;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.biomes.BCLBiome;
import ru.bclib.world.features.DefaultFeature;
import ru.betterend.registry.EndBiomes;
import ru.betterend.util.BlockFixer;
import ru.betterend.world.biome.EndBiome;
import ru.betterend.world.biome.cave.EndCaveBiome;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public abstract class EndCaveFeature extends DefaultFeature {
	protected static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();
	protected static final BlockState END_STONE = Blocks.END_STONE.defaultBlockState();
	protected static final BlockState WATER = Blocks.WATER.defaultBlockState();
	private static final Vec3i[] SPHERE;
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
		final Random random = featureConfig.random();
		final BlockPos pos = featureConfig.origin();
		final WorldGenLevel world = featureConfig.level();
		if (pos.getX() * pos.getX() + pos.getZ() * pos.getZ() <= 2500) {
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
		
		EndCaveBiome biome = EndBiomes.getCaveBiome(pos.getX(), pos.getZ());
		Set<BlockPos> caveBlocks = generate(world, center, radius, random);
		if (!caveBlocks.isEmpty()) {
			if (biome != null) {
				setBiomes(world, biome, caveBlocks);
				Set<BlockPos> floorPositions = Sets.newConcurrentHashSet();
				Set<BlockPos> ceilPositions = Sets.newConcurrentHashSet();
				caveBlocks.parallelStream().forEach((bpos) -> {
					if (world.getBlockState(bpos).getMaterial().isReplaceable()) {
						BlockPos side = bpos.below();
						if (world.getBlockState(side).is(TagAPI.BLOCK_GEN_TERRAIN)) {
							floorPositions.add(side);
						}
						side = bpos.above();
						if (world.getBlockState(side).is(TagAPI.BLOCK_GEN_TERRAIN)) {
							ceilPositions.add(side);
						}
					}
				});

				BlockState surfaceBlock = EndBiome.findTopMaterial(biome);
				placeFloor(world, biome, floorPositions, random, surfaceBlock);
				placeCeil(world, biome, ceilPositions, random);
				placeWalls(world, biome, caveBlocks, random);
			}
			fixBlocks(world, caveBlocks);
		}
		
		return true;
	}
	
	protected abstract Set<BlockPos> generate(WorldGenLevel world, BlockPos center, int radius, Random random);
	
	protected void placeFloor(WorldGenLevel world, EndCaveBiome biome, Set<BlockPos> floorPositions, Random random, BlockState surfaceBlock) {
		float density = biome.getFloorDensity();
		floorPositions.forEach((pos) -> {
			if (!surfaceBlock.is(Blocks.END_STONE)) {
				BlocksHelper.setWithoutUpdate(world, pos, surfaceBlock);
			}
			if (density > 0 && random.nextFloat() <= density) {
				Feature<?> feature = biome.getFloorFeature(random);
				if (feature != null) {
					feature.place(new FeaturePlaceContext<>(Optional.empty(), world, null, random, pos.above(), null));
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
					feature.place(new FeaturePlaceContext<>(Optional.empty(), world, null, random, pos.below(), null));
				}
			}
		});
	}
	
	protected void placeWalls(WorldGenLevel world, EndCaveBiome biome, Set<BlockPos> positions, Random random) {
		Set<BlockPos> placed = Sets.newHashSet();
		positions.forEach(pos -> {
			if (random.nextInt(4) == 0 && hasOpenSide(pos, positions)) {
				BlockState wallBlock = biome.getWall(pos);
				if (wallBlock != null) {
					for (Vec3i offset : SPHERE) {
						BlockPos wallPos = pos.offset(offset);
						if (!positions.contains(wallPos) && !placed.contains(wallPos) && world.getBlockState(wallPos).is(TagAPI.BLOCK_GEN_TERRAIN)) {
							wallBlock = biome.getWall(wallPos);
							BlocksHelper.setWithoutUpdate(world, wallPos, wallBlock);
							placed.add(wallPos);
						}
					}
				}
			}
		});
	}
	
	private boolean hasOpenSide(BlockPos pos, Set<BlockPos> positions) {
		for (Direction dir : BlocksHelper.DIRECTIONS) {
			if (!positions.contains(pos.relative(dir))) {
				return true;
			}
		}
		return false;
	}
	
	protected void setBiomes(WorldGenLevel world, EndCaveBiome biome, Set<BlockPos> blocks) {
		blocks.forEach((pos) -> setBiome(world, pos, biome));
	}

	protected void setBiome(WorldGenLevel world, BlockPos pos, EndCaveBiome biome) {
		BiomeAPI.setBiome(world, pos, biome.getActualBiome());
	}
	
	private BlockPos findPos(WorldGenLevel world, BlockPos pos, int radius, Random random) {
		int top = world.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
		MutableBlockPos bpos = new MutableBlockPos();
		bpos.setX(pos.getX());
		bpos.setZ(pos.getZ());
		bpos.setY(top - 1);
		
		BlockState state = world.getBlockState(bpos);
		while (!state.is(TagAPI.BLOCK_GEN_TERRAIN) && bpos.getY() > 5) {
			bpos.setY(bpos.getY() - 1);
			state = world.getBlockState(bpos);
		}
		if (bpos.getY() < 10) {
			return null;
		}
		top = (int) (bpos.getY() - (radius * 1.3F + 5));
		
		while (state.is(TagAPI.BLOCK_GEN_TERRAIN) || !state.getFluidState().isEmpty() && bpos.getY() > 5) {
			bpos.setY(bpos.getY() - 1);
			state = world.getBlockState(bpos);
		}
		int bottom = (int) (bpos.getY() + radius * 1.3F + 5);
		
		if (top <= bottom) {
			return null;
		}
		
		return new BlockPos(pos.getX(), MHelper.randRange(bottom, top, random), pos.getZ());
	}
	
	protected void fixBlocks(WorldGenLevel world, Set<BlockPos> caveBlocks) {
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
		BlockFixer.fixBlocks(world, start.offset(-2, -2, -2), end.offset(2, 2, 2));
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
				BCLBiome endBiome = BiomeAPI.getFromBiome(biome);
				boolean hasCaves = endBiome.getCustomData("has_caves", true);
				if (!hasCaves && BiomeAPI.END_LAND_BIOME_PICKER.containsImmutable(endBiome.getID())) {
					return true;
				}
			}
		}
		return false;
	}
	
	static {
		List<Vec3i> prePos = Lists.newArrayList();
		int radius = 5;
		int r2 = radius * radius;
		for (int x = -radius; x <= radius; x++) {
			int x2 = x * x;
			for (int y = -radius; y <= radius; y++) {
				int y2 = y * y;
				for (int z = -radius; z <= radius; z++) {
					int z2 = z * z;
					if (x2 + y2 + z2 < r2) {
						prePos.add(new Vec3i(x, y, z));
					}
				}
			}
		}
		SPHERE = prePos.toArray(new Vec3i[] {});
	}
}
