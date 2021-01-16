package ru.betterend.registry;

import java.util.List;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.FishBucketItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.config.Configs;
import ru.betterend.item.DrinkItem;
import ru.betterend.item.EnchantedPetalItem;
import ru.betterend.item.EndAxeItem;
import ru.betterend.item.EndHammerItem;
import ru.betterend.item.EndHoeItem;
import ru.betterend.item.EndPickaxeItem;
import ru.betterend.item.EndSpawnEggItem;
import ru.betterend.item.EternalCrystalItem;
import ru.betterend.item.PatternedItem;
import ru.betterend.item.material.EndArmorMaterial;
import ru.betterend.item.material.EndToolMaterial;
import ru.betterend.tab.CreativeTabs;
import ru.betterend.util.TagHelper;

public class EndItems {
	private static final List<Item> MOD_BLOCKS = Lists.newArrayList();
	private static final List<Item> MOD_ITEMS = Lists.newArrayList();
	
	// Materials //
	public final static Item ENDER_DUST = registerItem("ender_dust");
	public final static Item ENDER_SHARD = registerItem("ender_shard");
	public final static Item TERMINITE_INGOT = registerItem("terminite_ingot");
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
	
	// Armor //
	public static final Item TERMINITE_HELMET = registerItem("terminite_helmet", new ArmorItem(EndArmorMaterial.TERMINITE, EquipmentSlot.HEAD, makeItemSettings()));
	public static final Item TERMINITE_CHESTPLATE = registerItem("terminite_chestplate", new ArmorItem(EndArmorMaterial.TERMINITE, EquipmentSlot.CHEST, makeItemSettings()));
	public static final Item TERMINITE_LEGGINGS = registerItem("terminite_leggings", new ArmorItem(EndArmorMaterial.TERMINITE, EquipmentSlot.LEGS, makeItemSettings()));
	public static final Item TERMINITE_BOOTS = registerItem("terminite_boots", new ArmorItem(EndArmorMaterial.TERMINITE, EquipmentSlot.FEET, makeItemSettings()));
	public static final Item AETERNIUM_HELMET = registerItem("aeternium_helmet", new ArmorItem(EndArmorMaterial.AETERNIUM, EquipmentSlot.HEAD, makeItemSettings()));
	public static final Item AETERNIUM_CHESTPLATE = registerItem("aeternium_chestplate", new ArmorItem(EndArmorMaterial.AETERNIUM, EquipmentSlot.CHEST, makeItemSettings()));
	public static final Item AETERNIUM_LEGGINGS = registerItem("aeternium_leggings", new ArmorItem(EndArmorMaterial.AETERNIUM, EquipmentSlot.LEGS, makeItemSettings()));
	public static final Item AETERNIUM_BOOTS = registerItem("aeternium_boots", new ArmorItem(EndArmorMaterial.AETERNIUM, EquipmentSlot.FEET, makeItemSettings()));
	public static final Item CRYSTALITE_HELMET = registerItem("crystalite_helmet", new ArmorItem(EndArmorMaterial.CRYSTALITE, EquipmentSlot.HEAD, makeItemSettings().rarity(Rarity.UNCOMMON)));
	public static final Item CRYSTALITE_CHESTPLATE = registerItem("crystalite_chestplate", new ArmorItem(EndArmorMaterial.CRYSTALITE, EquipmentSlot.CHEST, makeItemSettings().rarity(Rarity.UNCOMMON)));
	public static final Item CRYSTALITE_LEGGINGS = registerItem("crystalite_leggings", new ArmorItem(EndArmorMaterial.CRYSTALITE, EquipmentSlot.LEGS, makeItemSettings().rarity(Rarity.UNCOMMON)));
	public static final Item CRYSTALITE_BOOTS = registerItem("crystalite_boots", new ArmorItem(EndArmorMaterial.CRYSTALITE, EquipmentSlot.FEET, makeItemSettings().rarity(Rarity.UNCOMMON)));
	
