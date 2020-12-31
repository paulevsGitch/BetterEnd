package ru.betterend.integration.rei;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.recipe.BlastingRecipe;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;
import ru.betterend.recipe.builders.AlloyingRecipe;
import ru.betterend.recipe.builders.AnvilSmithingRecipe;
import ru.betterend.recipe.builders.InfusionRecipe;
import ru.betterend.registry.EndBlocks;

@Environment(EnvType.CLIENT)
public class REIPlugin implements REIPluginV0 {

	public final static Identifier PLUGIN_ID = BetterEnd.makeID("rei_plugin");
	public final static Identifier ALLOYING = AlloyingRecipe.ID;
	public final static Identifier SMITHING = AnvilSmithingRecipe.ID;
	public final static Identifier INFUSION = InfusionRecipe.ID;
	
	public final static EntryStack END_STONE_SMELTER = EntryStack.create(EndBlocks.END_STONE_SMELTER);
	public final static EntryStack INFUSION_RITUAL = EntryStack.create(EndBlocks.INFUSION_PEDESTAL);
	public final static EntryStack ANVIL = EntryStack.create(Blocks.ANVIL);
	
	@Override
	public Identifier getPluginIdentifier() {
		return PLUGIN_ID;
	}
	
	@Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		recipeHelper.registerRecipes(ALLOYING, AlloyingRecipe.class, REIAlloyingDisplay::new);
		recipeHelper.registerRecipes(ALLOYING, BlastingRecipe.class, REIAlloyingDisplay::new);
		recipeHelper.registerRecipes(SMITHING, AnvilSmithingRecipe.class, REIAnvilDisplay::new);
		recipeHelper.registerRecipes(INFUSION, InfusionRecipe.class, REIInfusionDisplay::new);
	}
	
	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(ALLOYING, END_STONE_SMELTER);
		recipeHelper.registerWorkingStations(INFUSION, INFUSION_RITUAL);
		recipeHelper.registerWorkingStations(SMITHING, ANVIL);
		recipeHelper.removeAutoCraftButton(SMITHING);
    }
	
	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
		recipeHelper.registerCategories(new REIAlloyingCategory(),
										new REIInfusionCategory(),
										new REIAnvilCategory());
	}
}
