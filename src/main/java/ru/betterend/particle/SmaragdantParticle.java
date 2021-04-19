package ru.betterend.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
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
	
	protected SmaragdantParticle(ClientLevel world, double x, double y, double z, double r, double g, double b, SpriteSet sprites) {
		super(world, x, y, z, sprites, 0);
		setSprite(sprites.get(random));
		
		this.lifetime = MHelper.randRange(60, 120, random);
		this.quadSize = MHelper.randRange(0.05F, 0.15F, random);
		this.setColor(1, 1, 1);
		this.setAlpha(0);
		
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
			this.setAlpha(this.age / 31F);
		}
		else if (this.age >= this.lifetime - 31) {
			this.setAlpha((this.lifetime - this.age) / 31F);
		}
		
		if (this.age >= this.lifetime) {
			this.remove();
		}
		
		this.xd = Mth.lerp(delta, preVX, nextVX);
		this.yd = Mth.lerp(delta, preVY, nextVY);
		this.zd = Mth.lerp(delta, preVZ, nextVZ);
		
		super.tick();
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public static class SmaragdantParticleFactory implements ParticleProvider<SimpleParticleType> {

		private final SpriteSet sprites;

		public SmaragdantParticleFactory(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double vX, double vY, double vZ) {
			return new SmaragdantParticle(world, x, y, z, 1, 1, 1, sprites);
		}
	}
}