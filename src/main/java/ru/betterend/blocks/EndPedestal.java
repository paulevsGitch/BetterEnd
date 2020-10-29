package ru.betterend.blocks;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.basis.BlockPedestal;
import ru.betterend.interfaces.Patterned;

public class EndPedestal extends BlockPedestal {

	public EndPedestal(Block parent) {
		super(parent);
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(parent);
		String name = blockId.getPath();
		Map<String, String> textures = new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("%mod%", BetterEnd.MOD_ID );
				put("%top%", name + "_polished");
				put("%base%", name + "_polished");
				put("%pillar%", name + "_pillar_side");
				put("%bottom%", name + "_polished");
			}
		};
		if (block.contains("column_top")) {
			return Patterned.createJson(Patterned.PEDESTAL_MODEL_COLUMN_TOP, textures);
		} else if (block.contains("column")) {
			return Patterned.createJson(Patterned.PEDESTAL_MODEL_COLUMN, textures);
		} else if (block.contains("top")) {
			return Patterned.createJson(Patterned.PEDESTAL_MODEL_TOP, textures);
		} else if (block.contains("bottom")) {
			return Patterned.createJson(Patterned.PEDESTAL_MODEL_BOTTOM, textures);
		} else if (block.contains("pillar")) {
			return Patterned.createJson(Patterned.PEDESTAL_MODEL_PILLAR, textures);
		}
		return Patterned.createJson(Patterned.PEDESTAL_MODEL_DEFAULT, textures);
	}
}
