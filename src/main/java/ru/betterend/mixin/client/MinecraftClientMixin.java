package ru.betterend.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MusicType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.MusicSound;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import ru.betterend.util.IColorProvider;
import ru.betterend.util.MHelper;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow
	public ClientPlayerEntity player;
	
	@Shadow
	public Screen currentScreen;
	
	@Shadow
	@Final
	public InGameHud inGameHud;
	
	@Shadow
	public ClientWorld world;
	
	@Shadow
	@Final
	private BlockColors blockColors;

	@Shadow
	@Final
	private ItemColors itemColors;
	
	@Inject(method = "<init>*", at = @At("TAIL"))
	private void onInit(RunArgs args, CallbackInfo info) {
		Registry.BLOCK.forEach(block -> {
			if (block instanceof IColorProvider) {
				IColorProvider provider = (IColorProvider) block;
				blockColors.registerColorProvider(provider.getProvider(), block);
				itemColors.register(provider.getItemProvider(), block.asItem());
			}
		});
	}
	
	@Inject(method = "getMusicType", at = @At("HEAD"), cancellable = true)
	private void getEndMusic(CallbackInfoReturnable<MusicSound> info) {
		if (!(this.currentScreen instanceof CreditsScreen) && this.player != null) {
			if (this.player.world.getRegistryKey() == World.END) {
				if (this.inGameHud.getBossBarHud().shouldPlayDragonMusic() && MHelper.lengthSqr(this.player.getX(), this.player.getZ()) < 250000) {
					info.setReturnValue(MusicType.DRAGON);
				}
				else {
					MusicSound sound = (MusicSound) this.world.getBiomeAccess().method_27344(this.player.getBlockPos()).getMusic().orElse(MusicType.END);
					info.setReturnValue(sound);
				}
				info.cancel();
			}
		}
	}
}
