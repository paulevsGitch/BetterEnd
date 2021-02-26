package ru.betterend.integration.rei;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import me.shedaniel.rei.plugin.DefaultPlugin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.content.registry.FuelRegistryImpl;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.recipe.BlastingRecipe;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.basis.EndAnvilBlock;
import ru.betterend.blocks.basis.EndFurnaceBlock;
import ru.betterend.recipe.builders.AlloyingRecipe;
import ru.betterend.recipe.builders.AnvilRecipe;
import ru.betterend.recipe.builders.InfusionRecipe;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

@Environment(EnvType.CLIENT)
public class REIPlugin implements REIPluginV0 {

	public final static Identifier PLUGIN_ID = BetterEnd.makeID("rei_plugin");
	public final static Identifier ALLOYING_FUEL = BetterEnd.makeID("alloying_fuel");
	public final static Identifier ALLOYING = AlloyingRecipe.ID;
	public final static Identifier SMITHING = AnvilRecipe.ID;
	public final static Identifier INFUSION = InfusionRecipe.ID;

	public final static EntryStack END_STONE_SMELTER = EntryStack.create(EndBlocks.END_STONE_SMELTER);
	public final static EntryStack INFUSION_RITUAL = EntryStack.create(EndBlocks.INFUSION_PEDESTAL);
	public final static EntryStack[] FURNACES;
	public final static EntryStack[] ANVILS;

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
		FuelRegistryImpl.INSTANCE.getFuelTimes().forEach((item, time) -> {
			if (time >= 2000) {
				recipeHelper.registerDisplay(new REIAlloyingFuelDisplay(EntryStack.create(item), time));
			}
		});
	}
	
	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(ALLOYING_FUEL, END_STONE_SMELTER);
		recipeHelper.registerWorkingStations(ALLOYING, END_STONE_SMELTER);
		recipeHelper.registerWorkingStations(INFUSION, INFUSION_RITUAL);
		recipeHelper.registerWorkingStations(SMITHING, ANVILS);
		recipeHelper.removeAutoCraftButton(ALLOYING_FUEL);
		recipeHelper.removeAutoCraftButton(SMITHING);

		recipeHelper.registerWorkingStations(DefaultPlugin.SMELTING, FURNACES);
		recipeHelper.registerWorkingStations(DefaultPlugin.FUEL, FURNACES);
    }
	
	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
		recipeHelper.registerCategories(
				new REIAlloyingFuelCategory(),
				new REIAlloyingCategory(),
				new REIInfusionCategory(),
				new REIAnvilCategory());
	}

	static {
		List<EntryStack> anvils = Lists.newArrayList(EntryStack.ofItems(EndItems.getModBlocks().stream()
				.filter(item -> ((BlockItem) item).getBlock() instanceof EndAnvilBlock).collect(Collectors.toList())));
		anvils.add(0, EntryStack.create(Blocks.ANVIL));
		ANVILS = anvils.toArray(new EntryStack[0]);
		FURNACES = Lists.newArrayList(EntryStack.ofItems(EndItems.getModBlocks().stream()
				.filter(item -> ((BlockItem) item).getBlock() instanceof EndFurnaceBlock).collect(Collectors.toList())))
				.toArray(new EntryStack[0]);
	}
}