	// Tools //
	public static final ToolItem TERMINITE_SHOVEL = registerTool("terminite_shovel", new ShovelItem(EndToolMaterial.TERMINITE, 1.5F, -3.0F, makeItemSettings()));
	public static final ToolItem TERMINITE_SWORD = registerTool("terminite_sword", new SwordItem(EndToolMaterial.TERMINITE, 3, -2.4F, makeItemSettings()));
	public static final ToolItem TERMINITE_PICKAXE = registerTool("terminite_pickaxe", new EndPickaxeItem(EndToolMaterial.TERMINITE, 1, -2.8F, makeItemSettings()));
	public static final ToolItem TERMINITE_AXE = registerTool("terminite_axe", new EndAxeItem(EndToolMaterial.TERMINITE, 6.0F, -3.0F, makeItemSettings()));
	public static final ToolItem TERMINITE_HOE = registerTool("terminite_hoe", new EndHoeItem(EndToolMaterial.TERMINITE, -3, 0.0F, makeItemSettings()));
	public static final ToolItem TERMINITE_HAMMER = registerTool("terminite_hammer", new EndHammerItem(EndToolMaterial.TERMINITE, 5.0F, -3.2F, 0.3D, makeItemSettings()));
	public static final ToolItem AETERNIUM_SHOVEL = registerTool("aeternium_shovel", new ShovelItem(EndToolMaterial.AETERNIUM, 1.5F, -3.0F, makeItemSettings()));
	public static final ToolItem AETERNIUM_SWORD = registerTool("aeternium_sword", new SwordItem(EndToolMaterial.AETERNIUM, 3, -2.4F, makeItemSettings()));
	public static final ToolItem AETERNIUM_PICKAXE = registerTool("aeternium_pickaxe", new EndPickaxeItem(EndToolMaterial.AETERNIUM, 1, -2.8F, makeItemSettings()));
	public static final ToolItem AETERNIUM_AXE = registerTool("aeternium_axe", new EndAxeItem(EndToolMaterial.AETERNIUM, 5.0F, -3.0F, makeItemSettings()));
	public static final ToolItem AETERNIUM_HOE = registerTool("aeternium_hoe", new EndHoeItem(EndToolMaterial.AETERNIUM, -3, 0.0F, makeItemSettings()));
	public static final ToolItem AETERNIUM_HAMMER = registerTool("aeternium_hammer", new EndHammerItem(EndToolMaterial.AETERNIUM, 6.0F, -3.0F, 0.3D, makeItemSettings()));
	public static final ToolItem IRON_HAMMER = registerTool("iron_hammer", new EndHammerItem(ToolMaterials.IRON, 5.0F, -3.2F, 0.2D, makeItemSettings()));
	public static final ToolItem GOLDEN_HAMMER = registerTool("golden_hammer", new EndHammerItem(ToolMaterials.GOLD, 4.5F, -3.4F, 0.3D, makeItemSettings()));
	public static final ToolItem DIAMOND_HAMMER = registerTool("diamond_hammer", new EndHammerItem(ToolMaterials.DIAMOND, 5.5F, -3.1F, 0.2D, makeItemSettings()));
	public static final ToolItem NETHERITE_HAMMER = registerTool("netherite_hammer", new EndHammerItem(ToolMaterials.NETHERITE, 5.0F, -3.0F, 0.2D, makeItemSettings()));
	
	// Food //
	public final static Item SHADOW_BERRY_RAW = registerFood("shadow_berry_raw", 4, 0.5F);
	public final static Item SHADOW_BERRY_COOKED = registerFood("shadow_berry_cooked", 6, 0.7F);
	public final static Item END_FISH_RAW = registerFood("end_fish_raw", FoodComponents.SALMON);
	public final static Item END_FISH_COOKED = registerFood("end_fish_cooked", FoodComponents.COOKED_SALMON);
	public final static Item BUCKET_END_FISH = registerItem("bucket_end_fish", new FishBucketItem(EndEntities.END_FISH, Fluids.WATER, makeItemSettings().maxCount(1)));
	public final static Item SWEET_BERRY_JELLY = registerFood("sweet_berry_jelly", 3, 0.75F);
	public final static Item SHADOW_BERRY_JELLY = registerFood("shadow_berry_jelly", 4, 0.75F, new StatusEffectInstance(StatusEffects.NIGHT_VISION, 400));
	public final static Item BLOSSOM_BERRY = registerFood("blossom_berry", FoodComponents.APPLE);
	
	// Drinks
	public final static Item UMBRELLA_CLUSTER_JUICE = registerDrink("umbrella_cluster_juice");
	
	// Toolparts //
	public final static Item AETERNIUM_SHOVEL_HEAD = registerItem("aeternium_shovel_head");
	public final static Item AETERNIUM_PICKAXE_HEAD = registerItem("aeternium_pickaxe_head");
	public final static Item AETERNIUM_AXE_HEAD = registerItem("aeternium_axe_head");
	public final static Item AETERNIUM_HOE_HEAD = registerItem("aeternium_hoe_head");
	public final static Item AETERNIUM_HAMMER_HEAD = registerItem("aeternium_hammer_head");
	public final static Item AETERNIUM_SWORD_BLADE = registerItem("aeternium_sword_blade");
	public final static Item AETERNIUM_SWORD_HANDLE = registerItem("aeternium_sword_handle");
	
