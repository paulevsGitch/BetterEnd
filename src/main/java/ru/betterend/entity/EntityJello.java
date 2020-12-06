package ru.betterend.entity;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import ru.betterend.registry.EndItems;

public class EntityJello extends SchoolingFishEntity {
	public static final int VARIANTS = 5;
	private static final TrackedData<Byte> VARIANT = DataTracker.registerData(EntityJello.class, TrackedDataHandlerRegistry.BYTE);
	private static final TrackedData<Byte> SCALE = DataTracker.registerData(EntityJello.class, TrackedDataHandlerRegistry.BYTE);
	
	public EntityJello(EntityType<EntityJello> entityType, World world) {
		super(entityType, world);
		this.moveControl = new JelloMoveControl(this);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new EscapeDangerGoal(this, 1.25D));
		this.goalSelector.add(2, new FleeEntityGoal<PlayerEntity>(this, PlayerEntity.class, 8.0F, 0.4D, 0.6D, EntityPredicates.EXCEPT_SPECTATOR::test));
		this.goalSelector.add(4, new SwimToRandomPlaceGoal(this));
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(VARIANT, (byte) this.getRandom().nextInt(VARIANTS));
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

	@Override
	protected ItemStack getFishBucketItem() {
		return new ItemStack(EndItems.BUCKET_END_FISH);
	}

	@Override
	protected SoundEvent getFlopSound() {
		return SoundEvents.ENTITY_TROPICAL_FISH_FLOP;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SALMON_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SALMON_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_SALMON_HURT;
	}
	
	@Override
	public void tick() {
		super.tick();
		if (random.nextInt(8) == 0 && getBlockState().isOf(Blocks.WATER)) {
			double x = getX() + random.nextGaussian() * 0.2;
			double y = getY() + random.nextGaussian() * 0.2;
			double z = getZ() + random.nextGaussian() * 0.2;
			world.addParticle(ParticleTypes.BUBBLE, x, y, z, 0, 0, 0);
		}
	}
	
	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return LivingEntity.createLivingAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 2.0)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.05);
	}
	
	public int getVariant() {
		return (int) this.dataTracker.get(VARIANT);
	}
	
	public float getScale() {
		return this.dataTracker.get(SCALE) / 32F + 0.75F;
	}
	
	public static boolean canSpawn(EntityType<EntityJello> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		Box box = new Box(pos).expand(16);
		List<EntityJello> list = world.getEntitiesByClass(EntityJello.class, box, (entity) -> { return true; });
		return list.size() < 9;
	}
	
	static class JelloMoveControl extends MoveControl {
		private final EntityJello jello;

		JelloMoveControl(EntityJello owner) {
			super(owner);
			this.jello = owner;
		}

		public void tick() {
			if (this.jello.isSubmergedIn(FluidTags.WATER)) {
				this.jello.setVelocity(this.jello.getVelocity().add(0.0D, 0.005D, 0.0D));
			}

			if (this.state == MoveControl.State.MOVE_TO && !this.jello.getNavigation().isIdle()) {
				float f = (float) (this.speed * this.jello.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
				this.jello.setMovementSpeed(MathHelper.lerp(0.025F, this.jello.getMovementSpeed(), f));
				double d = this.targetX - this.jello.getX();
				double e = this.targetY - this.jello.getY();
				double g = this.targetZ - this.jello.getZ();
				if (e != 0.0D) {
					double h = (double) MathHelper.sqrt(d * d + e * e + g * g);
					this.jello.setVelocity(this.jello.getVelocity().add(0.0D, (double) this.jello.getMovementSpeed() * (e / h) * 0.1D, 0.0D));
				}

				if (d != 0.0D || g != 0.0D) {
					float i = (float) (MathHelper.atan2(g, d) * 57.2957763671875D) - 90.0F;
					this.jello.yaw = this.changeAngle(this.jello.yaw, i, 5.0F);
					this.jello.bodyYaw = this.jello.yaw;
				}

			}
			else {
				this.jello.setMovementSpeed(0.0F);
			}
		}
	}

	static class SwimToRandomPlaceGoal extends SwimAroundGoal {
		private final EntityJello fish;

		public SwimToRandomPlaceGoal(EntityJello jello) {
			super(jello, 1.0D, 40);
			this.fish = jello;
		}

		public boolean canStart() {
			return this.fish.hasSelfControl() && super.canStart();
		}
	}
}
