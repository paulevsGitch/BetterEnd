package ru.betterend.entity;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import ru.betterend.interfaces.ISlime;
import ru.betterend.registry.EndBiomes;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.world.biome.EndBiome;

public class EntityEndSlime extends SlimeEntity {
	private static final TrackedData<Byte> VARIANT = DataTracker.registerData(EntityEndSlime.class, TrackedDataHandlerRegistry.BYTE);
	private static final Mutable POS = new Mutable();
	
	public EntityEndSlime(EntityType<EntityEndSlime> entityType, World world) {
		super(entityType, world);
		this.moveControl = new EndSlimeMoveControl(this);
	}
	
	protected void initGoals() {
		this.goalSelector.add(1, new SwimmingGoal());
		this.goalSelector.add(2, new FaceTowardTargetGoal());
		this.goalSelector.add(3, new RandomLookGoal());
		this.goalSelector.add(5, new MoveGoal());
		this.targetSelector.add(1, new FollowTargetGoal<PlayerEntity>(this, PlayerEntity.class, 10, true, false, (livingEntity) -> {
			return Math.abs(livingEntity.getY() - this.getY()) <= 4.0D;
		}));
		this.targetSelector.add(3, new FollowTargetGoal<IronGolemEntity>(this, IronGolemEntity.class, true));
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
		EndBiome biome = EndBiomes.getFromBiome(world.getBiome(getBlockPos()));
		if (biome == EndBiomes.FOGGY_MUSHROOMLAND) {
			this.setMossy();
		}
		else if (biome == EndBiomes.MEGALAKE || biome == EndBiomes.MEGALAKE_GROVE) {
			this.setLake();
		}
		else if (biome == EndBiomes.AMBER_LAND) {
			this.setAmber(true);
		}
		this.calculateDimensions();
		return data;
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(VARIANT, (byte) 0);
	}
	
	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		tag.putByte("Variant", (byte) getSlimeType());
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		if (tag.contains("Variant")) {
			this.dataTracker.set(VARIANT, tag.getByte("Variant"));
		}
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
			int type = this.getSlimeType();
			
