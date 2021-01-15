package ru.betterend.world.structures.piece;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
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
import ru.betterend.util.BlocksHelper;
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
			int x2 = wx - center.getX();
			for (int z = 0; z < 16; z++) {
				mut.setZ(z);
				int wz = z | sz;
				double nz = wz * 0.1;
				int z2 = wz - center.getZ();
				float clamp = getHeightClamp(world, 8, wx, wz);
				if (clamp < 0.01) continue;
				
				double n = noise.eval(nx, nz) * 1.5 + 1.5;
				double x3 = MHelper.pow2(x2 + noise.eval(nx, nz, 100) * 10);
				double z3 = MHelper.pow2(z2 + noise.eval(nx, nz, -100) * 10);
				
				for (int y = minY; y <= maxY; y++) {
					mut.setY((int) (y + n));
					double y2 = MHelper.pow2((y - center.getY()) * aspect);
					double r2 = radius * clamp;
					double r3 = r2 + 8;
					r2 *= r2;
					r3 = r3 * r3 + 100;
					double dist = x3 + y2 + z3;
					if (dist < r2) {
						BlockState state = chunk.getBlockState(mut);
						if (state.isIn(EndTags.GEN_TERRAIN) || state.isAir()) {
							state = mut.getY() < center.getY() ? WATER : AIR;
							chunk.setBlockState(mut, state, false);
						}
					}
					else if (dist <= r3 && mut.getY() < center.getY()) {
						BlockState state = chunk.getBlockState(mut);
						BlockPos worldPos = mut.add(sx, 0, sz);
						if (!state.isFullCube(world, worldPos) && !state.isSolidBlock(world, worldPos)) {
							state = chunk.getBlockState(mut.up());
							if (state.isAir()) {
								state = random.nextBoolean() ? ENDSTONE : world.getBiome(worldPos).getGenerationSettings().getSurfaceConfig().getTopMaterial();
							}
							else {
								state = state.getFluidState().isEmpty() ? ENDSTONE : EndBlocks.ENDSTONE_DUST.getDefaultState();
							}
							chunk.setBlockState(mut, state, false);
						}
					}
				}
			}
		}
		fixWater(world, chunk, mut, random, sx, sz);
		return true;
	}
	
	private void fixWater(StructureWorldAccess world, Chunk chunk, Mutable mut, Random random, int sx, int sz) {
		int minY = this.boundingBox.minY;
		int maxY = this.boundingBox.maxY;
		for (int x = 0; x < 16; x++) {
			mut.setX(x);
			for (int z = 0; z < 16; z++) {
				mut.setZ(z);
				for (int y = minY; y <= maxY; y++) {
					mut.setY(y);
					FluidState state = chunk.getFluidState(mut);
					if (!state.isEmpty()) {
						mut.setY(y - 1);
						if (chunk.getBlockState(mut).isAir()) {
							mut.setY(y + 1);
							
							BlockState bState = chunk.getBlockState(mut);
							if (bState.isAir()) {
								bState = random.nextBoolean() ? ENDSTONE : world.getBiome(mut.add(sx, 0, sz)).getGenerationSettings().getSurfaceConfig().getTopMaterial();
							}
							else {
								bState = bState.getFluidState().isEmpty() ? ENDSTONE : EndBlocks.ENDSTONE_DUST.getDefaultState();
							}
							
							mut.setY(y);
							
							makeEndstonePillar(chunk, mut, bState);
						}
						else if (x > 1 && x < 15 && z > 1 && z < 15) {
							mut.setY(y);
							for (Direction dir: BlocksHelper.HORIZONTAL) {
								BlockPos wPos = mut.add(dir.getOffsetX(), 0, dir.getOffsetZ());
								if (chunk.getBlockState(wPos).isAir()) {
									mut.setY(y + 1);
									BlockState bState = chunk.getBlockState(mut);
									if (bState.isAir()) {
										bState = random.nextBoolean() ? ENDSTONE : world.getBiome(mut.add(sx, 0, sz)).getGenerationSettings().getSurfaceConfig().getTopMaterial();
									}
									else {
										bState = bState.getFluidState().isEmpty() ? ENDSTONE : EndBlocks.ENDSTONE_DUST.getDefaultState();
									}
									mut.setY(y);
									makeEndstonePillar(chunk, mut, bState);
									break;
								}
							}
						}
						else if (chunk.getBlockState(mut.move(Direction.UP)).isAir()) {
							chunk.getFluidTickScheduler().schedule(mut.move(Direction.DOWN), state.getFluid(), 0);
						}
					}
				}
			}
		}
	}
	
	private void makeEndstonePillar(Chunk chunk, Mutable mut, BlockState terrain) {
		chunk.setBlockState(mut, terrain, false);
		mut.setY(mut.getY() - 1);
		while (!chunk.getFluidState(mut).isEmpty()) {
			chunk.setBlockState(mut, ENDSTONE, false);
			mut.setY(mut.getY() - 1);
		}
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
		
		/*h = world.getTopY(Type.WORLD_SURFACE, pos.getX(), pos.getZ());
		if (!world.getBlockState(new BlockPos(pos.getX(), h - 1, pos.getZ())).getFluidState().isEmpty()) {
			heightmap.put(p, (byte) 0);
			return 0;
		}*/
		
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
		int maxY = MHelper.floor(center.getY() + depth);
		int maxZ = MHelper.floor(center.getZ() + radius + 8);
		this.boundingBox = new BlockBox(minX, minY, minZ, maxX, maxY, maxZ);
	}
}