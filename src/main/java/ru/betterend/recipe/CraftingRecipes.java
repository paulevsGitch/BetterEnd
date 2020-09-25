package ru.betterend.recipe;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;
import ru.betterend.registry.BlockRegistry;

public class CraftingRecipes {
	public static void register() {
		if (blockExists(BlockRegistry.ENDER_BLOCK)) {
			RecipeBuilder.make("ender_pearl_to_block", BlockRegistry.ENDER_BLOCK)
				.setShape(new String[] { "OO", "OO" })
				.addMaterial('O', Items.ENDER_PEARL)
				.build();
			RecipeBuilder.make("ender_block_to_pearl", Items.ENDER_PEARL)
				.addMaterial('#', BlockRegistry.ENDER_BLOCK)
				.setOutputCount(4)
				.setList("#")
				.build();
		}
		if (blockExists(BlockRegistry.END_STONE_SMELTER)) {
			RecipeBuilder.make("end_stone_smelter", BlockRegistry.END_STONE_SMELTER)
				.setShape(new String[] { "###", "V#V", "###" })
				.addMaterial('#', Blocks.END_STONE_BRICKS)
				.addMaterial('V', Items.BUCKET)
				.build();
				
		}
	}
	
	protected static boolean itemExists(Item item) {
		return Registry.ITEM.getId(item) != Registry.ITEM.getDefaultId();
	}

	protected static boolean blockExists(Block block) {
		return Registry.BLOCK.getId(block) != Registry.BLOCK.getDefaultId();
	}
}
