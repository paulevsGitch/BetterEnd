package ru.betterend.entity;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgableMob;
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
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.entity.SilkMothEntity.MothLookControl;
import ru.betterend.entity.SilkMothEntity.ReturnToHiveGoal;
import ru.betterend.entity.SilkMothEntity.WanderAroundGoal;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndItems;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class SilkMothEntity extends Animal implements FlyingAnimal {
	private BlockPos hivePos;
	private BlockPos entrance;
	private Level hiveWorld;
	
	public SilkMothEntity(EntityType<? extends SilkMothEntity> entityType, Level world) {
		super(entityType, world);
		this.moveControl = new FlyingMoveControl(this, 20, true);
		this.lookControl = new MothLookControl(this);
		this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
		this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
		this.xpReward = 1;
	}
	
	public static AttributeSupplier.Builder createMobAttributes() {
		return LivingEntity.createLivingAttributes()
				.add(Attributes.MAX_HEALTH, 2.0D)
				.add(Attributes.FOLLOW_RANGE, 16.0D)
				.add(Attributes.FLYING_SPEED, 0.4D)
				.add(Attributes.MOVEMENT_SPEED, 0.1D);
	}
	
	public void setHive(Level world, BlockPos hive) {
		this.hivePos = hive;
		this.hiveWorld = world;
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		if (hivePos != null) {
			tag.put("HivePos", NbtUtils.writeBlockPos(hivePos));
			tag.putString("HiveWorld", hiveWorld.dimension().location().toString());
		}
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		if (tag.contains("HivePos")) {
			hivePos = NbtUtils.readBlockPos(tag.getCompound("HivePos"));
			ResourceLocation worldID = new ResourceLocation(tag.getString("HiveWorld"));
			try {
				hiveWorld = level.getServer().getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, worldID));
			}
			catch (Exception e) {
				BetterEnd.LOGGER.warning("Silk Moth Hive World {} is missing!", worldID);
				hivePos = null;
			}
		}
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new ReturnToHiveGoal());
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
		this.goalSelector.addGoal(8, new WanderAroundGoal());
		this.goalSelector.addGoal(9, new FloatGoal(this));
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
	public boolean isPushable() {
		return false;
	}

	@Override
	protected boolean makeFlySound() {
		return true;
	}

	@Override
	public boolean causeFallDamage(float fallDistance, float damageMultiplier) {
		return false;
	}

	@Override
	public boolean isMovementNoisy() {
		return false;
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	@Override
	public AgableMob getBreedOffspring(ServerLevel world, AgableMob entity) {
		return EndEntities.SILK_MOTH.create(world);
	}
	
	@Override
	protected void dropFromLootTable(DamageSource source, boolean causedByPlayer) {
		int minCount = 0;
		int maxCount = 1;
		if (causedByPlayer && this.lastHurtByPlayer != null) {
			int looting = EnchantmentHelper.getMobLooting(this.lastHurtByPlayer);
			minCount += looting;
			maxCount += looting;
			if (maxCount > 2) {
				maxCount = 2;
			}
		}
		int count = minCount < maxCount ? MHelper.randRange(minCount, maxCount, random) : maxCount;
		ItemEntity drop = new ItemEntity(level, getX(), getY(), getZ(), new ItemStack(EndItems.SILK_FIBER, count));
		this.level.addFreshEntity(drop);
	}
	
	public static boolean canSpawn(EntityType<SilkMothEntity> type, ServerLevelAccessor world, MobSpawnType spawnReason, BlockPos pos, Random random) {
		int y = world.getChunk(pos).getHeight(Types.WORLD_SURFACE, pos.getX() & 15, pos.getY() & 15);
		return y > 0 && pos.getY() >= y && notManyEntities(world, pos, 32, 1);
	}
	
	private static boolean notManyEntities(ServerLevelAccessor world, BlockPos pos, int radius, int maxCount) {
		AABB box = new AABB(pos).inflate(radius);
		List<SilkMothEntity> list = world.getEntitiesOfClass(SilkMothEntity.class, box, (entity) -> true);
		return list.size() <= maxCount;
	}
	
	class MothLookControl extends LookControl {
		MothLookControl(Mob entity) {
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

		@Override
		public boolean canUse() {
			return SilkMothEntity.this.navigation.isDone() && SilkMothEntity.this.random.nextInt(10) == 0;
		}

		@Override
		public boolean canContinueToUse() {
			return SilkMothEntity.this.navigation.isInProgress();
		}

		@Override
		public void start() {
			Vec3 vec3d = null;
			if (SilkMothEntity.this.hivePos != null && SilkMothEntity.this.hiveWorld == SilkMothEntity.this.level) {
				if (SilkMothEntity.this.position().distanceToSqr(SilkMothEntity.this.hivePos.getX(), SilkMothEntity.this.hivePos.getY(), SilkMothEntity.this.hivePos.getZ()) > 16) {
					vec3d = SilkMothEntity.this.position().add(random.nextGaussian() * 2, 0, random.nextGaussian() * 2);
				}
			}
			vec3d = vec3d == null ? this.getRandomLocation() : vec3d;
			if (vec3d != null) {
				try {
					SilkMothEntity.this.navigation.moveTo(SilkMothEntity.this.navigation.createPath(new BlockPos(vec3d), 1), 1.0D);
				}
				catch (Exception e) {}
			}
		}

		@Nullable
		private Vec3 getRandomLocation() {
			Vec3 vec3d3 = SilkMothEntity.this.getViewVector(0.0F);
			Vec3 vec3d4 = RandomPos.getAboveLandPos(SilkMothEntity.this, 8, 7, vec3d3, 1.5707964F, 2, 1);
			return vec3d4 != null ? vec3d4 : RandomPos.getAirPos(SilkMothEntity.this, 8, 4, -2, vec3d3, 1.5707963705062866D);
		}
	}
	
	class ReturnToHiveGoal extends Goal {
		ReturnToHiveGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}
		
		@Override
		public boolean canUse() {
			return SilkMothEntity.this.hivePos != null
					&& SilkMothEntity.this.hiveWorld == SilkMothEntity.this.level
					&& SilkMothEntity.this.navigation.isDone()
					&& SilkMothEntity.this.random.nextInt(16) == 0
					&& SilkMothEntity.this.position().distanceToSqr(SilkMothEntity.this.hivePos.getX(), SilkMothEntity.this.hivePos.getY(), SilkMothEntity.this.hivePos.getZ()) < 64;
		}
		
		@Override
		public boolean canContinueToUse() {
			return SilkMothEntity.this.navigation.isInProgress() && level.getBlockState(entrance).isAir() && (level.getBlockState(hivePos).is(EndBlocks.SILK_MOTH_NEST) || level.getBlockState(hivePos).is(EndBlocks.SILK_MOTH_HIVE));
		}
		
		@Override
		public void start() {
			BlockState state = SilkMothEntity.this.level.getBlockState(SilkMothEntity.this.hivePos);
			if (!state.is(EndBlocks.SILK_MOTH_NEST) && !state.is(EndBlocks.SILK_MOTH_HIVE)) {
				SilkMothEntity.this.hivePos = null;
				return;
			}
			try {
				SilkMothEntity.this.entrance = SilkMothEntity.this.hivePos.relative(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
				SilkMothEntity.this.navigation.moveTo(SilkMothEntity.this.navigation.createPath(entrance, 1), 1.0D);
			}
			catch (Exception e) {}
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
				BlockState state = SilkMothEntity.this.level.getBlockState(hivePos);
				if (state.is(EndBlocks.SILK_MOTH_NEST) || state.is(EndBlocks.SILK_MOTH_HIVE)) {
					int fullness = state.getValue(BlockProperties.FULLNESS);
					boolean isHive = state.is(EndBlocks.SILK_MOTH_HIVE);
					if (fullness < 3 && (isHive || SilkMothEntity.this.random.nextBoolean())) {
						fullness += isHive ? MHelper.randRange(1, 2, random) : 1;
						if (fullness > 3) {
							fullness = 3;
						}
						BlocksHelper.setWithUpdate(SilkMothEntity.this.hiveWorld, SilkMothEntity.this.hivePos, state.setValue(BlockProperties.FULLNESS, fullness));
					}
					SilkMothEntity.this.level.playSound(null, SilkMothEntity.this.entrance, SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1, 1);
					SilkMothEntity.this.remove();
				}
				else {
					SilkMothEntity.this.hivePos = null;
				}
			}
		}
	}
}
