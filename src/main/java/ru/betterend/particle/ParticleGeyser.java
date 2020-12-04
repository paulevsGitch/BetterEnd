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
import ru.betterend.util.MHelper;

@Environment(EnvType.CLIENT)
public class ParticleGeyser extends SpriteBillboardParticle {
	protected ParticleGeyser(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, SpriteProvider sprites) {
		super(world, x, y, z, vx, vy, vz);
		setSprite(sprites);
		this.maxAge = MHelper.randRange(600, 1200, random);
		this.scale = MHelper.randRange(0.5F, 1.0F, random);
	}
	
	@Override
	public void tick() {
		if (this.age >= this.maxAge + 40) {
			this.setColorAlpha((this.maxAge - this.age) / 40F);
		}
		this.velocityY = 0.125;
		super.tick();
	}
	
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public static class FactoryGeyser implements ParticleFactory<DefaultParticleType> {

		private final SpriteProvider sprites;

		public FactoryGeyser(SpriteProvider sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
			return new ParticleGeyser(world, x, y, z, 0, 0.125, 0, sprites);
		}
	}
}