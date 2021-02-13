package ru.betterend.integration.rei;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.recipe.BlastingRecipe;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.basis.EndAnvilBlock;
import ru.betterend.recipe.builders.AlloyingRecipe;
import ru.betterend.recipe.builders.AnvilRecipe;
import ru.betterend.recipe.builders.InfusionRecipe;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

@Environment(EnvType.CLIENT)
public class REIPlugin implements REIPluginV0 {

	public final static Identifier PLUGIN_ID = BetterEnd.makeID("rei_plugin");
	public final static Identifier ALLOYING = AlloyingRecipe.ID;
	public final static Identifier SMITHING = AnvilRecipe.ID;
	public final static Identifier INFUSION = InfusionRecipe.ID;
	
	public final static EntryStack END_STONE_SMELTER = EntryStack.create(EndBlocks.END_STONE_SMELTER);
	public final static EntryStack INFUSION_RITUAL = EntryStack.create(EndBlocks.INFUSION_PEDESTAL);
	public final static List<EntryStack> ANVILS;
	
	@Override
	public Identifier getPluginIdentifier() {
		return PLUGIN_ID;
	}
	
	@Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		recipeHelper.registerRecipes(ALLOYING, AlloyingRecipe.class, REIAlloyingDisplay::new);
		recipeHelper.registerRecipes(ALLOYING, BlastingRecipe.class, REIAlloyingDisplay::new);
		recipeHelper.registerRecipes(SMITHING, AnvilRecipe.class, REIAnvilDisplay::new);
		recipeHelper.registerRecipes(INFUSION, InfusionRecipe.class, REIInfusionDisplay::new);
	}
	
	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(ALLOYING, END_STONE_SMELTER);
		recipeHelper.registerWorkingStations(INFUSION, INFUSION_RITUAL);
		recipeHelper.registerWorkingStations(SMITHING, ANVILS.toArray(new EntryStack[]{}));
		recipeHelper.removeAutoCraftButton(SMITHING);
    }
	
	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
		recipeHelper.registerCategories(new REIAlloyingCategory(),
										new REIInfusionCategory(),
										new REIAnvilCategory());
	}

	static {
		ANVILS = Lists.newArrayList(EntryStack.ofItems(EndItems.getModBlocks().stream()
				.filter(item -> ((BlockItem) item).getBlock() instanceof EndAnvilBlock).collect(Collectors.toList())));
		ANVILS.add(0, EntryStack.create(Blocks.ANVIL));
	}
}
