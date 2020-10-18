package ru.betterend.world.structures.piece;

import java.util.Random;

import net.minecraft.block.BlockState;
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
import ru.betterend.registry.BlockTagRegistry;
import ru.betterend.registry.StructureRegistry;
import ru.betterend.util.MHelper;

public class LakePiece extends BasePiece {
	private static final BlockState WATER = Blocks.WATER.getDefaultState();
	private OpenSimplexNoise noise;
	private BlockPos center;
	private float radius;
	private float depth;
	private float r2;
	
	public LakePiece(BlockPos center, float radius, float depth, int id) {
		super(StructureRegistry.LAKE_PIECE, id);
		this.center = center;
		this.radius = radius;
		this.depth = depth;
		this.r2 = radius * radius;
		this.noise = new OpenSimplexNoise(MHelper.getSeed(534, center.getX(), center.getZ()));
		makeBoundingBox();
	}

	public LakePiece(StructureManager manager, CompoundTag tag) {
		super(StructureRegistry.LAKE_PIECE, tag);
		makeBoundingBox();
	}

	@Override
	protected void toNbt(CompoundTag tag) {
		tag.put("center", NbtHelper.fromBlockPos(center));
		tag.putFloat("radius", radius);
		tag.putFloat("depth", depth);
	}

	@Override
	protected void fromNbt(CompoundTag tag) {
		center = NbtHelper.toBlockPos(tag.getCompound("center"));
		radius = tag.getFloat("radius");
		depth = tag.getFloat("depth");
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
					dist = 1 - dist / r2;
					int maxY = map.get(x, z);
					if (maxY > 55) {
						float minY = dist * depth * getHeightClamp(world, 4, px, pz);
						if (minY > 0) {
							minY *= (float) noise.eval(px * 0.05, pz * 0.05) * 0.3F + 0.7F;
							minY *= (float) noise.eval(px * 0.1, pz * 0.1) * 0.1F + 0.8F;
							float lerp = minY / 2F;
							if (lerp > 1) {
								lerp = 1;
							}
							minY = MathHelper.lerp(lerp, maxY - minY, 56 - minY);
							//System.out.println(minY + " " + maxY);
							for (int y = maxY; y >= minY; y--) {
								pos.setY(y - 1);
								BlockState state = chunk.getBlockState(pos);
								if (state.getMaterial().isReplaceable() || state.isIn(BlockTagRegistry.GEN_TERRAIN)) {
									pos.setY(y);
									chunk.setBlockState(pos, y > 56 ? AIR : WATER, false);
								}
								else {
									pos.setY(y);
									break;
								}
							}
							if (pos.getY() < 57) {
								BlockState state = chunk.getBlockState(pos);
								if (state.getMaterial().isReplaceable() || state.isIn(BlockTagRegistry.GEN_TERRAIN)) {
									chunk.setBlockState(pos, BlockRegistry.ENDSTONE_DUST.getDefaultState(), false);
									pos.setY(pos.getY() - 1);
									state = chunk.getBlockState(pos);
									if (state.getMaterial().isReplaceable() || state.isIn(BlockTagRegistry.GEN_TERRAIN)) {
										chunk.setBlockState(pos, BlockRegistry.ENDSTONE_DUST.getDefaultState(), false);
										pos.setY(pos.getY() - 1);
									}
									if (!chunk.getBlockState(pos).isIn(BlockTagRegistry.GEN_TERRAIN)) {
										
										chunk.setBlockState(pos, Blocks.END_STONE.getDefaultState(), false);
									}
								}
							}
						}
					}
				}
			}
		}
		
		map = chunk.getHeightmap(Type.WORLD_SURFACE);
		
		return true;
	}
	
	private int getHeight(StructureWorldAccess world, BlockPos pos) {
		if (BiomeRegistry.getFromBiome(world.getBiome(pos)) != BiomeRegistry.MEGALAKE) {
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
