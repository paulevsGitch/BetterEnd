package ru.betterend.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import ru.bclib.util.MHelper;

@Environment(EnvType.CLIENT)
public class FireflyParticle extends SimpleAnimatedParticle {
	private double preVX;
	private double preVY;
	private double preVZ;
	private double nextVX;
	private double nextVY;
	private double nextVZ;
	
	protected FireflyParticle(ClientLevel world, double x, double y, double z, SpriteSet sprites, double r, double g, double b) {
		super(world, x, y, z, sprites, 0);
		setSprite(sprites.get(random));
		this.lifetime = MHelper.randRange(150, 300, random);
		this.quadSize = MHelper.randRange(0.05F, 0.15F, random);
		this.setFadeColor(15916745);
		this.setSpriteFromAge(sprites);
		this.setAlpha(0);
		
		preVX = random.nextGaussian() * 0.02;
		preVY = random.nextGaussian() * 0.02;
		preVZ = random.nextGaussian() * 0.02;
		
		nextVX = random.nextGaussian() * 0.02;
		nextVY = random.nextGaussian() * 0.02;
		nextVZ = random.nextGaussian() * 0.02;
	}
	
	@Override
	public void tick() {
		int ticks = this.age & 31;
		if (ticks == 0) {
			preVX = nextVX;
			preVY = nextVY;
			preVZ = nextVZ;
			nextVX = random.nextGaussian() * 0.02;
			nextVY = random.nextGaussian() * 0.02;
			nextVZ = random.nextGaussian() * 0.02;
		}
		double delta = (double) ticks / 31.0;
		
		this.xd = Mth.lerp(delta, preVX, nextVX);
		this.yd = Mth.lerp(delta, preVY, nextVY);
		this.zd = Mth.lerp(delta, preVZ, nextVZ);
		
		if (this.age <= 60) {
			this.setAlpha(this.age / 60F);
		}
		else if (this.age > lifetime - 60) {
			this.setAlpha((lifetime - this.age) / 60F);
		}
		
		super.tick();
	}

	@Environment(EnvType.CLIENT)
	public static class FireflyParticleFactory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public FireflyParticleFactory(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double vX, double vY, double vZ) {
			return new FireflyParticle(world, x, y, z, sprites, 1, 1, 1);
		}
	}
}