package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.basis.BlockDoublePlant;
import ru.betterend.util.BlocksHelper;

public class SinglePlantFeature extends ScatterFeature {
	private final Block plant;
	private final boolean rawHeightmap;
	
	public SinglePlantFeature(Block plant, int radius) {
		this(plant, radius, true);
	}
	
	public SinglePlantFeature(Block plant, int radius, boolean rawHeightmap) {
		super(radius);
		this.plant = plant;
		this.rawHeightmap = rawHeightmap;
	}
	
	@Override
	protected BlockPos getCenterGround(StructureWorldAccess world, BlockPos pos) {
		return rawHeightmap ? getPosOnSurfaceWG(world, pos) : getPosOnSurface(world, pos);
	}
	
	@Override
	public boolean canGenerate(StructureWorldAccess world, Random random, BlockPos center, BlockPos blockPos, float radius) {
		return plant.canPlaceAt(plant.getDefaultState(), world, blockPos);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos blockPos) {
		if (plant instanceof BlockDoublePlant) {
			int rot = random.nextInt(4);
			BlockState state = plant.getDefaultState().with(BlockDoublePlant.ROTATION, rot);
			BlocksHelper.setWithoutUpdate(world, blockPos, state);
			BlocksHelper.setWithoutUpdate(world, blockPos.up(), state.with(BlockDoublePlant.TOP, true));
		}
		else {
			BlocksHelper.setWithoutUpdate(world, blockPos, plant);
		}
	}
}
