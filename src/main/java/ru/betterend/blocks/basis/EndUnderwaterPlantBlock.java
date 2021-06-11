package ru.betterend.blocks.basis;

import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.api.TagAPI;
import ru.bclib.blocks.UnderwaterPlantBlock;

public class EndUnderwaterPlantBlock extends UnderwaterPlantBlock {

	public EndUnderwaterPlantBlock() {}

	public EndUnderwaterPlantBlock(int light) {
		super(light);
	}

	public EndUnderwaterPlantBlock(Properties settings) {
		super(settings);
	}

	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(TagAPI.END_GROUND);
	}
}
