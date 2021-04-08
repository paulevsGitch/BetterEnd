package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.BooleanProperty;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.registry.EndBlocks;

public class RunedFlavolite extends BlockBase {
	public static final BooleanProperty ACTIVATED = BlockProperties.ACTIVE;

	public RunedFlavolite() {
		super(FabricBlockSettings.copyOf(EndBlocks.FLAVOLITE.polished)
				.resistance(Blocks.OBSIDIAN.getExplosionResistance()).luminance(state -> {
					return state.getValue(ACTIVATED) ? 8 : 0;
				}));
		this.setDefaultState(stateManager.defaultBlockState().with(ACTIVATED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(ACTIVATED);
	}
}
