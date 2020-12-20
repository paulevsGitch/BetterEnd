package ru.betterend.integration.byg;

import net.minecraft.block.Block;
import ru.betterend.blocks.basis.BlockVine;
import ru.betterend.blocks.basis.BlockWallPlant;
import ru.betterend.registry.EndBlocks;

public class BYGBlocks {
	public static final Block IVIS_MOSS = EndBlocks.registerBlock("ivis_moss", new BlockWallPlant());
	public static final Block IVIS_VINE = EndBlocks.registerBlock("ivis_vine", new BlockVine());
	
	public static void register() {}
}
