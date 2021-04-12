package ru.betterend.entity;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Flutterer;
import net.minecraft.world.entity.ItemEntity;
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
import net.minecraft.world.entity.ai.pathing.PathNodeType;
import net.minecraft.world.entity.attribute.DefaultAttributeContainer;
import net.minecraft.world.entity.attribute.Attributes;
import net.minecraft.world.entity.damage.DamageSource;
import net.minecraft.world.entity.mob.MobEntity;
import net.minecraft.world.entity.passive.AnimalEntity;
import net.minecraft.world.entity.passive.PassiveEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndItems;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class SilkMothEntity extends AnimalEntity implements Flutterer {
	private BlockPos hivePos;
	private BlockPos entrance;
	private Level hiveWorld;

	public SilkMothEntity(EntityType<? extends SilkMothEntity> entityType, Level world) {
		super(entityType, world);
		this.moveControl = new FlightMoveControl(this, 20, true);
		this.lookControl = new MothLookControl(this);
		this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
		this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
		this.experiencePoints = 1;
	}

	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, 2.0D)
				.add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.FLYING_SPEED, 0.4D)
				.add(Attributes.MOVEMENT_SPEED, 0.1D);
	}

	public void setHive(Level world, BlockPos hive) {
		this.hivePos = hive;
		this.hiveWorld = world;
	}

	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		if (hivePos != null) {
			tag.put("HivePos", NbtHelper.fromBlockPos(hivePos));
			tag.putString("HiveWorld", hiveWorld.dimension().location().toString());
		}
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
		if (tag.contains("HivePos")) {
			hivePos = NbtHelper.toBlockPos(tag.getCompound("HivePos"));
			ResourceLocation worldID = new ResourceLocation(tag.getString("HiveWorld"));
			try {
				hiveWorld = world.getServer().getLevel(ResourceKey.of(Registry.DIMENSION, worldID));
			} catch (Exception e) {
				BetterEnd.LOGGER.warning("Silk Moth Hive Level {} is missing!", worldID);
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
	public PassiveEntity createChild(ServerLevel world, PassiveEntity entity) {
		return EndEntities.SILK_MOTH.create(world);
	}

	@Override
	protected void dropLoot(DamageSource source, boolean causedByPlayer) {
		int minCount = 0;
		int maxCount = 1;
		if (causedByPlayer && this.attackingPlayer != null) {
			int looting = EnchantmentHelper.getLooting(this.attackingPlayer);
			minCount += looting;
			maxCount += looting;
			if (maxCount > 2) {
				maxCount = 2;
			}
		}
		int count = minCount < maxCount ? MHelper.randRange(minCount, maxCount, random) : maxCount;
		ItemEntity drop = new ItemEntity(world, getX(), getY(), getZ(), new ItemStack(EndItems.SILK_FIBER, count));
		this.world.spawnEntity(drop);
	}

	public static boolean canSpawn(EntityType<SilkMothEntity> type, ServerLevelAccessor world, SpawnReason spawnReason,
			BlockPos pos, Random random) {
		int y = world.getChunk(pos).sampleHeightmap(Type.WORLD_SURFACE, pos.getX() & 15, pos.getY() & 15);
		return y > 0 && pos.getY() >= y && notManyEntities(world, pos, 32, 1);
	}

	private static boolean notManyEntities(ServerLevelAccessor world, BlockPos pos, int radius, int maxCount) {
		Box box = new Box(pos).expand(radius);
		List<SilkMothEntity> list = world.getEntitiesByClass(SilkMothEntity.class, box, (entity) -> true);
		return list.size() <= maxCount;
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
			Vec3d vec3d = null;
			if (SilkMothEntity.this.hivePos != null && SilkMothEntity.this.hiveWorld == SilkMothEntity.this.world) {
				if (SilkMothEntity.this.getPos().squaredDistanceTo(SilkMothEntity.this.hivePos.getX(),
						SilkMothEntity.this.hivePos.getY(), SilkMothEntity.this.hivePos.getZ()) > 16) {
					vec3d = SilkMothEntity.this.getPos().add(random.nextGaussian() * 2, 0, random.nextGaussian() * 2);
				}
			}
			vec3d = vec3d == null ? this.getRandomLocation() : vec3d;
			if (vec3d != null) {
				try {
					SilkMothEntity.this.navigation
							.startMovingAlong(SilkMothEntity.this.navigation.findPathTo(new BlockPos(vec3d), 1), 1.0D);
				} catch (Exception e) {
				}
			}
		}

		@Nullable
		private Vec3d getRandomLocation() {
			Vec3d vec3d3 = SilkMothEntity.this.getRotationVec(0.0F);
			Vec3d vec3d4 = TargetFinder.findAirTarget(SilkMothEntity.this, 8, 7, vec3d3, 1.5707964F, 2, 1);
			return vec3d4 != null ? vec3d4
					: TargetFinder.findGroundTarget(SilkMothEntity.this, 8, 4, -2, vec3d3, 1.5707963705062866D);
		}
	}

	class ReturnToHiveGoal extends Goal {
		ReturnToHiveGoal() {
			this.setControls(EnumSet.of(Goal.Control.MOVE));
		}

		@Override
		public boolean canStart() {
			return SilkMothEntity.this.hivePos != null && SilkMothEntity.this.hiveWorld == SilkMothEntity.this.world
					&& SilkMothEntity.this.navigation.isIdle() && SilkMothEntity.this.random.nextInt(16) == 0
					&& SilkMothEntity.this.getPos().squaredDistanceTo(SilkMothEntity.this.hivePos.getX(),
							SilkMothEntity.this.hivePos.getY(), SilkMothEntity.this.hivePos.getZ()) < 64;
		}

		@Override
		public boolean shouldContinue() {
			return SilkMothEntity.this.navigation.isFollowingPath() && world.getBlockState(entrance).isAir()
					&& (world.getBlockState(hivePos).is(EndBlocks.SILK_MOTH_NEST)
							|| world.getBlockState(hivePos).is(EndBlocks.SILK_MOTH_HIVE));
		}

		@Override
		public void start() {
			BlockState state = SilkMothEntity.this.world.getBlockState(SilkMothEntity.this.hivePos);
			if (!state.is(EndBlocks.SILK_MOTH_NEST) && !state.is(EndBlocks.SILK_MOTH_HIVE)) {
				SilkMothEntity.this.hivePos = null;
				return;
			}
			try {
				SilkMothEntity.this.entrance = SilkMothEntity.this.hivePos
						.offset(state.getValue(Properties.HORIZONTAL_FACING));
				SilkMothEntity.this.navigation.startMovingAlong(SilkMothEntity.this.navigation.findPathTo(entrance, 1),
						1.0D);
			} catch (Exception e) {
			}
		}

		@Override
		public void tick() {
			super.tick();
			if (SilkMothEntity.this.entrance == null) {
				return;
			}
			double dx = Math.abs(SilkMothEntity.this.entrance.getX() - SilkMothEntity.this.getX());
			double dy = Math.abs(SilkMothEntity.this.entrance.getY() - SilkMothEntity.this.getY());
			double dz = Math.abs(SilkMothEntity.this.entrance.getZ() - SilkMothEntity.this.getZ());
			if (dx + dy + dz < 1) {
				BlockState state = SilkMothEntity.this.world.getBlockState(hivePos);
				if (state.is(EndBlocks.SILK_MOTH_NEST) || state.is(EndBlocks.SILK_MOTH_HIVE)) {
					int fullness = state.getValue(BlockProperties.FULLNESS);
					boolean isHive = state.is(EndBlocks.SILK_MOTH_HIVE);
					if (fullness < 3 && (isHive || SilkMothEntity.this.random.nextBoolean())) {
						fullness += isHive ? MHelper.randRange(1, 2, random) : 1;
						if (fullness > 3) {
							fullness = 3;
						}
						BlocksHelper.setWithUpdate(SilkMothEntity.this.hiveWorld, SilkMothEntity.this.hivePos,
								state.with(BlockProperties.FULLNESS, fullness));
					}
					SilkMothEntity.this.world.playLocalSound(null, SilkMothEntity.this.entrance,
							SoundEvents.BLOCK_BEEHIVE_ENTER, SoundSource.BLOCKS, 1, 1);
					SilkMothEntity.this.remove();
				} else {
					SilkMothEntity.this.hivePos = null;
				}
			}
		}
	}
}
