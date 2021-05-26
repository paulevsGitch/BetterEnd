package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.util.MHelper;
import ru.betterend.blocks.basis.DoublePlantBlock;
import ru.betterend.util.BlocksHelper;

public class DoublePlantFeature extends ScatterFeature {
	private final Block smallPlant;
	private final Block largePlant;
	private Block plant;
	
	public DoublePlantFeature(Block smallPlant, Block largePlant, int radius) {
		super(radius);
		this.smallPlant = smallPlant;
		this.largePlant = largePlant;
	}
	
	@Override
	public boolean canGenerate(WorldGenLevel world, Random random, BlockPos center, BlockPos blockPos, float radius) {
		float d = MHelper.length(center.getX() - blockPos.getX(), center.getZ() - blockPos.getZ()) / radius * 0.6F + random.nextFloat() * 0.4F;
		plant = d < 0.5F ? largePlant : smallPlant;
		return plant.canSurvive(plant.defaultBlockState(), world, blockPos);
	}

	@Override
	public void generate(WorldGenLevel world, Random random, BlockPos blockPos) {
		if (plant instanceof DoublePlantBlock) {
			int rot = random.nextInt(4);
			BlockState state = plant.defaultBlockState().setValue(DoublePlantBlock.ROTATION, rot);
			BlocksHelper.setWithoutUpdate(world, blockPos, state);
			BlocksHelper.setWithoutUpdate(world, blockPos.above(), state.setValue(DoublePlantBlock.TOP, true));
		}
		else {
			BlocksHelper.setWithoutUpdate(world, blockPos, plant);
		}
	}
}
