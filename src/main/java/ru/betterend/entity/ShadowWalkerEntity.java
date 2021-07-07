package ru.betterend.entity;

import java.util.List;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import ru.bclib.util.MHelper;
import ru.betterend.registry.EndSounds;

public class ShadowWalkerEntity extends Monster {
	public ShadowWalkerEntity(EntityType<ShadowWalkerEntity> entityType, Level world) {
		super(entityType, world);
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new AttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<Player>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder createMobAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.FOLLOW_RANGE, 35.0)
				.add(Attributes.MOVEMENT_SPEED, 0.15)
				.add(Attributes.ATTACK_DAMAGE, 4.5)
				.add(Attributes.ARMOR, 2.0)
				.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
	}
	
	@Override
	public void tick() {
		super.tick();
		level.addParticle(ParticleTypes.ASH,
				getX() + random.nextGaussian() * 0.2,
				getY() + random.nextGaussian() * 0.5 + 1,
				getZ() + random.nextGaussian() * 0.2,
				0, 0, 0);
		level.addParticle(ParticleTypes.SMOKE,
				getX() + random.nextGaussian() * 0.2,
				getY() + random.nextGaussian() * 0.5 + 1,
				getZ() + random.nextGaussian() * 0.2,
				0, 0, 0);
		level.addParticle(ParticleTypes.ENTITY_EFFECT,
				getX() + random.nextGaussian() * 0.2,
				getY() + random.nextGaussian() * 0.5 + 1,
				getZ() + random.nextGaussian() * 0.2,
				0, 0, 0);
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return EndSounds.ENTITY_SHADOW_WALKER;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return EndSounds.ENTITY_SHADOW_WALKER_DAMAGE;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return EndSounds.ENTITY_SHADOW_WALKER_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {}
	
	@Override
	protected float getSoundVolume() {
		return MHelper.randRange(0.25F, 0.5F, random);
	}
	
	@Override
	public float getVoicePitch() {
		return MHelper.randRange(0.75F, 1.25F, random);
	}
	
	@Override
	public boolean doHurtTarget(Entity target) {
		boolean attack = super.doHurtTarget(target);
		if (attack && target instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) target;
			if (!(living.hasEffect(MobEffects.BLINDNESS))) {
				living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60));
			}
		}
		return attack;
	}
	
	public static boolean canSpawn(EntityType<ShadowWalkerEntity> type, ServerLevelAccessor world, MobSpawnType spawnReason, BlockPos pos, Random random) {
		if (Monster.checkMonsterSpawnRules(type, world, spawnReason, pos, random)) {
			AABB box = new AABB(pos).inflate(16);
			List<ShadowWalkerEntity> entities = world.getEntitiesOfClass(ShadowWalkerEntity.class, box, (entity) -> { return true; });
			return entities.size() < 6;
		}
		return false;
	}
	
	private final class AttackGoal extends MeleeAttackGoal {
		private final ShadowWalkerEntity walker;
		private int ticks;

		public AttackGoal(ShadowWalkerEntity walker, double speed, boolean pauseWhenMobIdle) {
		      super(walker, speed, pauseWhenMobIdle);
		      this.walker = walker;
		   }

		public void start() {
			super.start();
			this.ticks = 0;
		}

		public void stop() {
			super.stop();
			this.walker.setAggressive(false);
		}

		public void tick() {
			super.tick();
			++this.ticks;
			if (this.ticks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
				this.walker.setAggressive(true);
			}
			else {
				this.walker.setAggressive(false);
			}
		}
	}
}
