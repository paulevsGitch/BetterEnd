package ru.betterend.util.sdf.operator;

public class SDFTranslate extends SDFUnary {
	float x;
	float y;
	float z;
	
	public SDFTranslate setTranslate(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	@Override
	public float getDistance(float x, float y, float z) {
		return source.getDistance(x - this.x, y - this.y, z - this.z);
	}
}
