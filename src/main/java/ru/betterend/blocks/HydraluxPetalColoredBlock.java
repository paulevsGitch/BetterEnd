package ru.betterend.blocks;

import java.io.Reader;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.resources.ResourceLocation;
import ru.betterend.interfaces.IColorProvider;
import ru.betterend.patterns.Patterns;
import ru.betterend.util.BlocksHelper;

public class HydraluxPetalColoredBlock extends HydraluxPetalBlock implements IColorProvider {
	public HydraluxPetalColoredBlock(FabricBlockSettings settings) {
		super(settings);
	}

	@Override
	public BlockColor getBlockProvider() {
		return (state, world, pos, tintIndex) -> {
			return BlocksHelper.getBlockColor(this);
		};
	}

	@Override
	public ItemColor getItemProvider() {
		return (stack, tintIndex) -> {
			return BlocksHelper.getBlockColor(this);
		};
	}

	@Override
	public String getStatesPattern(Reader data) {
		String path = "betterend:block/block_petal_colored";
		return Patterns.createJson(data, path, path);
	}

	@Override
	public String getModelPattern(String block) {
		String path = "betterend:block/block_petal_colored";
		return Patterns.createJson(Patterns.BLOCK_PETAL_COLORED, path, path);
	}

	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_DIRECT;
	}
}
