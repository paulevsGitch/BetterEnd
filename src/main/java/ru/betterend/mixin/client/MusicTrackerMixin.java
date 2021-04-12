package ru.betterend.mixin.client;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.betterend.client.ClientOptions;

@Mixin(MusicManager.class)
public class MusicTrackerMixin {
	@Shadow
	@Final
	private Minecraft client;
	
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
	public void be_onTick(CallbackInfo info) {
		if (ClientOptions.blendBiomeMusic()) {
			Music musicSound = client.getSituationalMusic();
			if (be_checkNullSound(musicSound) && volume > 0 && be_isInEnd() && be_shouldChangeSound(musicSound)) {
				if (volume > 0) {
					if (srcVolume < 0) {
						srcVolume = current.getVolume();
					}
					if (current instanceof AbstractSoundInstance) {
						((AbstractSoundInstanceAccessor) current).setVolume(volume);
					}
					client.getSoundManager().updateSourceVolume(current.getSource(), current.getVolume() * volume);
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
					this.timeUntilNextSong = Mth.nextInt(this.random, 0, musicSound.getMinDelay() / 2);
					this.current = null;
				}
				if (this.current == null && this.timeUntilNextSong-- <= 0) {
					this.play(musicSound);
				}
				info.cancel();
			}
			else {
				volume = 1;
			}
		}
	}
	
	private boolean be_isInEnd() {
		return client.level != null && client.level.dimension().equals(Level.END);
	}
	
	private boolean be_shouldChangeSound(Music musicSound) {
		return current != null && !musicSound.getEvent().getLocation().equals(this.current.getLocation()) && musicSound.replaceCurrentMusic();
	}
	
	private boolean be_checkNullSound(Music musicSound) {
		return musicSound != null && musicSound.getEvent() != null;
	}
	
	@Shadow
	public void play(Music type) {}
}
