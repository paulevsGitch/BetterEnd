package ru.betterend.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndItems;

public class TranslationHelper {
	public static void printMissingNames() {
		List<String> missingNamesEn = Lists.newArrayList();
		List<String> missingNamesRu = Lists.newArrayList();
		
		Gson gson = new Gson();
		InputStream streamEn = BetterEnd.class.getResourceAsStream("/assets/betterend/lang/en_us.json");
		InputStream streamRu = BetterEnd.class.getResourceAsStream("/assets/betterend/lang/ru_ru.json");
		JsonObject translationEn = gson.fromJson(new InputStreamReader(streamEn), JsonObject.class);
		JsonObject translationRu = gson.fromJson(new InputStreamReader(streamRu), JsonObject.class);
		
		EndItems.getModBlocks().forEach((block) -> {
			String name = block.getName().getString();
			if (!translationEn.has(name)) {
				missingNamesEn.add(name);
			}
			if (!translationRu.has(name)) {
				missingNamesRu.add(name);
			}
		});
		
		EndItems.getModItems().forEach((item) -> {
			String name = item.getName().getString();
			if (!translationEn.has(name)) {
				missingNamesEn.add(name);
			}
			if (!translationRu.has(name)) {
				missingNamesRu.add(name);
			}
		});
		
		EndBiomes.getModBiomes().forEach((endBiome) -> {
			if (endBiome.getID().getNamespace().equals(BetterEnd.MOD_ID)) {
				String name = "biome." + BetterEnd.MOD_ID + "." + endBiome.getID().getPath();
				if (!translationEn.has(name)) {
					missingNamesEn.add(name);
				}
				if (!translationRu.has(name)) {
					missingNamesRu.add(name);
				}
			}
		});
		
		Registry.ENTITY_TYPE.forEach((entity) -> {
			Identifier id = Registry.ENTITY_TYPE.getId(entity);
			if (id.getNamespace().equals(BetterEnd.MOD_ID)) {
				String name = "entity." + BetterEnd.MOD_ID + "." + id.getPath();
				if (!translationEn.has(name)) {
					missingNamesEn.add(name);
				}
				if (!translationRu.has(name)) {
					missingNamesRu.add(name);
				}
			}
		});
		
		if (!missingNamesEn.isEmpty() || !missingNamesRu.isEmpty()) {
			
			System.out.println("========================================");
			System.out.println("           MISSING NAMES LIST");
			
			if (!missingNamesEn.isEmpty()) {
				Collections.sort(missingNamesEn);
				System.out.println("========================================");
				System.out.println("                 ENGLISH");
				System.out.println("========================================");
				missingNamesEn.forEach((name) -> {
					System.out.println("	\"" + name + "\": \"" + fastTranslateEn(name) + "\",");
				});
			}
			
			if (!missingNamesRu.isEmpty()) {
				Collections.sort(missingNamesRu);
				System.out.println("========================================");
				System.out.println("                 RUSSIAN");
				System.out.println("========================================");
				missingNamesRu.forEach((name) -> {
					System.out.println("	\"" + name + "\": \"\",");
				});
			}
			
			System.out.println("========================================");
		}
	}
	
	public static String fastTranslateEn(String text) {
		String[] words = text.substring(text.lastIndexOf('.') + 1).split("_");
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			builder.append(Character.toUpperCase(word.charAt(0)));
			builder.append(word, 1, word.length());
			if (i < words.length - 1) {
				builder.append(' ');
			}
		}
		return builder.toString();
	}
}
