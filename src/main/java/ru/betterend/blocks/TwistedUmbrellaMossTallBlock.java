package ru.betterend.blocks;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import ru.betterend.blocks.basis.DoublePlantBlock;
import ru.betterend.registry.EndBlocks;

public class TwistedUmbrellaMossTallBlock extends DoublePlantBlock {
	public TwistedUmbrellaMossTallBlock() {
		super(12);
	}
	
	@Override
	public void performBonemeal(ServerLevel world, Random random, BlockPos pos, BlockState state) {
		ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(EndBlocks.TWISTED_UMBRELLA_MOSS));
		world.addFreshEntity(item);
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(EndBlocks.END_MOSS) || state.is(EndBlocks.END_MYCELIUM) || state.is(EndBlocks.JUNGLE_MOSS);
	}
}
