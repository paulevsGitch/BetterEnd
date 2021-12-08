package ru.betterend.world.surface;

import ru.bclib.api.biomes.SurfaceNoiseCondition;
import ru.bclib.mixin.common.SurfaceRulesContextAccessor;
import ru.bclib.util.MHelper;
import ru.betterend.noise.OpenSimplexNoise;

public class UmbraSurfaceNoiseCondition extends SurfaceNoiseCondition {
    private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(1512);

    private final double threshold;
    public UmbraSurfaceNoiseCondition(double threshold){
        this.threshold = threshold;
    }

    private static int lastX = Integer.MIN_VALUE;
    private static int lastZ = Integer.MIN_VALUE;
    private static double lastValue = 0;

    @Override
    public boolean test(SurfaceRulesContextAccessor context) {
        final int x = context.getBlockX();
        final int z = context.getBlockZ();
        if (lastX==x && lastZ==z) return lastValue > threshold;

        double value = NOISE.eval(x * 0.03, z * 0.03) + NOISE.eval(x * 0.1, z * 0.1) * 0.3 + MHelper.randRange(
                -0.1,
                0.1,
                MHelper.RANDOM
        );

        lastX=x;
        lastZ=z;
        lastValue=value;
        return value > threshold;
        //int depth = (int) (NOISE.eval(x * 0.1, z * 0.1) * 20 + NOISE.eval(x * 0.5, z * 0.5) * 10 + 60);
        //SurfaceBuilder.DEFAULT.apply(random, chunk, biome, x, z, height, noise + depth, defaultBlock, defaultFluid, seaLevel, seed, n, config);
    }
}
