package ru.betterend.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.recipebook.BlastingRecipeBookComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import ru.betterend.blocks.entities.EndStoneSmelterBlockEntity;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class EndStoneSmelterRecipeBookScreen extends BlastingRecipeBookComponent {
	private Iterator<Item> fuelIterator;
	private Set<Item> fuels;
	private Slot fuelSlot;
	private Item currentItem;
	private float frameTime;
	
	@Override
	protected Set<Item> getFuelItems() {
		return EndStoneSmelterBlockEntity.availableFuels().keySet();
	}
	
	@Override
	public void slotClicked(Slot slot) {
		super.slotClicked(slot);
		if (slot != null && slot.index < this.menu.getSize()) {
			this.fuelSlot = null;
		}
	}
	
	@Override
	public void setupGhostRecipe(Recipe<?> recipe, List<Slot> slots) {
		this.ghostRecipe.clear();
		ItemStack result = recipe.getResultItem();
		this.ghostRecipe.setRecipe(recipe);
		this.ghostRecipe.addIngredient(Ingredient.of(result), (slots.get(3)).x, (slots.get(3)).y);
		NonNullList<Ingredient> inputs = recipe.getIngredients();
		Iterator<Ingredient> iterator = inputs.iterator();
		for (int i = 0; i < 2; i++) {
			if (!iterator.hasNext()) {
				return;
			}
			Ingredient ingredient = iterator.next();
			if (!ingredient.isEmpty()) {
				Slot slot = slots.get(i);
				this.ghostRecipe.addIngredient(ingredient, slot.x, slot.y);
			}
		}
		this.fuelSlot = slots.get(2);
		if (this.fuels == null) {
			this.fuels = this.getFuelItems();
		}
		
		this.fuelIterator = this.fuels.iterator();
		this.currentItem = null;
	}
	
	@Override
	public void renderGhostRecipe(PoseStack matrices, int x, int y, boolean bl, float f) {
		this.ghostRecipe.render(matrices, minecraft, x, y, bl, f);
		if (fuelSlot != null) {
			if (!Screen.hasControlDown()) {
				this.frameTime += f;
			}
			
			int slotX = this.fuelSlot.x + x;
			int slotY = this.fuelSlot.y + y;
			GuiComponent.fill(matrices, slotX, slotY, slotX + 16, slotY + 16, 822018048);
			//TODO: test k=0
			this.minecraft.getItemRenderer()
						  .renderAndDecorateItem(minecraft.player,
							  this.getFuel().getDefaultInstance(),
							  slotX,
							  slotY,
							  0
						  );
			RenderSystem.depthFunc(516);
			GuiComponent.fill(matrices, slotX, slotY, slotX + 16, slotY + 16, 822083583);
			RenderSystem.depthFunc(515);
		}
	}
	
	private Item getFuel() {
		if (this.currentItem == null || this.frameTime > 30.0F) {
			this.frameTime = 0.0F;
			if (this.fuelIterator == null || !this.fuelIterator.hasNext()) {
				if (this.fuels == null) {
					this.fuels = this.getFuelItems();
				}
				this.fuelIterator = this.fuels.iterator();
			}
			this.currentItem = this.fuelIterator.next();
		}
		return this.currentItem;
	}
}
