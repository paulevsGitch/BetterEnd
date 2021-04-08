package ru.betterend.world.structures.piece;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.BlockBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import ru.betterend.registry.EndStructures;
import ru.betterend.util.MHelper;

public class PaintedMountainPiece extends MountainPiece {
	private BlockState[] slises;

	public PaintedMountainPiece(BlockPos center, float radius, float height, Random random, Biome biome,
			BlockState[] slises) {
		super(EndStructures.PAINTED_MOUNTAIN_PIECE, center, radius, height, random, biome);
		this.slises = slises;
	}

	public PaintedMountainPiece(StructureManager manager, CompoundTag tag) {
		super(EndStructures.PAINTED_MOUNTAIN_PIECE, manager, tag);
	}

	@Override
	protected void toNbt(CompoundTag tag) {
		super.toNbt(tag);
		ListTag slise = new ListTag();
		for (BlockState state : slises) {
			slise.add(NbtHelper.fromBlockState(state));
		}
		tag.put("slises", slise);
	}

	@Override
	protected void fromNbt(CompoundTag tag) {
		super.fromNbt(tag);
		ListTag slise = tag.getList("slises", 10);
		slises = new BlockState[slise.size()];
		for (int i = 0; i < slises.length; i++) {
			slises[i] = NbtHelper.toBlockState(slise.getCompound(i));
		}
	}

	@Override
	public boolean generate(StructureWorldAccess world, StructureAccessor arg, ChunkGenerator chunkGenerator,
			Random random, BlockBox blockBox, ChunkPos chunkPos, BlockPos blockPos) {
		int sx = chunkPos.getStartX();
		int sz = chunkPos.getStartZ();
		MutableBlockPos pos = new MutableBlockPos();
		Chunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
		Heightmap map = chunk.getHeightmap(Type.WORLD_SURFACE);
		Heightmap map2 = chunk.getHeightmap(Type.WORLD_SURFACE_WG);
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
					int minY = map.get(x, z);
					pos.setY(minY - 1);
					while (chunk.getBlockState(pos).isAir() && pos.getY() > 50) {
						pos.setY(minY--);
					}
					minY = pos.getY();
					minY = Math.max(minY, map2.get(x, z));
					if (minY > center.getY() - 8) {
						float maxY = dist * height * getHeightClamp(world, 10, px, pz);
						if (maxY > 0) {
							maxY *= (float) noise1.eval(px * 0.05, pz * 0.05) * 0.3F + 0.7F;
							maxY *= (float) noise1.eval(px * 0.1, pz * 0.1) * 0.1F + 0.9F;
							maxY += center.getY();
							float offset = (float) (noise1.eval(px * 0.07, pz * 0.07) * 5
									+ noise1.eval(px * 0.2, pz * 0.2) * 2 + 7);
							for (int y = minY - 1; y < maxY; y++) {
								pos.setY(y);
								int index = MHelper.floor((y + offset) * 0.65F) % slises.length;
								chunk.setBlockAndUpdate(pos, slises[index], false);
							}
						}
					}
				}
			}
		}

		return true;
	}
}
