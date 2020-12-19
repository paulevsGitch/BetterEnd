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
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndStructures;
import ru.betterend.registry.EndTags;
import ru.betterend.util.MHelper;

public class LakePiece extends BasePiece {
	private static final BlockState WATER = Blocks.WATER.getDefaultState();
	private Map<Integer, Integer> heightmap = Maps.newHashMap();
	private OpenSimplexNoise noise1;
	private OpenSimplexNoise noise2;
	private BlockPos center;
	private float radius;
	private float depth;
	private float r2;
	private Identifier biomeID;
	private int seed1;
	private int seed2;
	
	public LakePiece(BlockPos center, float radius, float depth, Random random, Biome biome) {
		super(EndStructures.LAKE_PIECE, random.nextInt());
		this.center = center;
		this.radius = radius;
		this.depth = depth;
		this.r2 = radius * radius;
		this.seed1 = random.nextInt();
		this.seed2 = random.nextInt();
		this.noise1 = new OpenSimplexNoise(this.seed1);
		this.noise2 = new OpenSimplexNoise(this.seed2);
		this.biomeID = EndBiomes.getBiomeID(biome);
		makeBoundingBox();
	}

	public LakePiece(StructureManager manager, CompoundTag tag) {
		super(EndStructures.LAKE_PIECE, tag);
		makeBoundingBox();
	}

	@Override
	protected void toNbt(CompoundTag tag) {
		tag.put("center", NbtHelper.fromBlockPos(center));
		tag.putFloat("radius", radius);
		tag.putFloat("depth", depth);
		tag.putString("biome", biomeID.toString());
		tag.putInt("seed1", seed1);
		tag.putInt("seed2", seed2);
	}

	@Override
	protected void fromNbt(CompoundTag tag) {
		center = NbtHelper.toBlockPos(tag.getCompound("center"));
		radius = tag.getFloat("radius");
		depth = tag.getFloat("depth");
		r2 = radius * radius;
		seed1 = tag.getInt("seed1");
		seed2 = tag.getInt("seed2");
		noise1 = new OpenSimplexNoise(seed1);
		noise2 = new OpenSimplexNoise(seed2);
		biomeID = new Identifier(tag.getString("biome"));
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
						float minY = dist * depth * getHeightClamp(world, 8, px, pz);
						if (minY > 0) {
							minY *= (float) noise1.eval(px * 0.05, pz * 0.05) * 0.3F + 0.7F;
							minY *= (float) noise1.eval(px * 0.1, pz * 0.1) * 0.1F + 0.8F;
							float lerp = minY / 2F;
							if (lerp > 1) {
								lerp = 1;
							}
							minY = MathHelper.lerp(lerp, maxY - minY, 56 - minY);
							pos.setY(maxY);
							while (!chunk.getBlockState(pos).getMaterial().isReplaceable()) {
								pos.setY(maxY ++);
							}
							for (int y = maxY; y >= minY; y--) {
								pos.setY(y);
								BlockState state = chunk.getBlockState(pos);
								if (state.getMaterial().isReplaceable() || state.isIn(EndTags.GEN_TERRAIN)) {
									chunk.setBlockState(pos, y > 56 ? AIR : WATER, false);
								}
								else {
									break;
								}
							}
							maxY = MHelper.randRange(2, 3, random);
							int last = maxY - 1;
							for (int i = 0; i < maxY; i++) {
								pos.setY(pos.getY() - 1);
								BlockState state = chunk.getBlockState(pos);
								if (state.getMaterial().isReplaceable() || state.isIn(EndTags.GEN_TERRAIN)) {
									if (pos.getY() > 56) {
										chunk.setBlockState(pos, AIR, false);
										if (pos.getY() == last) {
											state = world.getBiome(pos.add(sx, 0, sz)).getGenerationSettings().getSurfaceConfig().getTopMaterial();
											chunk.setBlockState(pos.down(), state, false);
										}
									}
									else if (pos.getY() == 56) {
										if (random.nextBoolean()) {
											state = EndBlocks.ENDSTONE_DUST.getDefaultState();
										}
										else {
											state = world.getBiome(pos.add(sx, 0, sz)).getGenerationSettings().getSurfaceConfig().getTopMaterial();
										}
										chunk.setBlockState(pos, state, false);
										
										state = world.getBiome(pos.add(sx, 0, sz)).getGenerationSettings().getSurfaceConfig().getUnderMaterial();
										int count = (int) (noise1.eval((pos.getX() + sx) * 0.1, (pos.getZ() + sz) * 0.1) + 2);//MHelper.randRange(1, 2, random);
										for (int n = 0; n < count; n++) {
											pos.setY(pos.getY() - 1);
											chunk.setBlockState(pos, state, false);
										}
										break;
									}
									else {
										chunk.setBlockState(pos, EndBlocks.ENDSTONE_DUST.getDefaultState(), false);
										
										state = world.getBiome(pos.add(sx, 0, sz)).getGenerationSettings().getSurfaceConfig().getUnderMaterial();
										int count = (int) (noise1.eval((pos.getX() + sx) * 0.1, (pos.getZ() + sz) * 0.1) + 2);//int count = MHelper.randRange(1, 2, random);
										for (int n = 0; n < count; n++) {
											pos.setY(pos.getY() - 1);
											chunk.setBlockState(pos, state, false);
										}
										break;
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
		int p = ((pos.getX() & 2047) << 11) | (pos.getZ() & 2047);
		int h = heightmap.getOrDefault(p, Integer.MIN_VALUE);
		if (h > Integer.MIN_VALUE) {
			return h;
		}
		
		if (!EndBiomes.getBiomeID(world.getBiome(pos)).equals(biomeID)) {
			heightmap.put(p, -20);
			return -20;
		}
		h = world.getTopY(Type.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
		if (h < 57) {
			heightmap.put(p, -20);
			return -20;
		}
		h = MHelper.floor(noise2.eval(pos.getX() * 0.01, pos.getZ() * 0.01) * noise2.eval(pos.getX() * 0.002, pos.getZ() * 0.002) * 8 + 8);
		
		if (h < 0) {
			heightmap.put(p, 0);
			return 0;
		}
		
		heightmap.put(p, h);
		
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
