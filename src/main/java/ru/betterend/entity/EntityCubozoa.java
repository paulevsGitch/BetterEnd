package ru.betterend.entity;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import ru.betterend.registry.EndBiomes;
import ru.betterend.util.MHelper;

public class EntityCubozoa extends WaterCreatureEntity {
	public static final int VARIANTS = 2;
	private static final TrackedData<Byte> VARIANT = DataTracker.registerData(EntityCubozoa.class, TrackedDataHandlerRegistry.BYTE);
	private static final TrackedData<Byte> SCALE = DataTracker.registerData(EntityCubozoa.class, TrackedDataHandlerRegistry.BYTE);
	private int moveTicks = 0;
	private double moveX;
	private double moveY;
	private double moveZ;
	private int timer;

	public EntityCubozoa(EntityType<EntityCubozoa> entityType, World world) {
		super(entityType, world);
		timer = MHelper.randRange(20, 40, random);
	}

	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CompoundTag entityTag) {
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

	public static boolean canSpawn(EntityType<EntityCubozoa> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		Box box = new Box(pos).expand(16);
		List<EntityCubozoa> list = world.getEntitiesByClass(EntityCubozoa.class, box, (entity) -> {
			return true;
		});
		return list.size() < 9;
	}

	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
		return dimensions.height * 0.5F;
	}

	@Override
	public void tickMovement() {
		super.tickMovement();
		if (moveTicks >= timer) {
			if (random.nextInt(4) == 0 && world.getBlockState(getBlockPos().down()).isOf(Blocks.WATER)) {
				moveX = 0;
				moveZ = 0;
				moveY = -0.05;
			}
			else if (random.nextBoolean()) {
				moveX = random.nextGaussian();
				moveZ = random.nextGaussian();
				moveY = (moveX == 0 && moveZ == 0) ? 1 : random.nextDouble();
				double l = MHelper.lengthSqr(moveX, moveY, moveZ) / 0.05;
				moveX /= l;
				moveY /= l;
				moveZ /= l;
			}
			moveTicks = 0;
			timer = MHelper.randRange(20, 40, random);
			this.yaw = (float) Math.atan2(moveX, moveZ);
			this.bodyYaw = this.yaw;
			this.pitch = (float) Math.asin(-moveY);
		}
		moveX *= 0.98;
		moveY *= 0.98;
		moveZ *= 0.98;
		this.setVelocity(moveX, moveY, moveZ);
		moveTicks ++;
	}
}
