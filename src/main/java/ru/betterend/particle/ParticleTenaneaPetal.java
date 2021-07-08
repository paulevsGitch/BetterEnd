package ru.betterend.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import ru.bclib.interfaces.IColorProvider;
import ru.bclib.util.MHelper;
import ru.betterend.registry.EndBlocks;

@Environment(EnvType.CLIENT)
public class ParticleTenaneaPetal extends TextureSheetParticle {
	private static BlockColor provider;

	private double preVX;
	private double preVY;
	private double preVZ;
	private double nextVX;
	private double nextVY;
	private double nextVZ;

	protected ParticleTenaneaPetal(ClientLevel world, double x, double y, double z, double r, double g, double b, SpriteSet sprites) {
		super(world, x, y, z, r, g, b);
		pickSprite(sprites);

		if (provider == null) {
			IColorProvider block = (IColorProvider) EndBlocks.TENANEA_FLOWERS;
			provider = block.getProvider();
		}
		int color = provider.getColor(null, null, new BlockPos(x, y, z), 0);
		this.rCol = ((color >> 16) & 255) / 255F;
		this.gCol = ((color >> 8) & 255) / 255F;
		this.bCol = ((color) & 255) / 255F;

		this.lifetime = MHelper.randRange(120, 200, random);
		this.quadSize = MHelper.randRange(0.05F, 0.15F, random);
		this.setAlpha(0);

		preVX = 0;
		preVY = 0;
		preVZ = 0;

		nextVX = random.nextGaussian() * 0.02;
		nextVY = -random.nextDouble() * 0.02 - 0.02;
		nextVZ = random.nextGaussian() * 0.02;
	}

	@Override
	public int getLightColor(float tint) {
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
			this.setAlpha(this.age / 40F);
		}
		else if (this.age >= this.lifetime - 40) {
			this.setAlpha((this.lifetime - this.age) / 40F);
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
	public static class FactoryTenaneaPetal implements ParticleProvider<SimpleParticleType> {

		private final SpriteSet sprites;

		public FactoryTenaneaPetal(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double vX, double vY, double vZ) {
			return new ParticleTenaneaPetal(world, x, y, z, 1, 1, 1, sprites);
		}
	}
}