package ru.betterend.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import ru.betterend.blocks.basis.EndPlantBlock;
import ru.betterend.registry.EndBlocks;

public class GlowingMossBlock extends EndPlantBlock {
	public GlowingMossBlock(int light) {
		super(light);
	}

	@Override
	protected boolean isTerrain(BlockState state) {
		return state.getBlock() == EndBlocks.END_MOSS || state.getBlock() == EndBlocks.END_MYCELIUM;
	}

	@Environment(EnvType.CLIENT)
	public boolean hasEmissiveLighting(BlockGetter world, BlockPos pos) {
		return true;
	}

	@Environment(EnvType.CLIENT)
	public float getAmbientOcclusionLightLevel(BlockGetter world, BlockPos pos) {
		return 1F;
	}
}