package ru.betterend.util;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemConvertible;
import net.minecraft.tags.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

public class TagHelper {
	private static final Map<ResourceLocation, Set<ResourceLocation>> TAGS_BLOCK = Maps.newHashMap();
	private static final Map<ResourceLocation, Set<ResourceLocation>> TAGS_ITEM = Maps.newHashMap();

	public static void addTag(Tag.Identified<Block> tag, Block... blocks) {
		ResourceLocation tagID = tag.getId();
		Set<ResourceLocation> set = TAGS_BLOCK.get(tagID);
		if (set == null) {
			set = Sets.newHashSet();
			TAGS_BLOCK.put(tagID, set);
		}
		for (Block block : blocks) {
			ResourceLocation id = Registry.BLOCK.getKey(block);
			if (id != Registry.BLOCK.getDefaultId()) {
				set.add(id);
			}
		}
	}

	public static void addTag(Tag.Identified<Item> tag, ItemConvertible... items) {
		ResourceLocation tagID = tag.getId();
		Set<ResourceLocation> set = TAGS_ITEM.get(tagID);
		if (set == null) {
			set = Sets.newHashSet();
			TAGS_ITEM.put(tagID, set);
		}
		for (ItemConvertible item : items) {
			ResourceLocation id = Registry.ITEM.getId(item.asItem());
			if (id != Registry.ITEM.getDefaultId()) {
				set.add(id);
			}
		}
	}

	@SafeVarargs
	public static void addTags(ItemConvertible item, Tag.Identified<Item>... tags) {
		for (Tag.Identified<Item> tag : tags) {
			addTag(tag, item);
		}
	}

	@SafeVarargs
	public static void addTags(Block block, Tag.Identified<Block>... tags) {
		for (Tag.Identified<Block> tag : tags) {
			addTag(tag, block);
		}
	}

	public static Tag.Builder apply(Tag.Builder builder, Set<ResourceLocation> ids) {
		ids.forEach((value) -> {
			builder.add(value, "Better End Code");
		});
		return builder;
	}

	public static void apply(String entry, Map<ResourceLocation, Tag.Builder> tagsMap) {
		Map<ResourceLocation, Set<ResourceLocation>> endTags = null;
		if (entry.equals("block")) {
			endTags = TAGS_BLOCK;
		} else if (entry.equals("item")) {
			endTags = TAGS_ITEM;
		}
		if (endTags != null) {
			endTags.forEach((id, ids) -> {
				if (tagsMap.containsKey(id)) {
					apply(tagsMap.get(id), ids);
				} else {
					tagsMap.put(id, apply(Tag.Builder.create(), ids));
				}
			});
		}
	}
}
