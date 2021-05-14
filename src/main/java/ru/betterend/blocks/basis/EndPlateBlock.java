package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.betterend.patterns.BlockModelProvider;
import ru.betterend.patterns.Patterns;

public class EndPlateBlock extends PressurePlateBlock implements BlockModelProvider {
	private final Block parent;
	
	public EndPlateBlock(Sensitivity rule, Block source) {
		super(rule, FabricBlockSettings.copyOf(source).noCollission().noOcclusion().strength(0.5F));
		this.parent = source;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		return Patterns.createJson(data, parentId.getPath(), blockId.getPath());
	}
	
	@Override
	public String getModelString(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		if (block.contains("down")) {
			return Patterns.createJson(Patterns.BLOCK_PLATE_DOWN, parentId.getPath(), blockId.getPath());
		}
		return Patterns.createJson(Patterns.BLOCK_PLATE_UP, parentId.getPath(), blockId.getPath());
	}
	
	@Override
	public ResourceLocation statePatternId() {
		return this.stateDefinition.getProperty("facing") != null ? Patterns.STATE_PLATE_ROTATED : Patterns.STATE_PLATE;
	}
}
