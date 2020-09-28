package ru.betterend.util.sdf.operator;

import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Quaternion;

public class SDFRotation extends SDFUnary {
	private static final Vector3f POS = new Vector3f();
	private Quaternion rotation;
	
	@Override
	public float getDistance(float x, float y, float z) {
		POS.set(x, y, z);
		POS.rotate(rotation);
		return source.getDistance(POS.getX(), POS.getY(), POS.getZ());
	}
}
