package ru.betterend.entity;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Box;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import ru.betterend.registry.BiomeRegistry;
import ru.betterend.util.ISlime;

public class EntityEndSlime extends SlimeEntity {
	private static final TrackedData<Boolean> MOSSY = DataTracker.registerData(EntityEndSlime.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final Mutable POS = new Mutable();
	
	public EntityEndSlime(EntityType<EntityEndSlime> entityType, World world) {
		super(entityType, world);
	}
	
	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return LivingEntity.createLivingAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0D)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0D)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15D);
	}
	
	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CompoundTag entityTag) {
		EntityData data = super.initialize(world, difficulty, spawnReason, entityData, entityTag);
		if (BiomeRegistry.getFromBiome(world.getBiome(getBlockPos())) == BiomeRegistry.FOGGY_MUSHROOMLAND) {
			this.setMossy(true);
		}
		return data;
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(MOSSY, false);
	}

	@Override
	protected ParticleEffect getParticles() {
		return ParticleTypes.PORTAL;
	}
	
	@Override
	public void remove() {
		int i = this.getSize();
		if (!this.world.isClient && i > 1 && this.isDead()) {
			Text text = this.getCustomName();
			boolean bl = this.isAiDisabled();
			float f = (float) i / 4.0F;
			int j = i / 2;
			int k = 2 + this.random.nextInt(3);
			boolean mossy = this.isMossy();
			
			for (int l = 0; l < k; ++l) {
				float g = ((float) (l % 2) - 0.5F) * f;
				float h = ((float) (l / 2) - 0.5F) * f;
				EntityEndSlime slimeEntity = (EntityEndSlime) this.getType().create(this.world);
				if (this.isPersistent()) {
					slimeEntity.setPersistent();
				}

				slimeEntity.setMossy(mossy);
				slimeEntity.setCustomName(text);
				slimeEntity.setAiDisabled(bl);
				slimeEntity.setInvulnerable(this.isInvulnerable());
				((ISlime) slimeEntity).setSlimeSize(j, true);
				slimeEntity.refreshPositionAndAngles(this.getX() + (double) g, this.getY() + 0.5D, this.getZ() + (double) h, this.random.nextFloat() * 360.0F, 0.0F);
				this.world.spawnEntity(slimeEntity);
			}
		}
		this.removed = true;
	}
	
	protected void setMossy(boolean mossy) {
		this.dataTracker.set(MOSSY, mossy);
	}

	public boolean isMossy() {
		return this.dataTracker.get(MOSSY);
	}
	
	public static boolean canSpawn(EntityType<EntityEndSlime> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		return notManyEntities(world, pos, 32, 3) && isWaterNear(world, pos, 32);
	}
	
	private static boolean notManyEntities(ServerWorldAccess world, BlockPos pos, int radius, int maxCount) {
		Box box = new Box(pos).expand(radius);
		List<EntityEndSlime> list = world.getEntitiesByClass(EntityEndSlime.class, box, (entity) -> { return true; });
		return list.size() <= maxCount;
	}
	
	private static boolean isWaterNear(ServerWorldAccess world, BlockPos pos, int radius) {
		for (int x = pos.getX() - radius; x <= pos.getX() + radius; x++) {
			POS.setX(x);
			for (int z = pos.getZ() - radius; z <= pos.getZ() + radius; z++) {
				POS.setZ(z);
				for (int y = pos.getY() - radius; y <= pos.getY() + radius; y++) {
					POS.setY(y);
					if (world.getBlockState(POS).getBlock() == Blocks.WATER) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
