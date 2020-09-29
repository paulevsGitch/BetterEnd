package ru.betterend.util.sdf.operator;

public class SDFFlatWave extends SDFDisplacement {
	private int rayCount = 1;
	private float intensity;
	private float angle;
	
	public SDFFlatWave() {
		setFunction((pos) -> {
			return (float) Math.cos(Math.atan2(pos.getX(), pos.getZ()) * rayCount + angle) * intensity;
		});
	}
	
	public SDFFlatWave setRaysCount(int count) {
		this.rayCount = count;
		return this;
	}
	
	public SDFFlatWave setAngle(float angle) {
		this.angle = angle;
		return this;
	}
	
	public SDFFlatWave setIntensity(float intensity) {
		this.intensity = intensity;
		return this;
	}
}
