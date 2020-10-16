package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.StructureWorldAccess;

public abstract class UnderwaterPlantScatter extends ScatterFeature {
	private static final Mutable POS = new Mutable();
	
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
	public boolean canGenerate(StructureWorldAccess world, Random random, BlockPos center, BlockPos blockPos, float radius) {
		return !world.getFluidState(blockPos).isEmpty();
	}
	
	@Override
	protected boolean canSpawn(StructureWorldAccess world, BlockPos pos) {
		return !world.getFluidState(pos).isEmpty();
	}
	
	@Override
	protected boolean getGroundPlant(StructureWorldAccess world, Mutable pos) {
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
	
	private BlockPos getGround(StructureWorldAccess world, Mutable pos) {
		while (pos.getY() < 128 && world.getFluidState(pos).isEmpty()) {
			pos.setY(pos.getY() + 1);
		}
		return pos;
	}
}
