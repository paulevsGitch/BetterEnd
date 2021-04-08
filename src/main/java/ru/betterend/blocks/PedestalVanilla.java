package ru.betterend.blocks;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import ru.betterend.blocks.basis.PedestalBlock;
import ru.betterend.patterns.Patterns;

public class PedestalVanilla extends PedestalBlock {

	public PedestalVanilla(Block parent) {
		super(parent);
	}

	@Override
	public String getModelPattern(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(parent);
		String name = blockId.getPath().replace("_block", "");
		Map<String, String> textures = new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("%mod%", blockId.getNamespace());
				put("%top%", "polished_" + name);
				put("%base%", "polished_" + name);
				put("%pillar%", name + "_pillar");
				put("%bottom%", "polished_" + name);
			}
		};
		if (block.contains("column_top")) {
			return Patterns.createJson(Patterns.BLOCK_PEDESTAL_COLUMN_TOP, textures);
		} else if (block.contains("column")) {
			return Patterns.createJson(Patterns.BLOKC_PEDESTAL_COLUMN, textures);
		} else if (block.contains("top")) {
			return Patterns.createJson(Patterns.BLOCK_PEDESTAL_TOP, textures);
		} else if (block.contains("bottom")) {
			return Patterns.createJson(Patterns.BLOCK_PEDESTAL_BOTTOM, textures);
		} else if (block.contains("pillar")) {
			return Patterns.createJson(Patterns.BLOCK_PEDESTAL_PILLAR, textures);
		}
		return Patterns.createJson(Patterns.BLOCK_PEDESTAL_DEFAULT, textures);
	}
}
