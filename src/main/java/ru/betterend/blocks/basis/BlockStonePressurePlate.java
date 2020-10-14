package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.interfaces.Patterned;

public class BlockStonePressurePlate extends PressurePlateBlock implements Patterned {
	
	private final Block parent;
	
	public BlockStonePressurePlate(Block source) {
		super(ActivationRule.MOBS, FabricBlockSettings.copyOf(source).nonOpaque());
		this.parent = source;
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		Identifier blockId = Registry.BLOCK.getId(this);
		Identifier parentId = Registry.BLOCK.getId(parent);
		return Patterned.createJson(data, parentId, blockId.getPath());
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier parentId = Registry.BLOCK.getId(parent);
		if (block.contains("down")) {
			return Patterned.createJson(Patterned.PLATE_MODEL_DOWN, parentId, block);
		}
		return Patterned.createJson(Patterned.PLATE_MODEL_UP, parentId, block);
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterned.PLATE_STATES_PATTERN;
	}
}
