package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import ru.betterend.client.ERenderLayer;
import ru.betterend.client.IRenderTypeable;
import ru.betterend.interfaces.Patterned;

public class BlockTrapdoor extends TrapdoorBlock implements IRenderTypeable, Patterned {
	public BlockTrapdoor(Block source) {
		super(FabricBlockSettings.copyOf(source).nonOpaque());
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public String getStatesPattern(Reader data, String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterned.createJson(data, blockId, block);
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		String name = blockId.getPath();
		return Patterned.createJson(Patterned.TRAPDOOR_MODEL, new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("%block%", name);
				put("%texture%", name.replace("trapdoor", "door_side"));
			}
		});
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterned.TRAPDOOR_STATES_PATTERN;
	}
}
