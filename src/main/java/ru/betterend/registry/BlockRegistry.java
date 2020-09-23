package ru.betterend.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.BlockEndstoneDust;
import ru.betterend.blocks.BlockWetMycelium;
import ru.betterend.tab.CreativeTab;

public class BlockRegistry {
	public static final Block ENDSTONE_DUST = registerBlock("endstone_dust", new BlockEndstoneDust());
	public static final Block WET_MYCELIUM = registerBlock("wet_mycelium", new BlockWetMycelium());
	
	public static void register() {}
	
	private static Block registerBlock(String name, Block block)
	{
		Registry.register(Registry.BLOCK, new Identifier(BetterEnd.MOD_ID, name), block);
		ItemRegistry.registerItem(name, new BlockItem(block, new Item.Settings().group(CreativeTab.END_TAB)));
		return block;
	}
	
	public static Block registerBlockNI(String name, Block block)
	{
		return Registry.register(Registry.BLOCK, new Identifier(BetterEnd.MOD_ID, name), block);
	}
}
