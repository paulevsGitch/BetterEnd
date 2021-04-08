package ru.betterend.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.UpDownPlantBlock;
import ru.betterend.registry.EndBlocks;

public class GlowingPillarRootsBlock extends UpDownPlantBlock {
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}

	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(EndBlocks.AMBER_MOSS);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(EndBlocks.GLOWING_PILLAR_SEED);
	}
}
