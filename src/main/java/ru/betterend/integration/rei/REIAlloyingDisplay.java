package ru.betterend.integration.rei;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.TransferRecipeDisplay;
import me.shedaniel.rei.server.ContainerInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import ru.betterend.blocks.entities.EndStoneSmelterBlockEntity;
import ru.betterend.recipe.builders.AlloyingRecipe;

public class REIAlloyingDisplay implements TransferRecipeDisplay {

	private static List<EntryStack> fuel;
	
	private Recipe<?> recipe;
	private List<List<EntryStack>> input;
	private List<EntryStack> output;
	private float xp;
	private double smeltTime;
	
	public REIAlloyingDisplay(AlloyingRecipe recipe) {
		this.recipe = recipe;
		this.input = EntryStack.ofIngredients(recipe.getIngredients());
		this.output = Collections.singletonList(EntryStack.create(recipe.getResultItem()));
		this.xp = recipe.getExperience();
		this.smeltTime = recipe.getSmeltTime();
	}
	
	public REIAlloyingDisplay(BlastingRecipe recipe) {
		this.recipe = recipe;
		this.input = EntryStack.ofIngredients(recipe.getIngredients());
		this.output = Collections.singletonList(EntryStack.create(recipe.getResultItem()));
		this.xp = recipe.getExperience();
		this.smeltTime = recipe.getCookingTime();
	}
	
	public static List<EntryStack> getFuel() {
		return fuel;
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
		return AlloyingRecipe.ID;
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
	
	public Optional<Recipe<?>> getOptionalRecipe() {
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
	public List<List<EntryStack>> getOrganisedInputEntries(ContainerInfo<AbstractContainerMenu> containerInfo, AbstractContainerMenu container) {
		return this.input;
	}
	
	static {
		fuel = EndStoneSmelterBlockEntity.availableFuels().keySet().stream()
				.map(Item::getDefaultInstance).map(EntryStack::create)
				.map(e -> e.setting(EntryStack.Settings.TOOLTIP_APPEND_EXTRA, stack -> Collections.singletonList(new TranslatableComponent("category.rei.smelting.fuel")
						.withStyle(ChatFormatting.YELLOW)))).collect(Collectors.toList());
	}
}
