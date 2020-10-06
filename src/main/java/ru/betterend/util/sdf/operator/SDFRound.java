package ru.betterend.util.sdf.operator;

public class SDFRound extends SDFUnary {
	private float radius;
	
	public SDFRound setRadius(float radius) {
		this.radius = radius;
		return this;
	}
	
	@Override
	public float getDistance(float x, float y, float z) {
		return this.source.getDistance(x, y, z) - radius;
	}
}
