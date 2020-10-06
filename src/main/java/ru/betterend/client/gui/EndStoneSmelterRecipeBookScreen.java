package ru.betterend.client.gui;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.recipebook.BlastFurnaceRecipeBookScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import ru.betterend.blocks.entities.EndStoneSmelterBlockEntity;

@Environment(EnvType.CLIENT)
public class EndStoneSmelterRecipeBookScreen extends BlastFurnaceRecipeBookScreen {
	private Iterator<Item> fuelIterator;
	private Set<Item> fuels;
	private Slot fuelSlot;
	private Item currentItem;
	private float frameTime;
	
	@Override
	protected Set<Item> getAllowedFuels() {
		return EndStoneSmelterBlockEntity.availableFuels().keySet();
	}
	
	@Override
	public void slotClicked(Slot slot) {
		super.slotClicked(slot);
		if (slot != null && slot.id < this.craftingScreenHandler.getCraftingSlotCount()) {
			this.fuelSlot = null;
		}
	}
	
	@Override
	public void showGhostRecipe(Recipe<?> recipe, List<Slot> slots) {
		this.ghostSlots.reset();
		ItemStack result = recipe.getOutput();
		this.ghostSlots.setRecipe(recipe);
		this.ghostSlots.addSlot(Ingredient.ofStacks(result), (slots.get(3)).x, (slots.get(3)).y);
		DefaultedList<Ingredient> inputs = recipe.getPreviewInputs();
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
		this.fuelSlot = slots.get(2);
		if (this.fuels == null) {
			this.fuels = this.getAllowedFuels();
		}

		this.fuelIterator = this.fuels.iterator();
		this.currentItem = null;
	}
	
	@Override
	public void drawGhostSlots(MatrixStack matrices, int x, int y, boolean bl, float f) {
		this.ghostSlots.draw(matrices, client, x, y, bl, f);
		if (fuelSlot != null) {
			if (!Screen.hasControlDown()) {
				this.frameTime += f;
			}

			int slotX = this.fuelSlot.x + x;
			int slotY = this.fuelSlot.y + y;
			DrawableHelper.fill(matrices, slotX, slotY, slotX + 16, slotY + 16, 822018048);
			this.client.getItemRenderer().renderInGuiWithOverrides(client.player, this.getItem().getDefaultStack(), slotX, slotY);
			RenderSystem.depthFunc(516);
			DrawableHelper.fill(matrices, slotX, slotY, slotX + 16, slotY + 16, 822083583);
			RenderSystem.depthFunc(515);
		}
	}

	private Item getItem() {
		if (this.currentItem == null || this.frameTime > 30.0F) {
			this.frameTime = 0.0F;
			if (this.fuelIterator == null || !this.fuelIterator.hasNext()) {
				if (this.fuels == null) {
					this.fuels = this.getAllowedFuels();
				}
				this.fuelIterator = this.fuels.iterator();
			}
			this.currentItem = this.fuelIterator.next();
		}
		return this.currentItem;
	}
}
