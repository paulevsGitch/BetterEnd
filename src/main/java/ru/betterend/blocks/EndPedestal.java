package ru.betterend.blocks;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.basis.PedestalBlock;

import java.util.HashMap;
import java.util.Map;

public class EndPedestal extends PedestalBlock {

	public EndPedestal(Block parent) {
		super(parent);
	}

	@Override
	protected Map<String, String> createTexturesMap() {
		ResourceLocation blockId = Registry.BLOCK.getKey(parent);
		String name = blockId.getPath();
		return new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;

			{
				put("%mod%", BetterEnd.MOD_ID);
				put("%top%", name + "_polished");
				put("%base%", name + "_polished");
				put("%pillar%", name + "_pillar_side");
				put("%bottom%", name + "_polished");
			}
		};
	}
}
