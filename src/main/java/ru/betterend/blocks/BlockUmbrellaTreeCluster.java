package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import ru.betterend.blocks.basis.BlockBase;

public class BlockUmbrellaTreeCluster extends BlockBase {
	public static final BooleanProperty NATURAL = BooleanProperty.of("natural");
	
	public BlockUmbrellaTreeCluster() {
		super(FabricBlockSettings.copyOf(Blocks.NETHER_WART_BLOCK)
				.materialColor(MaterialColor.PURPLE)
				.luminance(15));
		setDefaultState(stateManager.getDefaultState().with(NATURAL, false));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(NATURAL);
	}
}
