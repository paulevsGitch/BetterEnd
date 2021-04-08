package ru.betterend.blocks.basis;

import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.level.Level;
import ru.betterend.blocks.BlockProperties;

public abstract class EndPlantWithAgeBlock extends EndPlantBlock {
	public static final IntegerProperty AGE = BlockProperties.AGE;

	public EndPlantWithAgeBlock() {
		this(FabricBlockSettings.of(Material.PLANT).breakByTool(FabricToolTags.SHEARS).sounds(SoundType.GRASS)
				.breakByHand(true).ticksRandomly().noCollision());
	}

	public EndPlantWithAgeBlock(FabricBlockSettings settings) {
		super(settings);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(AGE);
	}

	public abstract void growAdult(StructureWorldAccess world, Random random, BlockPos pos);

	@Override
	public void grow(ServerLevel world, Random random, BlockPos pos, BlockState state) {
		int age = state.getValue(AGE);
		if (age < 3) {
			world.setBlockAndUpdate(pos, state.with(AGE, age + 1));
		} else {
			growAdult(world, random, pos);
		}
	}

	@Override
	public boolean canGrow(Level world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void scheduledTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		if (random.nextInt(8) == 0) {
			grow(world, random, pos, state);
		}
	}
}
