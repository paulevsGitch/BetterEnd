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
		recipeBook = new EndStoneSmelterRecipeBookScreen();
	}
	
	public void init() {
		super.init();
		//TODO: test in 1.17
		narrow = width < 379;
		recipeBook.init(width, height, minecraft, narrow, menu);
		leftPos = recipeBook.updateScreenPosition(width, imageWidth);
		addRenderableWidget(new ImageButton(
			leftPos + 20,
			height / 2 - 49,
			20,
			18,
			0,
			0,
			19,
			RECIPE_BUTTON_TEXTURE,
			(buttonWidget) -> {
				recipeBook.initVisuals();
				recipeBook.toggleVisibility();
				leftPos = recipeBook.updateScreenPosition(width, imageWidth);
				((ImageButton) buttonWidget).setPosition(leftPos + 20, height / 2 - 49);
			}
		));
		titleLabelX = (imageWidth - font.width(title)) / 2;
	}
	
	@Override
	public void containerTick() {
		super.containerTick();
		recipeBook.tick();
	}
	
	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		if (recipeBook.isVisible() && narrow) {
			renderBg(matrices, delta, mouseX, mouseY);
			recipeBook.render(matrices, mouseX, mouseY, delta);
		}
		else {
			recipeBook.render(matrices, mouseX, mouseY, delta);
			super.render(matrices, mouseX, mouseY, delta);
			recipeBook.renderGhostRecipe(matrices, leftPos, topPos, true, delta);
		}
		renderTooltip(matrices, mouseX, mouseY);
		recipeBook.renderTooltip(matrices, leftPos, topPos, mouseX, mouseY);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.recipeBook.mouseClicked(mouseX, mouseY, button)) {
			return true;
		}
		else {
			return narrow && recipeBook.isVisible() || super.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	@Override
	protected void slotClicked(Slot slot, int invSlot, int clickData, ClickType actionType) {
		super.slotClicked(slot, invSlot, clickData, actionType);
		this.recipeBook.slotClicked(slot);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return !recipeBook.keyPressed(keyCode, scanCode, modifiers) && super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	protected boolean hasClickedOutside(double mouseX, double mouseY, int left, int top, int button) {
		boolean isMouseOut = mouseX < left || mouseY < top || mouseX >= (left + imageWidth) || mouseY >= (top + imageHeight);
		return this.recipeBook.hasClickedOutside(
			mouseX,
			mouseY,
			leftPos,
			topPos,
			imageWidth,
			imageHeight,
			button
		) && isMouseOut;
	}
	
	@Override
	public boolean charTyped(char chr, int keyCode) {
		return recipeBook.charTyped(chr, keyCode) || super.charTyped(chr, keyCode);
	}
	
	@Override
	public void recipesUpdated() {
		recipeBook.recipesUpdated();
	}
	
	@Override
	public RecipeBookComponent getRecipeBookComponent() {
		return recipeBook;
	}
	
	@Override
	protected void renderBg(PoseStack matrices, float delta, int mouseX, int mouseY) {
		if (minecraft == null) return;
		//TODO: verify
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		//minecraft.getTextureManager().bind(BACKGROUND_TEXTURE);
		blit(matrices, leftPos, topPos, 0, 0, imageWidth, imageHeight);
		int progress;
		if (menu.isBurning()) {
			progress = menu.getFuelProgress();
			blit(matrices, leftPos + 56, topPos + 36 + 12 - progress, 176, 12 - progress, 14, progress + 1);
		}
		progress = menu.getSmeltProgress();
		blit(matrices, leftPos + 92, topPos + 34, 176, 14, progress + 1, 16);
	}
	
	@Override
	public void removed() {
		recipeBook.removed();
		super.removed();
	}
}
