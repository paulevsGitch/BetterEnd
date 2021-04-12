package ru.betterend.integration.byg;

import net.minecraft.world.level.block.Block;
import ru.betterend.blocks.basis.EndWallPlantBlock;
import ru.betterend.blocks.basis.VineBlock;
import ru.betterend.registry.EndBlocks;

public class BYGBlocks {
	public static final Block IVIS_MOSS = EndBlocks.registerBlock("ivis_moss", new EndWallPlantBlock());
	public static final Block NIGHTSHADE_MOSS = EndBlocks.registerBlock("nightshade_moss", new EndWallPlantBlock());
	
	public static final Block IVIS_VINE = EndBlocks.registerBlock("ivis_vine", new VineBlock());
	
	public static void register() {}
}
