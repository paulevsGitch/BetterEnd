package ru.betterend.world.structures.piece;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndStructures;
import ru.betterend.registry.EndTags;
import ru.betterend.util.MHelper;

public class LakePiece2 extends BasePiece {
	private static final BlockState ENDSTONE = Blocks.END_STONE.getDefaultState();
	private static final BlockState WATER = Blocks.WATER.getDefaultState();
	private Map<Integer, Byte> heightmap = Maps.newHashMap();
	private OpenSimplexNoise noise;
	private BlockPos center;
	private float radius;
	private float aspect;
	private float depth;
	private int seed;
	
	private Identifier biomeID;
	
	public LakePiece2(BlockPos center, float radius, float depth, Random random, Biome biome) {
		super(EndStructures.LAKE_PIECE, random.nextInt());
		this.center = center;
		this.radius = radius;
		this.depth = depth;
		this.seed = random.nextInt();
		this.noise = new OpenSimplexNoise(this.seed);
		this.aspect = radius / depth;
		this.biomeID = EndBiomes.getBiomeID(biome);
		makeBoundingBox();
	}

	public LakePiece2(StructureManager manager, CompoundTag tag) {
		super(EndStructures.LAKE_PIECE, tag);
		makeBoundingBox();
	}

	@Override
	protected void toNbt(CompoundTag tag) {
		tag.put("center", NbtHelper.fromBlockPos(center));
		tag.putFloat("radius", radius);
		tag.putFloat("depth", depth);
		tag.putInt("seed", seed);
		tag.putString("biome", biomeID.toString());
	}

	@Override
	protected void fromNbt(CompoundTag tag) {
		center = NbtHelper.toBlockPos(tag.getCompound("center"));
		radius = tag.getFloat("radius");
		depth = tag.getFloat("depth");
		seed = tag.getInt("seed");
		noise = new OpenSimplexNoise(seed);
		aspect = radius / depth;
		biomeID = new Identifier(tag.getString("biome"));
	}

	@Override
	public boolean generate(StructureWorldAccess world, StructureAccessor arg, ChunkGenerator chunkGenerator, Random random, BlockBox blockBox, ChunkPos chunkPos, BlockPos blockPos) {
		int minY = this.boundingBox.minY;
		int maxY = this.boundingBox.maxY;
		int sx = chunkPos.x << 4;
		int sz = chunkPos.z << 4;
		Mutable mut = new Mutable();
		Chunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
		for (int x = 0; x < 16; x++) {
			mut.setX(x);
			int wx = x | sx;
			double nx = wx * 0.1;
			int x2 = MHelper.pow2(wx - center.getX());
			for (int z = 0; z < 16; z++) {
				mut.setZ(z);
				int wz = z | sz;
				double nz = wz * 0.1;
				int z2 = MHelper.pow2(wz - center.getZ());
				float clamp = getHeightClamp(world, 8, wx, wz);
				if (clamp < 0.01) continue;
				for (int y = minY; y <= maxY; y++) {
					mut.setY(y);
					double ny = y * 0.1;
					double y2 = MHelper.pow2((y - center.getY()) * aspect);
					double r2 = (noise.eval(nx, ny, nz) * 10 + radius) * clamp;
					double r3 = r2 + 8;
					r2 *= r2;
					r3 *= r3;
					double dist = x2 + y2 + z2;
					if (dist < r2) {
						BlockState state = chunk.getBlockState(mut);
						if (state.isIn(EndTags.GEN_TERRAIN) || state.getMaterial().isReplaceable()) {
							state = y < center.getY() ? WATER : AIR;
							chunk.setBlockState(mut, state, false);
						}
					}
					else if (dist < r3 && y < center.getY()) {
						BlockState state = chunk.getBlockState(mut);
						BlockPos worldPos = mut.add(sx, 0, sz);
						if (!state.isFullCube(world, worldPos) || !state.isSolidBlock(world, worldPos)) {
							chunk.setBlockState(mut, ENDSTONE, false);
						}
					}
				}
			}
		}
		
		return true;
	}
	
	private int getHeight(StructureWorldAccess world, BlockPos pos) {
		int p = ((pos.getX() & 2047) << 11) | (pos.getZ() & 2047);
		int h = heightmap.getOrDefault(p, Byte.MIN_VALUE);
		if (h > Byte.MIN_VALUE) {
			return h;
		}
		
		if (!EndBiomes.getBiomeID(world.getBiome(pos)).equals(biomeID)) {
			heightmap.put(p, (byte) 0);
			return 0;
		}
		
		h = world.getTopY(Type.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
		h = MathHelper.abs(h - center.getY());
		h = h < 8 ? 1 : 0;
		
		heightmap.put(p, (byte) h);
		return h;
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
		return MathHelper.clamp(height, 0, 1);
	}
	
	private void makeBoundingBox() {
		int minX = MHelper.floor(center.getX() - radius - 8);
		int minY = MHelper.floor(center.getX() - depth - 8);
		int minZ = MHelper.floor(center.getZ() - radius - 8);
		int maxX = MHelper.floor(center.getX() + radius + 8);
		int maxY = MHelper.floor(center.getY() + depth + 8);
		int maxZ = MHelper.floor(center.getZ() + radius + 8);
		this.boundingBox = new BlockBox(minX, minY, minZ, maxX, maxY, maxZ);
	}
}