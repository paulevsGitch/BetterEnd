package ru.betterend.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import ru.betterend.util.MHelper;

public class PaticlePortalSphere extends SimpleAnimatedParticle {
	private int ticks;
	private double preVX;
	private double preVY;
	private double preVZ;
	private double nextVX;
	private double nextVY;
	private double nextVZ;

	public PaticlePortalSphere(ClientLevel world, double x, double y, double z, SpriteSet spriteProvider) {
		super(world, x, y, z, spriteProvider, 0);
		setSprite(spriteProvider.getSprite(random));
		this.maxAge = MHelper.randRange(20, 80, random);
		this.scale = MHelper.randRange(0.05F, 0.15F, random);
		this.setColor(0xFEBBD5);
		this.setTargetColor(0xBBFEE4);
		this.setSpriteForAge(spriteProvider);

		preVX = random.nextGaussian() * 0.02;
		preVY = random.nextGaussian() * 0.02;
		preVZ = random.nextGaussian() * 0.02;

		nextVX = random.nextGaussian() * 0.02;
		nextVY = random.nextGaussian() * 0.02;
		nextVZ = random.nextGaussian() * 0.02;
	}

	@Override
	public void tick() {
		ticks++;
		if (ticks > 30) {
			preVX = nextVX;
			preVY = nextVY;
			preVZ = nextVZ;
			nextVX = random.nextGaussian() * 0.02;
			nextVY = random.nextGaussian() * 0.02;
			nextVZ = random.nextGaussian() * 0.02;
			ticks = 0;
		}
		double delta = (double) ticks / 30.0;

		this.velocityX = Mth.lerp(delta, preVX, nextVX);
		this.velocityY = Mth.lerp(delta, preVY, nextVY);
		this.velocityZ = Mth.lerp(delta, preVZ, nextVZ);

		super.tick();
	}

	@Environment(EnvType.CLIENT)
	public static class FactoryPortalSphere implements ParticleFactory<SimpleParticleType> {

		private final SpriteSet sprites;

		public FactoryPortalSphere(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z,
				double vX, double vY, double vZ) {
			return new PaticlePortalSphere(world, x, y, z, sprites);
		}
	}
}
