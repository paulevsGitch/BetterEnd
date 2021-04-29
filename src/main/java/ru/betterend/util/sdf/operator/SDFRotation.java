package ru.betterend.util.sdf.operator;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

public class SDFRotation extends SDFUnary {
	private static final Vector3f POS = new Vector3f();
	private Quaternion rotation;
	
	public SDFRotation setRotation(Vector3f axis, float rotationAngle) {
		rotation = new Quaternion(axis, rotationAngle, false);
		return this;
	}
	
	@Override
	public float getDistance(float x, float y, float z) {
		POS.set(x, y, z);
		POS.transform(rotation);
		return source.getDistance(POS.x(), POS.y(), POS.z());
	}
}
