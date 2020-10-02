package ru.betterend.compat;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.recipe.BlastingRecipe;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;
import ru.betterend.recipe.AlloyingRecipe;
import ru.betterend.registry.BlockRegistry;

@Environment(EnvType.CLIENT)
public class REIPlugin implements REIPluginV0 {

	public final static Identifier PLUGIN_ID = BetterEnd.getIdentifier("rei_plugin");
	public final static EntryStack END_STONE_SMELTER = EntryStack.create(BlockRegistry.END_STONE_SMELTER);
	
	@Override
	public Identifier getPluginIdentifier() {
		return PLUGIN_ID;
	}
	
	@Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		recipeHelper.registerRecipes(AlloyingRecipe.ID, AlloyingRecipe.class, REIAlloyingDisplay::new);
		recipeHelper.registerRecipes(AlloyingRecipe.ID, BlastingRecipe.class, REIAlloyingDisplay::new);
	}
	
	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(AlloyingRecipe.ID, END_STONE_SMELTER);
    }
	
	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
		recipeHelper.registerCategory(new REIAlloyingCategory());
	}
}
