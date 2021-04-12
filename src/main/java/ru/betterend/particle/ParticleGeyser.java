package ru.betterend.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import ru.betterend.util.MHelper;

@Environment(EnvType.CLIENT)
public class ParticleGeyser extends TextureSheetParticle {
	private MutableBlockPos mut = new MutableBlockPos();
	private boolean changeDir = false;
	private boolean check = true;
	
	protected ParticleGeyser(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet sprites) {
		super(world, x, y, z, vx, vy, vz);
		pickSprite(sprites);
		this.lifetime = MHelper.randRange(400, 800, random);
		this.quadSize = MHelper.randRange(0.5F, 1.0F, random);
		this.xd = vx;
		this.zd = vz;
		this.yo = y - 0.125;
	}
	
	@Override
	public void tick() {
		
		if (this.yo == this.y || this.age > this.lifetime) {
			this.remove();
		}
		else {
			if (this.age >= this.lifetime - 200) {
				this.setAlpha((this.lifetime - this.age) / 200F);
			}
			
			this.quadSize += 0.005F;
			this.yd = 0.125;
			
			if (changeDir) {
				changeDir = false;
				check = false;
				this.xd += MHelper.randRange(-0.2, 0.2, random);
				this.zd += MHelper.randRange(-0.2, 0.2, random);
			}
			else if (check) {
				changeDir = level.getBlockState(mut.set(x, y, z)).getFluidState().isEmpty();
				this.xd = 0;
				this.zd = 0;
			}
		}
		super.tick();
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public static class FactoryGeyser implements ParticleProvider<SimpleParticleType> {

		private final SpriteSet sprites;

		public FactoryGeyser(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double vX, double vY, double vZ) {
			return new ParticleGeyser(world, x, y, z, 0, 0.125, 0, sprites);
		}
	}
}