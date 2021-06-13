package ru.betterend.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.world.level.Level;
import ru.bclib.interfaces.IColorProvider;
import ru.bclib.util.MHelper;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
	@Shadow
	public LocalPlayer player;
	
	@Shadow
	public Screen screen;

	@Final
	@Shadow
	public Gui gui;
	
	@Shadow
	public ClientLevel level;

	@Final
	@Shadow
	private BlockColors blockColors;

	@Final
	@Shadow
	private ItemColors itemColors;
	
	@Inject(method = "<init>*", at = @At("TAIL"))
	private void be_onInit(GameConfig args, CallbackInfo info) {
		Registry.BLOCK.forEach(block -> {
			if (block instanceof IColorProvider) {
				IColorProvider provider = (IColorProvider) block;
				blockColors.register(provider.getProvider(), block);
				itemColors.register(provider.getItemProvider(), block.asItem());
			}
		});
	}
	
	@Inject(method = "getSituationalMusic", at = @At("HEAD"), cancellable = true)
	private void be_getEndMusic(CallbackInfoReturnable<Music> info) {
		if (!(this.screen instanceof WinScreen) && this.player != null) {
			if (this.player.level.dimension() == Level.END) {
				if (this.gui.getBossOverlay().shouldPlayMusic() && MHelper.lengthSqr(this.player.getX(), this.player.getZ()) < 250000) {
					info.setReturnValue(Musics.END_BOSS);
				}
				else {
					Music sound = (Music) this.level.getBiomeManager().getNoiseBiomeAtPosition(this.player.blockPosition()).getBackgroundMusic().orElse(Musics.END);
					info.setReturnValue(sound);
				}
				info.cancel();
			}
		}
	}
}
