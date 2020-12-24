package ru.betterend.blocks;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import ru.betterend.blocks.basis.BlockDoublePlant;
import ru.betterend.registry.EndBlocks;

public class BlockTwistedUmbrellaMossTall extends BlockDoublePlant {
	public BlockTwistedUmbrellaMossTall() {
		super(12);
	}
	
	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(EndBlocks.TWISTED_UMBRELLA_MOSS));
		world.spawnEntity(item);
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.isOf(EndBlocks.END_MOSS) || state.isOf(EndBlocks.END_MYCELIUM) || state.isOf(EndBlocks.JUNGLE_MOSS);
	}
}
