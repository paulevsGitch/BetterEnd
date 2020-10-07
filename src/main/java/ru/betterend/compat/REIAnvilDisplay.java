package ru.betterend.compat;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.TransferRecipeDisplay;
import me.shedaniel.rei.server.ContainerInfo;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import ru.betterend.recipe.AnvilSmithingRecipe;

public class REIAnvilDisplay implements TransferRecipeDisplay {
	
	private AnvilSmithingRecipe recipe;
	private List<List<EntryStack>> input;
	private List<EntryStack> output;
	
	public REIAnvilDisplay(AnvilSmithingRecipe recipe) {
		this.recipe = recipe;
		this.input = EntryStack.ofIngredients(recipe.getPreviewInputs());
		this.output = Collections.singletonList(EntryStack.create(recipe.getOutput()));
	}
	
	@Override
	public @NotNull Optional<Identifier> getRecipeLocation() {
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
	public @NotNull Identifier getRecipeCategory() {
		return REIPlugin.SMITHING;
	}
	
	@Override
	public @NotNull List<List<EntryStack>> getRequiredEntries() {
		return this.input;
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
	public List<List<EntryStack>> getOrganisedInputEntries(ContainerInfo<ScreenHandler> containerInfo,
			ScreenHandler container) {
		return this.input;
	}
}
