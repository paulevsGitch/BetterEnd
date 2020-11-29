package ru.betterend.world.features;

import java.util.List;
import java.util.Random;

import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.registry.EndTags;

public class ListFeature extends NBTStructureFeature {
	private final List<StructureInfo> list;
	private StructureInfo selected;
	
	public ListFeature(List<StructureInfo> list) {
		this.list = list;
	}
	
	@Override
	protected Structure getStructure(StructureWorldAccess world, BlockPos pos, Random random) {
		selected = list.get(random.nextInt(list.size()));
		return selected.structure;
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
		return selected.offsetY;
	}

	@Override
	protected TerrainMerge getTerrainMerge(StructureWorldAccess world, BlockPos pos, Random random) {
		return selected.terrainMerge;
	}
	
	@Override
	protected void addStructureData(StructurePlacementData data) {}
	
	public static final class StructureInfo {
		public final TerrainMerge terrainMerge;
		public final Structure structure;
		public final int offsetY;
		
		public StructureInfo(Structure structure, int offsetY, TerrainMerge terrainMerge) {
			this.terrainMerge = terrainMerge;
			this.structure = structure;
			this.offsetY = offsetY;
		}
	}
}
