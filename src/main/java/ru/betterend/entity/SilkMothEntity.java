package ru.betterend.entity;

import java.util.EnumSet;
import java.util.Random;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.Heightmap.Type;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.SilkMothNestBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndEntities;
import ru.betterend.util.BlocksHelper;

public class SilkMothEntity extends AnimalEntity implements Flutterer {
	private BlockPos hivePos;
	private BlockPos entrance;
	private World hiveWorld;
	
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
	
	public void setHive(World world, BlockPos hive) {
		this.hivePos = hive;
		this.hiveWorld = world;
	}
	
	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		if (hivePos != null) {
			tag.put("HivePos", NbtHelper.fromBlockPos(hivePos));
			tag.putString("HiveWorld", hiveWorld.getRegistryKey().getValue().toString());
		}
	}
	
	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
		if (tag.contains("HivePos")) {
			hivePos = NbtHelper.toBlockPos(tag.getCompound("HivePos"));
			Identifier worldID = new Identifier(tag.getString("HiveWorld"));
			try {
				hiveWorld = world.getServer().getWorld(RegistryKey.of(Registry.DIMENSION, worldID));
			}
			catch (Exception e) {
				BetterEnd.LOGGER.warning("Silk Moth Hive World {} is missing!", worldID);
				hivePos = null;
			}
		}
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(1, new ReturnToHiveGoal());
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
	
	public static boolean canSpawn(EntityType<SilkMothEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		int y = world.getChunk(pos).sampleHeightmap(Type.WORLD_SURFACE, pos.getX() & 15, pos.getY() & 15);
		return y > 0 && pos.getY() >= y;
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

		@Override
		public boolean canStart() {
			return SilkMothEntity.this.navigation.isIdle() && SilkMothEntity.this.random.nextInt(10) == 0;
		}

		@Override
		public boolean shouldContinue() {
			return SilkMothEntity.this.navigation.isFollowingPath();
		}

		@Override
		public void start() {
			Vec3d vec3d = this.getRandomLocation();
			if (vec3d != null) {
				try {
					SilkMothEntity.this.navigation.startMovingAlong(SilkMothEntity.this.navigation.findPathTo(new BlockPos(vec3d), 1), 1.0D);
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
	
	class ReturnToHiveGoal extends Goal {
		ReturnToHiveGoal() {
			this.setControls(EnumSet.of(Goal.Control.MOVE));
		}
		
		@Override
		public boolean canStart() {
			return SilkMothEntity.this.hivePos != null
					&& SilkMothEntity.this.hiveWorld == SilkMothEntity.this.world
					&& SilkMothEntity.this.navigation.isIdle()
					&& SilkMothEntity.this.random.nextInt(16) == 0
					&& SilkMothEntity.this.getPos().squaredDistanceTo(SilkMothEntity.this.hivePos.getX(), SilkMothEntity.this.hivePos.getY(), SilkMothEntity.this.hivePos.getZ()) < 32;
		}
		
		@Override
		public boolean shouldContinue() {
			return SilkMothEntity.this.navigation.isFollowingPath() && world.getBlockState(entrance).isAir() && world.getBlockState(hivePos).isOf(EndBlocks.SILK_MOTH_NEST);
		}
		
		@Override
		public void start() {
			BlockState state = SilkMothEntity.this.world.getBlockState(SilkMothEntity.this.hivePos);
			if (!state.isOf(EndBlocks.SILK_MOTH_NEST)) {
				SilkMothEntity.this.hivePos = null;
			}
			try {
				SilkMothEntity.this.entrance = SilkMothEntity.this.hivePos.offset(state.get(SilkMothNestBlock.FACING));
				SilkMothEntity.this.navigation.startMovingAlong(SilkMothEntity.this.navigation.findPathTo(entrance, 1), 1.0D);
			}
			catch (Exception e) {}
		}
		
		@Override
		public void tick() {
			super.tick();
			double dx = Math.abs(SilkMothEntity.this.entrance.getX() - SilkMothEntity.this.getX());
			double dy = Math.abs(SilkMothEntity.this.entrance.getY() - SilkMothEntity.this.getY());
			double dz = Math.abs(SilkMothEntity.this.entrance.getZ() - SilkMothEntity.this.getZ());
			if (dx + dy + dz < 1) {
				BlockState state = SilkMothEntity.this.world.getBlockState(hivePos);
				if (state.isOf(EndBlocks.SILK_MOTH_NEST)) {
					int fullness = state.get(BlockProperties.FULLNESS);
					if (fullness < 3 && SilkMothEntity.this.random.nextBoolean()) {
						fullness ++;
						BlocksHelper.setWithUpdate(SilkMothEntity.this.hiveWorld, SilkMothEntity.this.hivePos, state);
					}
					SilkMothEntity.this.world.playSound(null, SilkMothEntity.this.entrance, SoundEvents.BLOCK_BEEHIVE_ENTER, SoundCategory.BLOCKS, 1, 1);
					SilkMothEntity.this.remove();
				}
			}
		}
	}
}
