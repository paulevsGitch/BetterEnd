package ru.betterend.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
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
	
	@Inject(method = "getSituationalMusic", at = @At("HEAD"), cancellable = true)
	private void be_getEndMusic(CallbackInfoReturnable<Music> info) {
		if (!(this.screen instanceof WinScreen) && this.player != null) {
			if (this.player.level.dimension() == Level.END) {
				if (this.gui.getBossOverlay().shouldPlayMusic() && MHelper.lengthSqr(this.player.getX(), this.player.getZ()) < 250000) {
					info.setReturnValue(Musics.END_BOSS);
				}
				else {
					Music sound = this.level.getBiomeManager().getNoiseBiomeAtPosition(this.player.blockPosition()).getBackgroundMusic().orElse(Musics.END);
					info.setReturnValue(sound);
				}
				info.cancel();
			}
		}
	}
}
