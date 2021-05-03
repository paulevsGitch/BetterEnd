package ru.betterend.registry;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.DispenserBlock;
import ru.betterend.BetterEnd;
import ru.betterend.config.Configs;
import ru.betterend.interfaces.BreakableItem;
import ru.betterend.item.*;
import ru.betterend.item.material.EndArmorMaterial;
import ru.betterend.item.material.EndToolMaterial;
import ru.betterend.item.tool.*;
import ru.betterend.tab.CreativeTabs;
import ru.betterend.util.TagHelper;

import java.util.List;

public class EndItems {
	private static final List<Item> MOD_BLOCKS = Lists.newArrayList();
	private static final List<Item> MOD_ITEMS = Lists.newArrayList();
	
	// Materials //
	public final static Item ENDER_DUST = registerItem("ender_dust");
	public final static Item ENDER_SHARD = registerItem("ender_shard");
	public final static Item AETERNIUM_INGOT = registerItem("aeternium_ingot");
	public final static Item END_LILY_LEAF = registerItem("end_lily_leaf");
	public final static Item END_LILY_LEAF_DRIED = registerItem("end_lily_leaf_dried");
	public final static Item CRYSTAL_SHARDS = registerItem("crystal_shards");
	public final static Item RAW_AMBER = registerItem("raw_amber");
	public final static Item AMBER_GEM = registerItem("amber_gem");
	public final static Item GLOWING_BULB = registerItem("glowing_bulb");
	public final static Item CRYSTALLINE_SULPHUR = registerItem("crystalline_sulphur");
	public final static Item HYDRALUX_PETAL = registerItem("hydralux_petal");
	public final static Item GELATINE = registerItem("gelatine");
	public static final Item ETERNAL_CRYSTAL = registerItem("eternal_crystal", new EternalCrystalItem());
	public final static Item ENCHANTED_PETAL = registerItem("enchanted_petal", new EnchantedPetalItem());
	public final static Item LEATHER_STRIPE = registerItem("leather_stripe");
	public final static Item LEATHER_WRAPPED_STICK = registerItem("leather_wrapped_stick");
	public final static Item SILK_FIBER = registerItem("silk_fiber");
	public final static Item LUMECORN_ROD = registerItem("lumecorn_rod");
	public final static Item SILK_MOTH_MATRIX = registerItem("silk_moth_matrix");
	
	// Music Discs
	public final static Item MUSIC_DISC_STRANGE_AND_ALIEN = registerDisc("music_disc_strange_and_alien", 0, EndSounds.STRANGE_AND_ALIEN);
	
	// Armor //
	public static final Item AETERNIUM_HELMET = registerItem("aeternium_helmet", new EndArmorItem(EndArmorMaterial.AETERNIUM, EquipmentSlot.HEAD, makeItemSettings().fireResistant()));
	public static final Item AETERNIUM_CHESTPLATE = registerItem("aeternium_chestplate", new EndArmorItem(EndArmorMaterial.AETERNIUM, EquipmentSlot.CHEST, makeItemSettings().fireResistant()));
	public static final Item AETERNIUM_LEGGINGS = registerItem("aeternium_leggings", new EndArmorItem(EndArmorMaterial.AETERNIUM, EquipmentSlot.LEGS, makeItemSettings().fireResistant()));
	public static final Item AETERNIUM_BOOTS = registerItem("aeternium_boots", new EndArmorItem(EndArmorMaterial.AETERNIUM, EquipmentSlot.FEET, makeItemSettings().fireResistant()));
	public static final Item CRYSTALITE_HELMET = registerItem("crystalite_helmet", new EndArmorItem(EndArmorMaterial.CRYSTALITE, EquipmentSlot.HEAD, makeItemSettings().rarity(Rarity.UNCOMMON)));
	public static final Item CRYSTALITE_CHESTPLATE = registerItem("crystalite_chestplate", new EndArmorItem(EndArmorMaterial.CRYSTALITE, EquipmentSlot.CHEST, makeItemSettings().rarity(Rarity.UNCOMMON)));
	public static final Item CRYSTALITE_LEGGINGS = registerItem("crystalite_leggings", new EndArmorItem(EndArmorMaterial.CRYSTALITE, EquipmentSlot.LEGS, makeItemSettings().rarity(Rarity.UNCOMMON)));
	public static final Item CRYSTALITE_BOOTS = registerItem("crystalite_boots", new EndArmorItem(EndArmorMaterial.CRYSTALITE, EquipmentSlot.FEET, makeItemSettings().rarity(Rarity.UNCOMMON)));
	public static final Item ARMORED_ELYTRA = registerItem("elytra_armored", new ArmoredElytra("elytra_armored", Items.PHANTOM_MEMBRANE, 700, 0.96D, true));

