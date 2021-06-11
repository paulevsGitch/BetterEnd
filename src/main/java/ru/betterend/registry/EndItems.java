package ru.betterend.registry;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import ru.bclib.items.BaseArmorItem;
import ru.bclib.items.tool.BaseAxeItem;
import ru.bclib.items.tool.BaseHoeItem;
import ru.bclib.items.tool.BasePickaxeItem;
import ru.bclib.items.tool.BaseShovelItem;
import ru.bclib.items.tool.BaseSwordItem;
import ru.bclib.registry.ItemsRegistry;
import ru.betterend.BetterEnd;
import ru.betterend.config.Configs;
import ru.betterend.item.*;
import ru.betterend.item.material.EndArmorMaterial;
import ru.betterend.item.material.EndToolMaterial;
import ru.betterend.item.tool.EndHammerItem;
import ru.betterend.tab.CreativeTabs;

@SuppressWarnings("unused")
public class EndItems extends ItemsRegistry {
	// Materials //
	public final static Item ENDER_DUST = registerEndItem("ender_dust");
	public final static Item ENDER_SHARD = registerEndItem("ender_shard");
	public final static Item AETERNIUM_INGOT = registerEndItem("aeternium_ingot");
	public final static Item AETERNIUM_FORGED_PLATE = registerEndItem("aeternium_forged_plate");
	public final static Item END_LILY_LEAF = registerEndItem("end_lily_leaf");
	public final static Item END_LILY_LEAF_DRIED = registerEndItem("end_lily_leaf_dried");
	public final static Item CRYSTAL_SHARDS = registerEndItem("crystal_shards");
	public final static Item RAW_AMBER = registerEndItem("raw_amber");
	public final static Item AMBER_GEM = registerEndItem("amber_gem");
	public final static Item GLOWING_BULB = registerEndItem("glowing_bulb");
	public final static Item CRYSTALLINE_SULPHUR = registerEndItem("crystalline_sulphur");
	public final static Item HYDRALUX_PETAL = registerEndItem("hydralux_petal");
	public final static Item GELATINE = registerEndItem("gelatine");
	public static final Item ETERNAL_CRYSTAL = registerEndItem("eternal_crystal", new EternalCrystalItem());
	public final static Item ENCHANTED_PETAL = registerEndItem("enchanted_petal", new EnchantedItem(HYDRALUX_PETAL));
	public final static Item LEATHER_STRIPE = registerEndItem("leather_stripe");
	public final static Item LEATHER_WRAPPED_STICK = registerEndItem("leather_wrapped_stick");
	public final static Item SILK_FIBER = registerEndItem("silk_fiber");
	public final static Item LUMECORN_ROD = registerEndItem("lumecorn_rod");
	public final static Item SILK_MOTH_MATRIX = registerEndItem("silk_moth_matrix");
	public final static Item ENCHANTED_MEMBRANE = registerEndItem("enchanted_membrane", new EnchantedItem(Items.PHANTOM_MEMBRANE));

	// Music Discs
	public final static Item MUSIC_DISC_STRANGE_AND_ALIEN = registerEndDisc("music_disc_strange_and_alien", 0, EndSounds.RECORD_STRANGE_AND_ALIEN);
	public final static Item MUSIC_DISC_GRASPING_AT_STARS = registerEndDisc("music_disc_grasping_at_stars", 0, EndSounds.RECORD_GRASPING_AT_STARS);
	public final static Item MUSIC_DISC_ENDSEEKER = registerEndDisc("music_disc_endseeker", 0, EndSounds.RECORD_ENDSEEKER);
	public final static Item MUSIC_DISC_EO_DRACONA = registerEndDisc("music_disc_eo_dracona", 0, EndSounds.RECORD_EO_DRACONA);
	
