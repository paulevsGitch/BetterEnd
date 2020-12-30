package ru.betterend.integration.rei;

import me.shedaniel.rei.plugin.containers.CraftingContainerInfoWrapper;
import me.shedaniel.rei.server.ContainerInfoHandler;
import ru.betterend.client.gui.EndStoneSmelterScreenHandler;
import ru.betterend.recipe.builders.AlloyingRecipe;

public class REIContainer implements Runnable {

	@Override
	public void run() {
		ContainerInfoHandler.registerContainerInfo(AlloyingRecipe.ID, CraftingContainerInfoWrapper.create(EndStoneSmelterScreenHandler.class));
	}
}
