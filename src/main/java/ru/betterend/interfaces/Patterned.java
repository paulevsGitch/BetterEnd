package ru.betterend.interfaces;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public interface Patterned {
	default String blockStatePattern(String name) {
		return null;
	}
	default String modelPattern(String name) {
		return null;
	}
	
	public static String createJson(Identifier patternId, String name) {
		ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
		try (InputStream input = resourceManager.getResource(patternId).getInputStream()) {
			return new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))
					.lines()
					.collect(Collectors.joining()).replace("%name%", name);
			
		} catch (Exception ex) {}
		
		return null;
	}
}
