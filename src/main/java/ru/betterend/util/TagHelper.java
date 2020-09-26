package ru.betterend.util;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;

public class TagHelper {
	private static final Map<Identifier, Set<Identifier>> TAGS = Maps.newHashMap();
	
	public static final Identifier CLIMBABLE = new Identifier("climbable");
	
	public static void addTag(Identifier tagID, String... values) {
		Set<Identifier> set = TAGS.get(tagID);
		if (set == null) {
			set = Sets.newHashSet();
			TAGS.put(tagID, set);
		}
		for (String value: values) {
			set.add(new Identifier(BetterEnd.MOD_ID, value));
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
