package ru.betterend.blocks.basis;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;

public abstract class BlockUnderwaterPlantWithAge extends BlockUnderwaterPlant {
	public static final IntProperty AGE = IntProperty.of("age", 0, 3);
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(AGE);
	}
	
	public abstract void grow(StructureWorldAccess world, Random random, BlockPos pos);
	
	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		int age = state.get(AGE);
		if (age < 3) {
			world.setBlockState(pos, state.with(AGE, age + 1));
		}
		else {
			grow(world, random, pos);
		}
	}
}
