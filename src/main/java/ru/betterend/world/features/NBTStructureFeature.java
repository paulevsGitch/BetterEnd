package ru.betterend.world.features;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

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
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.util.BlocksHelper;
import ru.betterend.world.processors.DestructionStructureProcessor;

public abstract class NBTStructureFeature extends DefaultFeature {
	protected static final DestructionStructureProcessor DESTRUCTION = new DestructionStructureProcessor();
	
	protected abstract Structure getStructure();
	
	protected abstract boolean canSpawn(StructureWorldAccess world, BlockPos pos);
	
	protected abstract BlockRotation getRotation(StructureWorldAccess world, BlockPos pos, Random random);
	
	protected abstract BlockMirror getMirror(StructureWorldAccess world, BlockPos pos, Random random);
	
	protected abstract int getYOffset(Structure structure, StructureWorldAccess world, BlockPos pos, Random random);
	
	protected abstract boolean hasErosion();
	
	protected abstract boolean hasTerrainOverlay();
	
	protected abstract void addProcessors(StructurePlacementData placementData, Random random);
	
	protected BlockPos getGround(StructureWorldAccess world, BlockPos center) {
		return getPosOnSurface(world, center);
	}
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos center, DefaultFeatureConfig featureConfig) {
		center = new BlockPos(((center.getX() >> 4) << 4) | 8, 128, ((center.getZ() >> 4) << 4) | 8);
		center = getGround(world, center);
		
		if (!canSpawn(world, center)) {
			return false;
		}
		
		Structure structure = getStructure();
		BlockRotation rotation = getRotation(world, center, random);
		BlockMirror mirror = getMirror(world, center, random);
		BlockPos offset = Structure.transformAround(structure.getSize(), mirror, rotation, BlockPos.ORIGIN);
		center = center.add(0, getYOffset(structure, world, center, random), 0);
		
		BlockBox bounds = makeBox(center);
		StructurePlacementData placementData = new StructurePlacementData().setRotation(rotation).setMirror(mirror).setBoundingBox(bounds);
		addProcessors(placementData, random);
		structure.place(world, center.add(-offset.getX() * 0.5, 0, -offset.getZ() * 0.5), placementData, random);
		
		int x1 = center.getX();
		int y1 = center.getX();
		int z1 = center.getX();
		int x2 = x1 + offset.getX();
		int y2 = y1 + offset.getY();
		int z2 = z1 + offset.getZ();
		
		boolean erosion = hasErosion();
		boolean overlay = hasTerrainOverlay();

		if (erosion || overlay) {
			Mutable mut = new Mutable();
			for (int x = x1; x <= x2; x++) {
				mut.setX(x);
				for (int z = z1; z <= z2; z++) {
					mut.setZ(z);
					for (int y = y1; y <= y2; y++) {
						mut.setY(y);
						if (bounds.contains(mut)) {
							if (overlay) {
								if (world.getBlockState(mut).isOf(Blocks.END_STONE) && world.isAir(mut.up())) {
									BlockState terrain = world.getBiome(mut).getGenerationSettings().getSurfaceConfig().getTopMaterial();
									BlocksHelper.setWithoutUpdate(world, mut, terrain);
								}
							}
							if (erosion) {
								mut.setY(mut.getY() - 1);
								int down = BlocksHelper.downRay(world, mut, 32);
								if (down > 0) {
									mut.setY(mut.getY() + 1);
									BlockState state = world.getBlockState(mut);
									BlocksHelper.setWithoutUpdate(world, mut, AIR);
									mut.setY(mut.getY() - down);
									BlocksHelper.setWithoutUpdate(world, mut, state);
								}
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
