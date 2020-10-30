package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndTags;
import ru.betterend.util.MHelper;

public class EternalPortalFeature extends NBTStructureFeature {
	private static final Structure PORTAL = readStructure(BetterEnd.makeID("portal/eternal_portal"));
	
	@Override
	protected Structure getStructure() {
		return PORTAL;
	}

	@Override
	protected boolean canSpawn(StructureWorldAccess world, BlockPos pos) {
		if (world.getBlockState(pos.down()).isIn(EndTags.END_GROUND)) {
			return world.getBlockState(pos.north(2)).getMaterial().isReplaceable()
					&& world.getBlockState(pos.south(2)).getMaterial().isReplaceable()
					&& world.getBlockState(pos.east(2)).getMaterial().isReplaceable()
					&& world.getBlockState(pos.west(2)).getMaterial().isReplaceable();
		}
		return false;
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
		return -3;
	}
	
	@Override
	protected void addProcessors(StructurePlacementData placementData, Random random) {
		DESTRUCTION.setChance(MHelper.randRange(4, 16, random));
		placementData.addProcessor(DESTRUCTION);
	}

	@Override
	protected boolean hasErosion() {
		return true;
	}

	@Override
	protected boolean hasTerrainOverlay() {
		return true;
	}
}
