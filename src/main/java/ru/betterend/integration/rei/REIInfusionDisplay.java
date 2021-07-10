package ru.betterend.integration.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;
import ru.betterend.recipe.builders.InfusionRecipe;

import java.util.Collections;
import java.util.Optional;

public class REIInfusionDisplay extends BasicDisplay implements SimpleGridMenuDisplay {
	
	private final InfusionRecipe recipe;
	private final int time;
	
	public REIInfusionDisplay(InfusionRecipe recipe) {
		super(EntryIngredients.ofIngredients(recipe.getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.getResultItem())));
		this.recipe = recipe;
		this.time = recipe.getInfusionTime();
	}
	
	public int getInfusionTime() {
		return this.time;
	}
	
	@Override
	public @NotNull Optional<ResourceLocation> getDisplayLocation() {
		return Optional.ofNullable(recipe).map(Recipe::getId);
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REIPlugin.INFUSION;
	}
	
	// @Override
	// public @NotNull List<List<EntryStack>> getRequiredEntries() {
	// 	return this.input;
	// }
	
	@Override
	public int getWidth() {
		return 0;
	}
	
	@Override
	public int getHeight() {
		return 0;
	}
	
	// @Override
	// public List<List<EntryStack>> getOrganisedInputEntries(ContainerInfo<AbstractContainerMenu> containerInfo, AbstractContainerMenu container) {
	//	return this.input;
	//}
}
