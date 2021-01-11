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
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class EndDoorBlock extends DoorBlock implements IRenderTypeable, BlockPatterned {
	public EndDoorBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).strength(3F, 3F).nonOpaque());
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
		String blockId = Registry.BLOCK.getId(this).getPath();
		return Patterns.createJson(data, blockId, blockId);
	}
	
	@Override
	public String getModelPattern(String block) {
		String blockId = Registry.BLOCK.getId(this).getPath();
		if (block.contains("item")) {
			return Patterns.createJson(Patterns.ITEM_GENERATED, block);
		}
		if (block.contains("top_hinge")) {
			return Patterns.createJson(Patterns.BLOCK_DOOR_TOP_HINGE, blockId, blockId);
		}
		if (block.contains("bottom_hinge")) {
			return Patterns.createJson(Patterns.BLOCK_DOOR_BOTTOM_HINGE, blockId, blockId);
		}
		if (block.contains("top")) {
			return Patterns.createJson(Patterns.BLOCK_DOOR_TOP, blockId, blockId);
		}
		return Patterns.createJson(Patterns.BLOCK_DOOR_BOTTOM, blockId, blockId);
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_DOOR;
	}
}
