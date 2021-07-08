package ru.betterend.world.structures.piece;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.FluidState;
import ru.bclib.api.BiomeAPI;
import ru.bclib.api.TagAPI;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndStructures;

import java.util.Map;
import java.util.Random;

public class LakePiece extends BasePiece {
	private static final BlockState ENDSTONE = Blocks.END_STONE.defaultBlockState();
	private static final BlockState WATER = Blocks.WATER.defaultBlockState();
	private Map<Integer, Byte> heightmap = Maps.newHashMap();
	private OpenSimplexNoise noise;
	private BlockPos center;
	private float radius;
	private float aspect;
	private float depth;
	private int seed;

	private ResourceLocation biomeID;

	public LakePiece(BlockPos center, float radius, float depth, Random random, Biome biome) {
		super(EndStructures.LAKE_PIECE, random.nextInt(), null);
		this.center = center;
		this.radius = radius;
		this.depth = depth;
		this.seed = random.nextInt();
		this.noise = new OpenSimplexNoise(this.seed);
		this.aspect = radius / depth;
		this.biomeID = BiomeAPI.getBiomeID(biome);
		makeBoundingBox();
	}

	public LakePiece(ServerLevel serverLevel, CompoundTag tag) {
		super(EndStructures.LAKE_PIECE, tag);
		makeBoundingBox();
	}

	@Override
	protected void addAdditionalSaveData(ServerLevel serverLevel, CompoundTag tag) {
		tag.put("center", NbtUtils.writeBlockPos(center));
		tag.putFloat("radius", radius);
		tag.putFloat("depth", depth);
		tag.putInt("seed", seed);
		tag.putString("biome", biomeID.toString());
	}

	@Override
	protected void fromNbt(CompoundTag tag) {
		center = NbtUtils.readBlockPos(tag.getCompound("center"));
		radius = tag.getFloat("radius");
		depth = tag.getFloat("depth");
		seed = tag.getInt("seed");
		noise = new OpenSimplexNoise(seed);
		aspect = radius / depth;
		biomeID = new ResourceLocation(tag.getString("biome"));
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager arg, ChunkGenerator chunkGenerator, Random random, BoundingBox blockBox, ChunkPos chunkPos, BlockPos blockPos) {
		int minY = this.boundingBox.minY();
		int maxY = this.boundingBox.maxY();
		int sx = SectionPos.sectionToBlockCoord(chunkPos.x);
		int sz = SectionPos.sectionToBlockCoord(chunkPos.z);
		MutableBlockPos mut = new MutableBlockPos();
		ChunkAccess chunk = world.getChunk(chunkPos.x, chunkPos.z);
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
				double x3 = MHelper.sqr(x2 + noise.eval(nx, nz, 100) * 10);
				double z3 = MHelper.sqr(z2 + noise.eval(nx, nz, -100) * 10);

				for (int y = minY; y <= maxY; y++) {
					mut.setY((int) (y + n));
					double y2 = MHelper.sqr((y - center.getY()) * aspect);
					double r2 = radius * clamp;
					double r3 = r2 + 8;
					r2 *= r2;
					r3 = r3 * r3 + 100;
					double dist = x3 + y2 + z3;
					if (dist < r2) {
						BlockState state = chunk.getBlockState(mut);
						if (state.is(TagAPI.GEN_TERRAIN) || state.isAir()) {
							state = mut.getY() < center.getY() ? WATER : CAVE_AIR;
							chunk.setBlockState(mut, state, false);
						}
					}
					else if (dist <= r3 && mut.getY() < center.getY()) {
						BlockState state = chunk.getBlockState(mut);
						BlockPos worldPos = mut.offset(sx, 0, sz);
						if (!state.isCollisionShapeFullBlock(world, worldPos) && !state.isRedstoneConductor(world, worldPos)) {
							state = chunk.getBlockState(mut.above());
							if (state.isAir()) {
								state = random.nextBoolean() ? ENDSTONE : world.getBiome(worldPos).getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
							}
							else {
								state = state.getFluidState().isEmpty() ? ENDSTONE : EndBlocks.ENDSTONE_DUST.defaultBlockState();
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

	private void fixWater(WorldGenLevel world, ChunkAccess chunk, MutableBlockPos mut, Random random, int sx, int sz) {
		int minY = this.boundingBox.minY();
		int maxY = this.boundingBox.maxY();
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
								bState = random.nextBoolean() ? ENDSTONE : world.getBiome(mut.offset(sx, 0, sz)).getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
							}
							else {
								bState = bState.getFluidState().isEmpty() ? ENDSTONE : EndBlocks.ENDSTONE_DUST.defaultBlockState();
							}

							mut.setY(y);

							makeEndstonePillar(chunk, mut, bState);
						}
						else if (x > 1 && x < 15 && z > 1 && z < 15) {
							mut.setY(y);
							for (Direction dir : BlocksHelper.HORIZONTAL) {
								BlockPos wPos = mut.offset(dir.getStepX(), 0, dir.getStepZ());
								if (chunk.getBlockState(wPos).isAir()) {
									mut.setY(y + 1);
									BlockState bState = chunk.getBlockState(mut);
									if (bState.isAir()) {
										bState = random.nextBoolean() ? ENDSTONE : world.getBiome(mut.offset(sx, 0, sz)).getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
									}
									else {
										bState = bState.getFluidState().isEmpty() ? ENDSTONE : EndBlocks.ENDSTONE_DUST.defaultBlockState();
									}
									mut.setY(y);
									makeEndstonePillar(chunk, mut, bState);
									break;
								}
							}
						}
						else if (chunk.getBlockState(mut.move(Direction.UP)).isAir()) {
							chunk.getLiquidTicks().scheduleTick(mut.move(Direction.DOWN), state.getType(), 0);
						}
					}
				}
			}
		}
	}

	private void makeEndstonePillar(ChunkAccess chunk, MutableBlockPos mut, BlockState terrain) {
		chunk.setBlockState(mut, terrain, false);
		mut.setY(mut.getY() - 1);
		while (!chunk.getFluidState(mut).isEmpty()) {
			chunk.setBlockState(mut, ENDSTONE, false);
			mut.setY(mut.getY() - 1);
		}
	}

	private int getHeight(WorldGenLevel world, BlockPos pos) {
		int p = ((pos.getX() & 2047) << 11) | (pos.getZ() & 2047);
		int h = heightmap.getOrDefault(p, Byte.MIN_VALUE);
		if (h > Byte.MIN_VALUE) {
			return h;
		}

		if (!BiomeAPI.getBiomeID(world.getBiome(pos)).equals(biomeID)) {
			heightmap.put(p, (byte) 0);
			return 0;
		}

		h = world.getHeight(Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
		h = Mth.abs(h - center.getY());
		h = h < 8 ? 1 : 0;

		heightmap.put(p, (byte) h);
		return h;
	}

	private float getHeightClamp(WorldGenLevel world, int radius, int posX, int posZ) {
		MutableBlockPos mut = new MutableBlockPos();
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
		return Mth.clamp(height, 0, 1);
	}

	private void makeBoundingBox() {
		int minX = MHelper.floor(center.getX() - radius - 8);
		int minY = MHelper.floor(center.getY() - depth - 8);
		int minZ = MHelper.floor(center.getZ() - radius - 8);
		int maxX = MHelper.floor(center.getX() + radius + 8);
		int maxY = MHelper.floor(center.getY() + depth);
		int maxZ = MHelper.floor(center.getZ() + radius + 8);
		this.boundingBox = new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}
}