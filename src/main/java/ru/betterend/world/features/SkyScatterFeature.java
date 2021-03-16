package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public abstract class SkyScatterFeature extends ScatterFeature {
	public SkyScatterFeature(int radius) {
		super(radius);
	}
	
	@Override
	protected int getChance() {
		return 10;
	}

	@Override
	public boolean canGenerate(StructureWorldAccess world, Random random, BlockPos center, BlockPos blockPos, float radius) {
		if (!world.isAir(blockPos)) {
			return false;
		}
		
		for (Direction dir: BlocksHelper.HORIZONTAL) {
			if (!world.isAir(blockPos.offset(dir))) {
				return false;
			}
		}
		
		int maxD = getYOffset() + 2;
		int maxV = getYOffset() - 2;
		
		return BlocksHelper.upRay(world, blockPos, maxD) > maxV && BlocksHelper.downRay(world, blockPos, maxD) > maxV;
	}
	
	@Override
	protected boolean canSpawn(StructureWorldAccess world, BlockPos pos) {
		return true;
	}
	
	@Override
	protected BlockPos getCenterGround(StructureWorldAccess world, BlockPos pos) {
		return new BlockPos(pos.getX(), MHelper.randRange(32, 192, world.getRandom()), pos.getZ());
	}
	
	protected boolean getGroundPlant(StructureWorldAccess world, Mutable pos) {
		pos.setY(pos.getY() + MHelper.randRange(-getYOffset(), getYOffset(), world.getRandom()));
		return true;
	}
}
