package ru.betterend.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import ru.betterend.BetterEnd;

@Environment(EnvType.CLIENT)
public class EndStoneSmelterScreen extends AbstractContainerScreen<EndStoneSmelterScreenHandler> implements RecipeUpdateListener {

	private final static ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");
	private final static ResourceLocation BACKGROUND_TEXTURE = BetterEnd.makeID("textures/gui/smelter_gui.png");
	
	public final EndStoneSmelterRecipeBookScreen recipeBook;
	private boolean narrow;
	
	public EndStoneSmelterScreen(EndStoneSmelterScreenHandler handler, Inventory inventory, Component title) {
		super(handler, inventory, title);
		this.recipeBook = new EndStoneSmelterRecipeBookScreen();
	}
	
	public void init() {
		super.init();
		this.narrow = this.width < 379;
		this.recipeBook.init(width, height, minecraft, narrow, menu);
		this.leftPos = this.recipeBook.updateScreenPosition(narrow, width, imageWidth);
		this.addButton(new ImageButton(leftPos + 20, height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, (buttonWidget) -> {
			this.recipeBook.initVisuals(narrow);
			this.recipeBook.toggleVisibility();
			this.leftPos = this.recipeBook.updateScreenPosition(narrow, width, imageWidth);
			((ImageButton) buttonWidget).setPosition(this.leftPos + 20, height / 2 - 49);
		}));
		this.titleLabelX = (this.imageWidth - this.font.width((FormattedText)this.title)) / 2;
	}

	@Override
	public void tick() {
		super.tick();
		this.recipeBook.tick();
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		if (this.recipeBook.isVisible() && this.narrow) {
			this.renderBg(matrices, delta, mouseX, mouseY);
			this.recipeBook.render(matrices, mouseX, mouseY, delta);
		} else {
			this.recipeBook.render(matrices, mouseX, mouseY, delta);
			super.render(matrices, mouseX, mouseY, delta);
			this.recipeBook.renderGhostRecipe(matrices, leftPos, topPos, true, delta);
		}

		this.renderTooltip(matrices, mouseX, mouseY);
		this.recipeBook.renderTooltip(matrices, leftPos, topPos, mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.recipeBook.mouseClicked(mouseX, mouseY, button)) {
			return true;
		} else {
			return this.narrow && this.recipeBook.isVisible() ? true : super.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	protected void slotClicked(Slot slot, int invSlot, int clickData, ClickType actionType) {
		super.slotClicked(slot, invSlot, clickData, actionType);
		this.recipeBook.slotClicked(slot);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return this.recipeBook.keyPressed(keyCode, scanCode, modifiers) ? false : super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	protected boolean hasClickedOutside(double mouseX, double mouseY, int left, int top, int button) {
		boolean isMouseOut = mouseX < left || mouseY < top || mouseX >= (left + imageWidth) || mouseY >= (top + imageHeight);
		return this.recipeBook.hasClickedOutside(mouseX, mouseY, leftPos, topPos, imageWidth, imageHeight, button) && isMouseOut;
	}

	@Override
	public boolean charTyped(char chr, int keyCode) {
		return this.recipeBook.charTyped(chr, keyCode) ? true : super.charTyped(chr, keyCode);
	}

	@Override
	public void recipesUpdated() {
		this.recipeBook.recipesUpdated();
	}

	@Override
	public RecipeBookComponent getRecipeBookComponent() {
		return this.recipeBook;
	}

	@Override
	protected void renderBg(PoseStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(INVENTORY_LOCATION);
		this.blit(matrices, leftPos, topPos, 0, 0, imageWidth, imageHeight);
		int p;
		if (menu.isBurning()) {
			p = menu.getFuelProgress();
			this.blit(matrices, leftPos + 56, topPos + 36 + 12 - p, 176, 12 - p, 14, p + 1);
		}
		p = menu.getSmeltProgress();
		this.blit(matrices, leftPos + 92, topPos + 34, 176, 14, p + 1, 16);
	}

	@Override
	public void removed() {
		this.recipeBook.removed();
		super.removed();
	}
}
