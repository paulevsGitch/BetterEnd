package ru.betterend.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.api.tag.CommonBlockTags;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.biomes.BCLBiome;
import ru.betterend.interfaces.ISlime;
import ru.betterend.registry.EndBiomes;
import ru.betterend.util.GlobalState;

import java.util.EnumSet;
import java.util.Random;

public class EndSlimeEntity extends Slime {
	private static final EntityDataAccessor<Byte> VARIANT = SynchedEntityData.defineId(
		EndSlimeEntity.class,
		EntityDataSerializers.BYTE
	);
	
	public EndSlimeEntity(EntityType<EndSlimeEntity> entityType, Level world) {
		super(entityType, world);
		this.moveControl = new EndSlimeMoveControl(this);
	}
	
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new SwimmingGoal());
		this.goalSelector.addGoal(2, new FaceTowardTargetGoal());
		this.goalSelector.addGoal(3, new RandomLookGoal());
		this.goalSelector.addGoal(5, new MoveGoal());
		this.targetSelector.addGoal(
			1,
			new NearestAttackableTargetGoal<Player>(this, Player.class, 10, true, false, (livingEntity) -> {
				return Math.abs(livingEntity.getY() - this.getY()) <= 4.0D;
			})
		);
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<IronGolem>(this, IronGolem.class, true));
	}
	
	public static AttributeSupplier.Builder createMobAttributes() {
		return LivingEntity
			.createLivingAttributes()
			.add(Attributes.MAX_HEALTH, 1.0D)
			.add(Attributes.ATTACK_DAMAGE, 1.0D)
			.add(Attributes.FOLLOW_RANGE, 16.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.15D);
	}
	
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, SpawnGroupData entityData, CompoundTag entityTag) {
		SpawnGroupData data = super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityTag);
		BCLBiome biome = BiomeAPI.getFromBiome(world.getBiome(blockPosition()));
		if (biome == EndBiomes.FOGGY_MUSHROOMLAND) {
			this.setMossy();
		}
		else if (biome == EndBiomes.MEGALAKE || biome == EndBiomes.MEGALAKE_GROVE) {
			this.setLake();
		}
		else if (biome == EndBiomes.AMBER_LAND) {
			this.setAmber(true);
		}
		this.refreshDimensions();
		return data;
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT, (byte) 0);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putByte("Variant", (byte) getSlimeType());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Variant")) {
			this.entityData.set(VARIANT, tag.getByte("Variant"));
		}
	}
	
	@Override
	protected ParticleOptions getParticleType() {
		return ParticleTypes.PORTAL;
	}
	
	@Override
	public void remove(RemovalReason reason) {
		int i = this.getSize();
		if (!this.level.isClientSide && i > 1 && this.isDeadOrDying()) {
			Component text = this.getCustomName();
			boolean bl = this.isNoAi();
			float f = (float) i / 4.0F;
			int j = i / 2;
			int k = 2 + this.random.nextInt(3);
			int type = this.getSlimeType();
			
			for (int l = 0; l < k; ++l) {
				float g = ((float) (l % 2) - 0.5F) * f;
				float h = ((float) (l / 2) - 0.5F) * f;
				EndSlimeEntity slimeEntity = (EndSlimeEntity) this.getType().create(this.level);
				if (this.isPersistenceRequired()) {
					slimeEntity.setPersistenceRequired();
				}
				
				slimeEntity.setSlimeType(type);
				slimeEntity.setCustomName(text);
				slimeEntity.setNoAi(bl);
				slimeEntity.setInvulnerable(this.isInvulnerable());
				((ISlime) slimeEntity).be_setSlimeSize(j, true);
				slimeEntity.refreshDimensions();
				slimeEntity.moveTo(
					this.getX() + (double) g,
					this.getY() + 0.5D,
					this.getZ() + (double) h,
					this.random.nextFloat() * 360.0F,
					0.0F
				);
				this.level.addFreshEntity(slimeEntity);
			}
		}
		
		((ISlime) this).entityRemove(reason);
	}
	
	@Override
	protected void dropFromLootTable(DamageSource source, boolean causedByPlayer) {
		int maxCount = this.getSize();
		int minCount = maxCount >> 1;
		if (minCount < 1) {
			minCount = 1;
		}
		if (causedByPlayer && this.lastHurtByPlayer != null) {
			int looting = EnchantmentHelper.getMobLooting(this.lastHurtByPlayer);
			minCount += looting;
		}
		int count = minCount < maxCount ? MHelper.randRange(minCount, maxCount, random) : maxCount;
		ItemEntity drop = new ItemEntity(level, getX(), getY(), getZ(), new ItemStack(Items.SLIME_BALL, count));
		this.level.addFreshEntity(drop);
	}
	
	public int getSlimeType() {
		return this.entityData.get(VARIANT).intValue();
	}
	
	public void setSlimeType(int value) {
		this.entityData.set(VARIANT, (byte) value);
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
		this.entityData.set(VARIANT, (byte) 3);
	}
	
	public boolean isAmber() {
		return this.entityData.get(VARIANT) == 3;
	}
	
	public boolean isChorus() {
		return this.entityData.get(VARIANT) == 0;
	}
	
	public static boolean canSpawn(EntityType entityType, LevelAccessor world, MobSpawnType spawnType, BlockPos pos, Random random) {
		if (!world.getBlockState(pos.below()).is(CommonBlockTags.END_STONES)) {
			return false;
		}
		BCLBiome biome = BiomeAPI.getFromBiome(world.getBiome(pos));
		if (biome == EndBiomes.CHORUS_FOREST || biome == EndBiomes.MEGALAKE) {
			return true;
		}
		if (biome == EndBiomes.MEGALAKE_GROVE && random.nextBoolean()) {
			return true;
		}
		return random.nextInt(4) == 0 && isWaterNear(world, pos);
	}
	
	private static boolean isWaterNear(LevelAccessor world, BlockPos pos) {
		final MutableBlockPos POS = GlobalState.stateForThread().POS;
		
		for (int x = pos.getX() - 32; x <= pos.getX() + 32; x++) {
			POS.setX(x);
			for (int z = pos.getZ() - 32; z <= pos.getZ() + 32; z++) {
				POS.setZ(z);
				for (int y = pos.getY() - 8; y <= pos.getY() + 8; y++) {
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
			this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
		}
		
		public boolean canUse() {
			if (EndSlimeEntity.this.isPassenger()) {
				return false;
			}
			
			float yaw = EndSlimeEntity.this.getYHeadRot();
			float speed = EndSlimeEntity.this.getSpeed();
			if (speed > 0.1) {
				float dx = Mth.sin(-yaw * 0.017453292F);
				float dz = Mth.cos(-yaw * 0.017453292F);
				BlockPos pos = EndSlimeEntity.this.blockPosition().offset(dx * speed * 4, 0, dz * speed * 4);
				int down = BlocksHelper.downRay(EndSlimeEntity.this.level, pos, 16);
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
			this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
			EndSlimeEntity.this.getNavigation().setCanFloat(true);
		}
		
		public boolean canUse() {
			return (EndSlimeEntity.this.isInWater() || EndSlimeEntity.this.isInLava()) && EndSlimeEntity.this.getMoveControl() instanceof EndSlimeMoveControl;
		}
		
		public void tick() {
			if (EndSlimeEntity.this.getRandom().nextFloat() < 0.8F) {
				EndSlimeEntity.this.getJumpControl().jump();
			}
			
			((EndSlimeMoveControl) EndSlimeEntity.this.getMoveControl()).move(1.2D);
		}
	}
	
	class RandomLookGoal extends Goal {
		private float targetYaw;
		private int timer;
		
		public RandomLookGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.LOOK));
		}
		
		public boolean canUse() {
			return EndSlimeEntity.this.getTarget() == null && (EndSlimeEntity.this.onGround || EndSlimeEntity.this.isInWater() || EndSlimeEntity.this
				.isInLava() || EndSlimeEntity.this.hasEffect(MobEffects.LEVITATION)) && EndSlimeEntity.this.getMoveControl() instanceof EndSlimeMoveControl;
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
			this.setFlags(EnumSet.of(Goal.Flag.LOOK));
		}
		
		public boolean canUse() {
			LivingEntity livingEntity = EndSlimeEntity.this.getTarget();
			if (livingEntity == null) {
				return false;
			}
			else if (!livingEntity.isAlive()) {
				return false;
			}
			else {
				return livingEntity instanceof Player && ((Player) livingEntity).getAbilities().invulnerable ? false : EndSlimeEntity.this
					.getMoveControl() instanceof EndSlimeMoveControl;
			}
		}
		
		public void start() {
			this.ticksLeft = 300;
			super.start();
		}
		
		public boolean canContinueToUse() {
			LivingEntity livingEntity = EndSlimeEntity.this.getTarget();
			if (livingEntity == null) {
				return false;
			}
			else if (!livingEntity.isAlive()) {
				return false;
			}
			else if (livingEntity instanceof Player && ((Player) livingEntity).getAbilities().invulnerable) {
				return false;
			}
			else {
				return --this.ticksLeft > 0;
			}
		}
		
		public void tick() {
			EndSlimeEntity.this.lookAt(EndSlimeEntity.this.getTarget(), 10.0F, 10.0F);
			((EndSlimeMoveControl) EndSlimeEntity.this.getMoveControl()).look(
				EndSlimeEntity.this.getYRot(),
				EndSlimeEntity.this.isDealsDamage()
			);
		}
	}
	
	class EndSlimeMoveControl extends MoveControl {
		private float targetYaw;
		private int ticksUntilJump;
		private boolean jumpOften;
		
		public EndSlimeMoveControl(EndSlimeEntity slime) {
			super(slime);
			this.targetYaw = 180.0F * slime.getYRot() / 3.1415927F;
		}
		
		public void look(float targetYaw, boolean jumpOften) {
			this.targetYaw = targetYaw;
			this.jumpOften = jumpOften;
		}
		
		public void move(double speed) {
			this.speedModifier = speed;
			this.operation = MoveControl.Operation.MOVE_TO;
		}
		
		public void tick() {
			this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.targetYaw, 90.0F));
			this.mob.yHeadRot = this.mob.getYRot();
			this.mob.yBodyRot = this.mob.getYRot();
			if (this.operation != MoveControl.Operation.MOVE_TO) {
				this.mob.setZza(0.0F);
			}
			else {
				this.operation = MoveControl.Operation.WAIT;
				if (this.mob.isOnGround()) {
					this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
					if (this.ticksUntilJump-- <= 0) {
						this.ticksUntilJump = EndSlimeEntity.this.getJumpDelay();
						if (this.jumpOften) {
							this.ticksUntilJump /= 3;
						}
						
						EndSlimeEntity.this.getJumpControl().jump();
						if (EndSlimeEntity.this.doPlayJumpSound()) {
							EndSlimeEntity.this.playSound(
								EndSlimeEntity.this.getJumpSound(),
								EndSlimeEntity.this.getSoundVolume(),
								getJumpSoundPitch()
							);
						}
					}
					else {
						EndSlimeEntity.this.xxa = 0.0F;
						EndSlimeEntity.this.zza = 0.0F;
						this.mob.setSpeed(0.0F);
					}
				}
				else {
					this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
				}
				
			}
		}
		
		private float getJumpSoundPitch() {
			float f = EndSlimeEntity.this.isTiny() ? 1.4F : 0.8F;
			return ((EndSlimeEntity.this.random.nextFloat() - EndSlimeEntity.this.random.nextFloat()) * 0.2F + 1.0F) * f;
		}
	}
}
