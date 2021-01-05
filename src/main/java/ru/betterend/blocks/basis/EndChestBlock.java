package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;
import ru.betterend.registry.EndBlockEntities;

public class EndChestBlock extends ChestBlock implements BlockPatterned {
	private final Block parent;
	
	public EndChestBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).nonOpaque(), () -> {
			return EndBlockEntities.CHEST;
		});
		this.parent = source;
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockView world)
	{
		return EndBlockEntities.CHEST.instantiate();
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder)
	{
		List<ItemStack> drop = super.getDroppedStacks(state, builder);
		drop.add(new ItemStack(this.asItem()));
		return drop;
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		Identifier blockId = Registry.BLOCK.getId(this);
		Identifier parentId = Registry.BLOCK.getId(parent);
		return Patterns.createJson(data, parentId.getPath(), blockId.getPath());
	}
	
	@Override
	public String getModelPattern(String path) {
		Identifier blockId = Registry.BLOCK.getId(this);
		Identifier parentId = Registry.BLOCK.getId(parent);
		if (path.contains("item")) {
			return Patterns.createJson(Patterns.ITEM_CHEST, blockId.getPath());
		}
		return Patterns.createJson(Patterns.BLOCK_EMPTY, parentId.getPath());
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_SIMPLE;
	}
}
