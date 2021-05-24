package ru.betterend.blocks.basis;

import java.util.Optional;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import ru.betterend.client.models.Patterns;

public class BarkBlock extends EndPillarBlock {
	public BarkBlock(Properties settings) {
		super(settings);
	}

	@Override
	protected Optional<String> createBlockPattern(ResourceLocation blockId) {
		blockId = Registry.BLOCK.getKey(this);
		return Patterns.createJson(Patterns.BLOCK_BASE, getName(blockId), blockId.getPath());
	}

	private String getName(ResourceLocation blockId) {
		String name = blockId.getPath();
		return name.replace("_bark", "_log_side");
	}
}
