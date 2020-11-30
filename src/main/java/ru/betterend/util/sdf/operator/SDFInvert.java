package ru.betterend.util.sdf.operator;

public class SDFInvert extends SDFUnary {
	@Override
	public float getDistance(float x, float y, float z) {
		return -this.source.getDistance(x, y, z);
	}
}
