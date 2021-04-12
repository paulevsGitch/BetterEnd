package ru.betterend.effects.status;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectType;

public class EndVeilEffect extends MobEffect {

	public EndVeilEffect() {
		super(MobEffectType.BENEFICIAL, 0x0D554A);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return false;
	}
}
