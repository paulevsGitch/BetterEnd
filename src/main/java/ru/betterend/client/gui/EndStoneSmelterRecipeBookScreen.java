package ru.betterend.client.gui;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.client.gui.screen.recipebook.BlastFurnaceRecipeBookScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import ru.betterend.blocks.entities.EndStoneSmelterBlockEntity;

public class EndStoneSmelterRecipeBookScreen extends BlastFurnaceRecipeBookScreen {
	@Override
	protected Set<Item> getAllowedFuels() {
		return EndStoneSmelterBlockEntity.availableFuels().keySet();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void showGhostRecipe(Recipe<?> recipe, List<Slot> slots) {
		ItemStack result = recipe.getOutput();
		this.ghostSlots.setRecipe(recipe);
		this.ghostSlots.addSlot(Ingredient.ofStacks(result), (slots.get(3)).x, (slots.get(3)).y);
		DefaultedList<Ingredient> inputs = recipe.getPreviewInputs();
		try {
			Field outputSlot = super.getClass().getDeclaredField("outputSlot");
			outputSlot.setAccessible(true);
			outputSlot.set(Slot.class, slots.get(3));
			Field fuels = super.getClass().getDeclaredField("fuels");
			fuels.setAccessible(true);
			if (fuels.get(Set.class) == null) {
				fuels.set(Set.class, this.getAllowedFuels());
			}
			
			Field fuelIterator = super.getClass().getDeclaredField("fuelIterator");
			fuelIterator.setAccessible(true);
			fuelIterator.set(Iterator.class, ((Set<Item>) fuels.get(Set.class)).iterator());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Iterator<Ingredient> iterator = inputs.iterator();
		for(int i = 0; i < 2; i++) {
			if (!iterator.hasNext()) {
				return;
			}
			Ingredient ingredient = iterator.next();
			if (!ingredient.isEmpty()) {
				Slot slot = slots.get(i);
				this.ghostSlots.addSlot(ingredient, slot.x, slot.y);
			}
		}
	}
}
