package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.interfaces.Patterned;

public class BlockCraftingTable extends CraftingTableBlock implements Patterned {
	public BlockCraftingTable(Block source) {
		super(FabricBlockSettings.copyOf(source));
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this.asItem()));
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterned.createJson(data, blockId, blockId.getPath());
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		String blockName = blockId.getPath();
		return Patterned.createJson(Patterned.SIDED_BLOCK_MODEL, new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("%particle%", blockName + "_front");
				put("%down%", blockName + "_bottom");
				put("%up%", blockName + "_top");
				put("%north%", blockName + "_front");
				put("%south%", blockName + "_side");
				put("%west%", blockName + "_front");
				put("%east%", blockName + "_side");
			}
		});
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterned.BLOCK_STATES_PATTERN;
	}
}
