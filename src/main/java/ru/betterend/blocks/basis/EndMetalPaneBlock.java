package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PaneBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class EndMetalPaneBlock extends PaneBlock implements BlockPatterned, IRenderTypeable {
	public EndMetalPaneBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).strength(5.0F, 6.0F).nonOpaque());
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterns.createJson(data, blockId.getPath(), blockId.getPath());
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		if (block.contains("item")) {
			return Patterns.createJson(Patterns.ITEM_BLOCK, blockId.getPath());
		}
		if (block.contains("post")) {
			return Patterns.createJson(Patterns.BLOCK_BARS_POST, blockId.getPath(), blockId.getPath());
		}
		else {
			return Patterns.createJson(Patterns.BLOCK_BARS_SIDE, blockId.getPath(), blockId.getPath());
		}
	}
	
	@Environment(EnvType.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		if (direction.getAxis().isVertical() && stateFrom.getBlock().is(this) && !stateFrom.equals(state)) {
			return false;
		}
		return super.isSideInvisible(state, stateFrom, direction);
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_BARS;
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
}
