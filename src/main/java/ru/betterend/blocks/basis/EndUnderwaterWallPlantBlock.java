package ru.betterend.blocks.basis;

import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.api.TagAPI;
import ru.bclib.blocks.BaseUnderwaterWallPlantBlock;

public class EndUnderwaterWallPlantBlock extends BaseUnderwaterWallPlantBlock {

	public EndUnderwaterWallPlantBlock() {
	}

	public EndUnderwaterWallPlantBlock(int light) {
		super(light);
	}

	public EndUnderwaterWallPlantBlock(Properties settings) {
		super(settings);
	}

	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(TagAPI.END_GROUND);
	}
}
