package ru.betterend.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import ru.betterend.util.MHelper;

@Environment(EnvType.CLIENT)
public class SmaragdantParticle extends SimpleAnimatedParticle {
	private double preVX;
	private double preVY;
	private double preVZ;
	private double nextVX;
	private double nextVY;
	private double nextVZ;

	protected SmaragdantParticle(ClientLevel world, double x, double y, double z, double r, double g, double b,
			SpriteSet sprites) {
		super(world, x, y, z, sprites, 0);
		setSprite(sprites.getSprite(random));

		this.maxAge = MHelper.randRange(60, 120, random);
		this.scale = MHelper.randRange(0.05F, 0.15F, random);
		this.setColor(1, 1, 1);
		this.setColorAlpha(0);

		preVX = random.nextGaussian() * 0.01;
		preVY = random.nextGaussian() * 0.01;
		preVZ = random.nextGaussian() * 0.01;

		nextVX = random.nextGaussian() * 0.01;
		nextVY = random.nextGaussian() * 0.01;
		nextVZ = random.nextGaussian() * 0.01;
	}

	@Override
	public void tick() {
		int ticks = this.age & 31;
		if (ticks == 0) {
			preVX = nextVX;
			preVY = nextVY;
			preVZ = nextVZ;
			nextVX = random.nextGaussian() * 0.015;
			nextVY = random.nextFloat() * 0.02 + 0.01;
			nextVZ = random.nextGaussian() * 0.015;
		}
		double delta = (double) ticks / 31.0;

		if (this.age <= 31) {
			this.setColorAlpha(this.age / 31F);
		} else if (this.age >= this.maxAge - 31) {
			this.setColorAlpha((this.maxAge - this.age) / 31F);
		}

		if (this.age >= this.maxAge) {
			this.markDead();
		}

		this.velocityX = Mth.lerp(delta, preVX, nextVX);
		this.velocityY = Mth.lerp(delta, preVY, nextVY);
		this.velocityZ = Mth.lerp(delta, preVZ, nextVZ);

		super.tick();
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public static class SmaragdantParticleFactory implements ParticleFactory<SimpleParticleType> {

		private final SpriteSet sprites;

		public SmaragdantParticleFactory(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z,
				double vX, double vY, double vZ) {
			return new SmaragdantParticle(world, x, y, z, 1, 1, 1, sprites);
		}
	}
}