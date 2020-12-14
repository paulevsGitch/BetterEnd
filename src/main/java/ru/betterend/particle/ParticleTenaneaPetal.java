package ru.betterend.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import ru.betterend.interfaces.IColorProvider;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.MHelper;

@Environment(EnvType.CLIENT)
public class ParticleTenaneaPetal extends SpriteBillboardParticle {
	private static BlockColorProvider provider;
	
	private double preVX;
	private double preVY;
	private double preVZ;
	private double nextVX;
	private double nextVY;
	private double nextVZ;
	private float angleSpeed;
	
	protected ParticleTenaneaPetal(ClientWorld world, double x, double y, double z, double r, double g, double b, SpriteProvider sprites) {
		super(world, x, y, z, r, g, b);
		setSprite(sprites);
		
		if (provider == null) {
			IColorProvider block = (IColorProvider) EndBlocks.TENANEA_FLOWERS;
			provider = block.getProvider();
		}
		int color = provider.getColor(null, null, new BlockPos(x, y, z), 0);
		this.colorRed = ((color >> 16) & 255) / 255F;
		this.colorGreen = ((color >> 8) & 255) / 255F;
		this.colorBlue = ((color) & 255) / 255F;
		
		this.maxAge = MHelper.randRange(120, 200, random);
		this.scale = MHelper.randRange(0.05F, 0.15F, random);
		this.setColorAlpha(0);
		this.angle = 0.1F;//random.nextFloat() * MHelper.PI2;
		
		preVX = 0;
		preVY = 0;
		preVZ = 0;
		
		nextVX = random.nextGaussian() * 0.02;
		nextVY = -random.nextDouble() * 0.02 - 0.02;
		nextVZ = random.nextGaussian() * 0.02;
		
		angleSpeed = random.nextFloat() * 0.0001F + 0.0001F;
		if (random.nextBoolean()) {
			angleSpeed = -angleSpeed;
		}
	}

	@Override
	public int getColorMultiplier(float tint) {
		return 15728880;
	}
	
	@Override
	public void tick() {
		int ticks = this.age & 63;
		if (ticks == 0) {
			preVX = nextVX;
			preVY = nextVY;
			preVZ = nextVZ;
			nextVX = random.nextGaussian() * 0.02;
			nextVY = -random.nextDouble() * 0.02 - 0.02;
			nextVZ = random.nextGaussian() * 0.02;
		}
		double delta = (double) ticks / 63.0;
		
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
		//this.angle += angleSpeed;
		
		super.tick();
	}
	
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public static class FactoryTenaneaPetal implements ParticleFactory<DefaultParticleType> {

		private final SpriteProvider sprites;

		public FactoryTenaneaPetal(SpriteProvider sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
			return new ParticleTenaneaPetal(world, x, y, z, 1, 1, 1, sprites);
		}
	}
}