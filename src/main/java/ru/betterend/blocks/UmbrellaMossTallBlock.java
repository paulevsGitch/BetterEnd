package ru.betterend.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.blocks.BaseDoublePlantBlock;
import ru.betterend.registry.EndBlocks;

import java.util.Random;

public class UmbrellaMossTallBlock extends BaseDoublePlantBlock {
	public UmbrellaMossTallBlock() {
		super(12);
	}

	@Override
	public void performBonemeal(ServerLevel world, Random random, BlockPos pos, BlockState state) {
		ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(EndBlocks.UMBRELLA_MOSS));
		world.addFreshEntity(item);
	}

	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(EndBlocks.END_MOSS) || state.is(EndBlocks.END_MYCELIUM) || state.is(EndBlocks.JUNGLE_MOSS);
	}
}
