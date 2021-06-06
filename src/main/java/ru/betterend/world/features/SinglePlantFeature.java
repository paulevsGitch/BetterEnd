package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.util.BlocksHelper;
import ru.betterend.blocks.basis.DoublePlantBlock;
import ru.betterend.blocks.basis.EndCropBlock;
import ru.betterend.blocks.basis.EndPlantWithAgeBlock;

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
		if (plant instanceof DoublePlantBlock) {
			int rot = random.nextInt(4);
			BlockState state = plant.defaultBlockState().setValue(DoublePlantBlock.ROTATION, rot);
			BlocksHelper.setWithoutUpdate(world, blockPos, state);
			BlocksHelper.setWithoutUpdate(world, blockPos.above(), state.setValue(DoublePlantBlock.TOP, true));
		}
		else if (plant instanceof EndCropBlock) {
			BlockState state = plant.defaultBlockState().setValue(EndCropBlock.AGE, 3);
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
