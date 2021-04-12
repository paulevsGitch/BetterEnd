package ru.betterend.util;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

public class TagHelper {
	private static final Map<ResourceLocation, Set<ResourceLocation>> TAGS_BLOCK = Maps.newHashMap();
	private static final Map<ResourceLocation, Set<ResourceLocation>> TAGS_ITEM = Maps.newHashMap();

	public static void addTag(Tag.Named<Block> tag, Block... blocks) {
		ResourceLocation tagID = tag.getName();
		Set<ResourceLocation> set = TAGS_BLOCK.computeIfAbsent(tagID, k -> Sets.newHashSet());
		for (Block block : blocks) {
			ResourceLocation id = Registry.BLOCK.getKey(block);
			if (id != Registry.BLOCK.getDefaultKey()) {
				set.add(id);
			}
		}
	}

	public static void addTag(Tag.Named<Item> tag, ItemLike... items) {
		ResourceLocation tagID = tag.getName();
		Set<ResourceLocation> set = TAGS_ITEM.computeIfAbsent(tagID, k -> Sets.newHashSet());
		for (ItemLike item : items) {
			ResourceLocation id = Registry.ITEM.getKey(item.asItem());
			if (id != Registry.ITEM.getDefaultKey()) {
				set.add(id);
			}
		}
	}

	@SafeVarargs
	public static void addTags(ItemLike item, Tag.Named<Item>... tags) {
		for (Tag.Named<Item> tag : tags) {
			addTag(tag, item);
		}
	}

	@SafeVarargs
	public static void addTags(Block block, Tag.Named<Block>... tags) {
		for (Tag.Named<Block> tag : tags) {
			addTag(tag, block);
		}
	}

	public static Tag.Builder apply(Tag.Builder builder, Set<ResourceLocation> ids) {
		ids.forEach(value -> {
			builder.addTag(value, "Better End Code");
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
					tagsMap.put(id, apply(Tag.Builder.tag(), ids));
				}
			});
		}
	}
}
