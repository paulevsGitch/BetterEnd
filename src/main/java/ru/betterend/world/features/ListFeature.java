package ru.betterend.world.features;

import java.util.List;
import java.util.Random;

import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.BlockMirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import ru.betterend.registry.EndTags;
import ru.betterend.util.StructureHelper;

public class ListFeature extends NBTStructureFeature {
	private final List<StructureInfo> list;
	private StructureInfo selected;

	public ListFeature(List<StructureInfo> list) {
		this.list = list;
	}

	@Override
	protected Structure getStructure(WorldGenLevel world, BlockPos pos, Random random) {
		selected = list.get(random.nextInt(list.size()));
		return selected.getStructure();
	}

	@Override
	protected boolean canSpawn(WorldGenLevel world, BlockPos pos, Random random) {
		int cx = pos.getX() >> 4;
		int cz = pos.getZ() >> 4;
		return ((cx + cz) & 1) == 0 && pos.getY() > 58 && world.getBlockState(pos.below()).isIn(EndTags.GEN_TERRAIN);
	}

	@Override
	protected Rotation getRotation(WorldGenLevel world, BlockPos pos, Random random) {
		return Rotation.random(random);
	}

	@Override
	protected BlockMirror getMirror(WorldGenLevel world, BlockPos pos, Random random) {
		return BlockMirror.values()[random.nextInt(3)];
	}

	@Override
	protected int getYOffset(Structure structure, WorldGenLevel world, BlockPos pos, Random random) {
		return selected.offsetY;
	}

	@Override
	protected TerrainMerge getTerrainMerge(WorldGenLevel world, BlockPos pos, Random random) {
		return selected.terrainMerge;
	}

	@Override
	protected void addStructureData(StructurePlacementData data) {
	}

	public static final class StructureInfo {
		public final TerrainMerge terrainMerge;
		public final String structurePath;
		public final int offsetY;

		private Structure structure;

		public StructureInfo(String structurePath, int offsetY, TerrainMerge terrainMerge) {
			this.terrainMerge = terrainMerge;
			this.structurePath = structurePath;
			this.offsetY = offsetY;
		}

		public Structure getStructure() {
			if (structure == null) {
				structure = StructureHelper.readStructure(structurePath);
			}
			return structure;
		}
	}
}
