package ru.betterend.registry;

import java.util.List;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.tag.Tag;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.item.EndArmorMaterial;
import ru.betterend.item.EndAxe;
import ru.betterend.item.EndHammer;
import ru.betterend.item.EndHoe;
import ru.betterend.item.EndPickaxe;
import ru.betterend.item.EndToolMaterial;
import ru.betterend.tab.CreativeTab;
import ru.betterend.util.TagHelper;

public class ItemRegistry {
	private static final List<Item> MOD_BLOCKS = Lists.newArrayList();
	private static final List<Item> MOD_ITEMS = Lists.newArrayList();
	
	//Materials
	public final static Item ENDER_DUST = registerItem("ender_dust", new Item(new Item.Settings().group(CreativeTab.END_TAB)));
	public final static Item TERMINITE_INGOT = registerItem("terminite_ingot", new Item(new Item.Settings().group(CreativeTab.END_TAB)));
	public final static Item AETERNIUM_INGOT = registerItem("aeternium_ingot", new Item(new Item.Settings().group(CreativeTab.END_TAB)));
	
	//Armor
	public static final Item TERMINITE_HELMET = registerItem("terminite_helmet", new ArmorItem(EndArmorMaterial.TERMINITE, EquipmentSlot.HEAD,new Item.Settings().group(CreativeTab.END_TAB)));
	public static final Item TERMINITE_CHESTPLATE = registerItem("terminite_chestplate", new ArmorItem(EndArmorMaterial.TERMINITE, EquipmentSlot.CHEST, new Item.Settings().group(CreativeTab.END_TAB)));
	public static final Item TERMINITE_LEGGINGS = registerItem("terminite_leggings", new ArmorItem(EndArmorMaterial.TERMINITE, EquipmentSlot.LEGS, new Item.Settings().group(CreativeTab.END_TAB)));
	public static final Item TERMINITE_BOOTS = registerItem("terminite_boots", new ArmorItem(EndArmorMaterial.TERMINITE, EquipmentSlot.FEET, new Item.Settings().group(CreativeTab.END_TAB)));
	public static final Item AETERNIUM_HELMET = registerItem("aeternium_helmet", new ArmorItem(EndArmorMaterial.AETERNIUM, EquipmentSlot.HEAD,new Item.Settings().group(CreativeTab.END_TAB)));
	public static final Item AETERNIUM_CHESTPLATE = registerItem("aeternium_chestplate", new ArmorItem(EndArmorMaterial.AETERNIUM, EquipmentSlot.CHEST, new Item.Settings().group(CreativeTab.END_TAB)));
	public static final Item AETERNIUM_LEGGINGS = registerItem("aeternium_leggings", new ArmorItem(EndArmorMaterial.AETERNIUM, EquipmentSlot.LEGS, new Item.Settings().group(CreativeTab.END_TAB)));
	public static final Item AETERNIUM_BOOTS = registerItem("aeternium_boots", new ArmorItem(EndArmorMaterial.AETERNIUM, EquipmentSlot.FEET, new Item.Settings().group(CreativeTab.END_TAB)));
	
	//Tools
	public static ToolItem TERMINITE_SHOVEL = registerTool("terminite_shovel", new ShovelItem(EndToolMaterial.TERMINITE, 1.5F, -3.0F, new Item.Settings().group(CreativeTab.END_TAB)));
	public static ToolItem TERMINITE_SWORD = registerTool("terminite_sword", new SwordItem(EndToolMaterial.TERMINITE, 3, -2.4F, new Item.Settings().group(CreativeTab.END_TAB)));
	public static ToolItem TERMINITE_PICKAXE = registerTool("terminite_pickaxe", new EndPickaxe(EndToolMaterial.TERMINITE, 1, -2.8F, new Item.Settings().group(CreativeTab.END_TAB)));
	public static ToolItem TERMINITE_AXE = registerTool("terminite_axe", new EndAxe(EndToolMaterial.TERMINITE, 5.0F, -3.0F, new Item.Settings().group(CreativeTab.END_TAB)));
	public static ToolItem TERMINITE_HOE = registerTool("terminite_hoe", new EndHoe(EndToolMaterial.TERMINITE, -3, 0.0F, new Item.Settings().group(CreativeTab.END_TAB)));
	public static ToolItem AETERNIUM_SHOVEL = registerTool("aeternium_shovel", new ShovelItem(EndToolMaterial.AETERNIUM, 1.5F, -3.0F, new Item.Settings().group(CreativeTab.END_TAB)));
	public static ToolItem AETERNIUM_SWORD = registerTool("aeternium_sword", new SwordItem(EndToolMaterial.AETERNIUM, 3, -2.4F, new Item.Settings().group(CreativeTab.END_TAB)));
	public static ToolItem AETERNIUM_PICKAXE = registerTool("aeternium_pickaxe", new EndPickaxe(EndToolMaterial.AETERNIUM, 1, -2.8F, new Item.Settings().group(CreativeTab.END_TAB)));
	public static ToolItem AETERNIUM_AXE = registerTool("aeternium_axe", new EndAxe(EndToolMaterial.AETERNIUM, 5.0F, -3.0F, new Item.Settings().group(CreativeTab.END_TAB)));
	public static ToolItem AETERNIUM_HOE = registerTool("aeternium_hoe", new EndHoe(EndToolMaterial.AETERNIUM, -3, 0.0F, new Item.Settings().group(CreativeTab.END_TAB)));
	public static ToolItem IRON_HAMMER = registerTool("iron_hammer", new EndHammer(ToolMaterials.IRON, 4.0F, -3.0F, 0.2D, new Item.Settings().group(CreativeTab.END_TAB)));

	protected static Item registerItem(String name, Item item) {
		if (item != Items.AIR) {
			Registry.register(Registry.ITEM, BetterEnd.getIdentifier(name), item);
			if (item instanceof BlockItem)
				MOD_BLOCKS.add(item);
			else
				MOD_ITEMS.add(item);
		}
		return item;
	}
	
	protected static ToolItem registerTool(String name, ToolItem item) {
		Registry.register(Registry.ITEM, BetterEnd.getIdentifier(name), item);
		MOD_ITEMS.add(item);
		
		if (item instanceof ShovelItem) {
			TagHelper.addTag((Tag.Identified<Item>) FabricToolTags.SHOVELS, item);
		} else if (item instanceof SwordItem) {
			TagHelper.addTag((Tag.Identified<Item>) FabricToolTags.SWORDS, item);
		} else if (item instanceof EndPickaxe) {
			TagHelper.addTag((Tag.Identified<Item>) FabricToolTags.PICKAXES, item);
		} else if (item instanceof EndAxe) {
			TagHelper.addTag((Tag.Identified<Item>) FabricToolTags.AXES, item);
		} else if (item instanceof EndHoe) {
			TagHelper.addTag((Tag.Identified<Item>) FabricToolTags.HOES, item);
		}
		
		return item;
	}

	public static void register() {}
	
	public static List<Item> getModBlocks() {
		return MOD_BLOCKS;
	}

	public static List<Item> getModItems() {
		return MOD_ITEMS;
	}
}
