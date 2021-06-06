package ru.betterend.world.features;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderConfiguration;
import ru.bclib.api.TagAPI;
import ru.bclib.util.BlocksHelper;
import ru.betterend.registry.EndBiomes;
import ru.betterend.util.BlockFixer;
import ru.betterend.world.processors.DestructionStructureProcessor;

public abstract class NBTStructureFeature extends DefaultFeature {
	protected static final DestructionStructureProcessor DESTRUCTION = new DestructionStructureProcessor();

	protected abstract StructureTemplate getStructure(WorldGenLevel world, BlockPos pos, Random random);

	protected abstract boolean canSpawn(WorldGenLevel world, BlockPos pos, Random random);

	protected abstract Rotation getRotation(WorldGenLevel world, BlockPos pos, Random random);

	protected abstract Mirror getMirror(WorldGenLevel world, BlockPos pos, Random random);

	protected abstract int getYOffset(StructureTemplate structure, WorldGenLevel world, BlockPos pos, Random random);

	protected abstract TerrainMerge getTerrainMerge(WorldGenLevel world, BlockPos pos, Random random);

	protected abstract void addStructureData(StructurePlaceSettings data);

	protected BlockPos getGround(WorldGenLevel world, BlockPos center) {
		Biome biome = world.getBiome(center);
		ResourceLocation id = EndBiomes.getBiomeID(biome);
		if (id.getNamespace().contains("moutain") || id.getNamespace().contains("lake")) {
			int y = getAverageY(world, center);
			return new BlockPos(center.getX(), y, center.getZ());
		} else {
			int y = getAverageYWG(world, center);
			return new BlockPos(center.getX(), y, center.getZ());
		}
	}

	protected int getAverageY(WorldGenLevel world, BlockPos center) {
		int y = getYOnSurface(world, center.getX(), center.getZ());
		y += getYOnSurface(world, center.getX() - 2, center.getZ() - 2);
		y += getYOnSurface(world, center.getX() + 2, center.getZ() - 2);
		y += getYOnSurface(world, center.getX() - 2, center.getZ() + 2);
		y += getYOnSurface(world, center.getX() + 2, center.getZ() + 2);
		return y / 5;
	}

	protected int getAverageYWG(WorldGenLevel world, BlockPos center) {
		int y = getYOnSurfaceWG(world, center.getX(), center.getZ());
		y += getYOnSurfaceWG(world, center.getX() - 2, center.getZ() - 2);
		y += getYOnSurfaceWG(world, center.getX() + 2, center.getZ() - 2);
		y += getYOnSurfaceWG(world, center.getX() - 2, center.getZ() + 2);
		y += getYOnSurfaceWG(world, center.getX() + 2, center.getZ() + 2);
		return y / 5;
	}

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, Random random, BlockPos center,
			NoneFeatureConfiguration featureConfig) {
		center = new BlockPos(((center.getX() >> 4) << 4) | 8, 128, ((center.getZ() >> 4) << 4) | 8);
		center = getGround(world, center);

		if (!canSpawn(world, center, random)) {
			return false;
		}

		int posY = center.getY() + 1;
		StructureTemplate structure = getStructure(world, center, random);
		Rotation rotation = getRotation(world, center, random);
		Mirror mirror = getMirror(world, center, random);
		BlockPos offset = StructureTemplate.transform(structure.getSize(), mirror, rotation, BlockPos.ZERO);
		center = center.offset(0, getYOffset(structure, world, center, random) + 0.5, 0);

		BoundingBox bounds = makeBox(center);
		StructurePlaceSettings placementData = new StructurePlaceSettings().setRotation(rotation).setMirror(mirror)
				.setBoundingBox(bounds);
		addStructureData(placementData);
		center = center.offset(-offset.getX() * 0.5, 0, -offset.getZ() * 0.5);
		structure.placeInWorldChunk(world, center, placementData, random);

		TerrainMerge merge = getTerrainMerge(world, center, random);
		int x1 = center.getX();
		int z1 = center.getZ();
		int x2 = x1 + offset.getX();
		int z2 = z1 + offset.getZ();
		if (merge != TerrainMerge.NONE) {
			MutableBlockPos mut = new MutableBlockPos();

			if (x2 < x1) {
				int a = x1;
				x1 = x2;
				x2 = a;
			}

			if (z2 < z1) {
				int a = z1;
				z1 = z2;
				z2 = a;
			}

			int surfMax = posY - 1;
			for (int x = x1; x <= x2; x++) {
				mut.setX(x);
				for (int z = z1; z <= z2; z++) {
					mut.setZ(z);
					mut.setY(surfMax);
					BlockState state = world.getBlockState(mut);
					if (!state.is(TagAPI.GEN_TERRAIN) && state.isFaceSturdy(world, mut, Direction.DOWN)) {
						for (int i = 0; i < 10; i++) {
							mut.setY(mut.getY() - 1);
							BlockState stateSt = world.getBlockState(mut);
							if (!stateSt.is(TagAPI.GEN_TERRAIN)) {
								if (merge == TerrainMerge.SURFACE) {
									SurfaceBuilderConfiguration config = world.getBiome(mut).getGenerationSettings().getSurfaceBuilderConfig();
									boolean isTop = mut.getY() == surfMax && state.getMaterial().isSolidBlocking();
									BlockState top = isTop ? config.getTopMaterial() : config.getUnderMaterial();
									BlocksHelper.setWithoutUpdate(world, mut, top);
								}
								else {
									BlocksHelper.setWithoutUpdate(world, mut, state);
								}
							}
							else {
								if (stateSt.is(TagAPI.END_GROUND) && state.getMaterial().isSolidBlocking()) {
									if (merge == TerrainMerge.SURFACE) {
										SurfaceBuilderConfiguration config = world.getBiome(mut).getGenerationSettings().getSurfaceBuilderConfig();
										BlocksHelper.setWithoutUpdate(world, mut, config.getUnderMaterial());
									}
									else {
										BlocksHelper.setWithoutUpdate(world, mut, state);
									}
								}
								break;
							}
						}
					}
				}
			}
		}
		BlockFixer.fixBlocks(world, new BlockPos(x1, center.getY(), z1), new BlockPos(x2, center.getY() + offset.getY(), z2));

		return true;
	}

	protected BoundingBox makeBox(BlockPos pos) {
		int sx = ((pos.getX() >> 4) << 4) - 16;
		int sz = ((pos.getZ() >> 4) << 4) - 16;
		int ex = sx + 47;
		int ez = sz + 47;
		return BoundingBox.createProper(sx, 0, sz, ex, 255, ez);
	}

	protected static StructureTemplate readStructure(ResourceLocation resource) {
		String ns = resource.getNamespace();
		String nm = resource.getPath();

		try {
			InputStream inputstream = MinecraftServer.class.getResourceAsStream("/data/" + ns + "/structures/" + nm + ".nbt");
			return readStructureFromStream(inputstream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static StructureTemplate readStructureFromStream(InputStream stream) throws IOException {
		CompoundTag nbttagcompound = NbtIo.readCompressed(stream);

		StructureTemplate template = new StructureTemplate();
		template.load(nbttagcompound);

		return template;
	}

	public static enum TerrainMerge {
		NONE, SURFACE, OBJECT;

		public static TerrainMerge getFromString(String type) {
			if (type.equals("surface")) {
				return SURFACE;
			} else if (type.equals("object")) {
				return OBJECT;
			} else {
				return NONE;
			}
		}
	}
}
