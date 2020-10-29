package ru.betterend.world.features;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

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
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.util.BlocksHelper;

public abstract class NBTStructureFeature extends DefaultFeature {
	protected abstract Structure getStructure();
	
	protected abstract boolean canSpawn(StructureWorldAccess world, BlockPos pos);
	
	protected abstract BlockRotation getRotation(StructureWorldAccess world, BlockPos pos, Random random);
	
	protected abstract BlockMirror getMirror(StructureWorldAccess world, BlockPos pos, Random random);
	
	protected abstract int getYOffset(Structure structure, StructureWorldAccess world, BlockPos pos, Random random);
	
	protected BlockPos getGround(StructureWorldAccess world, BlockPos center) {
		return getPosOnSurfaceWG(world, center);
	}
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos center, DefaultFeatureConfig featureConfig) {
		BlockPos a = center;
		center = new BlockPos(((center.getX() >> 4) << 4) | 8, 128, ((center.getZ() >> 4) << 4) | 8);
		center = getGround(world, center);
		
		if (!canSpawn(world, center)) {
			return false;
		}
		
		Structure structure = getStructure();
		BlockRotation rotation = getRotation(world, center, random);
		BlockMirror mirror = getMirror(world, center, random);
		BlockPos size = structure.getSize().rotate(rotation);
		double px = size.getX();
		double pz = size.getZ();
		if (mirror == BlockMirror.FRONT_BACK)
			px = -px;
		if (mirror == BlockMirror.LEFT_RIGHT)
			pz = -pz;
		
		center = center.add(-px * 0.5, 0, -pz * 0.5);
		//center.subtract(structure.getRotatedSize(rotation));
		int offset = getYOffset(structure, world, center, random);
		center = center.add(0, offset, 0);
		StructurePlacementData placementData = new StructurePlacementData()
				.setRotation(rotation)
				.setMirror(mirror)
				.setPosition(center)
				.setBoundingBox(BlockBox.create(center.getX() - 8 - 15, 0, center.getZ() - 8 - 15, center.getX() + 8 + 15, 128, center.getZ() + 8 + 15));
		structure.place(world, center, placementData, random);
		BlocksHelper.setWithoutUpdate(world, new BlockPos(a.getX(), center.getY() + 10, a.getZ()), Blocks.DIAMOND_BLOCK);
		BlocksHelper.setWithoutUpdate(world, center, Blocks.GOLD_BLOCK);
		
		return true;
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
