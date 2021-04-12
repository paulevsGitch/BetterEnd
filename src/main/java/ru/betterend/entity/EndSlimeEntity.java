package ru.betterend.entity;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.EntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnReason;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FollowTargetGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.attribute.DefaultAttributeContainer;
import net.minecraft.world.entity.attribute.Attributes;
import net.minecraft.world.entity.damage.DamageSource;
import net.minecraft.world.entity.data.DataTracker;
import net.minecraft.world.entity.data.TrackedData;
import net.minecraft.world.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.mob.SlimeEntity;
import net.minecraft.world.entity.passive.IronGolemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import ru.betterend.interfaces.ISlime;
import ru.betterend.registry.EndBiomes;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.world.biome.EndBiome;

public class EndSlimeEntity extends SlimeEntity {
	private static final TrackedData<Byte> VARIANT = DataTracker.registerData(EndSlimeEntity.class,
			TrackedDataHandlerRegistry.BYTE);
	private static final MutableBlockPos POS = new MutableBlockPos();

	public EndSlimeEntity(EntityType<EndSlimeEntity> entityType, Level world) {
		super(entityType, world);
		this.moveControl = new EndSlimeMoveControl(this);
	}

	protected void initGoals() {
		this.goalSelector.add(1, new SwimmingGoal());
		this.goalSelector.add(2, new FaceTowardTargetGoal());
		this.goalSelector.add(3, new RandomLookGoal());
		this.goalSelector.add(5, new MoveGoal());
		this.targetSelector.add(1, new FollowTargetGoal<Player>(this, Player.class, 10, true, false, (livingEntity) -> {
			return Math.abs(livingEntity.getY() - this.getY()) <= 4.0D;
		}));
		this.targetSelector.add(3, new FollowTargetGoal<IronGolemEntity>(this, IronGolemEntity.class, true));
	}

	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, 1.0D)
				.add(Attributes.ATTACK_DAMAGE, 1.0D).add(Attributes.FOLLOW_RANGE, 16.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.15D);
	}

	@Override
	public EntityData initialize(ServerLevelAccessor world, LocalDifficulty difficulty, SpawnReason spawnReason,
			EntityData entityData, CompoundTag entityTag) {
		EntityData data = super.initialize(world, difficulty, spawnReason, entityData, entityTag);
		EndBiome biome = EndBiomes.getFromBiome(world.getBiome(getBlockPos()));
		if (biome == EndBiomes.FOGGY_MUSHROOMLAND) {
			this.setMossy();
		} else if (biome == EndBiomes.MEGALAKE || biome == EndBiomes.MEGALAKE_GROVE) {
			this.setLake();
		} else if (biome == EndBiomes.AMBER_LAND) {
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
	protected ParticleOptions getParticles() {
		return ParticleTypes.PORTAL;
	}

	@Override
	public void remove() {
		int i = this.getSize();
		if (!this.world.isClientSide && i > 1 && this.isDead()) {
			Text text = this.getCustomName();
			boolean bl = this.isAiDisabled();
			float f = (float) i / 4.0F;
			int j = i / 2;
			int k = 2 + this.random.nextInt(3);
			int type = this.getSlimeType();

			for (int l = 0; l < k; ++l) {
				float g = ((float) (l % 2) - 0.5F) * f;
				float h = ((float) (l / 2) - 0.5F) * f;
				EndSlimeEntity slimeEntity = (EndSlimeEntity) this.getType().create(this.world);
				if (this.isPersistent()) {
					slimeEntity.setPersistent();
				}

				slimeEntity.setSlimeType(type);
				slimeEntity.setCustomName(text);
				slimeEntity.setAiDisabled(bl);
				slimeEntity.setInvulnerable(this.isInvulnerable());
				((ISlime) slimeEntity).beSetSlimeSize(j, true);
				slimeEntity.calculateDimensions();
				slimeEntity.refreshPositionAndAngles(this.getX() + (double) g, this.getY() + 0.5D,
						this.getZ() + (double) h, this.random.nextFloat() * 360.0F, 0.0F);
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

	public static boolean canSpawn(EntityType<EndSlimeEntity> type, ServerLevelAccessor world, SpawnReason spawnReason,
			BlockPos pos, Random random) {
		return random.nextInt(16) == 0 || isPermanentBiome(world, pos)
				|| (notManyEntities(world, pos, 32, 3) && isWaterNear(world, pos, 32, 8));
	}

	private static boolean isPermanentBiome(ServerLevelAccessor world, BlockPos pos) {
		Biome biome = world.getBiome(pos);
		return EndBiomes.getFromBiome(biome) == EndBiomes.CHORUS_FOREST;
	}

	private static boolean notManyEntities(ServerLevelAccessor world, BlockPos pos, int radius, int maxCount) {
		Box box = new Box(pos).expand(radius);
		List<EndSlimeEntity> list = world.getEntitiesByClass(EndSlimeEntity.class, box, (entity) -> {
			return true;
		});
		return list.size() <= maxCount;
	}

	private static boolean isWaterNear(ServerLevelAccessor world, BlockPos pos, int radius, int radius2) {
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
			if (EndSlimeEntity.this.hasVehicle()) {
				return false;
			}

			float yaw = EndSlimeEntity.this.getHeadYaw();
			float speed = EndSlimeEntity.this.getMovementSpeed();
			if (speed > 0.1) {
				float dx = Mth.sin(-yaw * 0.017453292F);
				float dz = Mth.cos(-yaw * 0.017453292F);
				BlockPos pos = EndSlimeEntity.this.getBlockPos().add(dx * speed * 4, 0, dz * speed * 4);
				int down = BlocksHelper.downRay(EndSlimeEntity.this.world, pos, 16);
				return down < 5;
			}

			return true;
		}

		public void tick() {
			((EndSlimeMoveControl) EndSlimeEntity.this.getMoveControl()).move(1.0D);
		}
	}

	class SwimmingGoal extends Goal {
		public SwimmingGoal() {
			this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
			EndSlimeEntity.this.getNavigation().setCanSwim(true);
		}

		public boolean canStart() {
			return (EndSlimeEntity.this.isTouchingWater() || EndSlimeEntity.this.isInLava())
					&& EndSlimeEntity.this.getMoveControl() instanceof EndSlimeMoveControl;
		}

		public void tick() {
			if (EndSlimeEntity.this.getRandom().nextFloat() < 0.8F) {
				EndSlimeEntity.this.getJumpControl().setActive();
			}

			((EndSlimeMoveControl) EndSlimeEntity.this.getMoveControl()).move(1.2D);
		}
	}

	class RandomLookGoal extends Goal {
		private float targetYaw;
		private int timer;

		public RandomLookGoal() {
			this.setControls(EnumSet.of(Goal.Control.LOOK));
		}

		public boolean canStart() {
			return EndSlimeEntity.this.getTarget() == null
					&& (EndSlimeEntity.this.onGround || EndSlimeEntity.this.isTouchingWater()
							|| EndSlimeEntity.this.isInLava()
							|| EndSlimeEntity.this.hasMobEffect(MobEffects.LEVITATION))
					&& EndSlimeEntity.this.getMoveControl() instanceof EndSlimeMoveControl;
		}

		public void tick() {
			if (--this.timer <= 0) {
				this.timer = 40 + EndSlimeEntity.this.getRandom().nextInt(60);
				this.targetYaw = (float) EndSlimeEntity.this.getRandom().nextInt(360);
			}

			((EndSlimeMoveControl) EndSlimeEntity.this.getMoveControl()).look(this.targetYaw, false);
		}
	}

	class FaceTowardTargetGoal extends Goal {
		private int ticksLeft;

		public FaceTowardTargetGoal() {
			this.setControls(EnumSet.of(Goal.Control.LOOK));
		}

		public boolean canStart() {
			LivingEntity livingEntity = EndSlimeEntity.this.getTarget();
			if (livingEntity == null) {
				return false;
			} else if (!livingEntity.isAlive()) {
				return false;
			} else {
				return livingEntity instanceof Player && ((Player) livingEntity).abilities.invulnerable ? false
						: EndSlimeEntity.this.getMoveControl() instanceof EndSlimeMoveControl;
			}
		}

		public void start() {
			this.ticksLeft = 300;
			super.start();
		}

		public boolean shouldContinue() {
			LivingEntity livingEntity = EndSlimeEntity.this.getTarget();
			if (livingEntity == null) {
				return false;
			} else if (!livingEntity.isAlive()) {
				return false;
			} else if (livingEntity instanceof Player && ((Player) livingEntity).abilities.invulnerable) {
				return false;
			} else {
				return --this.ticksLeft > 0;
			}
		}

		public void tick() {
			EndSlimeEntity.this.lookAtEntity(EndSlimeEntity.this.getTarget(), 10.0F, 10.0F);
			((EndSlimeMoveControl) EndSlimeEntity.this.getMoveControl()).look(EndSlimeEntity.this.yaw,
					EndSlimeEntity.this.canAttack());
		}
	}

	class EndSlimeMoveControl extends MoveControl {
		private float targetYaw;
		private int ticksUntilJump;
		private boolean jumpOften;

		public EndSlimeMoveControl(EndSlimeEntity slime) {
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
			} else {
				this.state = MoveControl.State.WAIT;
				if (this.entity.isOnGround()) {
					this.entity.setMovementSpeed(
							(float) (this.speed * this.entity.getAttributeValue(Attributes.MOVEMENT_SPEED)));
					if (this.ticksUntilJump-- <= 0) {
						this.ticksUntilJump = EndSlimeEntity.this.getTicksUntilNextJump();
						if (this.jumpOften) {
							this.ticksUntilJump /= 3;
						}

						EndSlimeEntity.this.getJumpControl().setActive();
						if (EndSlimeEntity.this.makesJumpSound()) {
							EndSlimeEntity.this.playSound(EndSlimeEntity.this.getJumpSound(),
									EndSlimeEntity.this.getSoundVolume(), getJumpSoundPitch());
						}
					} else {
						EndSlimeEntity.this.sidewaysSpeed = 0.0F;
						EndSlimeEntity.this.forwardSpeed = 0.0F;
						this.entity.setMovementSpeed(0.0F);
					}
				} else {
					this.entity.setMovementSpeed(
							(float) (this.speed * this.entity.getAttributeValue(Attributes.MOVEMENT_SPEED)));
				}

			}
		}

		private float getJumpSoundPitch() {
			float f = EndSlimeEntity.this.isSmall() ? 1.4F : 0.8F;
			return ((EndSlimeEntity.this.random.nextFloat() - EndSlimeEntity.this.random.nextFloat()) * 0.2F + 1.0F)
					* f;
		}
	}
}
