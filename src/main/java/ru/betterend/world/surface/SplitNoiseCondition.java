package ru.betterend.world.surface;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import ru.bclib.interfaces.NumericProvider;
import ru.bclib.mixin.common.SurfaceRulesContextAccessor;
import ru.bclib.util.MHelper;
import ru.betterend.BetterEnd;
import ru.betterend.noise.OpenSimplexNoise;

/**
 * Noise source that returns a value in [0, 1]
 */
public class SplitNoiseCondition implements NumericProvider {
	public static final SplitNoiseCondition DEFAULT = new SplitNoiseCondition();
	public static final Codec<SplitNoiseCondition> CODEC = Codec.BYTE.fieldOf("split_noise").xmap((obj)->DEFAULT, obj -> (byte)0).codec();

	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(4141);

	@Override
	public int getNumber(SurfaceRulesContextAccessor context) {
		final int x = context.getBlockX();
		final int z = context.getBlockZ();
		float noise = (float) NOISE.eval(x * 0.1, z * 0.1) + MHelper.randRange(-0.4F, 0.4F, MHelper.RANDOM);
		return noise > 0 ? 1 : 0;
	}

	@Override
	public Codec<? extends NumericProvider> pcodec() {
		return CODEC;
	}

	static {
		Registry.register(NumericProvider.NUMERIC_PROVIDER , BetterEnd.makeID("split_noise"), SplitNoiseCondition.CODEC);
	}
}
