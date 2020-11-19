package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.basis.BlockDoublePlant;
import ru.betterend.util.BlocksHelper;

public class UnderwaterPlantFeature extends UnderwaterPlantScatter {
	private final Block plant;
	
	public UnderwaterPlantFeature(Block plant, int radius) {
		super(radius);
		this.plant = plant;
	}
	
	@Override
	public boolean canGenerate(StructureWorldAccess world, Random random, BlockPos center, BlockPos blockPos, float radius) {
		return super.canSpawn(world, blockPos) && plant.canPlaceAt(plant.getDefaultState(), world, blockPos);
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
