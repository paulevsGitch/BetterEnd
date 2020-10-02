package ru.betterend.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;

@Environment(EnvType.CLIENT)
public class EndStoneSmelterScreen extends HandledScreen<EndStoneSmelterScreenHandler> implements RecipeBookProvider {

	private final static Identifier RECIPE_BUTTON_TEXTURE = new Identifier("textures/gui/recipe_button.png");
	private final static Identifier BACKGROUND_TEXTURE = BetterEnd.getIdentifier("textures/gui/smelter_gui.png");
	
	public final EndStoneSmelterRecipeBookScreen recipeBook;
	private boolean narrow;
	
	public EndStoneSmelterScreen(EndStoneSmelterScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.recipeBook = new EndStoneSmelterRecipeBookScreen();
	}
	
	public void init() {
		super.init();
		this.narrow = this.width < 379;
		this.recipeBook.initialize(width, height, client, narrow, handler);
		this.x = this.recipeBook.findLeftEdge(narrow, width, backgroundWidth);
		this.addButton(new TexturedButtonWidget(x + 20, height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, (buttonWidget) -> {
			this.recipeBook.reset(narrow);
			this.recipeBook.toggleOpen();
			this.x = this.recipeBook.findLeftEdge(narrow, width, backgroundWidth);
			((TexturedButtonWidget) buttonWidget).setPos(this.x + 20, height / 2 - 49);
		}));
		this.titleX = (this.backgroundWidth - this.textRenderer.getWidth((StringVisitable)this.title)) / 2;
	}

	@Override
	public void tick() {
		super.tick();
		this.recipeBook.update();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		if (this.recipeBook.isOpen() && this.narrow) {
			this.drawBackground(matrices, delta, mouseX, mouseY);
			this.recipeBook.render(matrices, mouseX, mouseY, delta);
		} else {
			this.recipeBook.render(matrices, mouseX, mouseY, delta);
			super.render(matrices, mouseX, mouseY, delta);
			this.recipeBook.drawGhostSlots(matrices, x, y, true, delta);
		}

		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
		this.recipeBook.drawTooltip(matrices, x, y, mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.recipeBook.mouseClicked(mouseX, mouseY, button)) {
			return true;
		} else {
			return this.narrow && this.recipeBook.isOpen() ? true : super.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	protected void onMouseClick(Slot slot, int invSlot, int clickData, SlotActionType actionType) {
		super.onMouseClick(slot, invSlot, clickData, actionType);
		this.recipeBook.slotClicked(slot);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return this.recipeBook.keyPressed(keyCode, scanCode, modifiers) ? false : super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
		boolean isMouseOut = mouseX < left || mouseY < top || mouseX >= (left + backgroundWidth) || mouseY >= (top + backgroundHeight);
		return this.recipeBook.isClickOutsideBounds(mouseX, mouseY, x, y, backgroundWidth, backgroundHeight, button) && isMouseOut;
	}

	@Override
	public boolean charTyped(char chr, int keyCode) {
		return this.recipeBook.charTyped(chr, keyCode) ? true : super.charTyped(chr, keyCode);
	}

	@Override
	public void refreshRecipeBook() {
		this.recipeBook.refresh();
	}

	@Override
	public RecipeBookWidget getRecipeBookWidget() {
		return this.recipeBook;
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		this.drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
		int p;
		if (handler.isBurning()) {
			p = handler.getFuelProgress();
			this.drawTexture(matrices, x + 56, y + 36 + 12 - p, 176, 12 - p, 14, p + 1);
		}
		p = handler.getSmeltProgress();
		this.drawTexture(matrices, x + 92, y + 34, 176, 14, p + 1, 16);
	}

	@Override
	public void removed() {
		this.recipeBook.close();
		super.removed();
	}
}
