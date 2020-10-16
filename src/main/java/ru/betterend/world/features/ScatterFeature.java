package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.registry.BlockTagRegistry;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public abstract class ScatterFeature extends DefaultFeature {
	private static final Mutable POS = new Mutable();
	private final int radius;
	
	public ScatterFeature(int radius) {
		this.radius = radius;
	}
	
	public abstract boolean canGenerate(StructureWorldAccess world, Random random, BlockPos center, BlockPos blockPos, float radius);
	
	public abstract void generate(StructureWorldAccess world, Random random, BlockPos blockPos);
	
	protected BlockPos getCenterGround(StructureWorldAccess world, BlockPos pos) {
		return getPosOnSurfaceWG(world, pos);
	}
	
	protected boolean canSpawn(StructureWorldAccess world, BlockPos pos) {
		if (pos.getY() < 5) {
			return false;
		}
		else if (!world.getBlockState(pos.down()).isIn(BlockTagRegistry.END_GROUND)) {
			return false;
		}
		return true;
	}
	
	protected boolean getGroundPlant(StructureWorldAccess world, Mutable pos) {
		int down = BlocksHelper.downRay(world, pos, 16);
		if (down > 10) {
			return false;
		}
		pos.setY(pos.getY() - down);
		return true;
	}
	
	protected int getYOffset() {
		return 5;
	}
	
	protected int getChance() {
		return 1;
	}
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos center, DefaultFeatureConfig featureConfig) {
		center = getCenterGround(world, center);
		
		if (!canSpawn(world, center)) {
			return false;
		}
		
		float r = MHelper.randRange(radius * 0.5F, radius, random);
		int count = MHelper.floor(r * r * MHelper.randRange(1.5F, 3F, random));
		for (int i = 0; i < count; i++) {
			float pr = r * (float) Math.sqrt(random.nextFloat());
			float theta = random.nextFloat() * MHelper.PI2;
			float x = pr * (float) Math.cos(theta);
			float z = pr * (float) Math.sin(theta);
			
			POS.set(center.getX() + x, center.getY() + getYOffset(), center.getZ() + z);
			if (getGroundPlant(world, POS) && canGenerate(world, random, center, POS, r) && (getChance() < 2 || random.nextInt(getChance()) == 0)) {
				generate(world, random, POS);
			}
		}
		
		return true;
	}
}