	// Armor //
	public static final Item AETERNIUM_HELMET = registerEndItem("aeternium_helmet", new BaseArmorItem(EndArmorMaterial.AETERNIUM, EquipmentSlot.HEAD, makeEndItemSettings().fireResistant()));
	public static final Item AETERNIUM_CHESTPLATE = registerEndItem("aeternium_chestplate", new BaseArmorItem(EndArmorMaterial.AETERNIUM, EquipmentSlot.CHEST, makeEndItemSettings().fireResistant()));
	public static final Item AETERNIUM_LEGGINGS = registerEndItem("aeternium_leggings", new BaseArmorItem(EndArmorMaterial.AETERNIUM, EquipmentSlot.LEGS, makeEndItemSettings().fireResistant()));
	public static final Item AETERNIUM_BOOTS = registerEndItem("aeternium_boots", new BaseArmorItem(EndArmorMaterial.AETERNIUM, EquipmentSlot.FEET, makeEndItemSettings().fireResistant()));
	public static final Item CRYSTALITE_HELMET = registerEndItem("crystalite_helmet", new CrystaliteHelmet());
	public static final Item CRYSTALITE_CHESTPLATE = registerEndItem("crystalite_chestplate", new CrystaliteChestplate());
	public static final Item CRYSTALITE_LEGGINGS = registerEndItem("crystalite_leggings", new CrystaliteLeggings());
	public static final Item CRYSTALITE_BOOTS = registerEndItem("crystalite_boots", new CrystaliteBoots());
	public static final Item ARMORED_ELYTRA = registerEndItem("elytra_armored", new ArmoredElytra("elytra_armored", EndArmorMaterial.AETERNIUM, Items.PHANTOM_MEMBRANE, 900, 0.975D, true));
	public static final Item CRYSTALITE_ELYTRA = registerEndItem("elytra_crystalite", new CrystaliteElytra(650, 0.99D));

	// Tools //
	public static final TieredItem AETERNIUM_SHOVEL = registerEndTool("aeternium_shovel", new BaseShovelItem(EndToolMaterial.AETERNIUM, 1.5F, -3.0F, makeEndItemSettings().fireResistant()));
	public static final TieredItem AETERNIUM_SWORD = registerEndTool("aeternium_sword", new BaseSwordItem(EndToolMaterial.AETERNIUM, 3, -2.4F, makeEndItemSettings().fireResistant()));
	public static final TieredItem AETERNIUM_PICKAXE = registerEndTool("aeternium_pickaxe", new BasePickaxeItem(EndToolMaterial.AETERNIUM, 1, -2.8F, makeEndItemSettings().fireResistant()));
	public static final TieredItem AETERNIUM_AXE = registerEndTool("aeternium_axe", new BaseAxeItem(EndToolMaterial.AETERNIUM, 5.0F, -3.0F, makeEndItemSettings().fireResistant()));
	public static final TieredItem AETERNIUM_HOE = registerEndTool("aeternium_hoe", new BaseHoeItem(EndToolMaterial.AETERNIUM, -3, 0.0F, makeEndItemSettings().fireResistant()));
	public static final TieredItem AETERNIUM_HAMMER = registerEndTool("aeternium_hammer", new EndHammerItem(EndToolMaterial.AETERNIUM, 6.0F, -3.0F, 0.3D, makeEndItemSettings().fireResistant()));
	
	// Toolparts //
	public final static Item AETERNIUM_SHOVEL_HEAD = registerEndItem("aeternium_shovel_head");
	public final static Item AETERNIUM_PICKAXE_HEAD = registerEndItem("aeternium_pickaxe_head");
	public final static Item AETERNIUM_AXE_HEAD = registerEndItem("aeternium_axe_head");
	public final static Item AETERNIUM_HOE_HEAD = registerEndItem("aeternium_hoe_head");
	public final static Item AETERNIUM_HAMMER_HEAD = registerEndItem("aeternium_hammer_head");
	public final static Item AETERNIUM_SWORD_BLADE = registerEndItem("aeternium_sword_blade");
	public final static Item AETERNIUM_SWORD_HANDLE = registerEndItem("aeternium_sword_handle");

	// Hammers //
	public static final TieredItem IRON_HAMMER = registerEndTool("iron_hammer", new EndHammerItem(Tiers.IRON, 5.0F, -3.2F, 0.2D, makeEndItemSettings()));
	public static final TieredItem GOLDEN_HAMMER = registerEndTool("golden_hammer", new EndHammerItem(Tiers.GOLD, 4.5F, -3.4F, 0.3D, makeEndItemSettings()));
	public static final TieredItem DIAMOND_HAMMER = registerEndTool("diamond_hammer", new EndHammerItem(Tiers.DIAMOND, 5.5F, -3.1F, 0.2D, makeEndItemSettings()));
	public static final TieredItem NETHERITE_HAMMER = registerEndTool("netherite_hammer", new EndHammerItem(Tiers.NETHERITE, 5.0F, -3.0F, 0.2D, makeEndItemSettings().fireResistant()));
	
