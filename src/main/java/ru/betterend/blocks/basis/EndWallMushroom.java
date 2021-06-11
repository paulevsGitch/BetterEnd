package ru.betterend.blocks.basis;

import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.api.TagAPI;
import ru.bclib.blocks.WallMushroomBlock;

public class EndWallMushroom extends WallMushroomBlock {

	public EndWallMushroom(int light) {
		super(light);
	}

	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(TagAPI.END_GROUND);
	}
}
