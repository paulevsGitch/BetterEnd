package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.client.ERenderLayer;
import ru.betterend.client.IRenderTypeable;
import ru.betterend.interfaces.Patterned;

public class BlockDoor extends DoorBlock implements IRenderTypeable, Patterned {
	public BlockDoor(Block block) {
		super(FabricBlockSettings.copy(block).nonOpaque());
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		if (state.get(HALF) == DoubleBlockHalf.LOWER)
			return Collections.singletonList(new ItemStack(this.asItem()));
		else
			return Collections.emptyList();
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterned.createJson(data, blockId, blockId.getPath());
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		if (block.contains("item")) {
			return Patterned.createJson(Patterned.ITEM_MODEL, blockId.getPath());
		}
		if (block.contains("top_hinge")) {
			return Patterned.createJson(Patterned.DOOR_MODEL_TOP_HINGE, blockId, blockId.getPath());
		}
		if (block.contains("bottom_hinge")) {
			return Patterned.createJson(Patterned.DOOR_MODEL_BOTTOM_HINGE, blockId, blockId.getPath());
		}
		if (block.contains("top")) {
			return Patterned.createJson(Patterned.DOOR_MODEL_TOP, blockId, blockId.getPath());
		}
		return Patterned.createJson(Patterned.DOOR_MODEL_BOTTOM, blockId, blockId.getPath());
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterned.DOOR_STATES_PATTERN;
	}
}
