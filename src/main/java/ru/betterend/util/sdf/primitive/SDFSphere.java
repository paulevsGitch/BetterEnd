package ru.betterend.util.sdf.primitive;

import ru.betterend.util.MHelper;
import ru.betterend.util.sdf.ISDF;

public class SDFSphere implements ISDF {
	private float radius;
	
	public SDFSphere setRadius(float radius) {
		this.radius =  radius;
		return this;
	}
	
	@Override
	public float getDistance(float x, float y, float z) {
		return MHelper.length(x, y, z) - radius;
	}
}