	// Tools //
	public static final TieredItem AETERNIUM_SHOVEL = registerTool("aeternium_shovel", new EndShovelItem(EndToolMaterial.AETERNIUM, 1.5F, -3.0F, makeItemSettings().fireResistant()));
	public static final TieredItem AETERNIUM_SWORD = registerTool("aeternium_sword", new EndSwordItem(EndToolMaterial.AETERNIUM, 3, -2.4F, makeItemSettings().fireResistant()));
	public static final TieredItem AETERNIUM_PICKAXE = registerTool("aeternium_pickaxe", new EndPickaxeItem(EndToolMaterial.AETERNIUM, 1, -2.8F, makeItemSettings().fireResistant()));
	public static final TieredItem AETERNIUM_AXE = registerTool("aeternium_axe", new EndAxeItem(EndToolMaterial.AETERNIUM, 5.0F, -3.0F, makeItemSettings().fireResistant()));
	public static final TieredItem AETERNIUM_HOE = registerTool("aeternium_hoe", new EndHoeItem(EndToolMaterial.AETERNIUM, -3, 0.0F, makeItemSettings().fireResistant()));
	public static final TieredItem AETERNIUM_HAMMER = registerTool("aeternium_hammer", new EndHammerItem(EndToolMaterial.AETERNIUM, 6.0F, -3.0F, 0.3D, makeItemSettings().fireResistant()));
	
	// Toolparts //
	public final static Item AETERNIUM_SHOVEL_HEAD = registerItem("aeternium_shovel_head");
	public final static Item AETERNIUM_PICKAXE_HEAD = registerItem("aeternium_pickaxe_head");
	public final static Item AETERNIUM_AXE_HEAD = registerItem("aeternium_axe_head");
	public final static Item AETERNIUM_HOE_HEAD = registerItem("aeternium_hoe_head");
	public final static Item AETERNIUM_HAMMER_HEAD = registerItem("aeternium_hammer_head");
	public final static Item AETERNIUM_SWORD_BLADE = registerItem("aeternium_sword_blade");
	public final static Item AETERNIUM_SWORD_HANDLE = registerItem("aeternium_sword_handle");

	// Hammers //
	public static final TieredItem IRON_HAMMER = registerTool("iron_hammer", new EndHammerItem(Tiers.IRON, 5.0F, -3.2F, 0.2D, makeItemSettings()));
	public static final TieredItem GOLDEN_HAMMER = registerTool("golden_hammer", new EndHammerItem(Tiers.GOLD, 4.5F, -3.4F, 0.3D, makeItemSettings()));
	public static final TieredItem DIAMOND_HAMMER = registerTool("diamond_hammer", new EndHammerItem(Tiers.DIAMOND, 5.5F, -3.1F, 0.2D, makeItemSettings()));
	public static final TieredItem NETHERITE_HAMMER = registerTool("netherite_hammer", new EndHammerItem(Tiers.NETHERITE, 5.0F, -3.0F, 0.2D, makeItemSettings().fireResistant()));
	
	// Food //
	public final static Item SHADOW_BERRY_RAW = registerFood("shadow_berry_raw", 4, 0.5F);
	public final static Item SHADOW_BERRY_COOKED = registerFood("shadow_berry_cooked", 6, 0.7F);
	public final static Item END_FISH_RAW = registerFood("end_fish_raw", Foods.SALMON);
	public final static Item END_FISH_COOKED = registerFood("end_fish_cooked", Foods.COOKED_SALMON);
	public final static Item BUCKET_END_FISH = registerItem("bucket_end_fish", new EndBucketItem());
	public final static Item BUCKET_CUBOZOA  = registerItem("bucket_cubozoa", new EndBucketItem());
	public final static Item SWEET_BERRY_JELLY = registerFood("sweet_berry_jelly", 6, 0.75F);
	public final static Item SHADOW_BERRY_JELLY = registerFood("shadow_berry_jelly", 7, 0.75F, new MobEffectInstance(MobEffects.NIGHT_VISION, 400));
	public final static Item BLOSSOM_BERRY = registerFood("blossom_berry", Foods.APPLE);
	public final static Item AMBER_ROOT_RAW = registerFood("amber_root_raw", 2, 0.8F);
	public final static Item CHORUS_MUSHROOM_RAW = registerFood("chorus_mushroom_raw", 3, 0.5F);
	public final static Item CHORUS_MUSHROOM_COOKED = registerFood("chorus_mushroom_cooked", Foods.MUSHROOM_STEW);
	public final static Item BOLUX_MUSHROOM_COOKED = registerFood("bolux_mushroom_cooked", Foods.MUSHROOM_STEW);
	public final static Item CAVE_PUMPKIN_PIE = registerFood("cave_pumpkin_pie", Foods.PUMPKIN_PIE);
	
	// Drinks //
	public final static Item UMBRELLA_CLUSTER_JUICE = registerDrink("umbrella_cluster_juice", 5, 0.7F);
	
	public static Item registerDisc(String name, int power, SoundEvent sound) {
		return registerItem(BetterEnd.makeID(name), new PatternedDiscItem(power, sound, makeItemSettings()));
	}
	
