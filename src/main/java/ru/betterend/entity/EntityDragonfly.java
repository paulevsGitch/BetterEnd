package ru.betterend.entity;

import java.util.EnumSet;

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
import net.minecraft.entity.ai.pathing.Path;
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
import net.minecraft.world.WorldView;
import ru.betterend.registry.EntityRegistry;

public class EntityDragonfly extends AnimalEntity implements Flutterer {
    public EntityDragonfly(EntityType<EntityDragonfly> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 20, true);
        this.lookControl = new DragonflyLookControl(this);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
		this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.experiencePoints = 1;
    }
    
	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return LivingEntity.createLivingAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0D)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0D)
				.add(EntityAttributes.GENERIC_FLYING_SPEED, 1.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1D);
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
	public float getPathfindingFavor(BlockPos pos, WorldView world) {
		return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
	}
	
	@Override
	protected void initGoals() {
		this.goalSelector.add(1, new SwimGoal(this));
		this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
		this.goalSelector.add(3, new FollowParentGoal(this, 1.0D));
		this.goalSelector.add(4, new WanderAroundGoal());
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
	
	class DragonflyLookControl extends LookControl {
		DragonflyLookControl(MobEntity entity) {
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
	         return EntityDragonfly.this.navigation.isIdle() && EntityDragonfly.this.random.nextInt(10) == 0;
	      }

	      public boolean shouldContinue() {
	         return EntityDragonfly.this.navigation.isFollowingPath();
	      }

	      public void start() {
	         Vec3d vec3d = this.getRandomLocation();
	         if (vec3d != null) {
	        	 Path path = EntityDragonfly.this.navigation.findPathTo(new BlockPos(vec3d), 1);
	        	 EntityDragonfly.this.navigation.startMovingAlong(path, 1.0D);
	         }
	         super.start();
	      }

		private Vec3d getRandomLocation() {
			/*return new Vec3d(
				EntityDragonfly.this.random.nextGaussian() * 10,
				EntityDragonfly.this.random.nextGaussian() * 2,
				EntityDragonfly.this.random.nextGaussian() * 10
			);*/
			Vec3d vec3d3 = EntityDragonfly.this.getRotationVec(0.0F);
			Vec3d vec3d4 = TargetFinder.findAirTarget(EntityDragonfly.this, 8, 7, vec3d3, 1.5707964F, 2, 1);
			return vec3d4 != null ? vec3d4 : TargetFinder.findGroundTarget(EntityDragonfly.this, 8, 4, -2, vec3d3, 1.5707963705062866D);
		}
	   }

	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		return EntityRegistry.DRAGONFLY.create(world);
	}
}
