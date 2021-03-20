package ru.betterend.world.features.terrain.caves;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
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
	protected static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
	protected static final BlockState END_STONE = Blocks.END_STONE.getDefaultState();
	protected static final BlockState WATER = Blocks.WATER.getDefaultState();
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!(GeneratorOptions.useNewGenerator() && GeneratorOptions.noRingVoid()) || pos.getX() * pos.getX() + pos.getZ() * pos.getZ() <= 22500) {
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
				Mutable mut = new Mutable();
				caveBlocks.forEach((bpos) -> {
					mut.set(bpos);
					if (world.getBlockState(mut).getMaterial().isReplaceable()) {
						mut.setY(bpos.getY() - 1);
						if (world.getBlockState(mut).isIn(EndTags.GEN_TERRAIN)) {
							floorPositions.add(mut.toImmutable());
						}
						mut.setY(bpos.getY() + 1);
						if (world.getBlockState(mut).isIn(EndTags.GEN_TERRAIN)) {
							ceilPositions.add(mut.toImmutable());
						}
					}
				});
				BlockState surfaceBlock = biome.getBiome().getGenerationSettings().getSurfaceConfig().getTopMaterial();
				placeFloor(world, biome, floorPositions, random, surfaceBlock);
				placeCeil(world, biome, ceilPositions, random);
			}
			fixBlocks(world, caveBlocks);
		}
		
		return true;
	}
	
	protected abstract Set<BlockPos> generate(StructureWorldAccess world, BlockPos center, int radius, Random random);
	
	protected void placeFloor(StructureWorldAccess world, EndCaveBiome biome, Set<BlockPos> floorPositions, Random random, BlockState surfaceBlock) {
		float density = biome.getFloorDensity();
		floorPositions.forEach((pos) -> {
			BlocksHelper.setWithoutUpdate(world, pos, surfaceBlock);
			if (density > 0 && random.nextFloat() <= density) {
				Feature<?> feature = biome.getFloorFeature(random);
				if (feature != null) {
					feature.generate(world, null, random, pos.up(), null);
				}
			}
		});
	}
	
	protected void placeCeil(StructureWorldAccess world, EndCaveBiome biome, Set<BlockPos> ceilPositions, Random random) {
		float density = biome.getCeilDensity();
		ceilPositions.forEach((pos) -> {
			BlockState ceilBlock = biome.getCeil(pos);
			if (ceilBlock != null) {
				BlocksHelper.setWithoutUpdate(world, pos, ceilBlock);
			}
			if (density > 0 && random.nextFloat() <= density) {
				Feature<?> feature = biome.getCeilFeature(random);
				if (feature != null) {
					feature.generate(world, null, random, pos.down(), null);
				}
			}
		});
	}
	
	protected void setBiomes(StructureWorldAccess world, EndCaveBiome biome, Set<BlockPos> blocks) {
		blocks.forEach((pos) -> setBiome(world, pos, biome));
	}
	
	private void setBiome(StructureWorldAccess world, BlockPos pos, EndCaveBiome biome) {
		IBiomeArray array = (IBiomeArray) world.getChunk(pos).getBiomeArray();
		if (array != null) {
			array.setBiome(biome.getActualBiome(), pos);
		}
	}
	
	private BlockPos findPos(StructureWorldAccess world, BlockPos pos, int radius, Random random) {
		int top = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
		Mutable bpos = new Mutable();
		bpos.setX(pos.getX());
		bpos.setZ(pos.getZ());
		bpos.setY(top - 1);
		
		BlockState state = world.getBlockState(bpos);
		while (!state.isIn(EndTags.GEN_TERRAIN) && bpos.getY() > 5) {
			bpos.setY(bpos.getY() - 1);
			state = world.getBlockState(bpos);
		}
		if (bpos.getY() < 10) {
			return null;
		}
		top = (int) (bpos.getY() - (radius * 1.3F + 5));
		
		while (state.isIn(EndTags.GEN_TERRAIN) || !state.getFluidState().isEmpty() && bpos.getY() > 5) {
			bpos.setY(bpos.getY() - 1);
			state = world.getBlockState(bpos);
		}
		int bottom = (int) (bpos.getY() + radius * 1.3F + 5);
		
		if (top <= bottom) {
			return null;
		}
		
		return new BlockPos(pos.getX(), MHelper.randRange(bottom, top, random), pos.getZ());
	}
	
	private void fixBlocks(StructureWorldAccess world, Set<BlockPos> caveBlocks) {
		BlockPos pos = caveBlocks.iterator().next();
		Mutable start = new Mutable().set(pos);
		Mutable end = new Mutable().set(pos);
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
		BlocksHelper.fixBlocks(world, start.add(-5, -5, -5), end.add(5, 5, 5));
	}
	
	protected boolean isWaterNear(StructureWorldAccess world, BlockPos pos) {
		for (Direction dir: BlocksHelper.DIRECTIONS) {
			if (!world.getFluidState(pos.offset(dir, 5)).isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean biomeMissingCaves(StructureWorldAccess world, BlockPos pos) {
		for (int x = -2; x < 3; x++) {
			for (int z = -2; z < 3; z++) {
				Biome biome = world.getBiome(pos.add(x << 4, 0, z << 4));
				EndBiome endBiome = EndBiomes.getFromBiome(biome);
				if (!endBiome.hasCaves()) {
					return true;
				}
			}
		}
		return false;
	}
}
