package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import ru.betterend.blocks.basis.BaseBlock;
import ru.betterend.registry.EndBlocks;

public class RunedFlavolite extends BaseBlock {
	public static final BooleanProperty ACTIVATED = BlockProperties.ACTIVE;

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
}
