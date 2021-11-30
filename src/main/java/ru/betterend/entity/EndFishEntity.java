package ru.betterend.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import ru.bclib.api.BiomeAPI;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndItems;

public class EndFishEntity extends AbstractSchoolingFish {
	public static final int VARIANTS_NORMAL = 5;
	public static final int VARIANTS_SULPHUR = 3;
	public static final int VARIANTS = VARIANTS_NORMAL + VARIANTS_SULPHUR;
	private static final EntityDataAccessor<Byte> VARIANT = SynchedEntityData.defineId(
		EndFishEntity.class,
		EntityDataSerializers.BYTE
	);
	private static final EntityDataAccessor<Byte> SCALE = SynchedEntityData.defineId(
		EndFishEntity.class,
		EntityDataSerializers.BYTE
	);
	
	public EndFishEntity(EntityType<EndFishEntity> entityType, Level world) {
		super(entityType, world);
	}
	
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, SpawnGroupData entityData, CompoundTag entityTag) {
		SpawnGroupData data = super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityTag);
		
		if (BiomeAPI.getFromBiome(world.getBiome(blockPosition())) == EndBiomes.SULPHUR_SPRINGS) {
			this.entityData.set(VARIANT, (byte) (random.nextInt(VARIANTS_SULPHUR) + VARIANTS_NORMAL));
		}
		
		if (entityTag != null) {
			if (entityTag.contains("Variant")) {
				this.entityData.set(VARIANT, entityTag.getByte("variant"));
			}
			if (entityTag.contains("Scale")) {
				this.entityData.set(SCALE, entityTag.getByte("scale"));
			}
		}
		
		this.refreshDimensions();
		return data;
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT, (byte) this.getRandom().nextInt(VARIANTS_NORMAL));
		this.entityData.define(SCALE, (byte) this.getRandom().nextInt(16));
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putByte("Variant", (byte) getVariant());
		tag.putByte("Scale", getByteScale());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Variant")) {
			this.entityData.set(VARIANT, tag.getByte("Variant"));
		}
		if (tag.contains("Scale")) {
			this.entityData.set(SCALE, tag.getByte("Scale"));
		}
	}
	
	@Override
	public ItemStack getBucketItemStack() {
		ItemStack bucket = EndItems.BUCKET_END_FISH.getDefaultInstance();
		CompoundTag tag = bucket.getOrCreateTag();
		tag.putByte("variant", entityData.get(VARIANT));
		tag.putByte("scale", entityData.get(SCALE));
		return bucket;
	}
	
	@Override
	protected SoundEvent getFlopSound() {
		return SoundEvents.TROPICAL_FISH_FLOP;
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.SALMON_AMBIENT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.SALMON_DEATH;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.SALMON_HURT;
	}
	
	@Override
	public void tick() {
		super.tick();
		if (random.nextInt(8) == 0 && getFeetBlockState().is(Blocks.WATER)) {
			double x = getX() + random.nextGaussian() * 0.2;
			double y = getY() + random.nextGaussian() * 0.2;
			double z = getZ() + random.nextGaussian() * 0.2;
			level.addParticle(ParticleTypes.BUBBLE, x, y, z, 0, 0, 0);
		}
	}
	
	public static AttributeSupplier.Builder createMobAttributes() {
		return LivingEntity
			.createLivingAttributes()
			.add(Attributes.MAX_HEALTH, 2.0)
			.add(Attributes.FOLLOW_RANGE, 16.0)
			.add(Attributes.MOVEMENT_SPEED, 0.75);
	}
	
	public int getVariant() {
		return (int) this.entityData.get(VARIANT);
	}
	
	public byte getByteScale() {
		return this.entityData.get(SCALE);
	}
	
	public float getScale() {
		return getByteScale() / 32F + 0.75F;
	}
	
	@Override
	protected void dropFromLootTable(DamageSource source, boolean causedByPlayer) {
		ItemEntity drop = new ItemEntity(level, getX(), getY(), getZ(), new ItemStack(EndItems.END_FISH_RAW));
		this.level.addFreshEntity(drop);
	}
}
