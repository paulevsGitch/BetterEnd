package ru.betterend.compat;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.util.Identifier;

import ru.betterend.BetterEnd;
import ru.betterend.recipe.AlloyingRecipe;
import ru.betterend.registry.BlockRegistry;

@Environment(EnvType.CLIENT)
public class REICompat implements REIPluginV0 {

	public final static Identifier PLUGIN_ID = BetterEnd.getIdentifier("rei_plugin");
	public final static Identifier ALLOYING = BetterEnd.getIdentifier("alloying");
	public final static EntryStack END_STONE_SMELTER = EntryStack.create(BlockRegistry.END_STONE_SMELTER);
	
	@Override
	public Identifier getPluginIdentifier() {
		return PLUGIN_ID;
	}
	
	@Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		recipeHelper.registerRecipes(ALLOYING, AlloyingRecipe.class, REIAlloyingDisplay::new);
	}
	
	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(ALLOYING, END_STONE_SMELTER);
    }
	
	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
		recipeHelper.registerCategory(new REIAlloyingCategory());
	}
}
