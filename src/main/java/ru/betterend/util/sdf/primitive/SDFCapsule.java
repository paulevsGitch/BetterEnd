package ru.betterend.util.sdf.primitive;

import net.minecraft.util.math.MathHelper;
import ru.betterend.util.MHelper;
import ru.betterend.util.sdf.ISDF;

public class SDFCapsule implements ISDF {
	private float radius;
	private float height;
	
	public SDFCapsule setRadius(float radius) {
		this.radius = radius;
		return this;
	}
	
	public SDFCapsule setHeight(float height) {
		this.height = height;
		return this;
	}
	
	@Override
	public float getDistance(float x, float y, float z) {
		return MHelper.length(x, MathHelper.clamp(y, -height, 0), z) - radius;
	}
}
