package ru.betterend.mixin.client;

import java.util.List;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import ru.betterend.interfaces.AnvilScreenHandlerExtended;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin extends ItemCombinerScreen<AnvilMenu> {

	@Shadow
	private EditBox nameField;
	
	private final List<AbstractWidget> be_buttons = Lists.newArrayList();
	private AnvilScreenHandlerExtended anvilHandler;
	
	public AnvilScreenMixin(AnvilMenu handler, Inventory playerInventory, Component title,
			ResourceLocation texture) {
		super(handler, playerInventory, title, texture);
	}

	@Inject(method = "setup", at = @At("TAIL"))
	protected void be_setup(CallbackInfo info) {
		this.be_buttons.clear();
		int x = (width - imageWidth) / 2;
	    int y = (height - imageHeight) / 2;
	    this.anvilHandler = (AnvilScreenHandlerExtended) this.menu;
	    this.be_buttons.add(new Button(x + 8, y + 45, 15, 20, new TextComponent("<"), (b) -> be_previousRecipe()));
		this.be_buttons.add(new Button(x + 154, y + 45, 15, 20, new TextComponent(">"), (b) -> be_nextRecipe()));
	}
	
	@Inject(method = "renderForeground", at = @At("TAIL"))
	protected void be_renderForeground(PoseStack matrices, int mouseX, int mouseY, float delta, CallbackInfo info) {
		this.be_buttons.forEach(button -> {
			button.render(matrices, mouseX, mouseY, delta);
		});
	}
	
	@Inject(method = "onSlotUpdate", at = @At("HEAD"), cancellable = true)
	public void be_onSlotUpdate(AbstractContainerMenu handler, int slotId, ItemStack stack, CallbackInfo info) {
		AnvilScreenHandlerExtended anvilHandler = (AnvilScreenHandlerExtended) handler;
		if (anvilHandler.be_getCurrentRecipe() != null) {
			if (anvilHandler.be_getRecipes().size() > 1) {
				this.be_buttons.forEach(button -> button.visible = true);
			} else {
				this.be_buttons.forEach(button -> button.visible = false);
			}
			this.nameField.setValue("");
			info.cancel();
		} else {
			this.be_buttons.forEach(button -> button.visible = false);
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
		if (minecraft != null) {
			for (AbstractWidget elem : be_buttons) {
				if (elem.visible && elem.mouseClicked(mouseX, mouseY, button)) {
					if (minecraft.gameMode != null) {
						int i = be_buttons.indexOf(elem);
						this.minecraft.gameMode.handleInventoryButtonClick(menu.containerId, i);
						return true;
					}
				}
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
}
