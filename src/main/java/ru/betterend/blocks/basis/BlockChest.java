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
import ru.betterend.interfaces.Patterned;
import ru.betterend.registry.BlockEntityRegistry;

public class BlockChest extends ChestBlock implements Patterned {

	private final Block parent;
	
	public BlockChest(Block source) {
		super(FabricBlockSettings.copyOf(source).nonOpaque(), () -> {
			return BlockEntityRegistry.CHEST;
		});
		this.parent = source;
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
	
	@Override
	public String getStatesPattern(Reader data) {
		Identifier blockId = Registry.BLOCK.getId(this);
		Identifier parentId = Registry.BLOCK.getId(parent);
		return Patterned.createJson(data, parentId, blockId.getPath());
	}
	
	@Override
	public String getModelPattern(String path) {
		Identifier blockId = Registry.BLOCK.getId(this);
		Identifier parentId = Registry.BLOCK.getId(parent);
		if (path.contains("item")) {
			return Patterned.createJson(Patterned.CHEST_ITEM_MODEL, blockId.getPath());
		}
		return Patterned.createJson(Patterned.EMPTY_MODEL, parentId.getPath());
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterned.BLOCK_STATES_PATTERN;
	}
}
