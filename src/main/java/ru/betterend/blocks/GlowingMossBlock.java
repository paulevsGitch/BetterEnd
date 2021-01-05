package ru.betterend.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
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
    public boolean hasEmissiveLighting(BlockView world, BlockPos pos) {
       return true;
    }

    @Environment(EnvType.CLIENT)
    public float getAmbientOcclusionLightLevel(BlockView world, BlockPos pos) {
       return 1F;
    }
}