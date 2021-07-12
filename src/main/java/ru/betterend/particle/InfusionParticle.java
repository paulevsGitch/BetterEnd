package ru.betterend.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

public class InfusionParticle extends TextureSheetParticle {
	
	private final SpriteSet spriteProvider;
	
	public InfusionParticle(ClientLevel clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float[] palette, SpriteSet spriteProvider) {
		super(clientWorld, x, y, z, 0.0, 0.0, 0.0);
		this.setSpriteFromAge(spriteProvider);
		this.spriteProvider = spriteProvider;
		this.setColor(palette[0], palette[1], palette[2]);
		this.setAlpha(palette[3]);
		this.xd = velocityX * 0.1D;
		this.yd = velocityY * 0.1D;
		this.zd = velocityZ * 0.1D;
		this.lifetime = (int) (3.0F / (this.random.nextFloat() * 0.9F + 0.1F));
		this.quadSize *= 0.9F;
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
		}
		else {
			this.setSpriteFromAge(spriteProvider);
			double velocityX = 2.0D * this.xd * this.random.nextDouble();
			double velocityY = 3.0D * this.yd * this.random.nextDouble();
			double velocityZ = 2.0D * this.zd * this.random.nextDouble();
			this.move(velocityX, velocityY, velocityZ);
		}
	}
	
	@Environment(EnvType.CLIENT)
	public static class InfusionFactory implements ParticleProvider<InfusionParticleType> {
		private final SpriteSet spriteProvider;
		
		public InfusionFactory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		public Particle createParticle(InfusionParticleType particleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
			return new InfusionParticle(clientWorld, d, e, f, g, h, i, particleType.getPalette(), this.spriteProvider);
		}
	}
}