	protected static Item registerItem(String name) {
		return registerItem(BetterEnd.makeID(name), new PatternedItem(makeItemSettings()));
	}
	
	protected static Item registerItem(String name, Item item) {
		return registerItem(BetterEnd.makeID(name), item);
	}
	
	public static Item registerItem(Identifier id, Item item) {
		if (item instanceof ArmorItem) {
			return registerArmor(id, item);
		}
		if (!Configs.ITEM_CONFIG.getBoolean("items", id.getPath(), true)) {
			return item;
		}
		registerItem(id, item, MOD_ITEMS);
		return item;
	}
	
	public static Item registerBlockItem(Identifier id, Item item) {
		registerItem(id, item, MOD_BLOCKS);
		return item;
	}
	
	private static void registerItem(Identifier id, Item item, List<Item> registry) {
		if (item != Items.AIR) {
			Registry.register(Registry.ITEM, id, item);
			registry.add(item);
		}
	}
	
	private static Item registerArmor(Identifier id, Item item) {
		if (!Configs.ITEM_CONFIG.getBoolean("armor", id.getPath(), true)) {
			return item;
		}
		registerItem(id, item, MOD_ITEMS);
		return item;
	}
	
	private static ToolItem registerTool(String name, ToolItem item) {
		Identifier id = BetterEnd.makeID(name);
		if (!Configs.ITEM_CONFIG.getBoolean("tools", id.getPath(), true)) {
			return item;
		}
		registerItem(id, item, MOD_ITEMS);
		
		if (item instanceof ShovelItem) {
			TagHelper.addTag((Tag.Identified<Item>) FabricToolTags.SHOVELS, item);
		} else if (item instanceof SwordItem) {
			TagHelper.addTag((Tag.Identified<Item>) FabricToolTags.SWORDS, item);
		} else if (item instanceof EndPickaxeItem) {
			TagHelper.addTag((Tag.Identified<Item>) FabricToolTags.PICKAXES, item);
		} else if (item instanceof EndAxeItem) {
			TagHelper.addTag((Tag.Identified<Item>) FabricToolTags.AXES, item);
		} else if (item instanceof EndHoeItem) {
			TagHelper.addTag((Tag.Identified<Item>) FabricToolTags.HOES, item);
		} else if (item instanceof EndHammerItem) {
			TagHelper.addTag((Tag.Identified<Item>) EndTags.HAMMERS, item);
		}
		
		return item;
	}
	
	public static Item registerEgg(String name, EntityType<?> type, int background, int dots) {
		SpawnEggItem item = new EndSpawnEggItem(type, background, dots, makeItemSettings());
		ItemDispenserBehavior behavior = new ItemDispenserBehavior() {
			public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
				Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
				EntityType<?> entityType = ((SpawnEggItem) stack.getItem()).getEntityType(stack.getTag());
				entityType.spawnFromItemStack(pointer.getWorld(), stack, null, pointer.getBlockPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
				stack.decrement(1);
				return stack;
			}
		};
		DispenserBlock.registerBehavior(item, behavior);
		return registerItem(name, item);
	}
	
	public static Item registerFood(String name, int hunger, float saturation, StatusEffectInstance... effects) {
		FoodComponent.Builder builder = new FoodComponent.Builder().hunger(hunger).saturationModifier(saturation);
		for (StatusEffectInstance effect: effects) {
			builder.statusEffect(effect, 1F);
		}
		return registerFood(name, builder.build());
	}
	
	public static Item registerFood(String name, FoodComponent foodComponent) {
		return registerItem(name, new PatternedItem(makeItemSettings().food(foodComponent)));
	}
	
	public static Item registerDrink(String name) {
		return registerItem(name, new DrinkItem(makeItemSettings().maxCount(1)));
	}

	public static Settings makeItemSettings() {
		return new Item.Settings().group(CreativeTabs.TAB_ITEMS);
	}
	
	public static Settings makeBlockItemSettings() {
		return new Item.Settings().group(CreativeTabs.TAB_BLOCKS);
	}

	public static void register() {}
	
	public static List<Item> getModBlocks() {
		return MOD_BLOCKS;
	}

	public static List<Item> getModItems() {
		return MOD_ITEMS;
	}
}
