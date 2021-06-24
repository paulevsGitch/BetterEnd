package ru.betterend.integration.rei;

import com.google.common.collect.Lists;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.impl.ClientInternals;
import me.shedaniel.rei.plugin.common.DefaultPlugin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import ru.bclib.blocks.BaseFurnaceBlock;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.basis.EndAnvilBlock;
import ru.betterend.recipe.builders.AlloyingRecipe;
import ru.betterend.recipe.builders.AnvilRecipe;
import ru.betterend.recipe.builders.InfusionRecipe;
import ru.betterend.registry.EndBlocks;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

//https://github.com/shedaniel/RoughlyEnoughItems/blob/6.x-1.17/default-plugin/src/main/java/me/shedaniel/rei/plugin/client/DefaultClientPlugin.java
@Environment(EnvType.CLIENT)
public class REIPlugin implements REIClientPlugin {

	public final static ResourceLocation PLUGIN_ID = BetterEnd.makeID("rei_plugin");
	public final static CategoryIdentifier ALLOYING_FUEL =  CategoryIdentifier.of(BetterEnd.MOD_ID, "alloying_fuel");
	public final static CategoryIdentifier ALLOYING = AlloyingRecipe.ID;
	public final static CategoryIdentifier SMITHING = CategoryIdentifier.of(BetterEnd.MOD_ID, AnvilRecipe.ID.getPath());
	public final static CategoryIdentifier INFUSION = InfusionRecipe.ID;

	public final static EntryStack END_STONE_SMELTER = EntryStacks.of(EndBlocks.END_STONE_SMELTER);
	public final static EntryStack INFUSION_RITUAL = EntryStacks.of(EndBlocks.INFUSION_PEDESTAL);
	public final static EntryStack[] FURNACES;
	public final static EntryStack[] ANVILS;

	//@Override
	//public ResourceLocation getPluginIdentifier() {
	//	return PLUGIN_ID;
	//}

	//public REIPlugin() {
	//	ClientInternals.attachInstance((Supplier<Object>) () -> this, PLUGIN_ID);
	//}
	
	/*@Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		recipeHelper.registerRecipes(ALLOYING, AlloyingRecipe.class, REIAlloyingDisplay::new);
		recipeHelper.registerRecipes(ALLOYING, BlastingRecipe.class, REIAlloyingDisplay::new);
		recipeHelper.registerRecipes(SMITHING, AnvilRecipe.class, REIAnvilDisplay::new);
		recipeHelper.registerRecipes(INFUSION, InfusionRecipe.class, REIInfusionDisplay::new);
		FuelRegistryImpl.INSTANCE.getFuelTimes().forEach((item, time) -> {
			if (time >= 2000) {
				recipeHelper.registerDisplay(new REIAlloyingFuelDisplay(EntryStack.of(item), time));
			}
		});
	}*/
	
	@Override
	public void registerCategories(CategoryRegistry registry) {
		registry.add(
				new REIAlloyingFuelCategory(),
				new REIAlloyingCategory(),
				new REIInfusionCategory(),
				new REIAnvilCategory()
		);

		registry.addWorkstations(ALLOYING_FUEL, END_STONE_SMELTER);
		registry.addWorkstations(ALLOYING, END_STONE_SMELTER);
		registry.addWorkstations(INFUSION, INFUSION_RITUAL);
		registry.addWorkstations(SMITHING, ANVILS);
		registry.removePlusButton(ALLOYING_FUEL);
		registry.removePlusButton(SMITHING);

		registry.addWorkstations(DefaultPlugin.SMELTING, FURNACES);
		registry.addWorkstations(DefaultPlugin.FUEL, FURNACES);
	}

	static {
		List<EntryStack> anvils = Lists.newArrayList(EntryIngredients.ofItems(EndBlocks.getModBlocks().stream()
				.filter(EndAnvilBlock.class::isInstance).collect(Collectors.toList())));
		anvils.add(0, EntryStacks.of(Blocks.ANVIL));
		ANVILS = anvils.toArray(new EntryStack[0]);
		FURNACES = Lists.newArrayList(EntryIngredients.ofItems(EndBlocks.getModBlocks().stream()
				.filter(BaseFurnaceBlock.class::isInstance).collect(Collectors.toList())))
				.toArray(new EntryStack[0]);
	}
}
