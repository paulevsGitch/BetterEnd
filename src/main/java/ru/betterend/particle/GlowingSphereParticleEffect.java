package ru.betterend.particle;

import java.util.Locale;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.Registry;
import ru.betterend.registry.EndParticles;

public class GlowingSphereParticleEffect implements ParticleOptions {
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
		return String.format(Locale.ROOT, "%s %.2f %.2f %.2f", Registry.PARTICLE_TYPE.getId(this.getType()), this.red,
				this.green, this.blue);
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
