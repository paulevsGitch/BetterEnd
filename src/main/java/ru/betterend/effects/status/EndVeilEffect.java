package ru.betterend.effects.status;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EndVeilEffect extends MobEffect {

	public EndVeilEffect() {
		super(MobEffectCategory.BENEFICIAL, 0x0D554A);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return false;
	}
}
