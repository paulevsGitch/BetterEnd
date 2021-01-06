package ru.betterend.blocks.basis;

import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;

public abstract class EndPlantWithAgeBlock extends EndPlantBlock {
	public static final IntProperty AGE = IntProperty.of("age", 0, 3);
	
	public EndPlantWithAgeBlock() {
		this(FabricBlockSettings.of(Material.PLANT)
				.breakByTool(FabricToolTags.SHEARS)
				.sounds(BlockSoundGroup.GRASS)
				.breakByHand(true)
				.ticksRandomly()
				.noCollision());
	}
	
	public EndPlantWithAgeBlock(FabricBlockSettings settings) {
		super(settings);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(AGE);
	}
	
	public abstract void growAdult(StructureWorldAccess world, Random random, BlockPos pos);
	
	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		int age = state.get(AGE);
		if (age < 3) {
			world.setBlockState(pos, state.with(AGE, age + 1));
		}
		else {
			growAdult(world, random, pos);
		}
	}
	
	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return random.nextInt(8) == 0;
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		if (canGrow(world, random, pos, state)) {
			grow(world, random, pos, state);
		}
	}
}
