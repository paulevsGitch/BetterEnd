package ru.betterend.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.UpDownPlantBlock;
import ru.betterend.registry.EndBlocks;

public class GlowingPillarRootsBlock extends UpDownPlantBlock {
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.isOf(EndBlocks.AMBER_MOSS);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(EndBlocks.GLOWING_PILLAR_SEED);
	}
}
