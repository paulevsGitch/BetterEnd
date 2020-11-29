package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.util.StructureHelper;

public class CrashedShipFeature extends NBTStructureFeature {
	private static final String STRUCTURE_PATH = "/data/minecraft/structures/end_city/ship.nbt";

	@Override
	protected Structure getStructure(StructureWorldAccess world, BlockPos pos, Random random) {
		return StructureHelper.readStructure(STRUCTURE_PATH);
	}

	@Override
	protected boolean canSpawn(StructureWorldAccess world, BlockPos pos, Random random) {
		int cx = pos.getX() >> 4;
		int cz = pos.getZ() >> 4;
		return ((cx + cz) & 1) == 0 && pos.getY() > 58 && world.getBlockState(pos.down()).isIn(EndTags.GEN_TERRAIN);
	}

	@Override
	protected BlockRotation getRotation(StructureWorldAccess world, BlockPos pos, Random random) {
		return BlockRotation.random(random);
	}

	@Override
	protected BlockMirror getMirror(StructureWorldAccess world, BlockPos pos, Random random) {
		return BlockMirror.values()[random.nextInt(3)];
	}

	@Override
	protected int getYOffset(Structure structure, StructureWorldAccess world, BlockPos pos, Random random) {
		int min = structure.getSize().getY() >> 3;
		int max = structure.getSize().getY() >> 2;
		return -MHelper.randRange(min, max, random);
	}

	@Override
	protected TerrainMerge getTerrainMerge(StructureWorldAccess world, BlockPos pos, Random random) {
		return TerrainMerge.NONE;
	}
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos center, DefaultFeatureConfig featureConfig) {
		center = new BlockPos(((center.getX() >> 4) << 4) | 8, 128, ((center.getZ() >> 4) << 4) | 8);
		center = getGround(world, center);
		
		if (!canSpawn(world, center, random)) {
			return false;
		}
		
		Structure structure = getStructure(world, center, random);
		BlockRotation rotation = getRotation(world, center, random);
		BlockMirror mirror = getMirror(world, center, random);
		BlockPos offset = Structure.transformAround(structure.getSize(), mirror, rotation, BlockPos.ORIGIN);
		center = center.add(0, getYOffset(structure, world, center, random) + 0.5, 0);
		
		BlockBox bounds = makeBox(center);
		StructurePlacementData placementData = new StructurePlacementData().setRotation(rotation).setMirror(mirror).setBoundingBox(bounds);
		addStructureData(placementData);
		center = center.add(-offset.getX() * 0.5, 0, -offset.getZ() * 0.5);
		structure.place(world, center, placementData, random);
		
		StructureHelper.erodeIntense(world, bounds, random);
		BlocksHelper.fixBlocks(world, new BlockPos(bounds.minX, bounds.minY, bounds.minZ), new BlockPos(bounds.maxX, bounds.maxY, bounds.maxZ));
		
		return true;
	}

	@Override
	protected void addStructureData(StructurePlacementData data) {
		data.addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS).setIgnoreEntities(true);
	}
}
