package ru.betterend.world.surface;

import ru.bclib.api.biomes.SurfaceNoiseCondition;
import ru.bclib.mixin.common.SurfaceRulesContextAccessor;
import ru.bclib.util.MHelper;
import ru.betterend.noise.OpenSimplexNoise;

public class SulphuricSurfaceNoiseCondition extends SurfaceNoiseCondition {
    private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(5123);

    private final double threshold;
    public SulphuricSurfaceNoiseCondition(double threshold){
        this.threshold = threshold;
    }

    @Override
    public boolean test(SurfaceRulesContextAccessor context) {
        final int x = context.getBlockX();
        final int z = context.getBlockZ();
        double value = NOISE.eval(x * 0.03, z * 0.03) + NOISE.eval(x * 0.1, z * 0.1) * 0.3 + MHelper.randRange(
                -0.1,
                0.1,
                MHelper.RANDOM
        );

        return value < threshold;
    }
}
