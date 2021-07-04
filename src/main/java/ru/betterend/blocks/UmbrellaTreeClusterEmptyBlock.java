package ru.betterend.blocks;

import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MaterialColor;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.util.BlocksHelper;
import ru.betterend.registry.EndBlocks;

public class UmbrellaTreeClusterEmptyBlock extends BaseBlock {
	public static final BooleanProperty NATURAL = EndBlockProperties.NATURAL;
	
	public UmbrellaTreeClusterEmptyBlock() {
		super(FabricBlockSettings.copyOf(Blocks.NETHER_WART_BLOCK)
				.materialColor(MaterialColor.COLOR_PURPLE)
				.randomTicks());
		registerDefaultState(stateDefinition.any().setValue(NATURAL, false));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(NATURAL);
	}
	
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		if (state.getValue(NATURAL) && random.nextInt(16) == 0) {
			BlocksHelper.setWithUpdate(world, pos, EndBlocks.UMBRELLA_TREE_CLUSTER.defaultBlockState().setValue(UmbrellaTreeClusterBlock.NATURAL, true));
		}
	}
}
