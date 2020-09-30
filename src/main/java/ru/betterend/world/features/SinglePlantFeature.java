package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.blocks.basis.BlockDoublePlant;
import ru.betterend.registry.BlockTagRegistry;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class SinglePlantFeature extends DefaultFeature {
	private static final Mutable POS = new Mutable();
	private final Block plant;
	private final int radius;
	
	public SinglePlantFeature(Block plant, int radius) {
		this.plant = plant;
		this.radius = radius;
	}
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig featureConfig) {
		blockPos = getPosOnSurface(world, blockPos);
		
		if (blockPos.getY() < 5) {
			return false;
		}
		if (!world.getBlockState(blockPos.down()).isIn(BlockTagRegistry.END_GROUND)) {
			return false;
		}
		
		float r = MHelper.randRange(radius * 0.5F, radius, random);
		int count = MHelper.floor(r * r * MHelper.randRange(1.5F, 3F, random));
		for (int i = 0; i < count; i++) {
			float pr = r * (float) Math.sqrt(random.nextFloat());
			float theta = random.nextFloat() * MHelper.PI2;
			float x = pr * (float) Math.cos(theta);
			float z = pr * (float) Math.sin(theta);
			
			POS.set(blockPos.getX() + x, blockPos.getY() + 5, blockPos.getZ() + z);
			int down = BlocksHelper.downRay(world, POS, 16);
			if (down > 10) continue;
			POS.setY(POS.getY() - down);
			
			if (plant.canPlaceAt(plant.getDefaultState(), world, POS)) {
				if (plant instanceof BlockDoublePlant) {
					int rot = random.nextInt(4);
					BlockState state = plant.getDefaultState().with(BlockDoublePlant.ROTATION, rot);
					BlocksHelper.setWithoutUpdate(world, POS, state);
					BlocksHelper.setWithoutUpdate(world, POS.up(), state.with(BlockDoublePlant.TOP, true));
				}
				else {
					BlocksHelper.setWithoutUpdate(world, POS, plant);
				}
			}
		}
		
		return true;
	}
}
