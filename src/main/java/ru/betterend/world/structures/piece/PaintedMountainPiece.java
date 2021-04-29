package ru.betterend.world.structures.piece;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import ru.betterend.registry.EndStructures;
import ru.betterend.util.MHelper;

public class PaintedMountainPiece extends MountainPiece {
	private BlockState[] slises;
	public PaintedMountainPiece(BlockPos center, float radius, float height, Random random, Biome biome, BlockState[] slises) {
		super(EndStructures.PAINTED_MOUNTAIN_PIECE, center, radius, height, random, biome);
		this.slises = slises;
	}

	public PaintedMountainPiece(StructureManager manager, CompoundTag tag) {
		super(EndStructures.PAINTED_MOUNTAIN_PIECE, manager, tag);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		ListTag slise = new ListTag();
		for (BlockState state: slises) {
			slise.add(NbtUtils.writeBlockState(state));
		}
		tag.put("slises", slise);
	}

	@Override
	protected void fromNbt(CompoundTag tag) {
		super.fromNbt(tag);
		ListTag slise = tag.getList("slises", 10);
		slises = new BlockState[slise.size()];
		for (int i = 0; i < slises.length; i++) {
			slises[i] = NbtUtils.readBlockState(slise.getCompound(i));
		}
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
					dist = 1 - dist / r2;
					int minY = map.getFirstAvailable(x, z);
					pos.setY(minY - 1);
					while (chunk.getBlockState(pos).isAir() && pos.getY() > 50) {
						pos.setY(minY --);
					}
					minY = pos.getY();
					minY = Math.max(minY, map2.getFirstAvailable(x, z));
					if (minY > center.getY() - 8) {
						float maxY = dist * height * getHeightClamp(world, 10, px, pz);
						if (maxY > 0) {
							maxY *= (float) noise1.eval(px * 0.05, pz * 0.05) * 0.3F + 0.7F;
							maxY *= (float) noise1.eval(px * 0.1, pz * 0.1) * 0.1F + 0.9F;
							maxY += center.getY();
							float offset = (float) (noise1.eval(px * 0.07, pz * 0.07) * 5 + noise1.eval(px * 0.2, pz * 0.2) * 2 + 7);
							for (int y = minY - 1; y < maxY; y++) {
								pos.setY(y);
								int index = MHelper.floor((y + offset) * 0.65F) % slises.length;
								chunk.setBlockState(pos, slises[index], false);
							}
						}
					}
				}
			}
		}
		
		return true;
	}
}
