package ru.betterend.world.features.terrain.caves;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.interfaces.IBiomeArray;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.world.biome.EndCaveBiome;
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
		
		int radius = MHelper.randRange(10, 30, random);
		BlockPos center = findPos(world, pos, radius, random);
		
		if (center == null) {
			return false;
		}
		
		EndCaveBiome biome = EndBiomes.getCaveBiome(random);
		Set<BlockPos> caveBlocks = generate(world, center, radius, random);
		if (biome != null && !caveBlocks.isEmpty()) {
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
		fixBlocks(world, center, radius);
		
		return true;
	}
	
	protected abstract Set<BlockPos> generate(StructureWorldAccess world, BlockPos center, int radius, Random random);
	
	protected void placeFloor(StructureWorldAccess world, EndCaveBiome biome, Set<BlockPos> floorPositions, Random random, BlockState surfaceBlock) {
		floorPositions.forEach((pos) -> {
			BlocksHelper.setWithoutUpdate(world, pos, surfaceBlock);
		});
	}
	
	protected void placeCeil(StructureWorldAccess world, EndCaveBiome biome, Set<BlockPos> ceilPositions, Random random) {
		
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
	
	private void fixBlocks(StructureWorldAccess world, BlockPos pos, int radius) {
		int x1 = pos.getX() - radius - 5;
		int y1 = pos.getY() - radius - 5;
		int z1 = pos.getZ() - radius - 5;
		int x2 = pos.getX() + radius + 5;
		int y2 = pos.getY() + radius + 5;
		int z2 = pos.getZ() + radius + 5;
		BlocksHelper.fixBlocks(world, new BlockPos(x1, y1, z1), new BlockPos(x2, y2, z2));
	}
}
