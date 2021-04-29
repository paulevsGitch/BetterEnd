package ru.betterend.blocks.basis;

import java.util.Collections;
import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

public class BaseBlockWithEntity extends BaseEntityBlock {
	public BaseBlockWithEntity(Properties settings) {
		super(settings);
	}

	@Override
	public BlockEntity newBlockEntity(BlockGetter world) {
		return null;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
}
