package ru.betterend.integration.rei;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.resources.ResourceLocation;

public class REIAlloyingFuelDisplay implements RecipeDisplay {
	private final EntryStack fuel;
	private final int fuelTime;

	public REIAlloyingFuelDisplay(EntryStack fuel, int fuelTime) {
		this.fuel = fuel;
		this.fuelTime = fuelTime;
	}

	@Override
	public @NotNull List<List<EntryStack>> getInputEntries() {
		return Collections.singletonList(Collections.singletonList(fuel));
	}

	@Override
	public @NotNull List<List<EntryStack>> getResultingEntries() {
		return Collections.emptyList();
	}

	@Override
	public @NotNull ResourceLocation getRecipeCategory() {
		return REIPlugin.ALLOYING_FUEL;
	}

	public int getFuelTime() {
		return fuelTime;
	}

}
