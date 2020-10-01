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
public class AnimatedTestParticle extends AnimatedParticle {
	private int ticks;
	
	protected AnimatedTestParticle(ClientWorld world, double x, double y, double z, SpriteProvider sprites) {
		super(world, x, y, z, sprites, 0);
		setSprite(sprites.getSprite(random));
		this.maxAge = MHelper.randRange(20, 80, random);
		this.scale = MHelper.randRange(0.1F, 0.3F, random);
		this.setTargetColor(15916745);
		this.setSpriteForAge(spriteProvider);
		this.velocityX = random.nextGaussian() * 0.1F;
		this.velocityY = random.nextGaussian() * 0.1F;
		this.velocityZ = random.nextGaussian() * 0.1F;
	}
	
	@Override
	public void tick() {
		ticks ++;
		if (ticks > 10) {
			this.velocityX = random.nextGaussian() * 0.1F;
			this.velocityY = random.nextGaussian() * 0.1F;
			this.velocityZ = random.nextGaussian() * 0.1F;
			ticks = 0;
		}
		super.tick();
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {

		private final SpriteProvider sprites;

		public Factory(SpriteProvider sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z,
				double vX, double vY, double vZ) {
			return new AnimatedTestParticle(world, x, y, z, sprites);
		}
	}
}