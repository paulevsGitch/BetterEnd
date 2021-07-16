package ru.betterend.integration.rei;

import com.google.common.collect.Lists;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.DefaultPlugin;
import net.fabricmc.fabric.impl.content.registry.FuelRegistryImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import ru.bclib.blocks.BaseFurnaceBlock;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.basis.EndAnvilBlock;
import ru.betterend.recipe.builders.AlloyingRecipe;
import ru.betterend.recipe.builders.AnvilRecipe;
import ru.betterend.recipe.builders.InfusionRecipe;
import ru.betterend.registry.EndBlocks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//https://github.com/shedaniel/RoughlyEnoughItems/blob/6.x-1.17/default-plugin/src/main/java/me/shedaniel/rei/plugin/client/DefaultClientPlugin.java
public class REIPlugin implements REIClientPlugin {
	public final static ResourceLocation PLUGIN_ID = BetterEnd.makeID("rei_plugin");
	public final static CategoryIdentifier<REIAlloyingFuelDisplay> ALLOYING_FUEL = CategoryIdentifier.of(BetterEnd.MOD_ID, "alloying_fuel");
	public final static CategoryIdentifier<REIAlloyingDisplay> ALLOYING = CategoryIdentifier.of(BetterEnd.MOD_ID, AlloyingRecipe.GROUP);
	public final static CategoryIdentifier<REIAnvilDisplay> SMITHING = CategoryIdentifier.of(BetterEnd.MOD_ID, AnvilRecipe.ID.getPath());
	public final static CategoryIdentifier<REIInfusionDisplay> INFUSION = CategoryIdentifier.of(BetterEnd.MOD_ID, InfusionRecipe.GROUP);
	
	@Override
	public void registerDisplays(DisplayRegistry registry) {
		registry.registerRecipeFiller(AlloyingRecipe.class, AlloyingRecipe.TYPE, REIAlloyingDisplay::new);
		registry.registerRecipeFiller(BlastingRecipe.class, RecipeType.BLASTING, REIBlastingDisplay::new);
		registry.registerRecipeFiller(AnvilRecipe.class, AnvilRecipe.TYPE, REIAnvilDisplay::new);
		registry.registerRecipeFiller(InfusionRecipe.class, InfusionRecipe.TYPE, REIInfusionDisplay::new);
		
		FuelRegistryImpl.INSTANCE.getFuelTimes().forEach((item, time) -> {
			if (time >= 2000) {
				final List<EntryIngredient> list = Collections.singletonList(EntryIngredients.of(item));
				registry.add(new REIAlloyingFuelDisplay(list, time));
			}
		});
	}
	
	@Override
	public void registerCategories(CategoryRegistry registry) {
		EntryStack<ItemStack> endStoneSmelter = EntryStacks.of(EndBlocks.END_STONE_SMELTER);
		EntryStack<ItemStack> infusionRitual = EntryStacks.of(EndBlocks.INFUSION_PEDESTAL);
		List<EntryStack<?>> anvils = Lists.newArrayList(EntryIngredients.ofItems(EndBlocks.getModBlocks().stream().filter(EndAnvilBlock.class::isInstance).collect(Collectors.toList())));
		anvils.add(0, EntryStacks.of(Blocks.ANVIL));
		List<EntryStack<?>> furnaces = Lists.newArrayList(EntryIngredients.ofItems(EndBlocks.getModBlocks().stream().filter(BaseFurnaceBlock.class::isInstance).collect(Collectors.toList())));
		EntryStack<?>[] anvilsArray = anvils.toArray(new EntryStack[0]);
		EntryStack<?>[] furnacesArray = furnaces.toArray(new EntryStack[0]);

		registry.add(new REIAlloyingFuelCategory(), new REIAlloyingCategory(endStoneSmelter), new REIInfusionCategory(infusionRitual), new REIAnvilCategory(anvilsArray));
		
		registry.addWorkstations(ALLOYING_FUEL, endStoneSmelter);
		registry.addWorkstations(ALLOYING, endStoneSmelter);
		registry.addWorkstations(INFUSION, infusionRitual);
		registry.addWorkstations(SMITHING, anvilsArray);
		registry.removePlusButton(ALLOYING_FUEL);
		registry.removePlusButton(SMITHING);
		
		registry.addWorkstations(DefaultPlugin.SMELTING, furnacesArray);
		registry.addWorkstations(DefaultPlugin.FUEL, furnacesArray);
	}
}
