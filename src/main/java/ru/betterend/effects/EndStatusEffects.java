package ru.betterend.effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.effects.status.EndVeilEffect;

public class EndStatusEffects {
	
	public final static StatusEffect END_VEIL = registerEffect("end_veil", new EndVeilEffect());
	
	public static <E extends StatusEffect> StatusEffect registerEffect(String name, E effect) {
		return Registry.register(Registry.STATUS_EFFECT, BetterEnd.makeID(name), effect);
	}
}
