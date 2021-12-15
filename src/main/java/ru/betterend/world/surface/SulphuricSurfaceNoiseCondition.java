package ru.betterend.world.surface;

import ru.bclib.interfaces.NumericProvider;
import ru.bclib.mixin.common.SurfaceRulesContextAccessor;
import ru.bclib.util.MHelper;
import ru.betterend.noise.OpenSimplexNoise;

/**
 * Noise source that returns a value in [0, 3]
 */
public class SulphuricSurfaceNoiseCondition implements NumericProvider {
    private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(5123);

    @Override
    public int getNumber(SurfaceRulesContextAccessor context) {
        final int x = context.getBlockX();
        final int z = context.getBlockZ();
        final double value = NOISE.eval(x * 0.03, z * 0.03) + NOISE.eval(x * 0.1, z * 0.1) * 0.3 + MHelper.randRange(
                -0.1,
                0.1,
                MHelper.RANDOM
        );
        if (value < -0.6) return 0;
        if (value < -0.3) return 1;
        if (value < -0.5) return 2;
        return 3;
    }
}
