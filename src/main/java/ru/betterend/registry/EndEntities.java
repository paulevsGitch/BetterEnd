package ru.betterend.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import ru.bclib.util.ColorUtil;
import ru.betterend.BetterEnd;
import ru.betterend.config.Configs;
import ru.betterend.entity.CubozoaEntity;
import ru.betterend.entity.DragonflyEntity;
import ru.betterend.entity.EndFishEntity;
import ru.betterend.entity.EndSlimeEntity;
import ru.betterend.entity.ShadowWalkerEntity;
import ru.betterend.entity.SilkMothEntity;
import ru.betterend.util.SpawnHelper;

public class EndEntities {
	public static final EntityType<DragonflyEntity> DRAGONFLY = register(
		"dragonfly",
		MobCategory.AMBIENT,
		0.6F,
		0.5F,
		DragonflyEntity::new,
		DragonflyEntity.createMobAttributes(),
		true,
		ColorUtil.color(32, 42, 176),
		ColorUtil.color(115, 225, 249)
	);
	public static final EntityType<EndSlimeEntity> END_SLIME = register(
		"end_slime",
		MobCategory.MONSTER,
		2F,
		2F,
		EndSlimeEntity::new,
		EndSlimeEntity.createMobAttributes(),
		false,
		ColorUtil.color(28, 28, 28),
		ColorUtil.color(99, 11, 99)
	);
	public static final EntityType<EndFishEntity> END_FISH = register(
		"end_fish",
		MobCategory.WATER_AMBIENT,
		0.5F,
		0.5F,
		EndFishEntity::new,
		EndFishEntity.createMobAttributes(),
		true,
		ColorUtil.color(3, 50, 76),
		ColorUtil.color(120, 206, 255)
	);
	public static final EntityType<ShadowWalkerEntity> SHADOW_WALKER = register(
		"shadow_walker",
		MobCategory.MONSTER,
		0.6F,
		1.95F,
		ShadowWalkerEntity::new,
		ShadowWalkerEntity.createMobAttributes(),
		true,
		ColorUtil.color(30, 30, 30),
		ColorUtil.color(5, 5, 5)
	);
	public static final EntityType<CubozoaEntity> CUBOZOA = register(
		"cubozoa",
		MobCategory.WATER_AMBIENT,
		0.6F,
		1F,
		CubozoaEntity::new,
		CubozoaEntity.createMobAttributes(),
		true,
		ColorUtil.color(151, 77, 181),
		ColorUtil.color(93, 176, 238)
	);
	public static final EntityType<SilkMothEntity> SILK_MOTH = register(
		"silk_moth",
		MobCategory.AMBIENT,
		0.6F,
		0.6F,
		SilkMothEntity::new,
		SilkMothEntity.createMobAttributes(),
		true,
		ColorUtil.color(198, 138, 204),
		ColorUtil.color(242, 220, 236)
	);
	
	public static void register() {
		SpawnHelper.restrictionAir(DRAGONFLY, DragonflyEntity::canSpawn);
		SpawnHelper.restrictionLand(END_SLIME, EndSlimeEntity::canSpawn);
		SpawnHelper.restrictionWater(END_FISH, EndFishEntity::canSpawn);
		SpawnHelper.restrictionLand(SHADOW_WALKER, ShadowWalkerEntity::canSpawn);
		SpawnHelper.restrictionWater(CUBOZOA, CubozoaEntity::canSpawn);
		SpawnHelper.restrictionAir(SILK_MOTH, SilkMothEntity::canSpawn);
	}
	
	protected static <T extends Entity> EntityType<T> register(String name, MobCategory group, float width, float height, EntityFactory<T> entity) {
		ResourceLocation id = BetterEnd.makeID(name);
		EntityType<T> type = FabricEntityTypeBuilder
			.<T>create(group, entity)
			.dimensions(EntityDimensions.fixed(width, height))
			.build();
		if (Configs.ENTITY_CONFIG.getBooleanRoot(id.getPath(), true)) {
			return Registry.register(Registry.ENTITY_TYPE, id, type);
		}
		return type;
	}
	
	private static <T extends Mob> EntityType<T> register(String name, MobCategory group, float width, float height, EntityFactory<T> entity, Builder attributes, boolean fixedSize, int eggColor, int dotsColor) {
		ResourceLocation id = BetterEnd.makeID(name);
		EntityType<T> type = FabricEntityTypeBuilder
			.<T>create(group, entity)
			.dimensions(fixedSize ? EntityDimensions.fixed(width, height) : EntityDimensions.scalable(width, height))
			.build();
		if (Configs.ENTITY_CONFIG.getBooleanRoot(id.getPath(), true)) {
			FabricDefaultAttributeRegistry.register(type, attributes);
			EndItems.registerEndEgg("spawn_egg_" + name, type, eggColor, dotsColor);
			return Registry.register(Registry.ENTITY_TYPE, BetterEnd.makeID(name), type);
		}
		return type;
	}
}
