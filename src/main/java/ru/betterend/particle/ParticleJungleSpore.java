package ru.betterend.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import ru.betterend.util.MHelper;

@Environment(EnvType.CLIENT)
public class ParticleJungleSpore extends SimpleAnimatedParticle {
	
	protected ParticleJungleSpore(ClientLevel world, double x, double y, double z, SpriteSet sprites, double r, double g, double b) {
		super(world, x, y, z, sprites, 0);
		setSprite(sprites.get(random));
		this.lifetime = MHelper.randRange(150, 300, random);
		this.quadSize = MHelper.randRange(0.05F, 0.15F, random);
		this.setFadeColor(15916745);
		this.setSpriteFromAge(sprites);
		this.setAlpha(0);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		int ticks = this.age % 30;
		if (ticks == 0) {
			this.xd = random.nextGaussian() * 0.02;
			this.yd = random.nextFloat() * 0.02 + 0.02;
			this.zd = random.nextGaussian() * 0.02;
			ticks = 0;
		}
		
		if (this.age <= 30) {
			float delta = ticks / 30F;
			this.setAlpha(delta);
		}
		else if (this.age >= this.lifetime) {
			this.setAlpha(0);
		}
		else if (this.age >= this.lifetime - 30) {
			this.setAlpha((this.lifetime - this.age) / 30F);
		}
		else {
			this.setAlpha(1);
		}
		
		this.yd -= 0.001F;
		this.xd *= 0.99F;
		this.zd *= 0.99F;
	}

	@Environment(EnvType.CLIENT)
	public static class FactoryJungleSpore implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public FactoryJungleSpore(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double vX, double vY, double vZ) {
			return new ParticleJungleSpore(world, x, y, z, sprites, 1, 1, 1);
		}
	}
}