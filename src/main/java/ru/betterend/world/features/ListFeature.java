package ru.betterend.world.features;

import java.util.List;
import java.util.Random;

import net.minecraft.structure.Structure;
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
		return pos.getY() > 58 && world.getBlockState(pos.down()).isIn(EndTags.GEN_TERRAIN);
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
	protected boolean adjustSurface(StructureWorldAccess world, BlockPos pos, Random random) {
		return selected.adjustTerrain;
	}

	public static final class StructureInfo {
		public final boolean adjustTerrain;
		public final Structure structure;
		public final int offsetY;
		
		public StructureInfo(Structure structure, int offsetY, boolean adjustTerrain) {
			this.adjustTerrain = adjustTerrain;
			this.structure = structure;
			this.offsetY = offsetY;
		}
	}
}
