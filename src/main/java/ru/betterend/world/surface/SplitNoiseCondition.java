package ru.betterend.world.surface;

import ru.bclib.interfaces.NumericProvider;
import ru.bclib.mixin.common.SurfaceRulesContextAccessor;
import ru.bclib.util.MHelper;
import ru.betterend.noise.OpenSimplexNoise;

/**
 * Noise source that returns a value in [0, 1]
 */
public class SplitNoiseCondition implements NumericProvider {
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(4141);

	@Override
	public int getNumber(SurfaceRulesContextAccessor context) {
		final int x = context.getBlockX();
		final int z = context.getBlockZ();
		float noise = (float) NOISE.eval(x * 0.1, z * 0.1) + MHelper.randRange(-0.4F, 0.4F, MHelper.RANDOM);
		return noise > 0 ? 1 : 0;
	}
}
