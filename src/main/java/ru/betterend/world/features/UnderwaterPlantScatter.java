package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.StructureWorldAccess;

public abstract class UnderwaterPlantScatter extends ScatterFeature {
	private static final MutableBlockPos POS = new MutableBlockPos();

	public UnderwaterPlantScatter(int radius) {
		super(radius);
	}

	@Override
	protected BlockPos getCenterGround(StructureWorldAccess world, BlockPos pos) {
		POS.setX(pos.getX());
		POS.setZ(pos.getZ());
		POS.setY(0);
		return getGround(world, POS).toImmutable();
	}

	@Override
	public boolean canGenerate(StructureWorldAccess world, Random random, BlockPos center, BlockPos blockPos,
			float radius) {
		return world.getBlockState(blockPos).is(Blocks.WATER);
	}

	@Override
	protected boolean canSpawn(StructureWorldAccess world, BlockPos pos) {
		return world.getBlockState(pos).is(Blocks.WATER);
	}

	@Override
	protected boolean getGroundPlant(StructureWorldAccess world, MutableBlockPos pos) {
		return getGround(world, pos).getY() < 128;
	}

	@Override
	protected int getYOffset() {
		return -5;
	}

	@Override
	protected int getChance() {
		return 5;
	}

	private BlockPos getGround(StructureWorldAccess world, MutableBlockPos pos) {
		while (pos.getY() < 128 && world.getFluidState(pos).isEmpty()) {
			pos.setY(pos.getY() + 1);
		}
		return pos;
	}
}
