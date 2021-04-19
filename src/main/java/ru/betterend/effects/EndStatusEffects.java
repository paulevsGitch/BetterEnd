package ru.betterend.effects;

import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import ru.betterend.BetterEnd;
import ru.betterend.effects.status.EndVeilEffect;

public class EndStatusEffects {
	
	public final static MobEffect END_VEIL = registerEffect("end_veil", new EndVeilEffect());
	
	public static <E extends MobEffect> MobEffect registerEffect(String name, E effect) {
		return Registry.register(Registry.MOB_EFFECT, BetterEnd.makeID(name), effect);
	}
}
