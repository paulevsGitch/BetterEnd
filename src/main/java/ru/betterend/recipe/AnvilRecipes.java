package ru.betterend.recipe;

import net.minecraft.world.item.Items;
import ru.betterend.item.material.EndToolMaterial;
import ru.betterend.recipe.builders.AnvilRecipe;
import ru.betterend.registry.EndItems;

public class AnvilRecipes {
	public static void register() {
		AnvilRecipe.Builder.create("ender_pearl_to_dust").setInput(Items.ENDER_PEARL).setOutput(EndItems.ENDER_DUST)
				.setToolLevel(4).setDamage(5).build();
		AnvilRecipe.Builder.create("ender_shard_to_dust").setInput(EndItems.ENDER_SHARD).setOutput(EndItems.ENDER_DUST)
				.setToolLevel(0).setDamage(3).build();

		int anvilLevel = EndToolMaterial.AETERNIUM.getLevel();
		AnvilRecipe.Builder.create("aeternium_axe_head").setInput(EndItems.AETERNIUM_INGOT)
				.setOutput(EndItems.AETERNIUM_AXE_HEAD).setAnvilLevel(anvilLevel).setToolLevel(4).setDamage(6).build();
		AnvilRecipe.Builder.create("aeternium_pickaxe_head").setInput(EndItems.AETERNIUM_INGOT)
				.setOutput(EndItems.AETERNIUM_PICKAXE_HEAD).setAnvilLevel(anvilLevel).setToolLevel(4).setDamage(6)
				.build();
		AnvilRecipe.Builder.create("aeternium_shovel_head").setInput(EndItems.AETERNIUM_INGOT)
				.setOutput(EndItems.AETERNIUM_SHOVEL_HEAD).setAnvilLevel(anvilLevel).setToolLevel(4).setDamage(6)
				.build();
		AnvilRecipe.Builder.create("aeternium_hoe_head").setInput(EndItems.AETERNIUM_INGOT)
				.setOutput(EndItems.AETERNIUM_HOE_HEAD).setAnvilLevel(anvilLevel).setToolLevel(4).setDamage(6).build();
		AnvilRecipe.Builder.create("aeternium_hammer_head").setInput(EndItems.AETERNIUM_INGOT)
				.setOutput(EndItems.AETERNIUM_HAMMER_HEAD).setAnvilLevel(anvilLevel).setToolLevel(4).setDamage(6)
				.build();
		AnvilRecipe.Builder.create("aeternium_sword_blade").setInput(EndItems.AETERNIUM_INGOT)
				.setOutput(EndItems.AETERNIUM_SWORD_BLADE).setAnvilLevel(anvilLevel).setToolLevel(4).setDamage(6)
				.build();
	}
}
