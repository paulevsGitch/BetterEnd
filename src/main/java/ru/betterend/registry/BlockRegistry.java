package ru.betterend.registry;

import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.AeterniumBlock;
import ru.betterend.blocks.BlockEndstoneDust;
import ru.betterend.blocks.BlockMossyGlowshroomCap;
import ru.betterend.blocks.BlockMossyGlowshroomFur;
import ru.betterend.blocks.BlockMossyGlowshroomHymenophore;
import ru.betterend.blocks.BlockOre;
import ru.betterend.blocks.BlockTerrain;
import ru.betterend.blocks.EndStoneSmelter;
import ru.betterend.blocks.EnderBlock;
import ru.betterend.blocks.TerminiteBlock;
import ru.betterend.blocks.complex.WoodenMaterial;
import ru.betterend.tab.CreativeTab;

public class BlockRegistry {
	// Terrain //
	public static final Block ENDSTONE_DUST = registerBlock("endstone_dust", new BlockEndstoneDust());
	public static final Block END_MYCELIUM = registerBlock("end_mycelium", new BlockTerrain(MaterialColor.LIGHT_BLUE));
	public static final Block END_MOSS = registerBlock("end_moss", new BlockTerrain(MaterialColor.CYAN));
	
	// Wooden Materials //
	public static final Block MOSSY_GLOWSHROOM_CAP = registerBlock("mossy_glowshroom_cap", new BlockMossyGlowshroomCap());
	public static final Block MOSSY_GLOWSHROOM_HYMENOPHORE = registerBlock("mossy_glowshroom_hymenophore", new BlockMossyGlowshroomHymenophore());
	public static final Block MOSSY_GLOWSHROOM_FUR = registerBlock("mossy_glowshroom_fur", new BlockMossyGlowshroomFur());
	public static final WoodenMaterial MOSSY_GLOWSHROOM = new WoodenMaterial("mossy_glowshroom", MaterialColor.GRAY, MaterialColor.WOOD);
	
	// Ores //
	public static final Block ENDER_ORE = registerBlock("ender_ore", new BlockOre(ItemRegistry.ENDER_DUST, 1, 3));
	
	// Materials //
	public static final Block TERMINITE_BLOCK = registerBlock("terminite_block", new TerminiteBlock());
	public static final Block AETERNIUM_BLOCK = registerBlock("aeternium_block", new AeterniumBlock());
	public static final Block ENDER_BLOCK = registerBlock("ender_block", new EnderBlock());
	
	// Block With Entities //
	public static final Block END_STONE_SMELTER = registerBlock("end_stone_smelter", new EndStoneSmelter());
	
	public static void register() {}
	
	public static Block registerBlock(String name, Block block) {
		Registry.register(Registry.BLOCK, BetterEnd.getResId(name), block);
		ItemRegistry.registerItem(name, new BlockItem(block, new Item.Settings().group(CreativeTab.END_TAB)));
		return block;
	}
	
	public static Block registerBlockNI(String name, Block block) {
		return Registry.register(Registry.BLOCK, BetterEnd.getResId(name), block);
	}
}
