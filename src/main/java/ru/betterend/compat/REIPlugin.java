package ru.betterend.compat;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.Blocks;
import net.minecraft.recipe.BlastingRecipe;
import net.minecraft.util.Identifier;

import ru.betterend.BetterEnd;
import ru.betterend.recipe.AlloyingRecipe;
import ru.betterend.recipe.AnvilSmithingRecipe;
import ru.betterend.registry.BlockRegistry;

@Environment(EnvType.CLIENT)
public class REIPlugin implements REIPluginV0 {

	public final static Identifier PLUGIN_ID = BetterEnd.makeID("rei_plugin");
	public final static Identifier ALLOING = AlloyingRecipe.ID;
	public final static Identifier SMITHING = AlloyingRecipe.ID;
	
	public final static EntryStack END_STONE_SMELTER = EntryStack.create(BlockRegistry.END_STONE_SMELTER);
	public final static EntryStack ANVIL = EntryStack.create(Blocks.ANVIL);
	
	@Override
	public Identifier getPluginIdentifier() {
		return PLUGIN_ID;
	}
	
	@Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		recipeHelper.registerRecipes(ALLOING, AlloyingRecipe.class, REIAlloyingDisplay::new);
		recipeHelper.registerRecipes(ALLOING, BlastingRecipe.class, REIAlloyingDisplay::new);
		recipeHelper.registerRecipes(SMITHING, AnvilSmithingRecipe.class, REIAnvilDisplay::new);
	}
	
	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(ALLOING, END_STONE_SMELTER);
		recipeHelper.registerWorkingStations(SMITHING, ANVIL);
		recipeHelper.removeAutoCraftButton(SMITHING);
    }
	
	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
		recipeHelper.registerCategories(new REIAlloyingCategory(),
										new REIAnvilCategory());
	}
}
