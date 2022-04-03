package ru.betterend.world.surface;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import ru.bclib.interfaces.NumericProvider;
import ru.bclib.mixin.common.SurfaceRulesContextAccessor;
import ru.bclib.util.MHelper;
import ru.betterend.BetterEnd;
import ru.betterend.noise.OpenSimplexNoise;

/**
 * Noise source that returns a value in [0, 3]
 */
public class SulphuricSurfaceNoiseCondition implements NumericProvider {
	public static final SulphuricSurfaceNoiseCondition DEFAULT = new SulphuricSurfaceNoiseCondition();
	public static final Codec<SulphuricSurfaceNoiseCondition> CODEC = Codec.BYTE.fieldOf("sulphuric_surf").xmap((obj)->DEFAULT, obj -> (byte)0).codec();

	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(5123);

	@Override
	public int getNumber(SurfaceRulesContextAccessor context) {
		final int x = context.getBlockX();
		final int z = context.getBlockZ();
		final double value = NOISE.eval(x * 0.03, z * 0.03) + NOISE.eval(x * 0.1, z * 0.1) * 0.3 + MHelper.randRange(-0.1, 0.1, MHelper.RANDOM);
		if (value < -0.6) return 0;
		if (value < -0.3) return 1;
		if (value < 0.5) return 2;
		return 3;
	}

	@Override
	public Codec<? extends NumericProvider> pcodec() {
		return CODEC;
	}

	static {
		Registry.register(NumericProvider.NUMERIC_PROVIDER , BetterEnd.makeID("sulphuric_surf"), SulphuricSurfaceNoiseCondition.CODEC);
	}
}
