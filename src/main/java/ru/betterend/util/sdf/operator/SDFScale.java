package ru.betterend.util.sdf.operator;

public class SDFScale extends SDFUnary {
	private float scale;
	
	public SDFScale setScale(float scale) {
		this.scale = scale;
		return this;
	}
	
	@Override
	public float getDistance(float x, float y, float z) {
		return source.getDistance(x / scale, y / scale, z / scale) * scale;
	}
}
