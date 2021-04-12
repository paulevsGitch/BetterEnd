package ru.betterend.entity;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Flutterer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnReason;
import net.minecraft.world.entity.ai.TargetFinder;
import net.minecraft.world.entity.ai.control.FlightMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.AnimalMateGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.SwimGoal;
import net.minecraft.world.entity.ai.pathing.BirdNavigation;
import net.minecraft.world.entity.ai.pathing.EntityNavigation;
import net.minecraft.world.entity.ai.pathing.Path;
import net.minecraft.world.entity.ai.pathing.PathNodeType;
import net.minecraft.world.entity.attribute.DefaultAttributeContainer;
import net.minecraft.world.entity.attribute.Attributes;
import net.minecraft.world.entity.mob.MobEntity;
import net.minecraft.world.entity.passive.AnimalEntity;
import net.minecraft.world.entity.passive.PassiveEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.WorldView;
import ru.betterend.entity.DragonflyEntity.DragonflyLookControl;
import ru.betterend.entity.DragonflyEntity.WanderAroundGoal;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndSounds;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class DragonflyEntity extends AnimalEntity implements Flutterer {
	public DragonflyEntity(EntityType<DragonflyEntity> entityType, Level world) {
		super(entityType, world);
		this.moveControl = new FlightMoveControl(this, 20, true);
		this.lookControl = new DragonflyLookControl(this);
		this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
		this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
		this.experiencePoints = 1;
	}

	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, 8.0D)
				.add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.FLYING_SPEED, 1.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.1D);
	}

	@Override
	protected EntityNavigation createNavigation(Level world) {
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

	@Override
	public SoundEvent getAmbientSound() {
		return EndSounds.ENTITY_DRAGONFLY;
	}

	@Override
	protected float getSoundVolume() {
		return MHelper.randRange(0.25F, 0.5F, random);
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
			return DragonflyEntity.this.navigation.isIdle() && DragonflyEntity.this.random.nextInt(10) == 0;
		}

		public boolean shouldContinue() {
			return DragonflyEntity.this.navigation.isFollowingPath();
		}

		public void start() {
			Vec3d vec3d = this.getRandomLocation();
			if (vec3d != null) {
				BlockPos pos = new BlockPos(vec3d);
				try {
					Path path = DragonflyEntity.this.navigation.findPathTo(pos, 1);
					if (path != null) {
						DragonflyEntity.this.navigation.startMovingAlong(path, 1.0D);
					}
				} catch (Exception e) {
				}
			}
			super.start();
		}

		private Vec3d getRandomLocation() {
			int h = BlocksHelper.downRay(DragonflyEntity.this.world, DragonflyEntity.this.getBlockPos(), 16);
			Vec3d rotation = DragonflyEntity.this.getRotationVec(0.0F);
			Vec3d airPos = TargetFinder.findAirTarget(DragonflyEntity.this, 8, 7, rotation, 1.5707964F, 2, 1);
			if (airPos != null) {
				if (isInVoid(airPos)) {
					for (int i = 0; i < 8; i++) {
						airPos = TargetFinder.findAirTarget(DragonflyEntity.this, 16, 7, rotation, MHelper.PI2, 2, 1);
						if (airPos != null && !isInVoid(airPos)) {
							return airPos;
						}
					}
					return null;
				}
				if (h > 5 && airPos.getY() >= DragonflyEntity.this.getBlockPos().getY()) {
					airPos = new Vec3d(airPos.x, airPos.y - h * 0.5, airPos.z);
				}
				return airPos;
			}
			return TargetFinder.findGroundTarget(DragonflyEntity.this, 8, 4, -2, rotation, 1.5707963705062866D);
		}

		private boolean isInVoid(Vec3d pos) {
			int h = BlocksHelper.downRay(DragonflyEntity.this.world, new BlockPos(pos), 128);
			return h > 100;
		}
	}

	@Override
	public PassiveEntity createChild(ServerLevel world, PassiveEntity entity) {
		return EndEntities.DRAGONFLY.create(world);
	}

	public static boolean canSpawn(EntityType<DragonflyEntity> type, ServerLevelAccessor world, SpawnReason spawnReason,
			BlockPos pos, Random random) {
		int y = world.getChunk(pos).sampleHeightmap(Type.WORLD_SURFACE, pos.getX() & 15, pos.getY() & 15);
		return y > 0 && pos.getY() >= y;
	}
}
