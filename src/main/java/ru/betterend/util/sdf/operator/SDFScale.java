package ru.betterend.util.sdf.operator;

import ru.betterend.util.sdf.ISDF;

public class SDFScale implements ISDF {
	private ISDF source;
	private float scale;
	
	public SDFScale setSorce(ISDF source) {
		this.source = source;
		return this;
	}
	
	public SDFScale setScale(float scale) {
		this.scale = scale;
		return this;
	}
	
	@Override
	public float getDistance(float x, float y, float z) {
		return source.getDistance(x / scale, y / scale, z / scale) * scale;
	}
}
