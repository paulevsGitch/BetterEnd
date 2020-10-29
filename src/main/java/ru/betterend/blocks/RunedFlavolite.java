package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.PortalFrameHelper;

public class RunedFlavolite extends BlockBase {
	public static final BooleanProperty ACTIVATED = BlockProperties.ACTIVATED;

	public RunedFlavolite() {
		super(FabricBlockSettings.copyOf(EndBlocks.FLAVOLITE.polished).resistance(Blocks.OBSIDIAN.getBlastResistance()).luminance(state -> {
			return state.get(ACTIVATED) ? 8 : 0;
		}));
		this.setDefaultState(stateManager.getDefaultState().with(ACTIVATED, false));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(ACTIVATED);
	}
	
	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		BlockPos bottom = PortalFrameHelper.findBottomCorner((World) world, pos, this);
		BlockPos top = PortalFrameHelper.findTopCorner((World) world, pos, this);
		if (bottom == null || top == null) return;
		for (BlockPos position : BlockPos.iterate(bottom, top)) {
			if (position.equals(pos)) continue;
			BlockState posState = world.getBlockState(position);
			if (posState.getBlock() instanceof RunedFlavolite && posState.get(ACTIVATED)) {
				BlocksHelper.setWithoutUpdate(world, position, posState.with(ACTIVATED, false));
			} else if (posState.isOf(EndBlocks.END_PORTAL_BLOCK)) {
				world.removeBlock(position, false);
			}
		}
	}
}
