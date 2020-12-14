package ru.betterend.entity;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import ru.betterend.registry.EndSounds;
import ru.betterend.util.MHelper;

public class EntityShadowWalker extends HostileEntity {
	public EntityShadowWalker(EntityType<EntityShadowWalker> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	protected void initGoals() {
		this.goalSelector.add(2, new AttackGoal(this, 1.0D, false));
		this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0D));
		this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(8, new LookAroundGoal(this));
		this.targetSelector.add(2, new FollowTargetGoal<PlayerEntity>(this, PlayerEntity.class, true));
	}

	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return HostileEntity.createHostileAttributes()
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.5)
				.add(EntityAttributes.GENERIC_ARMOR, 2.0)
				.add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS);
	}
	
	@Override
	public void tick() {
		super.tick();
		world.addParticle(ParticleTypes.ASH,
				getX() + random.nextGaussian() * 0.2,
				getY() + random.nextGaussian() * 0.5 + 1,
				getZ() + random.nextGaussian() * 0.2,
				0, 0, 0);
		world.addParticle(ParticleTypes.SMOKE,
				getX() + random.nextGaussian() * 0.2,
				getY() + random.nextGaussian() * 0.5 + 1,
				getZ() + random.nextGaussian() * 0.2,
				0, 0, 0);
		world.addParticle(ParticleTypes.ENTITY_EFFECT,
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
	protected float getSoundPitch() {
		return MHelper.randRange(0.75F, 1.25F, random);
	}
	
	@Override
	public boolean tryAttack(Entity target) {
		boolean attack = super.tryAttack(target);
		if (attack && target instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) target;
			if (!(living.hasStatusEffect(StatusEffects.BLINDNESS))) {
				living.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 60));
			}
		}
		return attack;
	}
	
	public static boolean canSpawn(EntityType<EntityShadowWalker> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		if (HostileEntity.canSpawnInDark(type, world, spawnReason, pos, random)) {
			Box box = new Box(pos).expand(16);
			List<EntityShadowWalker> entities = world.getEntitiesByClass(EntityShadowWalker.class, box, (entity) -> { return true; });
			return entities.size() < 6;
		}
		return false;
	}
	
	private final class AttackGoal extends MeleeAttackGoal {
		private final EntityShadowWalker walker;
		private int ticks;

		public AttackGoal(EntityShadowWalker walker, double speed, boolean pauseWhenMobIdle) {
		      super(walker, speed, pauseWhenMobIdle);
		      this.walker = walker;
		   }

		public void start() {
			super.start();
			this.ticks = 0;
		}

		public void stop() {
			super.stop();
			this.walker.setAttacking(false);
		}

		public void tick() {
			super.tick();
			++this.ticks;
			if (this.ticks >= 5 && this.method_28348() < this.method_28349() / 2) {
				this.walker.setAttacking(true);
			}
			else {
				this.walker.setAttacking(false);
			}
		}
	}
}
