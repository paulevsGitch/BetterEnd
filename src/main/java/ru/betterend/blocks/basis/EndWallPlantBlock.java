package ru.betterend.blocks.basis;

import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.api.TagAPI;
import ru.bclib.blocks.BaseWallPlantBlock;

public class EndWallPlantBlock extends BaseWallPlantBlock {

	public EndWallPlantBlock() {}

	public EndWallPlantBlock(int light) {
		super(light);
	}

	public EndWallPlantBlock(Properties settings) {
		super(settings);
	}

	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(TagAPI.END_GROUND);
	}
}
