package ru.betterend.particle;

import java.util.Locale;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.registry.Registry;
import ru.betterend.registry.EndParticles;

public class GlowingSphereParticleEffect implements ParticleEffect {
	private final float red;
	private final float green;
	private final float blue;

	public GlowingSphereParticleEffect(float red, float green, float blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	@Override
	public ParticleType<?> getType() {
		return EndParticles.GLOWING_SPHERE;
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeFloat(this.red);
		buf.writeFloat(this.green);
		buf.writeFloat(this.blue);
	}

	@Override
	public String asString() {
		return String.format(Locale.ROOT, "%s %.2f %.2f %.2f", Registry.PARTICLE_TYPE.getId(this.getType()), this.red, this.green, this.blue);
	}

	public float getRed() {
		return this.red;
	}

	public float getGreen() {
		return this.green;
	}

	public float getBlue() {
		return this.blue;
	}
}
