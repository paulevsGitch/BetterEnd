package ru.betterend.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.effects.status.EndVeilEffect;

public class EndMobEffects {

	public final static MobEffect END_VEIL = registerEffect("end_veil", new EndVeilEffect());

	public static <E extends MobEffect> MobEffect registerEffect(String name, E effect) {
		return Registry.register(Registry.STATUS_EFFECT, BetterEnd.makeID(name), effect);
	}
}
