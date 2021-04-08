package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.basis.DoublePlantBlock;
import ru.betterend.util.BlocksHelper;

public class UnderwaterPlantFeature extends UnderwaterPlantScatter {
	private final Block plant;

	public UnderwaterPlantFeature(Block plant, int radius) {
		super(radius);
		this.plant = plant;
	}

	@Override
	public boolean canGenerate(StructureWorldAccess world, Random random, BlockPos center, BlockPos blockPos,
			float radius) {
		return super.canSpawn(world, blockPos) && plant.canPlaceAt(plant.defaultBlockState(), world, blockPos);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos blockPos) {
		if (plant instanceof DoublePlantBlock) {
			int rot = random.nextInt(4);
			BlockState state = plant.defaultBlockState().with(DoublePlantBlock.ROTATION, rot);
			BlocksHelper.setWithoutUpdate(world, blockPos, state);
			BlocksHelper.setWithoutUpdate(world, blockPos.up(), state.with(DoublePlantBlock.TOP, true));
		} else {
			BlocksHelper.setWithoutUpdate(world, blockPos, plant);
		}
	}
}
