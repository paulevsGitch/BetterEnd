package ru.betterend.compat;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.TransferRecipeDisplay;
import me.shedaniel.rei.server.ContainerInfo;

import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import ru.betterend.blocks.entities.EndStoneSmelterBlockEntity;
import ru.betterend.recipe.AlloyingRecipe;

public class REIAlloyingDisplay implements TransferRecipeDisplay {

	private static List<EntryStack> fuel;
	
	private AlloyingRecipe recipe;
	private List<List<EntryStack>> input;
	private List<EntryStack> output;
	private float xp;
	private double smeltTime;
	
	public REIAlloyingDisplay(AlloyingRecipe recipe) {
		this.recipe = recipe;
		this.input = EntryStack.ofIngredients(recipe.getPreviewInputs());
		this.output = Collections.singletonList(EntryStack.create(recipe.getOutput()));
		this.xp = recipe.getExperience();
		this.smeltTime = recipe.getSmeltTime();
	}
	
	public static List<EntryStack> getFuel() {
		return fuel;
	}
	
	@Override
	public @NotNull Optional<Identifier> getRecipeLocation() {
		return Optional.ofNullable(recipe).map(AlloyingRecipe::getId);
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
		return REICompat.ALLOYING;
	}
	
	@Override
	public @NotNull List<List<EntryStack>> getRequiredEntries() {
		return this.input;
	}
	
	public float getXp() {
		return this.xp;
	}
	
	public double getSmeltTime() {
		return this.smeltTime;
	}
	
	public Optional<AlloyingRecipe> getOptionalRecipe() {
		return Optional.ofNullable(recipe);
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
		return input;
	}
	
	static {
		fuel = EndStoneSmelterBlockEntity.availableFuels().keySet().stream()
				.map(Item::getStackForRender).map(EntryStack::create)
				.map(e -> e.setting(EntryStack.Settings.TOOLTIP_APPEND_EXTRA, stack -> Collections.singletonList(new TranslatableText("category.rei.smelting.fuel")
						.formatted(Formatting.YELLOW)))).collect(Collectors.toList());
	}
}
