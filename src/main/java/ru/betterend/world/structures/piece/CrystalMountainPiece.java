package ru.betterend.world.structures.piece;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import ru.bclib.api.TagAPI;
import ru.bclib.util.MHelper;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndStructures;

public class CrystalMountainPiece extends MountainPiece {
	private BlockState top;
	
	public CrystalMountainPiece(BlockPos center, float radius, float height, Random random, Biome biome) {
		super(EndStructures.MOUNTAIN_PIECE, center, radius, height, random, biome);
		top = biome.getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
	}

	public CrystalMountainPiece(StructureManager manager, CompoundTag tag) {
		super(EndStructures.MOUNTAIN_PIECE, manager, tag);
	}

	@Override
	protected void fromNbt(CompoundTag tag) {
		super.fromNbt(tag);
		top = EndBiomes.getBiome(biomeID).getBiome().getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager arg, ChunkGenerator chunkGenerator, Random random, BoundingBox blockBox, ChunkPos chunkPos, BlockPos blockPos) {
		int sx = chunkPos.getMinBlockX();
		int sz = chunkPos.getMinBlockZ();
		MutableBlockPos pos = new MutableBlockPos();
		ChunkAccess chunk = world.getChunk(chunkPos.x, chunkPos.z);
		Heightmap map = chunk.getOrCreateHeightmapUnprimed(Types.WORLD_SURFACE);
		Heightmap map2 = chunk.getOrCreateHeightmapUnprimed(Types.WORLD_SURFACE_WG);
		for (int x = 0; x < 16; x++) {
			int px = x + sx;
			int px2 = px - center.getX();
			px2 *= px2;
			pos.setX(x);
			for (int z = 0; z < 16; z++) {
				int pz = z + sz;
				int pz2 = pz - center.getZ();
				pz2 *= pz2;
				float dist = px2 + pz2;
				if (dist < r2) {
					pos.setZ(z);
					dist = 1 - (float) Math.pow(dist / r2, 0.3);
					int minY = map.getFirstAvailable(x, z);
					if (minY < 10) {
						continue;
					}
					pos.setY(minY);
					while (!chunk.getBlockState(pos).is(TagAPI.GEN_TERRAIN) && pos.getY() > 56 && !chunk.getBlockState(pos.below()).is(Blocks.CAVE_AIR)) {
						pos.setY(pos.getY() - 1);
					}
					minY = pos.getY();
					minY = Math.max(minY, map2.getFirstAvailable(x, z));
					if (minY > center.getY() - 8) {
						float maxY = dist * height * getHeightClamp(world, 12, px, pz);
						if (maxY > 0) {
							maxY *= (float) noise1.eval(px * 0.05, pz * 0.05) * 0.3F + 0.7F;
							maxY *= (float) noise1.eval(px * 0.1, pz * 0.1) * 0.1F + 0.8F;
							maxY += center.getY();
							int maxYI = (int) (maxY);
							int cover = maxYI - 1;
							boolean needCover = (noise1.eval(px * 0.1, pz * 0.1) + MHelper.randRange(-0.4, 0.4, random) - (center.getY() + 14) * 0.1) > 0;
							for (int y = minY - 1; y < maxYI; y++) {
								pos.setY(y);
								chunk.setBlockState(pos, needCover && y == cover ? top : Blocks.END_STONE.defaultBlockState(), false);
							}
						}
					}
				}
			}
		}
		
		map = chunk.getOrCreateHeightmapUnprimed(Types.WORLD_SURFACE);
		
		// Big crystals
		int count = (map.getFirstAvailable(8, 8) - (center.getY() + 24)) / 7;
		count = Mth.clamp(count, 0, 8);
		for (int i = 0; i < count; i++) {
			int radius = MHelper.randRange(2, 3, random);
			float fill = MHelper.randRange(0F, 1F, random);
			int x = MHelper.randRange(radius, 15 - radius, random);
			int z = MHelper.randRange(radius, 15 - radius, random);
			int y = map.getFirstAvailable(x, z);
			if (y > 80) {
				pos.set(x, y, z);
				if (chunk.getBlockState(pos.below()).is(Blocks.END_STONE)) {
					int height = MHelper.floor(radius * MHelper.randRange(1.5F, 3F, random) + (y - 80) * 0.3F);
					crystal(chunk, pos, radius, height, fill, random);
				}
			}
		}
		
		// Small crystals
		count = (map.getFirstAvailable(8, 8) - (center.getY() + 24)) / 2;
		count = Mth.clamp(count, 4, 8);
		for (int i = 0; i < count; i++) {
			int radius = MHelper.randRange(1, 2, random);
			float fill = random.nextBoolean() ? 0 : 1;
			int x = MHelper.randRange(radius, 15 - radius, random);
			int z = MHelper.randRange(radius, 15 - radius, random);
			int y = map.getFirstAvailable(x, z);
			if (y > 80) {
				pos.set(x, y, z);
				if (chunk.getBlockState(pos.below()).getBlock() == Blocks.END_STONE) {
					int height = MHelper.floor(radius * MHelper.randRange(1.5F, 3F, random) + (y - 80) * 0.3F);
					crystal(chunk, pos, radius, height, fill, random);
				}
			}
		}
		
		return true;
	}
	
	private void crystal(ChunkAccess chunk, BlockPos pos, int radius, int height, float fill, Random random) {
		MutableBlockPos mut = new MutableBlockPos();
		int max = MHelper.floor(fill * radius + radius + 0.5F);
		height += pos.getY();
		Heightmap map = chunk.getOrCreateHeightmapUnprimed(Types.WORLD_SURFACE);
		int coefX = MHelper.randRange(-1, 1, random);
		int coefZ = MHelper.randRange(-1, 1, random);
		for (int x = -radius; x <= radius; x++) {
			mut.setX(x + pos.getX());
			if (mut.getX() >= 0 && mut.getX() < 16) {
				int ax = Math.abs(x);
				for (int z = -radius; z <= radius; z++) {
					mut.setZ(z + pos.getZ());
					if (mut.getZ() >= 0 && mut.getZ() < 16) {
						int az = Math.abs(z);
						if (ax + az < max) {
							int minY = map.getFirstAvailable(mut.getX(), mut.getZ()) - MHelper.randRange(3, 7, random);
							if (pos.getY() - minY > 8) {
								minY = pos.getY() - 8;
							}
							int h = coefX * x + coefZ * z + height;
							for (int y = minY; y < h; y++) {
								mut.setY(y);
								chunk.setBlockState(mut, EndBlocks.AURORA_CRYSTAL.defaultBlockState(), false);
							}
						}
					}
				}
			}
		}
	}
}
