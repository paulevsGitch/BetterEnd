package ru.betterend.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import ru.betterend.util.MHelper;

@Environment(EnvType.CLIENT)
public class ParticleSnowflake extends SpriteBillboardParticle {
	private int ticks;
	private double preVX;
	private double preVY;
	private double preVZ;
	private double nextVX;
	private double nextVY;
	private double nextVZ;
	
	protected ParticleSnowflake(ClientWorld world, double x, double y, double z, double r, double g, double b, SpriteProvider sprites) {
		super(world, x, y, z, r, g, b);
		setSprite(sprites);
		
		this.maxAge = MHelper.randRange(150, 300, random);
		this.scale = MHelper.randRange(0.05F, 0.3F, random);
		this.setColorAlpha(0F);
		
		preVX = random.nextGaussian() * 0.015;
		preVY = random.nextGaussian() * 0.015;
		preVZ = random.nextGaussian() * 0.015;
		
		nextVX = random.nextGaussian() * 0.015;
		nextVY = random.nextGaussian() * 0.015;
		nextVZ = random.nextGaussian() * 0.015;
	}
	
	@Override
	public void tick() {
		ticks ++;
		if (ticks > 200) {
			preVX = nextVX;
			preVY = nextVY;
			preVZ = nextVZ;
			nextVX = random.nextGaussian() * 0.015;
			nextVY = random.nextGaussian() * 0.015;
			nextVZ = random.nextGaussian() * 0.015;
			if (random.nextInt(4) == 0) {
				nextVY = Math.abs(nextVY);
			}
			ticks = 0;
		}
		double delta = (double) ticks / 200.0;
		
		if (this.age <= 40) {
			this.setColorAlpha(this.age / 40F);
		}
		else if (this.age >= this.maxAge - 40) {
			this.setColorAlpha((this.maxAge - this.age) / 40F);
		}
		
		if (this.age >= this.maxAge) {
			this.markDead();
		}
		
		this.velocityX = MathHelper.lerp(delta, preVX, nextVX);
		this.velocityY = MathHelper.lerp(delta, preVY, nextVY);
		this.velocityZ = MathHelper.lerp(delta, preVZ, nextVZ);
		
		super.tick();
	}
	
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public static class FactorySnowflake implements ParticleFactory<DefaultParticleType> {

		private final SpriteProvider sprites;

		public FactorySnowflake(SpriteProvider sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
			return new ParticleSnowflake(world, x, y, z, 1, 1, 1, sprites);
		}
	}
}