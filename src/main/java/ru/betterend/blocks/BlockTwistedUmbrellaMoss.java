package ru.betterend.blocks;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import ru.betterend.blocks.basis.BlockDoublePlant;
import ru.betterend.blocks.basis.BlockPlant;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;

public class BlockTwistedUmbrellaMoss extends BlockPlant {
	public BlockTwistedUmbrellaMoss() {
		super(11);
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
    
    @Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return world.isAir(pos.up());
	}
    
    @Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
    	int rot = world.random.nextInt(4);
		BlockState bs = EndBlocks.TWISTED_UMBRELLA_MOSS_TALL.getDefaultState().with(BlockDoublePlant.ROTATION, rot);
		BlocksHelper.setWithoutUpdate(world, pos, bs);
		BlocksHelper.setWithoutUpdate(world, pos.up(), bs.with(BlockDoublePlant.TOP, true));
	}
}
