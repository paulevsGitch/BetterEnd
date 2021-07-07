package ru.betterend.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndSounds;

import java.util.EnumSet;
import java.util.Random;

public class DragonflyEntity extends Animal implements FlyingAnimal {
	public DragonflyEntity(EntityType<DragonflyEntity> entityType, Level world) {
		super(entityType, world);
		this.moveControl = new FlyingMoveControl(this, 20, true);
		this.lookControl = new DragonflyLookControl(this);
		this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
		this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
		this.xpReward = 1;
	}

	public static AttributeSupplier.Builder createMobAttributes() {
		return LivingEntity.createLivingAttributes()
				.add(Attributes.MAX_HEALTH, 8.0D)
				.add(Attributes.FOLLOW_RANGE, 16.0D)
				.add(Attributes.FLYING_SPEED, 1.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.1D);
	}

	@Override
	public boolean canBeLeashed(Player player) {
		return false;
	}

	@Override
	protected PathNavigation createNavigation(Level world) {
		FlyingPathNavigation birdNavigation = new FlyingPathNavigation(this, world) {
			public boolean isStableDestination(BlockPos pos) {
				BlockState state = this.level.getBlockState(pos);
				return state.isAir() || !state.getMaterial().blocksMotion();
			}

			public void tick() {
				super.tick();
			}
		};
		birdNavigation.setCanOpenDoors(false);
		birdNavigation.setCanFloat(false);
		birdNavigation.setCanPassDoors(true);
		return birdNavigation;
	}

	@Override
	public float getWalkTargetValue(BlockPos pos, LevelReader world) {
		return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.0D));
		this.goalSelector.addGoal(4, new WanderAroundGoal());
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
		return false;
	}

	@Override
	protected Entity.MovementEmission getMovementEmission() {
		return Entity.MovementEmission.EVENTS;
	}

	@Override
	public boolean isFlying() {
		return !this.onGround;
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	@Override
	public SoundEvent getAmbientSound() {
		return EndSounds.ENTITY_DRAGONFLY;
	}

	@Override
	protected float getSoundVolume() {
		return MHelper.randRange(0.25F, 0.5F, random);
	}

	class DragonflyLookControl extends LookControl {
		DragonflyLookControl(Mob entity) {
			super(entity);
		}

		protected boolean resetXRotOnTick() {
			return true;
		}
	}

	class WanderAroundGoal extends Goal {
		WanderAroundGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			return DragonflyEntity.this.navigation.isDone() && DragonflyEntity.this.random.nextInt(10) == 0;
		}

		public boolean canContinueToUse() {
			return DragonflyEntity.this.navigation.isInProgress();
		}

		public void start() {
			Vec3 vec3d = this.getRandomLocation();
			if (vec3d != null) {
				BlockPos pos = new BlockPos(vec3d);
				try {
					Path path = DragonflyEntity.this.navigation.createPath(pos, 1);
					if (path != null) {
						DragonflyEntity.this.navigation.moveTo(path, 1.0D);
					}
				}
				catch (Exception e) {
				}
			}
			super.start();
		}

		private Vec3 getRandomLocation() {
			int h = BlocksHelper.downRay(DragonflyEntity.this.level, DragonflyEntity.this.blockPosition(), 16);
			Vec3 rotation = DragonflyEntity.this.getViewVector(0.0F);
			Vec3 airPos = HoverRandomPos.getPos(DragonflyEntity.this, 8, 7, rotation.x, rotation.z, 1.5707964F, 3, 1);
			if (airPos != null) {
				if (isInVoid(airPos)) {
					for (int i = 0; i < 8; i++) {
						airPos = HoverRandomPos.getPos(DragonflyEntity.this, 16, 7, rotation.x, rotation.z, MHelper.PI2, 3, 1);
						if (airPos != null && !isInVoid(airPos)) {
							return airPos;
						}
					}
					return null;
				}
				if (h > 5 && airPos.y() >= DragonflyEntity.this.blockPosition().getY()) {
					airPos = new Vec3(airPos.x, airPos.y - h * 0.5, airPos.z);
				}
				return airPos;
			}
			return AirAndWaterRandomPos.getPos(DragonflyEntity.this, 8, 4, -2, rotation.x, rotation.z, 1.5707963705062866D);
		}

		private boolean isInVoid(Vec3 pos) {
			int h = BlocksHelper.downRay(DragonflyEntity.this.level, new BlockPos(pos), 128);
			return h > 100;
		}
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
		return EndEntities.DRAGONFLY.create(world);
	}

	public static boolean canSpawn(EntityType<DragonflyEntity> type, ServerLevelAccessor world, MobSpawnType spawnReason, BlockPos pos, Random random) {
		int y = world.getChunk(pos).getHeight(Types.WORLD_SURFACE, pos.getX() & 15, pos.getY() & 15);
		return y > 0 && pos.getY() >= y;
	}
}
