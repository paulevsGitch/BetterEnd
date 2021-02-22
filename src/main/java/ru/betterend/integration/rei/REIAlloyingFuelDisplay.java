package ru.betterend.integration.rei;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.plugin.DefaultPlugin;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class REIAlloyingFuelDisplay implements RecipeDisplay {
	private EntryStack fuel;
	private int fuelTime;

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
	public @NotNull Identifier getRecipeCategory() {
		return REIPlugin.ALLOYING_FUEL;
	}

	public int getFuelTime() {
		return fuelTime;
	}

}
