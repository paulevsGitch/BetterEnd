package ru.betterend.world.surface;

import ru.bclib.interfaces.NumericProvider;
import ru.bclib.mixin.common.SurfaceRulesContextAccessor;
import ru.bclib.util.MHelper;
import ru.betterend.noise.OpenSimplexNoise;

/**
 * Noise source that returns a value in [0, 4]
 */
public class UmbraSurfaceNoiseCondition implements NumericProvider {
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(1512);

	@Override
	public int getNumber(SurfaceRulesContextAccessor context) {
		final int x = context.getBlockX();
		final int z = context.getBlockZ();
		return getDepth(x, z);
	}
	
	public static int getDepth(int x, int z) {
		final double value = NOISE.eval(x * 0.03, z * 0.03) + NOISE.eval(x * 0.1, z * 0.1) * 0.3 + MHelper.randRange(-0.1, 0.1, MHelper.RANDOM);
		if (value > 0.4) return 0;
		if (value > 0.15) return 1;
		if (value > -0.15) return 2;
		if (value > -0.4) return 3;
		return 4;
	}
}
