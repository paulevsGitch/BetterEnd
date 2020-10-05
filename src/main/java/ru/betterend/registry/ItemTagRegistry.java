package ru.betterend.registry;

import java.util.Arrays;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.impl.tool.attribute.ToolManagerImpl;
import net.fabricmc.fabric.impl.tool.attribute.handlers.ModdedToolsVanillaBlocksToolHandler;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;

public class ItemTagRegistry {
	public final static Tag<Item> HAMMERS = registerFabricItemTag("hammer");
	
	public static Tag<Item> registerItemTag(String name) {
		return TagRegistry.item(BetterEnd.makeID(name));
	}
	
	public static Tag<Item> registerFabricItemTag(String name) {
		return TagRegistry.item(new Identifier("fabric", name));
	}
	
	public static void register() {
		ToolManagerImpl.tag(HAMMERS).register(new ModdedToolsVanillaBlocksToolHandler(
			Arrays.asList(
				ItemRegistry.IRON_HAMMER,
				ItemRegistry.GOLDEN_HAMMER,
				ItemRegistry.DIAMOND_HAMMER,
				ItemRegistry.NETHERITE_HAMMER,
				ItemRegistry.TERMINITE_HAMMER,
				ItemRegistry.AETERNIUM_HAMMER
			)
		));
	}
}
