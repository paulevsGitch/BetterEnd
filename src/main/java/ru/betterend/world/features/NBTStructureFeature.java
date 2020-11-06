package ru.betterend.world.features;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.world.processors.DestructionStructureProcessor;

public abstract class NBTStructureFeature extends DefaultFeature {
	protected static final DestructionStructureProcessor DESTRUCTION = new DestructionStructureProcessor();
	
	protected abstract Structure getStructure(StructureWorldAccess world, BlockPos pos, Random random);
	
	protected abstract boolean canSpawn(StructureWorldAccess world, BlockPos pos, Random random);
	
	protected abstract BlockRotation getRotation(StructureWorldAccess world, BlockPos pos, Random random);
	
	protected abstract BlockMirror getMirror(StructureWorldAccess world, BlockPos pos, Random random);
	
	protected abstract int getYOffset(Structure structure, StructureWorldAccess world, BlockPos pos, Random random);
	
	protected abstract boolean adjustSurface(StructureWorldAccess world, BlockPos pos, Random random);
	
	protected BlockPos getGround(StructureWorldAccess world, BlockPos center) {
		Biome biome = world.getBiome(center);
		Identifier id = EndBiomes.getBiomeID(biome);
		if (id.getNamespace().contains("moutain") || id.getNamespace().contains("lake")) {
			int y = getAverageY(world, center);
			return new BlockPos(center.getX(), y, center.getZ());
		}
		else {
			int y = getAverageYWG(world, center);
			return new BlockPos(center.getX(), y, center.getZ());
		}
	}
	
	protected int getAverageY(StructureWorldAccess world, BlockPos center) {
		int y = getYOnSurface(world, center.getX(), center.getZ());
		y += getYOnSurface(world, center.getX() - 2, center.getZ() - 2);
		y += getYOnSurface(world, center.getX() + 2, center.getZ() - 2);
		y += getYOnSurface(world, center.getX() - 2, center.getZ() + 2);
		y += getYOnSurface(world, center.getX() + 2, center.getZ() + 2);
		return y / 5;
	}
	
	protected int getAverageYWG(StructureWorldAccess world, BlockPos center) {
		int y = getYOnSurfaceWG(world, center.getX(), center.getZ());
		y += getYOnSurfaceWG(world, center.getX() - 2, center.getZ() - 2);
		y += getYOnSurfaceWG(world, center.getX() + 2, center.getZ() - 2);
		y += getYOnSurfaceWG(world, center.getX() - 2, center.getZ() + 2);
		y += getYOnSurfaceWG(world, center.getX() + 2, center.getZ() + 2);
		return y / 5;
	}
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos center, DefaultFeatureConfig featureConfig) {
		center = new BlockPos(((center.getX() >> 4) << 4) | 8, 128, ((center.getZ() >> 4) << 4) | 8);
		center = getGround(world, center);
		
		if (!canSpawn(world, center, random)) {
			return false;
		}
		
		int posY = center.getY() + 1;
		Structure structure = getStructure(world, center, random);
		BlockRotation rotation = getRotation(world, center, random);
		BlockMirror mirror = getMirror(world, center, random);
		BlockPos offset = Structure.transformAround(structure.getSize(), mirror, rotation, BlockPos.ORIGIN);
		center = center.add(0, getYOffset(structure, world, center, random), 0);
		
		BlockBox bounds = makeBox(center);
		StructurePlacementData placementData = new StructurePlacementData().setRotation(rotation).setMirror(mirror).setBoundingBox(bounds);
		center = center.add(-offset.getX() * 0.5, 1, -offset.getZ() * 0.5);
		structure.place(world, center, placementData, random);
		
		if (adjustSurface(world, center, random)) {
			Mutable mut = new Mutable();
			int x1 = center.getX();
			int z1 = center.getZ();
			int x2 = x1 + offset.getX();
			int z2 = z1 + offset.getZ();
			
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
					mut.setY(posY);
					BlockState state = world.getBlockState(mut);
					if (!state.isIn(EndTags.GEN_TERRAIN) && state.isSideSolidFullSquare(world, mut, Direction.DOWN)) {
						for (int i = 0; i < 10; i--) {
							mut.setY(mut.getY() - 1);
							BlockState stateSt = world.getBlockState(mut);
							if (!stateSt.isIn(EndTags.GEN_TERRAIN)) {
								SurfaceConfig config = world.getBiome(mut).getGenerationSettings().getSurfaceConfig();
								boolean isTop = mut.getY() == surfMax && state.getMaterial().blocksLight();
								BlockState top = isTop ? config.getTopMaterial() : config.getUnderMaterial();
								BlocksHelper.setWithoutUpdate(world, mut, top);
							}
							else {
								if (stateSt.isIn(EndTags.END_GROUND) && state.getMaterial().blocksLight()) {
									SurfaceConfig config = world.getBiome(mut).getGenerationSettings().getSurfaceConfig();
									BlocksHelper.setWithoutUpdate(world, mut, config.getUnderMaterial());
								}
								break;
							}
						}
					}
				}
			}
		}
		
		return true;
	}
	
	private BlockBox makeBox(BlockPos pos) {
		int sx = ((pos.getX() >> 4) << 4) - 16;
		int sz = ((pos.getZ() >> 4) << 4) - 16;
		int ex = sx + 47;
		int ez = sz + 47;
		return BlockBox.create(sx, 0, sz, ex, 255, ez);
	}
	
	protected static Structure readStructure(Identifier resource) {
		String ns = resource.getNamespace();
		String nm = resource.getPath();

		try {
			InputStream inputstream = MinecraftServer.class.getResourceAsStream("/data/" + ns + "/structures/" + nm + ".nbt");
			return readStructureFromStream(inputstream);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private static Structure readStructureFromStream(InputStream stream) throws IOException {
		CompoundTag nbttagcompound = NbtIo.readCompressed(stream);

		Structure template = new Structure();
		template.fromTag(nbttagcompound);

		return template;
	}
}
