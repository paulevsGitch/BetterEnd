package ru.betterend.blocks;

import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;

public class BlockUmbrellaTreeClusterEmpty extends BlockBase {
	public static final BooleanProperty NATURAL = BooleanProperty.of("natural");
	
	public BlockUmbrellaTreeClusterEmpty() {
		super(FabricBlockSettings.copyOf(Blocks.NETHER_WART_BLOCK)
				.materialColor(MaterialColor.PURPLE)
				.ticksRandomly());
		setDefaultState(stateManager.getDefaultState().with(NATURAL, false));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(NATURAL);
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.get(NATURAL) && random.nextInt(16) == 0) {
			BlocksHelper.setWithUpdate(world, pos, EndBlocks.UMBRELLA_TREE_CLUSTER.getDefaultState().with(BlockUmbrellaTreeCluster.NATURAL, true));
		}
	}
}
