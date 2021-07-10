package ru.betterend.integration.rei;

import com.google.common.collect.Lists;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.DefaultPlugin;
import net.fabricmc.fabric.impl.content.registry.FuelRegistryImpl;
import net.minecraft.resources.ResourceLocation;
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
import java.util.List;
import java.util.stream.Collectors;

//https://github.com/shedaniel/RoughlyEnoughItems/blob/6.x-1.17/default-plugin/src/main/java/me/shedaniel/rei/plugin/client/DefaultClientPlugin.java
public class REIPlugin implements REIClientPlugin {
	public final static ResourceLocation PLUGIN_ID = BetterEnd.makeID("rei_plugin");
	public final static CategoryIdentifier ALLOYING_FUEL = CategoryIdentifier.of(BetterEnd.MOD_ID, "alloying_fuel");
	public final static CategoryIdentifier ALLOYING = CategoryIdentifier.of(BetterEnd.MOD_ID, AlloyingRecipe.GROUP);
	public final static CategoryIdentifier SMITHING = CategoryIdentifier.of(BetterEnd.MOD_ID, AnvilRecipe.ID.getPath());
	public final static CategoryIdentifier INFUSION = CategoryIdentifier.of(BetterEnd.MOD_ID, InfusionRecipe.GROUP);
	
	private EntryStack END_STONE_SMELTER;
	private EntryStack INFUSION_RITUAL;
	private EntryStack[] ANVILS;
	private EntryStack[] FURNACES;
	
	void init() {
		//we need to initialize those variables after the static initialization
		//otherwise the registry does not know the BlockItems
		if (END_STONE_SMELTER != null) {
			return;
		}
		
		END_STONE_SMELTER = EntryStacks.of(EndBlocks.END_STONE_SMELTER);
		INFUSION_RITUAL = EntryStacks.of(EndBlocks.INFUSION_PEDESTAL);
		
		List<EntryStack> anvils = Lists.newArrayList(EntryIngredients.ofItems(EndBlocks.getModBlocks().stream().filter(EndAnvilBlock.class::isInstance).collect(Collectors.toList())));
		anvils.add(0, EntryStacks.of(Blocks.ANVIL));
		ANVILS = anvils.toArray(new EntryStack[0]);
		
		FURNACES = Lists.newArrayList(EntryIngredients.ofItems(EndBlocks.getModBlocks().stream().filter(BaseFurnaceBlock.class::isInstance).collect(Collectors.toList()))).toArray(new EntryStack[0]);
	}
	
	@Override
	public void registerDisplays(DisplayRegistry registry) {
		registry.registerRecipeFiller(AlloyingRecipe.class, AlloyingRecipe.TYPE, REIAlloyingDisplay::new);
		registry.registerRecipeFiller(BlastingRecipe.class, RecipeType.BLASTING, REIBlastingDisplay::new);
		registry.registerRecipeFiller(AnvilRecipe.class, AnvilRecipe.TYPE, REIAnvilDisplay::new);
		registry.registerRecipeFiller(InfusionRecipe.class, InfusionRecipe.TYPE, REIInfusionDisplay::new);
		
		FuelRegistryImpl.INSTANCE.getFuelTimes().forEach((item, time) -> {
			if (time >= 2000) {
				final List<EntryIngredient> list = Arrays.asList(EntryIngredients.of(item));
				registry.add(new REIAlloyingFuelDisplay(list, time));
			}
		});
	}
	
	@Override
	public void registerCategories(CategoryRegistry registry) {
		init();
		
		registry.add(new REIAlloyingFuelCategory(), new REIAlloyingCategory(END_STONE_SMELTER), new REIInfusionCategory(INFUSION_RITUAL), new REIAnvilCategory(ANVILS));
		
		registry.addWorkstations(ALLOYING_FUEL, END_STONE_SMELTER);
		registry.addWorkstations(ALLOYING, END_STONE_SMELTER);
		registry.addWorkstations(INFUSION, INFUSION_RITUAL);
		registry.addWorkstations(SMITHING, ANVILS);
		registry.removePlusButton(ALLOYING_FUEL);
		registry.removePlusButton(SMITHING);
		
		registry.addWorkstations(DefaultPlugin.SMELTING, FURNACES);
		registry.addWorkstations(DefaultPlugin.FUEL, FURNACES);
	}
}
