package ru.betterend.integration.rei;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.TransferRecipeDisplay;
import me.shedaniel.rei.server.ContainerInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.Recipe;
import ru.betterend.recipe.builders.AnvilRecipe;

public class REIAnvilDisplay implements TransferRecipeDisplay {
	
	private final AnvilRecipe recipe;
	private final List<List<EntryStack>> input;
	private final List<EntryStack> output;
	
	public REIAnvilDisplay(AnvilRecipe recipe) {
		this.recipe = recipe;
		this.input = EntryStack.ofIngredients(recipe.getIngredients());
		this.output = Collections.singletonList(EntryStack.create(recipe.getResultItem()));
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
	public @NotNull Optional<ResourceLocation> getRecipeLocation() {
		return Optional.ofNullable(recipe).map(Recipe::getId);
	}

	@Override
	public @NotNull List<List<EntryStack>> getInputEntries() {
		return this.input;
	}
	
	@Override
	public @NotNull List<List<EntryStack>> getResultingEntries() {
		return Collections.singletonList(output);
	}

	@Override
	public @NotNull ResourceLocation getRecipeCategory() {
		return REIPlugin.SMITHING;
	}
	
	@Override
	public @NotNull List<List<EntryStack>> getRequiredEntries() {
		return input;
	}

	@Override
	public int getWidth() {
		return 2;
	}

	@Override
	public int getHeight() {
		return 1;
	}

	@Override
	public List<List<EntryStack>> getOrganisedInputEntries(ContainerInfo<AbstractContainerMenu> containerInfo,
			AbstractContainerMenu container) {
		return input;
	}
}
