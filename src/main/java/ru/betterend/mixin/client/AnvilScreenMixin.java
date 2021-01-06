package ru.betterend.mixin.client;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import ru.betterend.interfaces.AnvilScreenHandlerExtended;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin extends ForgingScreen<AnvilScreenHandler> {

	@Shadow
	private TextFieldWidget nameField;
	
	private List<AbstractButtonWidget> be_buttons = Lists.newArrayList();
	private AnvilScreenHandlerExtended anvilHandler;
	
	public AnvilScreenMixin(AnvilScreenHandler handler, PlayerInventory playerInventory, Text title,
			Identifier texture) {
		super(handler, playerInventory, title, texture);
	}

	@Inject(method = "setup", at = @At("TAIL"))
	protected void setup(CallbackInfo info) {
		this.be_buttons.clear();
		int x = (width - backgroundWidth) / 2;
	    int y = (height - backgroundHeight) / 2;
	    this.anvilHandler = AnvilScreenHandlerExtended.class.cast(this.handler);
	    this.be_buttons.add(new ButtonWidget(x + 8, y + 45, 15, 20, new LiteralText("<"), (b) -> be_previousRecipe()));
		this.be_buttons.add(new ButtonWidget(x + 154, y + 45, 15, 20, new LiteralText(">"), (b) -> be_nextRecipe()));
	}
	
	@Inject(method = "renderForeground", at = @At("TAIL"))
	protected void renderForeground(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo info) {
		if (anvilHandler.be_getRecipes().size() > 1) {
			this.be_buttons.forEach(button -> button.render(matrices, mouseX, mouseY, delta));
		}
	}
	
	@Inject(method = "onSlotUpdate", at = @At("HEAD"), cancellable = true)
	public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack, CallbackInfo info) {
		if (anvilHandler.be_getCurrentRecipe() != null) {
			this.nameField.setText("");
			this.nameField.setEditable(false);
			this.setFocused(null);
			info.cancel();
		}
	}
	
	private void be_nextRecipe() {
		this.anvilHandler.be_nextRecipe();
	}
	
	private void be_previousRecipe() {
		this.anvilHandler.be_previousRecipe();
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (AbstractButtonWidget elem : be_buttons) {
			if (elem.mouseClicked(mouseX, mouseY, button)) {
				int i = be_buttons.indexOf(elem);
				this.client.interactionManager.clickButton(handler.syncId, i);
				return true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
}
