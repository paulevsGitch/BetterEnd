package ru.betterend.effects.status;

import net.minecraft.world.entity.effect.StatusEffect;
import net.minecraft.world.entity.effect.StatusEffectType;

public class EndVeilEffect extends StatusEffect {

	public EndVeilEffect() {
		super(StatusEffectType.BENEFICIAL, 0x0D554A);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return false;
	}
}
