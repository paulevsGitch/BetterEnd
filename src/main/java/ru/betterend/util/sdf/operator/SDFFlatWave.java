package ru.betterend.util.sdf.operator;

public class SDFFlatWave extends SDFDisplacement {
	private int rayCount = 1;
	private float intensity;
	
	public SDFFlatWave() {
		setFunction((pos) -> {
			return (float) Math.cos(Math.atan2(pos.getX(), pos.getZ()) * rayCount) * intensity;
		});
	}
	
	public SDFFlatWave setRaysCount(int count) {
		this.rayCount = count;
		return this;
	}
	
	public SDFFlatWave setIntensity(float intensity) {
		this.intensity = intensity;
		return this;
	}
}
