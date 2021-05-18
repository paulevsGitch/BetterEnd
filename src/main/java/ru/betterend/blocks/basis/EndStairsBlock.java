package ru.betterend.blocks.basis;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.Patterns;

public class EndStairsBlock extends StairBlock implements BlockModelProvider {
	
	private final Block parent;
	
	public EndStairsBlock(Block source) {
		super(source.defaultBlockState(), FabricBlockSettings.copyOf(source));
		this.parent = source;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}

	@Override
	public Optional<String> getModelString(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_STAIR, parentId.getPath(), blockId.getPath());
		if (block.contains("inner")) {
			pattern = Patterns.createJson(Patterns.BLOCK_STAIR_INNER, parentId.getPath(), blockId.getPath());
		}
		if (block.contains("outer")) {
			pattern = Patterns.createJson(Patterns.BLOCK_STAIR_OUTER, parentId.getPath(), blockId.getPath());
		}
		return pattern;
	}

}