	// Food //
	public final static Item SHADOW_BERRY_RAW = registerEndFood("shadow_berry_raw", 4, 0.5F);
	public final static Item SHADOW_BERRY_COOKED = registerEndFood("shadow_berry_cooked", 6, 0.7F);
	public final static Item END_FISH_RAW = registerEndFood("end_fish_raw", Foods.SALMON);
	public final static Item END_FISH_COOKED = registerEndFood("end_fish_cooked", Foods.COOKED_SALMON);
	public final static Item BUCKET_END_FISH = registerEndItem("bucket_end_fish", new EndBucketItem(EndEntities.END_FISH));
	public final static Item BUCKET_CUBOZOA  = registerEndItem("bucket_cubozoa", new EndBucketItem(EndEntities.CUBOZOA));
	public final static Item SWEET_BERRY_JELLY = registerEndFood("sweet_berry_jelly", 8, 0.7F);
	public final static Item SHADOW_BERRY_JELLY = registerEndFood("shadow_berry_jelly", 6, 0.8F, new MobEffectInstance(MobEffects.NIGHT_VISION, 400));
	public final static Item BLOSSOM_BERRY_JELLY = registerEndFood("blossom_berry_jelly", 8, 0.7F);
	public final static Item BLOSSOM_BERRY = registerEndFood("blossom_berry", Foods.APPLE);
	public final static Item AMBER_ROOT_RAW = registerEndFood("amber_root_raw", 2, 0.8F);
	public final static Item CHORUS_MUSHROOM_RAW = registerEndFood("chorus_mushroom_raw", 3, 0.5F);
	public final static Item CHORUS_MUSHROOM_COOKED = registerEndFood("chorus_mushroom_cooked", Foods.MUSHROOM_STEW);
	public final static Item BOLUX_MUSHROOM_COOKED = registerEndFood("bolux_mushroom_cooked", Foods.MUSHROOM_STEW);
	public final static Item CAVE_PUMPKIN_PIE = registerEndFood("cave_pumpkin_pie", Foods.PUMPKIN_PIE);

	// Drinks //
	public final static Item UMBRELLA_CLUSTER_JUICE = registerEndDrink("umbrella_cluster_juice", 5, 0.7F);

	private static ItemsRegistry ITEM_REGISTRY;

	protected EndItems(CreativeModeTab creativeTab) {
		super(creativeTab);
	}

	public static List<Item> getModItems() {
		return getModItems(BetterEnd.MOD_ID);
	}

	public static Item registerEndDisc(String name, int power, SoundEvent sound) {
		return getItemRegistry().registerDisc(name, power, sound);
	}
	
	public static Item registerEndItem(String name) {
		return getItemRegistry().registerItem(name);
	}
	
	public static Item registerEndItem(String name, Item item) {
		return getItemRegistry().register(BetterEnd.makeID(name), item);
	}
	
	public static Item registerEndItem(ResourceLocation id, Item item) {
		if (item instanceof ArmorItem) {
			return registerEndArmor(id, item);
		}
		if (!Configs.ITEM_CONFIG.getBoolean("items", id.getPath(), true)) {
			return item;
		}
		getItemRegistry().register(id, item);
		return item;
	}
	
	private static Item registerEndArmor(ResourceLocation itemId, Item item) {
		if (!Configs.ITEM_CONFIG.getBoolean("armor", itemId.getPath(), true)) {
			return item;
		}
		getItemRegistry().register(itemId, item);
		return item;
	}
	
	public static TieredItem registerEndTool(String name, TieredItem item) {
		if (!Configs.ITEM_CONFIG.getBoolean("tools", name, true)) {
			return item;
		}
		return getItemRegistry().registerTool(name, item);
	}
	
	public static Item registerEndEgg(String name, EntityType<?> type, int background, int dots) {
		return getItemRegistry().registerEgg(name, type, background, dots);
	}
	
	public static Item registerEndFood(String name, int hunger, float saturation, MobEffectInstance... effects) {
		return getItemRegistry().registerFood(name, hunger, saturation, effects);
	}
	
	public static Item registerEndFood(String name, FoodProperties foodComponent) {
		return getItemRegistry().registerFood(name, foodComponent);
	}
	
	public static Item registerEndDrink(String name) {
		return getItemRegistry().registerDrink(name);
	}
	
	public static Item registerEndDrink(String name, FoodProperties foodComponent) {
		return getItemRegistry().registerDrink(name, foodComponent);
	}
	
	public static Item registerEndDrink(String name, int hunger, float saturation) {
		return getItemRegistry().registerDrink(name, hunger, saturation);
	}

	public static FabricItemSettings makeEndItemSettings() {
		return ITEM_REGISTRY.makeItemSettings();
	}

	@Override
	public ResourceLocation createModId(String name) {
		return BetterEnd.makeID(name);
	}

	@NotNull
	private static ItemsRegistry getItemRegistry() {
		if (ITEM_REGISTRY == null) {
			ITEM_REGISTRY = new EndItems(CreativeTabs.TAB_ITEMS);
		}
		return ITEM_REGISTRY;
	}
}
