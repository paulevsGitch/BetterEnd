package ru.betterend.world.structures.piece;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.BiomeRegistry;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.StructureRegistry;
import ru.betterend.util.MHelper;

public class MountainPiece extends BasePiece {
	private OpenSimplexNoise noise;
	private BlockPos center;
	private float radius;
	private float height;
	private float r2;
	
	public MountainPiece(BlockPos center, float radius, float height, int id) {
		super(StructureRegistry.MOUNTAIN_PIECE, id);
		this.center = center;
		this.radius = radius;
		this.height = height;
		this.r2 = radius * radius;
		this.noise = new OpenSimplexNoise(MHelper.getSeed(534, center.getX(), center.getZ()));
		makeBoundingBox();
	}

	public MountainPiece(StructureManager manager, CompoundTag tag) {
		super(StructureRegistry.MOUNTAIN_PIECE, tag);
		makeBoundingBox();
	}

	@Override
	protected void toNbt(CompoundTag tag) {
		tag.put("center", NbtHelper.fromBlockPos(center));
		tag.putFloat("radius", radius);
		tag.putFloat("height", height);
	}

	@Override
	protected void fromNbt(CompoundTag tag) {
		center = NbtHelper.toBlockPos(tag.getCompound("center"));
		radius = tag.getFloat("radius");
		height = tag.getFloat("height");
		r2 = radius * radius;
		noise = new OpenSimplexNoise(MHelper.getSeed(534, center.getX(), center.getZ()));
	}

	@Override
	public boolean generate(StructureWorldAccess world, StructureAccessor arg, ChunkGenerator chunkGenerator, Random random, BlockBox blockBox, ChunkPos chunkPos, BlockPos blockPos) {
		int sx = chunkPos.getStartX();
		int sz = chunkPos.getStartZ();
		Mutable pos = new Mutable();
		Chunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
		Heightmap map = chunk.getHeightmap(Type.WORLD_SURFACE_WG);
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
					int minY = map.get(x, z);
					if (minY > 56) {
						float maxY = dist * height * getHeightClamp(world, 8, px, pz);
						if (maxY > 0) {
							maxY *= (float) noise.eval(px * 0.05, pz * 0.05) * 0.3F + 0.7F;
							maxY *= (float) noise.eval(px * 0.1, pz * 0.1) * 0.1F + 0.8F;
							maxY += minY;
							for (int y = minY; y < maxY; y++) {
								pos.setY(y);
								chunk.setBlockState(pos, Blocks.END_STONE.getDefaultState(), false);
							}
						}
					}
				}
			}
		}
		
		map = chunk.getHeightmap(Type.WORLD_SURFACE);
		
		// Big crystals
		int count = (map.get(8, 8) - 80) / 7;
		count = MathHelper.clamp(count, 0, 8);
		for (int i = 0; i < count; i++) {
			int radius = MHelper.randRange(2, 3, random);
			float fill = MHelper.randRange(0F, 1F, random);
			int x = MHelper.randRange(radius, 15 - radius, random);
			int z = MHelper.randRange(radius, 15 - radius, random);
			int y = map.get(x, z);
			if (y > 80) {
				pos.set(x, y, z);
				if (chunk.getBlockState(pos.down()).getBlock() == Blocks.END_STONE) {
					int height = MHelper.floor(radius * MHelper.randRange(1.5F, 3F, random) + (y - 80) * 0.3F);
					crystal(chunk, pos, radius, height, fill, random);
				}
			}
		}
		
		// Small crystals
		count = (map.get(8, 8) - 80) / 2;
		count = MathHelper.clamp(count, 4, 8);
		for (int i = 0; i < count; i++) {
			int radius = MHelper.randRange(1, 2, random);
			float fill = random.nextBoolean() ? 0 : 1;
			int x = MHelper.randRange(radius, 15 - radius, random);
			int z = MHelper.randRange(radius, 15 - radius, random);
			int y = map.get(x, z);
			if (y > 80) {
				pos.set(x, y, z);
				if (chunk.getBlockState(pos.down()).getBlock() == Blocks.END_STONE) {
					int height = MHelper.floor(radius * MHelper.randRange(1.5F, 3F, random) + (y - 80) * 0.3F);
					crystal(chunk, pos, radius, height, fill, random);
				}
			}
		}
		
		return true;
	}
	
	public int getProtoHeight(int px, int py, int pz) {
		int px2 = px - center.getX();
		px2 *= px2;

		int pz2 = pz - center.getZ();
		pz2 *= pz2;
		float dist = px2 + pz2;

		dist = 1 - (float) Math.pow(dist / r2, 0.3);
		if (py > 56) {
			float maxY = dist * height;
			if (maxY > 0) {
				maxY *= (float) noise.eval(px * 0.05, pz * 0.05) * 0.3F + 0.7F;
				maxY *= (float) noise.eval(px * 0.1, pz * 0.1) * 0.1F + 0.8F;
				return MHelper.floor(maxY + py * ((float) MathHelper.clamp((py - 57) / 7F, 0, 1)));
			}
		}

		return py;
	}
	
	private int getHeight(StructureWorldAccess world, BlockPos pos) {
		if (BiomeRegistry.getFromBiome(world.getBiome(pos)) != BiomeRegistry.END_HIGHLANDS) {
			return -4;
		}
		int h = world.getTopY(Type.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
		if (h < 57) {
			return 0;
		}
		return h - 57;
	}
	
	private float getHeightClamp(StructureWorldAccess world, int radius, int posX, int posZ) {
		Mutable mut = new Mutable();
		int r2 = radius * radius;
		float height = 0;
		float max = 0;
		for (int x = -radius; x <= radius; x++) {
			mut.setX(posX + x);
			int x2 = x * x;
			for (int z = -radius; z <= radius; z++) {
				mut.setZ(posZ + z);
				int z2 = z * z;
				if (x2 + z2 < r2) {
					float mult = 1 - (float) Math.sqrt(x2 + z2) / radius;
					max += mult;
					height += getHeight(world, mut) * mult;
				}
			}
		}
		height /= max;
		return MathHelper.clamp(height / radius, 0, 1);
	}
	
	private void makeBoundingBox() {
		int minX = MHelper.floor(center.getX() - radius);
		int minZ = MHelper.floor(center.getZ() - radius);
		int maxX = MHelper.floor(center.getX() + radius + 1);
		int maxZ = MHelper.floor(center.getZ() + radius + 1);
		this.boundingBox = new BlockBox(minX, minZ, maxX, maxZ);
	}
	
	private void crystal(Chunk chunk, BlockPos pos, int radius, int height, float fill, Random random) {
		Mutable mut = new Mutable();
		int max = MHelper.floor(fill * radius + radius + 0.5F);
		height += pos.getY();
		Heightmap map = chunk.getHeightmap(Type.WORLD_SURFACE);
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
							int minY = map.get(mut.getX(), mut.getZ()) - MHelper.randRange(3, 7, random);
							int h = coefX * x + coefZ * z + height;
							for (int y = minY; y < h; y++) {
								mut.setY(y);
								chunk.setBlockState(mut, BlockRegistry.AURORA_CRYSTAL.getDefaultState(), false);
							}
						}
					}
				}
			}
		}
	}
}
