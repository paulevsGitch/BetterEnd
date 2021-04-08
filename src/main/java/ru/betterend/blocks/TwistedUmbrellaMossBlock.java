package ru.betterend.blocks;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import ru.betterend.blocks.basis.DoublePlantBlock;
import ru.betterend.blocks.basis.EndPlantBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;

public class TwistedUmbrellaMossBlock extends EndPlantBlock {
	public TwistedUmbrellaMossBlock() {
		super(11);
	}

	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(EndBlocks.END_MOSS) || state.is(EndBlocks.END_MYCELIUM) || state.is(EndBlocks.JUNGLE_MOSS);
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
	public boolean canGrow(Level world, Random random, BlockPos pos, BlockState state) {
		return world.isAir(pos.up());
	}

	@Override
	public void grow(ServerLevel world, Random random, BlockPos pos, BlockState state) {
		int rot = world.random.nextInt(4);
		BlockState bs = EndBlocks.TWISTED_UMBRELLA_MOSS_TALL.defaultBlockState().with(DoublePlantBlock.ROTATION, rot);
		BlocksHelper.setWithoutUpdate(world, pos, bs);
		BlocksHelper.setWithoutUpdate(world, pos.up(), bs.with(DoublePlantBlock.TOP, true));
	}
}
