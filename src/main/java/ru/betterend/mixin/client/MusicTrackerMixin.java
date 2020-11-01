package ru.betterend.mixin.client;

import java.util.Random;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.MusicSound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

@Mixin(MusicTracker.class)
public class MusicTrackerMixin {
	@Shadow
	@Final
	private MinecraftClient client;
	
	@Shadow
	@Final
	private Random random;
	
	@Shadow
	private SoundInstance current;
	
	@Shadow
	private int timeUntilNextSong;
	
	private static float volume = 1;
	private static float srcVolume = 0;
	private static long time;
	
	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void beOnTick(CallbackInfo info) {
		MusicSound musicSound = client.getMusicType();
		if (volume > 0 && beIsInEnd() && beShouldChangeSound(musicSound)) {
			if (volume > 0) {
				if (srcVolume < 0) {
					srcVolume = current.getVolume();
				}
				if (current instanceof AbstractSoundInstance) {
					((AbstractSoundInstanceAccessor) current).setVolume(volume);
				}
				client.getSoundManager().updateSoundVolume(current.getCategory(), current.getVolume() * volume);
				long t = System.currentTimeMillis();
				if (volume == 1 && time == 0) {
					time = t;
				}
				float delta = (t - time) * 0.0005F;
				time = t;
				volume -= delta;
				if (volume < 0) {
					volume = 0;
				}
			}
			if (volume == 0) {
				volume = 1;
				time = 0;
				srcVolume = -1;
				this.client.getSoundManager().stop(this.current);
				this.timeUntilNextSong = MathHelper.nextInt(this.random, 0, musicSound.getMinDelay() / 2);
				this.current = null;
			}
			if (this.current == null && this.timeUntilNextSong-- <= 0) {
				this.play(musicSound);
			}
			System.out.println(volume);
			info.cancel();
		}
		else {
			volume = 1;
		}
	}
	
	private boolean beIsInEnd() {
		return client.world != null && client.world.getRegistryKey().equals(World.END);
	}
	
	private boolean beShouldChangeSound(MusicSound musicSound) {
		return current != null && !musicSound.getSound().getId().equals(this.current.getId()) && musicSound.shouldReplaceCurrentMusic();
	}
	
	@Shadow
	public void play(MusicSound type) {}
}
