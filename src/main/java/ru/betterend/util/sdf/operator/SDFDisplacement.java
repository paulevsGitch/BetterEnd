package ru.betterend.util.sdf.operator;

import com.mojang.math.Vector3f;
import java.util.function.Function;

public class SDFDisplacement extends SDFUnary {
	private static final Vector3f POS = new Vector3f();
	private Function<Vector3f, Float> displace;
	
	public SDFDisplacement setFunction(Function<Vector3f, Float> displace) {
		this.displace = displace;
		return this;
	}
	
	@Override
	public float getDistance(float x, float y, float z) {
		POS.set(x, y, z);
		return this.source.getDistance(x, y, z) + displace.apply(POS);
	}
}
