package ru.betterend.entity;

import java.util.List;
import java.util.Random;

import net.minecraft.world.entity.EntityData;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnReason;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.attribute.DefaultAttributeContainer;
import net.minecraft.world.entity.attribute.EntityAttributes;
import net.minecraft.world.entity.damage.DamageSource;
import net.minecraft.world.entity.data.DataTracker;
import net.minecraft.world.entity.data.TrackedData;
import net.minecraft.world.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.world.entity.effect.StatusEffectInstance;
import net.minecraft.world.entity.effect.StatusEffects;
import net.minecraft.world.entity.passive.SchoolingFishEntity;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.Mth;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.level.Level;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndItems;

public class CubozoaEntity extends SchoolingFishEntity {
	public static final int VARIANTS = 2;
	private static final TrackedData<Byte> VARIANT = DataTracker.registerData(CubozoaEntity.class,
			TrackedDataHandlerRegistry.BYTE);
	private static final TrackedData<Byte> SCALE = DataTracker.registerData(CubozoaEntity.class,
			TrackedDataHandlerRegistry.BYTE);

	public CubozoaEntity(EntityType<CubozoaEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
			EntityData entityData, CompoundTag entityTag) {
		EntityData data = super.initialize(world, difficulty, spawnReason, entityData, entityTag);
		if (EndBiomes.getFromBiome(world.getBiome(getBlockPos())) == EndBiomes.SULPHUR_SPRINGS) {
			this.dataTracker.set(VARIANT, (byte) 1);
		}
		this.calculateDimensions();
		return data;
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(VARIANT, (byte) 0);
		this.dataTracker.startTracking(SCALE, (byte) this.getRandom().nextInt(16));
	}

	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		tag.putByte("Variant", (byte) getVariant());
		tag.putByte("Scale", (byte) getScale());
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		if (tag.contains("Variant")) {
			this.dataTracker.set(VARIANT, tag.getByte("Variant"));
		}
		if (tag.contains("Scale")) {
			this.dataTracker.set(SCALE, tag.getByte("Scale"));
		}
	}

	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 2.0)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5);
	}

	public int getVariant() {
		return (int) this.dataTracker.get(VARIANT);
	}

	public float getScale() {
		return this.dataTracker.get(SCALE) / 32F + 0.75F;
	}

	public static boolean canSpawn(EntityType<CubozoaEntity> type, ServerWorldAccess world, SpawnReason spawnReason,
			BlockPos pos, Random random) {
		Box box = new Box(pos).expand(16);
		List<CubozoaEntity> list = world.getEntitiesByClass(CubozoaEntity.class, box, (entity) -> {
			return true;
		});
		return list.size() < 9;
	}

	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
		return dimensions.height * 0.5F;
	}

	@Override
	protected void dropLoot(DamageSource source, boolean causedByPlayer) {
		int count = random.nextInt(3);
		if (count > 0) {
			ItemEntity drop = new ItemEntity(world, getX(), getY(), getZ(), new ItemStack(EndItems.GELATINE, count));
			this.world.spawnEntity(drop);
		}
	}

	@Override
	protected ItemStack getFishBucketItem() {
		return new ItemStack(Items.WATER_BUCKET);
	}

	@Override
	protected SoundEvent getFlopSound() {
		return SoundEvents.ENTITY_SALMON_FLOP;
	}

	@Override
	public void onPlayerCollision(PlayerEntity player) {
		if (player instanceof ServerPlayer && player.damage(DamageSource.mob(this), 0.5F)) {
			if (!this.isSilent()) {
				((ServerPlayer) player).networkHandler
						.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PUFFERFISH_STING, 0.0F));
			}
			if (random.nextBoolean()) {
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 20, 0));
			}
		}
	}

	static class CubozoaMoveControl extends MoveControl {
		CubozoaMoveControl(CubozoaEntity owner) {
			super(owner);
		}

		public void tick() {
			if (this.entity.isSubmergedIn(FluidTags.WATER)) {
				this.entity.setVelocity(this.entity.getVelocity().add(0.0D, 0.005D, 0.0D));
			}

			if (this.state == MoveControl.State.MOVE_TO && !this.entity.getNavigation().isIdle()) {
				float f = (float) (this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
				this.entity.setMovementSpeed(Mth.lerp(0.125F, this.entity.getMovementSpeed(), f));
				double d = this.targetX - this.entity.getX();
				double e = this.targetY - this.entity.getY();
				double g = this.targetZ - this.entity.getZ();
				if (e != 0.0D) {
					double h = (double) Mth.sqrt(d * d + e * e + g * g);
					this.entity.setVelocity(this.entity.getVelocity().add(0.0D,
							(double) this.entity.getMovementSpeed() * (e / h) * 0.1D, 0.0D));
				}

				if (d != 0.0D || g != 0.0D) {
					float i = (float) (Mth.atan2(g, d) * 57.2957763671875D) - 90.0F;
					this.entity.yaw = this.changeAngle(this.entity.yaw, i, 90.0F);
					this.entity.bodyYaw = this.entity.yaw;
				}

			} else {
				this.entity.setMovementSpeed(0.0F);
			}
		}
	}
}
