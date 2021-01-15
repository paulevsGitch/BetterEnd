package ru.betterend.entity;

import java.util.EnumSet;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ru.betterend.registry.EndEntities;

public class SilkMothEntity extends AnimalEntity implements Flutterer {
	public SilkMothEntity(EntityType<? extends SilkMothEntity> entityType, World world) {
		super(entityType, world);
		this.moveControl = new FlightMoveControl(this, 20, true);
		this.lookControl = new MothLookControl(this);
		this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
		this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
		this.experiencePoints = 1;
	}
	
	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return LivingEntity.createLivingAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 2.0D)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0D)
				.add(EntityAttributes.GENERIC_FLYING_SPEED, 0.4D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1D);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
		this.goalSelector.add(5, new FollowParentGoal(this, 1.25D));
		this.goalSelector.add(8, new WanderAroundGoal());
		this.goalSelector.add(9, new SwimGoal(this));
	}
	
	@Override
	protected EntityNavigation createNavigation(World world) {
		BirdNavigation birdNavigation = new BirdNavigation(this, world) {
			public boolean isValidPosition(BlockPos pos) {
				BlockState state = this.world.getBlockState(pos);
				return state.isAir() || !state.getMaterial().blocksMovement();
			}

			public void tick() {
				super.tick();
			}
		};
		birdNavigation.setCanPathThroughDoors(false);
		birdNavigation.setCanSwim(false);
		birdNavigation.setCanEnterOpenDoors(true);
		return birdNavigation;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected boolean hasWings() {
		return true;
	}

	@Override
	public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
		return false;
	}

	@Override
	public boolean canClimb() {
		return false;
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		return EndEntities.SILK_MOTH.create(world);
	}
	
	class MothLookControl extends LookControl {
		MothLookControl(MobEntity entity) {
			super(entity);
		}

		protected boolean shouldStayHorizontal() {
			return true;
		}
	}

	class WanderAroundGoal extends Goal {
		WanderAroundGoal() {
			this.setControls(EnumSet.of(Goal.Control.MOVE));
		}

		public boolean canStart() {
			return SilkMothEntity.this.navigation.isIdle() && SilkMothEntity.this.random.nextInt(10) == 0;
		}

		public boolean shouldContinue() {
			return SilkMothEntity.this.navigation.isFollowingPath();
		}

		public void start() {
			Vec3d vec3d = this.getRandomLocation();
			if (vec3d != null) {
				try {
					SilkMothEntity.this.navigation.startMovingAlong(SilkMothEntity.this.navigation.findPathTo((BlockPos) (new BlockPos(vec3d)), 1), 1.0D);
				}
				catch (Exception e) {}
			}

		}

		@Nullable
		private Vec3d getRandomLocation() {
			Vec3d vec3d3 = SilkMothEntity.this.getRotationVec(0.0F);
			Vec3d vec3d4 = TargetFinder.findAirTarget(SilkMothEntity.this, 8, 7, vec3d3, 1.5707964F, 2, 1);
			return vec3d4 != null ? vec3d4 : TargetFinder.findGroundTarget(SilkMothEntity.this, 8, 4, -2, vec3d3, 1.5707963705062866D);
		}
	}
}
