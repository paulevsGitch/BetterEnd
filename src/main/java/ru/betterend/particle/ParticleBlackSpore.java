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
public class ParticleBlackSpore extends SimpleAnimatedParticle {
	private double preVX;
	private double preVY;
	private double preVZ;
	private double nextVX;
	private double nextVY;
	private double nextVZ;

	protected ParticleBlackSpore(ClientLevel world, double x, double y, double z, double r, double g, double b,
			SpriteSet sprites) {
		super(world, x, y, z, sprites, 0);
		setSprite(sprites.getSprite(random));

		this.maxAge = MHelper.randRange(30, 60, random);
		this.scale = MHelper.randRange(0.05F, 0.15F, random);
		this.setColor(1, 1, 1);
		this.setColorAlpha(0);

		preVX = random.nextGaussian() * 0.015;
		preVY = 0;
		preVZ = random.nextGaussian() * 0.015;

		nextVX = random.nextGaussian() * 0.015;
		nextVY = random.nextFloat() * 0.02 + 0.01;
		nextVZ = random.nextGaussian() * 0.015;
	}

	@Override
	public void tick() {
		int ticks = this.age & 15;
		if (ticks == 0) {
			preVX = nextVX;
			preVY = nextVY;
			preVZ = nextVZ;
			nextVX = random.nextGaussian() * 0.015;
			nextVY = random.nextFloat() * 0.02 + 0.01;
			nextVZ = random.nextGaussian() * 0.015;
		}
		double delta = (double) ticks / 15.0;

		if (this.age <= 15) {
			this.setColorAlpha(this.age / 15F);
		} else if (this.age >= this.maxAge - 15) {
			this.setColorAlpha((this.maxAge - this.age) / 15F);
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
	public static class FactoryBlackSpore implements ParticleFactory<SimpleParticleType> {

		private final SpriteSet sprites;

		public FactoryBlackSpore(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z,
				double vX, double vY, double vZ) {
			return new ParticleBlackSpore(world, x, y, z, 1, 1, 1, sprites);
		}
	}
}