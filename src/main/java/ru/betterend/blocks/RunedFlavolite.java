package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.util.BlocksHelper;

public class RunedFlavolite extends BlockBase {
	public static final BooleanProperty ACTIVATED = BooleanProperty.of("active");

	public RunedFlavolite() {
		super(FabricBlockSettings.copyOf(BlockRegistry.FLAVOLITE.polished).luminance(state -> {
			return state.get(ACTIVATED) ? 8 : 0;
		}));
		this.setDefaultState(stateManager.getDefaultState().with(ACTIVATED, false));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(ACTIVATED);
	}
	
	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		super.onBreak(world, pos, state, player);
		if(state.get(ACTIVATED)) {
			for (BlockPos position : BlockPos.iterate(pos.add(-3, -4, -3), pos.add(3, 4, 3))) {
				if (position.equals(pos)) continue;
				BlockState posState = world.getBlockState(position);
				if (posState.getBlock() instanceof RunedFlavolite) {
					BlocksHelper.setWithoutUpdate(world, position, posState.with(ACTIVATED, false));
				} else if (posState.isOf(BlockRegistry.END_PORTAL_BLOCK)) {
					world.removeBlock(position, false);
				}
			}
		}
	}
}
