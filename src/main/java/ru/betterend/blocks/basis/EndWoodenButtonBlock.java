package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WoodButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.Patterns;

public class EndWoodenButtonBlock extends WoodButtonBlock implements BlockModelProvider {
	private final Block parent;
	
	public EndWoodenButtonBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).strength(0.5F, 0.5F).noOcclusion());
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
	public Optional<String> getModelString(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		if (block.contains("item")) {
			return Patterns.createJson(Patterns.ITEM_BUTTON, parentId.getPath(), blockId.getPath());
		}
		if (block.contains("pressed")) {
			return Patterns.createJson(Patterns.BLOCK_BUTTON_PRESSED, parentId.getPath(), blockId.getPath());
		}
		return Patterns.createJson(Patterns.BLOCK_BUTTON, parentId.getPath(), blockId.getPath());
	}
	
	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_BUTTON;
	}
}
