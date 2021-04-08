package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class EndComposterBlock extends ComposterBlock implements BlockPatterned {
	public EndComposterBlock(Block source) {
		super(FabricBlockSettings.copyOf(source));
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this.asItem()));
	}

	@Override
	public String getStatesPattern(Reader data) {
		String blockId = Registry.BLOCK.getKey(this).getPath();
		return Patterns.createJson(data, blockId, blockId);
	}

	@Override
	public String getModelPattern(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		String blockName = blockId.getPath();
		return Patterns.createJson(Patterns.BLOCK_COMPOSTER, blockName);
	}

	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_COMPOSTER;
	}
}
