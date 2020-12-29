package ru.betterend.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import ru.betterend.util.MHelper;

@Environment(EnvType.CLIENT)
public class ParticleJungleSpore extends AnimatedParticle {
	
	protected ParticleJungleSpore(ClientWorld world, double x, double y, double z, SpriteProvider sprites, double r, double g, double b) {
		super(world, x, y, z, sprites, 0);
		setSprite(sprites.getSprite(random));
		this.maxAge = MHelper.randRange(150, 300, random);
		this.scale = MHelper.randRange(0.05F, 0.15F, random);
		this.setTargetColor(15916745);
		this.setSpriteForAge(spriteProvider);
		this.setColorAlpha(0);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		int ticks = this.age % 30;
		if (ticks == 0) {
			this.velocityX = random.nextGaussian() * 0.02;
			this.velocityY = random.nextFloat() * 0.02 + 0.02;
			this.velocityZ = random.nextGaussian() * 0.02;
			ticks = 0;
		}
		
		if (this.age <= 30) {
			float delta = ticks / 30F;
			this.setColorAlpha(delta);
		}
		else if (this.age >= this.maxAge) {
			this.setColorAlpha(0);
		}
		else if (this.age >= this.maxAge - 30) {
			this.setColorAlpha((this.maxAge - this.age) / 30F);
		}
		else {
			this.setColorAlpha(1);
		}
		
		this.velocityY -= 0.001F;
		this.velocityX *= 0.99F;
		this.velocityZ *= 0.99F;
	}

	@Environment(EnvType.CLIENT)
	public static class FactoryJungleSpore implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider sprites;

		public FactoryJungleSpore(SpriteProvider sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
			return new ParticleJungleSpore(world, x, y, z, sprites, 1, 1, 1);
		}
	}
}