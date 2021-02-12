package ru.betterend.blocks;

import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.blocks.basis.EndAnvilBlock;
import ru.betterend.item.material.EndToolMaterial;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;
import ru.betterend.registry.EndBlocks;

public class AeterniumAnvil extends EndAnvilBlock {
	public static final IntProperty DESTRUCTION = BlockProperties.DESTRUCTION_LONG;
	
	public AeterniumAnvil() {
		super(EndBlocks.AETERNIUM_BLOCK.getDefaultMaterialColor(), EndToolMaterial.AETERNIUM.getMiningLevel());
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(DESTRUCTION);
		builder.add(FACING);
	}

	@Override
	public IntProperty getDestructionProperty() {
		return DESTRUCTION;
	}

	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_ANVIL_LONG;
	}
}
