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
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.BiomeRegistry;
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
					int minY = world.getTopY(Type.WORLD_SURFACE_WG, px, pz);
					if (minY > 56) {
						float maxY = dist * height * getHeightClamp(world, 8, px, pz);
						if (maxY > 0) {
							maxY *= (float) noise.eval(px * 0.05, pz * 0.05) * 0.3F + 0.7F;
							maxY *= (float) noise.eval(px * 0.1, pz * 0.1) * 0.1F + 0.8F;
							maxY +=  minY;
							for (int y = minY; y < maxY; y++) {
								pos.setY(y);
								chunk.setBlockState(pos, Blocks.END_STONE.getDefaultState(), false);
							}
						}
					}
				}
			}
		}
		return true;
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
}
