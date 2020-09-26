package ru.betterend.blocks.basis;

import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.world.BlockView;
import ru.betterend.registry.BlockEntityRegistry;

public class BlockChest extends ChestBlock
{
	public BlockChest(Block source) {
		super(FabricBlockSettings.copyOf(source).nonOpaque(), () -> {
			return BlockEntityRegistry.CHEST;
		});
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockView world)
	{
		return BlockEntityRegistry.CHEST.instantiate();
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder)
	{
		List<ItemStack> drop = super.getDroppedStacks(state, builder);
		drop.add(new ItemStack(this.asItem()));
		return drop;
	}
}
