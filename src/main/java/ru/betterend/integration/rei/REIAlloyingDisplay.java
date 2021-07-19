package ru.betterend.integration.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;
import ru.betterend.blocks.entities.EndStoneSmelterBlockEntity;
import ru.betterend.recipe.builders.AlloyingRecipe;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class REIAlloyingDisplay extends BasicDisplay implements SimpleGridMenuDisplay {
	
	private static List<EntryStack> fuel;
	
	private Recipe<?> recipe;
	private float xp;
	private double smeltTime;
	
	
	public REIAlloyingDisplay(AlloyingRecipe recipe) {
		this(recipe, recipe.getExperience(), recipe.getSmeltTime());
	}
	
	protected REIAlloyingDisplay(Recipe<?> recipe, float xp, double smeltTime) {
		super(
			EntryIngredients.ofIngredients(recipe.getIngredients()),
			Collections.singletonList(EntryIngredients.of(recipe.getResultItem()))
		);
		this.recipe = recipe;
		this.xp = xp;
		this.smeltTime = smeltTime;
	}
	
	
	public static List<EntryStack> getFuel() {
		return fuel;
	}
	
	@Override
	public @NotNull Optional<ResourceLocation> getDisplayLocation() {
		return Optional.ofNullable(recipe).map(Recipe::getId);
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REIPlugin.ALLOYING;
	}
	
	// @Override
	// public @NotNull List<List<EntryStack>> getRequiredEntries() {
	// 	return this.input;
	// }
	
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
	
	// @Override
	// public List<List<EntryStack>> getOrganisedInputEntries(ContainerInfo<AbstractContainerMenu> containerInfo, AbstractContainerMenu container) {
	//	return this.input;
	// }
	
	static {
		fuel = EndStoneSmelterBlockEntity.availableFuels()
										 .keySet()
										 .stream()
										 .map(Item::getDefaultInstance)
										 .map(EntryStacks::of)
										 .map(e -> e.setting(
											 EntryStack.Settings.TOOLTIP_APPEND_EXTRA,
											 stack -> Collections.singletonList(new TranslatableComponent(
												 "category.rei.smelting.fuel").withStyle(ChatFormatting.YELLOW))
										 ))
										 .collect(Collectors.toList());
	}
}
