package ru.betterend.util.sdf.primitive;

import net.minecraft.util.Mth;
import ru.betterend.util.MHelper;

public class SDFLine extends SDFPrimitive {
	private float radius;
	private float x1;
	private float y1;
	private float z1;
	private float x2;
	private float y2;
	private float z2;

	public SDFLine setRadius(float radius) {
		this.radius = radius;
		return this;
	}

	public SDFLine setStart(float x, float y, float z) {
		this.x1 = x;
		this.y1 = y;
		this.z1 = z;
		return this;
	}

	public SDFLine setEnd(float x, float y, float z) {
		this.x2 = x;
		this.y2 = y;
		this.z2 = z;
		return this;
	}

	@Override
	public float getDistance(float x, float y, float z) {
		float pax = x - x1;
		float pay = y - y1;
		float paz = z - z1;

		float bax = x2 - x1;
		float bay = y2 - y1;
		float baz = z2 - z1;

		float dpb = MHelper.dot(pax, pay, paz, bax, bay, baz);
		float dbb = MHelper.dot(bax, bay, baz, bax, bay, baz);
		float h = Mth.clamp(dpb / dbb, 0F, 1F);
		return MHelper.length(pax - bax * h, pay - bay * h, paz - baz * h) - radius;
	}
}
