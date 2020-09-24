package ru.betterend.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.AeterniumBlock;
import ru.betterend.blocks.BlockEndstoneDust;
import ru.betterend.blocks.BlockOre;
import ru.betterend.blocks.BlockWetMycelium;
import ru.betterend.blocks.EnderBlock;
import ru.betterend.blocks.TerminiteBlock;
import ru.betterend.tab.CreativeTab;

public class BlockRegistry {
	public static final Block ENDSTONE_DUST = registerBlock("endstone_dust", new BlockEndstoneDust());
	public static final Block WET_MYCELIUM = registerBlock("wet_mycelium", new BlockWetMycelium());
	public static final Block ENDER_ORE = registerBlock("ender_ore", new BlockOre(ItemRegistry.ENDER_DUST, 1, 3));
	public static final Block TERMINITE_BLOCK = registerBlock("terminite_block", new TerminiteBlock());
	public static final Block AETERNIUM_BLOCK = registerBlock("aeternium_block", new AeterniumBlock());
	public static final Block ENDER_BLOCK = registerBlock("ender_block", new EnderBlock());
	
	public static void register() {}
	
	private static Block registerBlock(String name, Block block) {
		Registry.register(Registry.BLOCK, new Identifier(BetterEnd.MOD_ID, name), block);
		ItemRegistry.registerItem(name, new BlockItem(block, new Item.Settings().group(CreativeTab.END_TAB)));
		return block;
	}
	
	public static Block registerBlockNI(String name, Block block) {
		return Registry.register(Registry.BLOCK, new Identifier(BetterEnd.MOD_ID, name), block);
	}
}
