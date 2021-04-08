package ru.betterend.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.BlockPos.MutableBlockPos;
import ru.betterend.util.MHelper;

@Environment(EnvType.CLIENT)
public class ParticleGeyser extends SpriteBillboardParticle {
	private MutableBlockPos mut = new MutableBlockPos();
	private boolean changeDir = false;
	private boolean check = true;

	protected ParticleGeyser(ClientLevel world, double x, double y, double z, double vx, double vy, double vz,
			SpriteSet sprites) {
		super(world, x, y, z, vx, vy, vz);
		setSprite(sprites);
		this.maxAge = MHelper.randRange(400, 800, random);
		this.scale = MHelper.randRange(0.5F, 1.0F, random);
		this.velocityX = vx;
		this.velocityZ = vz;
		this.prevPosY = y - 0.125;
	}

	@Override
	public void tick() {

		if (this.prevPosY == this.y || this.age > this.maxAge) {
			this.markDead();
		} else {
			if (this.age >= this.maxAge - 200) {
				this.setColorAlpha((this.maxAge - this.age) / 200F);
			}

			this.scale += 0.005F;
			this.velocityY = 0.125;

			if (changeDir) {
				changeDir = false;
				check = false;
				this.velocityX += MHelper.randRange(-0.2, 0.2, random);
				this.velocityZ += MHelper.randRange(-0.2, 0.2, random);
			} else if (check) {
				changeDir = world.getBlockState(mut.set(x, y, z)).getFluidState().isEmpty();
				this.velocityX = 0;
				this.velocityZ = 0;
			}
		}
		super.tick();
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public static class FactoryGeyser implements ParticleFactory<SimpleParticleType> {

		private final SpriteSet sprites;

		public FactoryGeyser(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z,
				double vX, double vY, double vZ) {
			return new ParticleGeyser(world, x, y, z, 0, 0.125, 0, sprites);
		}
	}
}