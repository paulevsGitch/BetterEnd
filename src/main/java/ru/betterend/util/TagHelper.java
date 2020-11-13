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
	private static final Map<Identifier, Set<Identifier>> TAGS_BLOCK = Maps.newHashMap();
	private static final Map<Identifier, Set<Identifier>> TAGS_ITEM = Maps.newHashMap();
	
	public static void addTag(Tag.Identified<Block> tag, Block... blocks) {
		Identifier tagID = tag.getId();
		Set<Identifier> set = TAGS_BLOCK.get(tagID);
		if (set == null) {
			set = Sets.newHashSet();
			TAGS_BLOCK.put(tagID, set);
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
		Set<Identifier> set = TAGS_ITEM.get(tagID);
		if (set == null) {
			set = Sets.newHashSet();
			TAGS_ITEM.put(tagID, set);
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
	
	private static Tag.Builder apply(String entry, Set<Identifier> ids, Tag.Builder builder) {
		ids.forEach((value) -> {
			builder.add(value, "Better End Code");
		});
		return builder;
	}
	
	public static void apply(String entry, Map<Identifier, Tag.Builder> tagsMap) {
		Map<Identifier, Set<Identifier>> endTags = null;
		if (entry.equals("block")) {
			endTags = TAGS_BLOCK;
		} else if (entry.equals("item")) {
			endTags = TAGS_ITEM;
		}
		if (endTags != null) {
			endTags.forEach((id, ids) -> {
				if (tagsMap.containsKey(id)) {
					apply(entry, ids, tagsMap.get(id));
				} else {
					tagsMap.put(id, apply(entry, ids, Tag.Builder.create()));
				}
			});
		}
	}
}
