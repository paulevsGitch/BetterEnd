package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.enums.DoubleBlockHalf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class EndDoorBlock extends DoorBlock implements IRenderTypeable, BlockPatterned {
	public EndDoorBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).strength(3F, 3F).nonOpaque());
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (state.getValue(HALF) == DoubleBlockHalf.LOWER)
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
		String blockId = Registry.BLOCK.getKey(this).getPath();
		return Patterns.createJson(data, blockId, blockId);
	}

	@Override
	public String getModelPattern(String block) {
		String blockId = Registry.BLOCK.getKey(this).getPath();
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
	public ResourceLocation statePatternId() {
		return Patterns.STATE_DOOR;
	}
}
