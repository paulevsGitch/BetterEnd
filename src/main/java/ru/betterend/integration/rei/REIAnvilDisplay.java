package ru.betterend.integration.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;
import ru.betterend.recipe.builders.AnvilRecipe;

import java.util.Collections;
import java.util.Optional;

public class REIAnvilDisplay extends BasicDisplay implements SimpleGridMenuDisplay {

	private final AnvilRecipe recipe;

	public REIAnvilDisplay(AnvilRecipe recipe) {
		super(
				EntryIngredients.ofIngredients(recipe.getIngredients()),
				Collections.singletonList(EntryIngredients.of(recipe.getResultItem()))
		);
		this.recipe = recipe;
	}

	public int getDamage() {
		return recipe.getDamage();
	}

	public int getInputCount() {
		return recipe.getInputCount();
	}

	public int getAnvilLevel() {
		return recipe.getAnvilLevel();
	}

	@Override
	public @NotNull Optional<ResourceLocation> getDisplayLocation() {
		return Optional.ofNullable(recipe).map(Recipe::getId);
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REIPlugin.SMITHING;
	}

	// @Override
	// public @NotNull List<List<EntryStack>> getRequiredEntries() {
	// 	return input;
	// }

	@Override
	public int getWidth() {
		return 2;
	}

	@Override
	public int getHeight() {
		return 1;
	}

	// @Override
	// public List<List<EntryStack>> getOrganisedInputEntries(ContainerInfo<AbstractContainerMenu> containerInfo,
	//		AbstractContainerMenu container) {
	//	return input;
	// }
}
