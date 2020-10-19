package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.registry.BlockRegistry;

public class RunedFlavolite extends BlockBase {
	public static final BooleanProperty ACTIVATED = BooleanProperty.of("active");

	public RunedFlavolite() {
		super(FabricBlockSettings.copyOf(BlockRegistry.FLAVOLITE.polished).lightLevel(6));
		this.setDefaultState(stateManager.getDefaultState().with(ACTIVATED, false));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(ACTIVATED);
	}
}
