package ru.betterend.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.multiplayer.ClientLevel;

public class InfusionParticle extends SpriteBillboardParticle {

	private final SpriteSet spriteProvider;

	public InfusionParticle(ClientLevel clientWorld, double x, double y, double z, double velocityX, double velocityY,
			double velocityZ, float[] palette, SpriteSet spriteProvider) {
		super(clientWorld, x, y, z, 0.0, 0.0, 0.0);
		this.setSpriteForAge(spriteProvider);
		this.spriteProvider = spriteProvider;
		this.setColor(palette[0], palette[1], palette[2]);
		this.setColorAlpha(palette[3]);
		this.velocityX = velocityX * 0.1D;
		this.velocityY = velocityY * 0.1D;
		this.velocityZ = velocityZ * 0.1D;
		this.maxAge = (int) (3.0F / (this.random.nextFloat() * 0.9F + 0.1F));
		this.scale *= 0.9F;
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;
		if (this.age++ >= this.maxAge) {
			this.markDead();
		} else {
			this.setSpriteForAge(spriteProvider);
			double velocityX = 2.0D * this.velocityX * this.random.nextDouble();
			double velocityY = 3.0D * this.velocityY * this.random.nextDouble();
			double velocityZ = 2.0D * this.velocityZ * this.random.nextDouble();
			this.move(velocityX, velocityY, velocityZ);
		}
	}

	@Environment(EnvType.CLIENT)
	public static class InfusionFactory implements ParticleFactory<InfusionParticleType> {
		private final SpriteSet spriteProvider;

		public InfusionFactory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(InfusionParticleType particleType, ClientLevel clientWorld, double d, double e,
				double f, double g, double h, double i) {
			return new InfusionParticle(clientWorld, d, e, f, g, h, i, particleType.getPalette(), this.spriteProvider);
		}
	}
}
