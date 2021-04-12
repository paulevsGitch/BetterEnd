package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;
import ru.betterend.registry.EndBlockEntities;

public class EndChestBlock extends ChestBlock implements BlockPatterned {
	private final Block parent;
	
	public EndChestBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).noOcclusion(), () -> {
			return EndBlockEntities.CHEST;
		});
		this.parent = source;
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockGetter world)
	{
		return EndBlockEntities.CHEST.create();
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
	{
		List<ItemStack> drop = super.getDrops(state, builder);
		drop.add(new ItemStack(this.asItem()));
		return drop;
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		return Patterns.createJson(data, parentId.getPath(), blockId.getPath());
	}
	
	@Override
	public String getModelPattern(String path) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		if (path.contains("item")) {
			return Patterns.createJson(Patterns.ITEM_CHEST, blockId.getPath());
		}
		return Patterns.createJson(Patterns.BLOCK_EMPTY, parentId.getPath());
	}
	
	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_SIMPLE;
	}
}
