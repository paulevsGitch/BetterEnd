package ru.betterend.recipe;

import net.minecraft.item.Items;
import ru.betterend.recipe.builders.InfusionRecipe;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class InfusionRecipes {
	public static void register() {
		InfusionRecipe.Builder.create("runed_flavolite")
			.setInput(EndBlocks.FLAVOLITE.polished)
			.setOutput(EndBlocks.FLAVOLITE_RUNED)
			.addCatalyst(0, EndItems.CRYSTAL_SHARDS)
			.addCatalyst(2, EndItems.CRYSTAL_SHARDS)
			.addCatalyst(4, EndItems.CRYSTAL_SHARDS)
			.addCatalyst(6, EndItems.CRYSTAL_SHARDS)
			.setTime(100)
			.build();
		
		InfusionRecipe.Builder.create("eternal_crystal")
			.setInput(Items.END_CRYSTAL)
			.setOutput(EndItems.ETERNAL_CRYSTAL)
			.addCatalyst(0, EndItems.CRYSTAL_SHARDS)
			.addCatalyst(2, EndItems.CRYSTAL_SHARDS)
			.addCatalyst(4, EndItems.CRYSTAL_SHARDS)
			.addCatalyst(6, EndItems.CRYSTAL_SHARDS)
			.addCatalyst(1, EndItems.ENDER_SHARD)
			.addCatalyst(3, EndItems.ENDER_SHARD)
			.addCatalyst(5, EndItems.ENDER_SHARD)
			.addCatalyst(7, EndItems.ENDER_SHARD)
			.setTime(250)
			.build();
		
		InfusionRecipe.Builder.create("crystalite_helmet")
			.setInput(EndItems.TERMINITE_HELMET)
			.setOutput(EndItems.CRYSTALITE_HELMET)
			.addCatalyst(0, EndItems.AMBER_GEM)
			.addCatalyst(2, EndItems.CRYSTAL_SHARDS)
			.addCatalyst(6, EndItems.CRYSTAL_SHARDS)
			.setTime(150)
			.build();
		InfusionRecipe.Builder.create("crystalite_chestplate")
			.setInput(EndItems.TERMINITE_CHESTPLATE)
			.setOutput(EndItems.CRYSTALITE_CHESTPLATE)
			.addCatalyst(0, EndItems.AMBER_GEM)
			.addCatalyst(1, EndItems.CRYSTAL_SHARDS)
			.addCatalyst(3, EndItems.CRYSTAL_SHARDS)
			.addCatalyst(5, EndItems.CRYSTAL_SHARDS)
			.addCatalyst(7, EndItems.CRYSTAL_SHARDS)
			.setTime(300)
			.build();
		InfusionRecipe.Builder.create("crystalite_leggings")
			.setInput(EndItems.TERMINITE_LEGGINGS)
			.setOutput(EndItems.CRYSTALITE_LEGGINGS)
			.addCatalyst(0, EndItems.AMBER_GEM)
			.addCatalyst(2, EndItems.CRYSTAL_SHARDS)
			.addCatalyst(4, EndItems.CRYSTAL_SHARDS)
			.addCatalyst(6, EndItems.CRYSTAL_SHARDS)
			.setTime(225)
			.build();
		InfusionRecipe.Builder.create("crystalite_boots")
			.setInput(EndItems.TERMINITE_BOOTS)
			.setOutput(EndItems.CRYSTALITE_BOOTS)
			.addCatalyst(0, EndItems.AMBER_GEM)
			.addCatalyst(2, EndItems.CRYSTAL_SHARDS)
			.addCatalyst(6, EndItems.CRYSTAL_SHARDS)
			.setTime(150)
			.build();
	}
}
