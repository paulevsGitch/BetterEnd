package ru.betterend.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.blocks.BaseCropBlock;
import ru.bclib.blocks.BaseDoublePlantBlock;
import ru.bclib.util.BlocksHelper;
import ru.betterend.blocks.basis.EndPlantWithAgeBlock;

import java.util.Random;

public class SinglePlantFeature extends ScatterFeature {
	private final Block plant;
	private final boolean rawHeightmap;
	private final int chance;

	public SinglePlantFeature(Block plant, int radius) {
		this(plant, radius, true, 1);
	}

	public SinglePlantFeature(Block plant, int radius, int chance) {
		this(plant, radius, true, chance);
	}

	public SinglePlantFeature(Block plant, int radius, boolean rawHeightmap) {
		this(plant, radius, rawHeightmap, 1);
	}

	public SinglePlantFeature(Block plant, int radius, boolean rawHeightmap, int chance) {
		super(radius);
		this.plant = plant;
		this.rawHeightmap = rawHeightmap;
		this.chance = chance;
	}

	protected int getChance() {
		return chance;
	}

	@Override
	protected BlockPos getCenterGround(WorldGenLevel world, BlockPos pos) {
		return rawHeightmap ? getPosOnSurfaceWG(world, pos) : getPosOnSurface(world, pos);
	}

	@Override
	public boolean canGenerate(WorldGenLevel world, Random random, BlockPos center, BlockPos blockPos, float radius) {
		return plant.canSurvive(plant.defaultBlockState(), world, blockPos);
	}

	@Override
	public void generate(WorldGenLevel world, Random random, BlockPos blockPos) {
		if (plant instanceof BaseDoublePlantBlock) {
			int rot = random.nextInt(4);
			BlockState state = plant.defaultBlockState().setValue(BaseDoublePlantBlock.ROTATION, rot);
			BlocksHelper.setWithoutUpdate(world, blockPos, state);
			BlocksHelper.setWithoutUpdate(world, blockPos.above(), state.setValue(BaseDoublePlantBlock.TOP, true));
		}
		else if (plant instanceof BaseCropBlock) {
			BlockState state = plant.defaultBlockState().setValue(BaseCropBlock.AGE, 3);
			BlocksHelper.setWithoutUpdate(world, blockPos, state);
		}
		else if (plant instanceof EndPlantWithAgeBlock) {
			int age = random.nextInt(4);
			BlockState state = plant.defaultBlockState().setValue(EndPlantWithAgeBlock.AGE, age);
			BlocksHelper.setWithoutUpdate(world, blockPos, state);
		}
		else {
			BlocksHelper.setWithoutUpdate(world, blockPos, plant);
		}
	}
}