			for (int l = 0; l < k; ++l) {
				float g = ((float) (l % 2) - 0.5F) * f;
				float h = ((float) (l / 2) - 0.5F) * f;
				EntityEndSlime slimeEntity = (EntityEndSlime) this.getType().create(this.world);
				if (this.isPersistent()) {
					slimeEntity.setPersistent();
				}

				slimeEntity.setSlimeType(type);
				slimeEntity.setCustomName(text);
				slimeEntity.setAiDisabled(bl);
				slimeEntity.setInvulnerable(this.isInvulnerable());
				((ISlime) slimeEntity).beSetSlimeSize(j, true);
				slimeEntity.calculateDimensions();
				slimeEntity.refreshPositionAndAngles(this.getX() + (double) g, this.getY() + 0.5D, this.getZ() + (double) h, this.random.nextFloat() * 360.0F, 0.0F);
				this.world.spawnEntity(slimeEntity);
			}
		}
		this.removed = true;
	}
	
	@Override
	protected void dropLoot(DamageSource source, boolean causedByPlayer) {
		int maxCount = this.getSize();
		int minCount = maxCount >> 1;
		if (minCount < 1) {
			minCount = 1;
		}
		if (causedByPlayer && this.attackingPlayer != null) {
			int looting = EnchantmentHelper.getLooting(this.attackingPlayer);
			minCount += looting;
		}
		int count = minCount < maxCount ? MHelper.randRange(minCount, maxCount, random) : maxCount;
		ItemEntity drop = new ItemEntity(world, getX(), getY(), getZ(), new ItemStack(Items.SLIME_BALL, count));
		this.world.spawnEntity(drop);
	}
	
	public int getSlimeType() {
		return this.dataTracker.get(VARIANT).intValue();
	}
	
	public void setSlimeType(int value) {
		this.dataTracker.set(VARIANT, (byte) value);
	}
	
	protected void setMossy() {
		setSlimeType(1);
	}

	public boolean isMossy() {
		return getSlimeType() == 1;
	}
	
	protected void setLake() {
		setSlimeType(2);
	}

	public boolean isLake() {
		return getSlimeType() == 2;
	}
	
	protected void setAmber(boolean mossy) {
		this.dataTracker.set(VARIANT, (byte) 3);
	}

	public boolean isAmber() {
		return this.dataTracker.get(VARIANT) == 3;
	}
	
	public boolean isChorus() {
		return this.dataTracker.get(VARIANT) == 0;
	}
	
	public static boolean canSpawn(EntityType<EntityEndSlime> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		return random.nextInt(16) == 0 || isPermanentBiome(world, pos) || (notManyEntities(world, pos, 32, 3) && isWaterNear(world, pos, 32, 8));
	}
	
	private static boolean isPermanentBiome(ServerWorldAccess world, BlockPos pos) {
		Biome biome = world.getBiome(pos);
		return EndBiomes.getFromBiome(biome) == EndBiomes.CHORUS_FOREST;
	}
	
	private static boolean notManyEntities(ServerWorldAccess world, BlockPos pos, int radius, int maxCount) {
		Box box = new Box(pos).expand(radius);
		List<EntityEndSlime> list = world.getEntitiesByClass(EntityEndSlime.class, box, (entity) -> { return true; });
		return list.size() <= maxCount;
	}
	
	private static boolean isWaterNear(ServerWorldAccess world, BlockPos pos, int radius, int radius2) {
		for (int x = pos.getX() - radius; x <= pos.getX() + radius; x++) {
			POS.setX(x);
			for (int z = pos.getZ() - radius; z <= pos.getZ() + radius; z++) {
				POS.setZ(z);
				for (int y = pos.getY() - radius2; y <= pos.getY() + radius2; y++) {
					POS.setY(y);
					if (world.getBlockState(POS).getBlock() == Blocks.WATER) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	class MoveGoal extends Goal {
		public MoveGoal() {
			this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
		}

		public boolean canStart() {
			if (EntityEndSlime.this.hasVehicle()) {
				return false;
			}
			
			float yaw = EntityEndSlime.this.getHeadYaw();
			float speed = EntityEndSlime.this.getMovementSpeed();
			if (speed > 0.1) {
				float dx = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
				float dz = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
				BlockPos pos = EntityEndSlime.this.getBlockPos().add(dx * speed * 4, 0, dz * speed * 4);
				int down = BlocksHelper.downRay(EntityEndSlime.this.world, pos, 16);
				return down < 5;
			}
			
			return true;
		}

		public void tick() {
			((EndSlimeMoveControl) EntityEndSlime.this.getMoveControl()).move(1.0D);
		}
	}

	class SwimmingGoal extends Goal {
		public SwimmingGoal() {
			this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
			EntityEndSlime.this.getNavigation().setCanSwim(true);
		}

		public boolean canStart() {
			return (EntityEndSlime.this.isTouchingWater()
					|| EntityEndSlime.this.isInLava())
					&& EntityEndSlime.this.getMoveControl() instanceof EndSlimeMoveControl;
		}

		public void tick() {
			if (EntityEndSlime.this.getRandom().nextFloat() < 0.8F) {
				EntityEndSlime.this.getJumpControl().setActive();
			}

			((EndSlimeMoveControl) EntityEndSlime.this.getMoveControl()).move(1.2D);
		}
	}

	class RandomLookGoal extends Goal {
		private float targetYaw;
		private int timer;

		public RandomLookGoal() {
			this.setControls(EnumSet.of(Goal.Control.LOOK));
		}

		public boolean canStart() {
			return EntityEndSlime.this.getTarget() == null
					&& (EntityEndSlime.this.onGround
							|| EntityEndSlime.this.isTouchingWater()
							|| EntityEndSlime.this.isInLava()
							|| EntityEndSlime.this.hasStatusEffect(StatusEffects.LEVITATION))
					&& EntityEndSlime.this.getMoveControl() instanceof EndSlimeMoveControl;
		}

		public void tick() {
			if (--this.timer <= 0) {
				this.timer = 40 + EntityEndSlime.this.getRandom().nextInt(60);
				this.targetYaw = (float) EntityEndSlime.this.getRandom().nextInt(360);
			}

			((EndSlimeMoveControl) EntityEndSlime.this.getMoveControl()).look(this.targetYaw, false);
		}
	}

	class FaceTowardTargetGoal extends Goal {
		private int ticksLeft;

		public FaceTowardTargetGoal() {
			this.setControls(EnumSet.of(Goal.Control.LOOK));
		}

		public boolean canStart() {
			LivingEntity livingEntity = EntityEndSlime.this.getTarget();
			if (livingEntity == null) {
				return false;
			}
			else if (!livingEntity.isAlive()) {
				return false;
			}
			else {
				return livingEntity instanceof PlayerEntity && ((PlayerEntity) livingEntity).abilities.invulnerable ? false : EntityEndSlime.this.getMoveControl() instanceof EndSlimeMoveControl;
			}
		}

		public void start() {
			this.ticksLeft = 300;
			super.start();
		}

		public boolean shouldContinue() {
			LivingEntity livingEntity = EntityEndSlime.this.getTarget();
			if (livingEntity == null) {
				return false;
			}
			else if (!livingEntity.isAlive()) {
				return false;
			}
			else if (livingEntity instanceof PlayerEntity && ((PlayerEntity) livingEntity).abilities.invulnerable) {
				return false;
			}
			else {
				return --this.ticksLeft > 0;
			}
		}

		public void tick() {
			EntityEndSlime.this.lookAtEntity(EntityEndSlime.this.getTarget(), 10.0F, 10.0F);
			((EndSlimeMoveControl) EntityEndSlime.this.getMoveControl()).look(EntityEndSlime.this.yaw, EntityEndSlime.this.canAttack());
		}
	}

	class EndSlimeMoveControl extends MoveControl {
		private float targetYaw;
		private int ticksUntilJump;
		private boolean jumpOften;

		public EndSlimeMoveControl(EntityEndSlime slime) {
			super(slime);
			this.targetYaw = 180.0F * slime.yaw / 3.1415927F;
		}

		public void look(float targetYaw, boolean jumpOften) {
			this.targetYaw = targetYaw;
			this.jumpOften = jumpOften;
		}

		public void move(double speed) {
			this.speed = speed;
			this.state = MoveControl.State.MOVE_TO;
		}

		public void tick() {
			this.entity.yaw = this.changeAngle(this.entity.yaw, this.targetYaw, 90.0F);
			this.entity.headYaw = this.entity.yaw;
			this.entity.bodyYaw = this.entity.yaw;
			if (this.state != MoveControl.State.MOVE_TO) {
				this.entity.setForwardSpeed(0.0F);
			}
			else {
				this.state = MoveControl.State.WAIT;
				if (this.entity.isOnGround()) {
					this.entity.setMovementSpeed((float) (this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));
					if (this.ticksUntilJump-- <= 0) {
						this.ticksUntilJump = EntityEndSlime.this.getTicksUntilNextJump();
						if (this.jumpOften) {
							this.ticksUntilJump /= 3;
						}

						EntityEndSlime.this.getJumpControl().setActive();
						if (EntityEndSlime.this.makesJumpSound()) {
							EntityEndSlime.this.playSound(EntityEndSlime.this.getJumpSound(), EntityEndSlime.this.getSoundVolume(), getJumpSoundPitch());
						}
					}
					else {
						EntityEndSlime.this.sidewaysSpeed = 0.0F;
						EntityEndSlime.this.forwardSpeed = 0.0F;
						this.entity.setMovementSpeed(0.0F);
					}
				}
				else {
					this.entity.setMovementSpeed((float) (this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));
				}

			}
		}

		private float getJumpSoundPitch() {
			float f = EntityEndSlime.this.isSmall() ? 1.4F : 0.8F;
			return ((EntityEndSlime.this.random.nextFloat() - EntityEndSlime.this.random.nextFloat()) * 0.2F + 1.0F) * f;
		}
	}
}
