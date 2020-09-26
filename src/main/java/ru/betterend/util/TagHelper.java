package ru.betterend.util;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TagHelper {
	private static final Map<Identifier, Set<Identifier>> TAGS = Maps.newHashMap();
	
	public static void addTag(Tag.Identified<Block> tag, Block... blocks) {
		Identifier tagID = tag.getId();
		Set<Identifier> set = TAGS.get(tagID);
		if (set == null) {
			set = Sets.newHashSet();
			TAGS.put(tagID, set);
		}
		for (Block block: blocks) {
			Identifier id = Registry.BLOCK.getId(block);
			if (id != Registry.BLOCK.getDefaultId()) {
				set.add(id);
			}
		}
	}
	
	public static void addTag(Tag.Identified<Item> tag, ItemConvertible... items) {
		Identifier tagID = tag.getId();
		Set<Identifier> set = TAGS.get(tagID);
		if (set == null) {
			set = Sets.newHashSet();
			TAGS.put(tagID, set);
		}
		for (ItemConvertible item: items) {
			Identifier id = Registry.ITEM.getId(item.asItem());
			if (id != Registry.ITEM.getDefaultId()) {
				set.add(id);
			}
		}
	}
	
	@SafeVarargs
	public static void addTags(ItemConvertible item, Tag.Identified<Item>... tags) {
		for (Tag.Identified<Item> tag: tags) {
			addTag(tag, item);
		}
	}
	
	@SafeVarargs
	public static void addTags(Block block, Tag.Identified<Block>... tags) {
		for (Tag.Identified<Block> tag: tags) {
			addTag(tag, block);
		}
	}
	
	public static void apply(Identifier id, Tag.Builder builder) {
		Set<Identifier> values = TAGS.get(id);
		if (values != null) {
			values.forEach((value) -> {
				builder.add(value, "Better End Code");
			});
		}
	}
}
