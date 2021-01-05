package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class EndComposterBlock extends ComposterBlock implements BlockPatterned {
	public EndComposterBlock(Block source) {
		super(FabricBlockSettings.copyOf(source));
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this.asItem()));
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		String blockId = Registry.BLOCK.getId(this).getPath();
		return Patterns.createJson(data, blockId, blockId);
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		String blockName = blockId.getPath();
		return Patterns.createJson(Patterns.BLOCK_COMPOSTER, blockName);
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_COMPOSTER;
	}
}
