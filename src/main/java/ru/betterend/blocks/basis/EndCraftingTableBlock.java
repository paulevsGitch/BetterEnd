package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class EndCraftingTableBlock extends CraftingTableBlock implements BlockPatterned {
	public EndCraftingTableBlock(Block source) {
		super(FabricBlockSettings.copyOf(source));
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this.asItem()));
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		String blockId = Registry.BLOCK.getKey(this).getPath();
		return Patterns.createJson(data, blockId, blockId);
	}
	
	@Override
	public String getModelString(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		String blockName = blockId.getPath();
		return Patterns.createJson(Patterns.BLOCK_SIDED, new HashMap<String, String>() {
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
	public ResourceLocation statePatternId() {
		return Patterns.STATE_SIMPLE;
	}
}