	public static Item registerItem(String name) {
		return registerItem(BetterEnd.makeID(name), new PatternedItem(makeItemSettings()));
	}
	
	public static Item registerItem(String name, Item item) {
		return registerItem(BetterEnd.makeID(name), item);
	}
	
	public static Item registerItem(ResourceLocation id, Item item) {
		if (item instanceof ArmorItem) {
			return registerArmor(id, item);
		}
		if (!Configs.ITEM_CONFIG.getBoolean("items", id.getPath(), true)) {
			return item;
		}
		registerItem(id, item, MOD_ITEMS);
		if (item instanceof BreakableItem) {
			((BreakableItem) item).registerBrokenItem();
		}
		return item;
	}
	
	public static Item registerBlockItem(ResourceLocation id, Item item) {
		registerItem(id, item, MOD_BLOCKS);
		return item;
	}
	
	private static void registerItem(ResourceLocation id, Item item, List<Item> registry) {
		if (item != Items.AIR) {
			Registry.register(Registry.ITEM, id, item);
			registry.add(item);
		}
	}
	
	private static Item registerArmor(ResourceLocation id, Item item) {
		if (!Configs.ITEM_CONFIG.getBoolean("armor", id.getPath(), true)) {
			return item;
		}
		registerItem(id, item, MOD_ITEMS);
		return item;
	}
	
	public static TieredItem registerTool(String name, TieredItem item) {
		ResourceLocation id = BetterEnd.makeID(name);
		if (!Configs.ITEM_CONFIG.getBoolean("tools", id.getPath(), true)) {
			return item;
		}
		registerItem(id, item, MOD_ITEMS);
		
		if (item instanceof ShovelItem) {
			TagHelper.addTag((Tag.Named<Item>) FabricToolTags.SHOVELS, item);
		} else if (item instanceof SwordItem) {
			TagHelper.addTag((Tag.Named<Item>) FabricToolTags.SWORDS, item);
		} else if (item instanceof EndPickaxeItem) {
			TagHelper.addTag((Tag.Named<Item>) FabricToolTags.PICKAXES, item);
		} else if (item instanceof EndAxeItem) {
			TagHelper.addTag((Tag.Named<Item>) FabricToolTags.AXES, item);
		} else if (item instanceof EndHoeItem) {
			TagHelper.addTag((Tag.Named<Item>) FabricToolTags.HOES, item);
		} else if (item instanceof EndHammerItem) {
			TagHelper.addTag(EndTags.HAMMERS, item);
		}
		
		return item;
	}
	
	public static Item registerEgg(String name, EntityType<?> type, int background, int dots) {
		SpawnEggItem item = new EndSpawnEggItem(type, background, dots, makeItemSettings());
		DefaultDispenseItemBehavior behavior = new DefaultDispenseItemBehavior() {
			public ItemStack execute(BlockSource pointer, ItemStack stack) {
				Direction direction = pointer.getBlockState().getValue(DispenserBlock.FACING);
				EntityType<?> entityType = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
				entityType.spawn(pointer.getLevel(), stack, null, pointer.getPos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
				stack.shrink(1);
				return stack;
			}
		};
		DispenserBlock.registerBehavior(item, behavior);
		return registerItem(name, item);
	}
	
	public static Item registerFood(String name, int hunger, float saturation, MobEffectInstance... effects) {
		FoodProperties.Builder builder = new FoodProperties.Builder().nutrition(hunger).saturationMod(saturation);
		for (MobEffectInstance effect: effects) {
			builder.effect(effect, 1F);
		}
		return registerFood(name, builder.build());
	}
	
	public static Item registerFood(String name, FoodProperties foodComponent) {
		return registerItem(name, new PatternedItem(makeItemSettings().food(foodComponent)));
	}
	
	public static Item registerDrink(String name) {
		return registerItem(name, new DrinkItem(makeItemSettings().stacksTo(1)));
	}
	
	public static Item registerDrink(String name, FoodProperties foodComponent) {
		return registerItem(name, new DrinkItem(makeItemSettings().stacksTo(1).food(foodComponent)));
	}
	
	public static Item registerDrink(String name, int hunger, float saturation) {
		FoodProperties.Builder builder = new FoodProperties.Builder().nutrition(hunger).saturationMod(saturation);
		return registerDrink(name, builder.build());
	}

	public static FabricItemSettings makeItemSettings() {
		FabricItemSettings properties = new FabricItemSettings();
		return (FabricItemSettings) properties.tab(CreativeTabs.TAB_ITEMS);
	}
	
	public static FabricItemSettings makeBlockItemSettings() {
		FabricItemSettings properties = new FabricItemSettings();
		return (FabricItemSettings) properties.tab(CreativeTabs.TAB_BLOCKS);
	}

	public static void register() {}
	
	public static List<Item> getModBlocks() {
		return MOD_BLOCKS;
	}

	public static List<Item> getModItems() {
		return MOD_ITEMS;
	}
}
