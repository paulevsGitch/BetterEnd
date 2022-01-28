package ru.betterend.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import ru.betterend.util.GlobalState;

import java.util.Random;

public abstract class UnderwaterPlantScatter extends ScatterFeature {
	public UnderwaterPlantScatter(int radius) {
		super(radius);
	}
	
	@Override
	protected BlockPos getCenterGround(WorldGenLevel world, BlockPos pos) {
		final MutableBlockPos POS = GlobalState.stateForThread().POS;
		POS.setX(pos.getX());
		POS.setZ(pos.getZ());
		POS.setY(0);
		return getGround(world, POS).immutable();
	}
	
	@Override
	public boolean canGenerate(WorldGenLevel world, Random random, BlockPos center, BlockPos blockPos, float radius) {
		return world.getBlockState(blockPos).is(Blocks.WATER);
	}
	
	@Override
	protected boolean canSpawn(WorldGenLevel world, BlockPos pos) {
		return world.getBlockState(pos).is(Blocks.WATER);
	}
	
	@Override
	protected boolean getGroundPlant(WorldGenLevel world, MutableBlockPos pos) {
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
	
	private BlockPos getGround(WorldGenLevel world, MutableBlockPos pos) {
		while (pos.getY() < 128 && world.getFluidState(pos).isEmpty()) {
			pos.setY(pos.getY() + 1);
		}
		return pos;
	}
}
