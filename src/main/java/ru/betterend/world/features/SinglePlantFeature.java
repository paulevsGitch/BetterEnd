package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.basis.DoublePlantBlock;
import ru.betterend.blocks.basis.EndCropBlock;
import ru.betterend.blocks.basis.EndPlantWithAgeBlock;
import ru.betterend.util.BlocksHelper;

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
	protected BlockPos getCenterGround(StructureWorldAccess world, BlockPos pos) {
		return rawHeightmap ? getPosOnSurfaceWG(world, pos) : getPosOnSurface(world, pos);
	}
	
	@Override
	public boolean canGenerate(StructureWorldAccess world, Random random, BlockPos center, BlockPos blockPos, float radius) {
		return plant.canPlaceAt(plant.getDefaultState(), world, blockPos);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos blockPos) {
		if (plant instanceof DoublePlantBlock) {
			int rot = random.nextInt(4);
			BlockState state = plant.getDefaultState().with(DoublePlantBlock.ROTATION, rot);
			BlocksHelper.setWithoutUpdate(world, blockPos, state);
			BlocksHelper.setWithoutUpdate(world, blockPos.up(), state.with(DoublePlantBlock.TOP, true));
		}
		else if (plant instanceof EndCropBlock) {
			BlockState state = plant.getDefaultState().with(EndCropBlock.AGE, 3);
			BlocksHelper.setWithoutUpdate(world, blockPos, state);
		}
		else if (plant instanceof EndPlantWithAgeBlock) {
			int age = random.nextInt(4);
			BlockState state = plant.getDefaultState().with(EndPlantWithAgeBlock.AGE, age);
			BlocksHelper.setWithoutUpdate(world, blockPos, state);
		}
		else {
			BlocksHelper.setWithoutUpdate(world, blockPos, plant);
		}
	}
}
