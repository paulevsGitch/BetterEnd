package ru.betterend.util.sdf.operator;

public class SDFScale3D extends SDFUnary {
	private float x;
	private float y;
	private float z;
	
	public SDFScale3D setScale(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	@Override
	public float getDistance(float x, float y, float z) {
		return source.getDistance(x / this.x, y / this.y, z / this.z);
	}
}
