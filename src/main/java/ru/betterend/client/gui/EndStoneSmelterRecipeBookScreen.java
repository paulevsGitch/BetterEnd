package ru.betterend.client.gui;

import java.util.Set;

import net.minecraft.client.gui.screen.recipebook.BlastFurnaceRecipeBookScreen;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import ru.betterend.blocks.entities.EndStoneSmelterBlockEntity;

public class EndStoneSmelterRecipeBookScreen extends BlastFurnaceRecipeBookScreen {
	private static final Text TITLE = new TranslatableText("gui.recipebook.toggleRecipes.blastable");
	
	protected Text getToggleCraftableButtonText() {
		return TITLE;
	}
	
	protected Set<Item> getAllowedFuels() {
		return EndStoneSmelterBlockEntity.availableFuels().keySet();
	}
}
