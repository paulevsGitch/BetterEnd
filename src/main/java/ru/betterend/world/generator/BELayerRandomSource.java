package ru.betterend.world.generator;

import java.util.Random;

import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;

public class BELayerRandomSource implements LayerRandomnessSource {
	private Random random = new Random(0);
	
	@Override
	public int nextInt(int bound) {
		return random.nextInt(bound);
	}

	@Override
	public PerlinNoiseSampler getNoiseSampler() {
		return null;
	}
}
