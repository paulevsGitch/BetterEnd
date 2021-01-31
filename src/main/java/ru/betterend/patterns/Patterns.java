package ru.betterend.patterns;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;

public class Patterns {
	
	//Blockstates
	public final static Identifier STATE_SIMPLE = BetterEnd.makeID("patterns/blockstate/block.json");
	public final static Identifier STATE_SLAB = BetterEnd.makeID("patterns/blockstate/slab.json");
	public final static Identifier STATE_STAIRS = BetterEnd.makeID("patterns/blockstate/stairs.json");
	public final static Identifier STATE_WALL = BetterEnd.makeID("patterns/blockstate/wall.json");
	public final static Identifier STATE_FENCE = BetterEnd.makeID("patterns/blockstate/fence.json");
	public final static Identifier STATE_BUTTON = BetterEnd.makeID("patterns/blockstate/button.json");
	public final static Identifier STATE_PILLAR = BetterEnd.makeID("patterns/blockstate/pillar.json");
	public final static Identifier STATE_PLATE = BetterEnd.makeID("patterns/blockstate/pressure_plate.json");
	public final static Identifier STATE_PLATE_ROTATED = BetterEnd.makeID("patterns/blockstate/pressure_plate_rotated.json");
	public final static Identifier STATE_DOOR = BetterEnd.makeID("patterns/blockstate/door.json");
	public final static Identifier STATE_SAPLING = BetterEnd.makeID("patterns/blockstate/sapling.json");
	public final static Identifier STATE_GATE = BetterEnd.makeID("patterns/blockstate/fence_gate.json");
	public final static Identifier STATE_TRAPDOOR = BetterEnd.makeID("patterns/blockstate/trapdoor.json");
	public final static Identifier STATE_LADDER = BetterEnd.makeID("patterns/blockstate/ladder.json");
	public final static Identifier STATE_BARREL = BetterEnd.makeID("patterns/blockstate/barrel.json");
	public final static Identifier STATE_PEDESTAL = BetterEnd.makeID("patterns/blockstate/pedestal.json");
	public final static Identifier STATE_STONE_LANTERN = BetterEnd.makeID("patterns/blockstate/stone_lantern.json");
	public final static Identifier STATE_DIRECT = BetterEnd.makeID("patterns/blockstate/direct.json");
	public final static Identifier STATE_BULB_LANTERN = BetterEnd.makeID("patterns/blockstate/bulb_lantern.json");
	public final static Identifier STATE_COMPOSTER = BetterEnd.makeID("patterns/blockstate/composter.json");
	public final static Identifier STATE_BARS = BetterEnd.makeID("patterns/blockstate/bars.json");
	public final static Identifier STATE_ANVIL = BetterEnd.makeID("patterns/blockstate/anvil.json");
	public final static Identifier STATE_CHAIN = BetterEnd.makeID("patterns/blockstate/chain.json");
	public final static Identifier STATE_CHANDELIER = BetterEnd.makeID("patterns/blockstate/chandelier.json");
	public final static Identifier STATE_FURNACE = BetterEnd.makeID("patterns/blockstate/furnace.json");
	
	//Models Block
	public final static Identifier BLOCK_EMPTY = BetterEnd.makeID("patterns/block/empty.json");
	public final static Identifier BLOCK_BASE = BetterEnd.makeID("patterns/block/block.json");
	public final static Identifier BLOCK_SIDED = BetterEnd.makeID("patterns/block/block_sided.json");
	public final static Identifier BLOCK_BOTTOM_TOP = BetterEnd.makeID("patterns/block/block_bottom_top.json");
	public final static Identifier BLOCK_SLAB = BetterEnd.makeID("patterns/block/slab.json");
	public final static Identifier BLOCK_STAIR = BetterEnd.makeID("patterns/block/stairs.json");
	public final static Identifier BLOCK_STAIR_INNER = BetterEnd.makeID("patterns/block/inner_stairs.json");
	public final static Identifier BLOCK_STAIR_OUTER = BetterEnd.makeID("patterns/block/outer_stairs.json");
	public final static Identifier BLOCK_WALL_POST = BetterEnd.makeID("patterns/block/wall_post.json");
	public final static Identifier BLOCK_WALL_SIDE = BetterEnd.makeID("patterns/block/wall_side.json");
	public final static Identifier BLOCK_WALL_SIDE_TALL = BetterEnd.makeID("patterns/block/wall_side_tall.json");
	public final static Identifier BLOCK_FENCE_POST = BetterEnd.makeID("patterns/block/fence_post.json");
	public final static Identifier BLOCK_FENCE_SIDE = BetterEnd.makeID("patterns/block/fence_side.json");
	public final static Identifier BLOCK_BUTTON = BetterEnd.makeID("patterns/block/button.json");
	public final static Identifier BLOCK_BUTTON_PRESSED = BetterEnd.makeID("patterns/block/button_pressed.json");
	public final static Identifier BLOCK_PILLAR = BetterEnd.makeID("patterns/block/pillar.json");
	public final static Identifier BLOCK_PLATE_UP = BetterEnd.makeID("patterns/block/pressure_plate_up.json");
	public final static Identifier BLOCK_PLATE_DOWN = BetterEnd.makeID("patterns/block/pressure_plate_down.json");
	public final static Identifier BLOCK_DOOR_TOP = BetterEnd.makeID("patterns/block/door_top.json");
	public final static Identifier BLOCK_DOOR_TOP_HINGE = BetterEnd.makeID("patterns/block/door_top_hinge.json");
	public final static Identifier BLOCK_DOOR_BOTTOM = BetterEnd.makeID("patterns/block/door_bottom.json");
	public final static Identifier BLOCK_DOOR_BOTTOM_HINGE = BetterEnd.makeID("patterns/block/door_bottom_hinge.json");
	public final static Identifier BLOCK_SAPLING = BetterEnd.makeID("patterns/block/sapling.json");
	public final static Identifier BLOCK_GATE_CLOSED = BetterEnd.makeID("patterns/block/fence_gate_closed.json");
	public final static Identifier BLOCK_GATE_CLOSED_WALL = BetterEnd.makeID("patterns/block/wall_gate_closed.json");
	public final static Identifier BLOCK_GATE_OPEN = BetterEnd.makeID("patterns/block/fence_gate_open.json");
	public final static Identifier BLOCK_GATE_OPEN_WALL = BetterEnd.makeID("patterns/block/wall_gate_open.json");
	public final static Identifier BLOCK_TRAPDOOR = BetterEnd.makeID("patterns/block/trapdoor.json");
	public final static Identifier BLOCK_LADDER = BetterEnd.makeID("patterns/block/ladder.json");
	public final static Identifier BLOCK_BARREL_OPEN = BetterEnd.makeID("patterns/block/barrel_open.json");
	public final static Identifier BLOCK_PEDESTAL_DEFAULT = BetterEnd.makeID("patterns/block/pedestal_default.json");
	public final static Identifier BLOKC_PEDESTAL_COLUMN = BetterEnd.makeID("patterns/block/pedestal_column.json");
	public final static Identifier BLOCK_PEDESTAL_COLUMN_TOP = BetterEnd.makeID("patterns/block/pedestal_column_top.json");
	public final static Identifier BLOCK_PEDESTAL_TOP = BetterEnd.makeID("patterns/block/pedestal_top.json");
	public final static Identifier BLOCK_PEDESTAL_BOTTOM = BetterEnd.makeID("patterns/block/pedestal_bottom.json");
	public final static Identifier BLOCK_PEDESTAL_PILLAR = BetterEnd.makeID("patterns/block/pedestal_pillar.json");
	public final static Identifier BLOCK_BOOKSHELF = BetterEnd.makeID("patterns/block/bookshelf.json");
	public final static Identifier BLOCK_STONE_LANTERN_CEIL = BetterEnd.makeID("patterns/block/stone_lantern_ceil.json");
	public final static Identifier BLOCK_STONE_LANTERN_FLOOR = BetterEnd.makeID("patterns/block/stone_lantern_floor.json");
	public final static Identifier BLOCK_BULB_LANTERN_FLOOR = BetterEnd.makeID("patterns/block/bulb_lantern_floor.json");
	public final static Identifier BLOCK_BULB_LANTERN_CEIL = BetterEnd.makeID("patterns/block/bulb_lantern_ceil.json");
	public final static Identifier BLOCK_PETAL_COLORED = BetterEnd.makeID("models/block/block_petal_colored.json");
	public final static Identifier BLOCK_COMPOSTER = BetterEnd.makeID("patterns/block/composter.json");
	public final static Identifier BLOCK_COLORED = BetterEnd.makeID("patterns/block/block_colored.json");
	public final static Identifier BLOCK_BARS_POST = BetterEnd.makeID("patterns/block/bars_post.json");
	public final static Identifier BLOCK_BARS_SIDE = BetterEnd.makeID("patterns/block/bars_side.json");
	public final static Identifier BLOCK_ANVIL = BetterEnd.makeID("patterns/block/anvil.json");
	public final static Identifier BLOCK_CHAIN = BetterEnd.makeID("patterns/block/chain.json");
	public final static Identifier BLOCK_CHANDELIER_FLOOR = BetterEnd.makeID("patterns/block/chandelier_floor.json");
	public final static Identifier BLOCK_CHANDELIER_WALL = BetterEnd.makeID("patterns/block/chandelier_wall.json");
	public final static Identifier BLOCK_CHANDELIER_CEIL = BetterEnd.makeID("patterns/block/chandelier_ceil.json");
	public final static Identifier BLOCK_FURNACE = BetterEnd.makeID("patterns/block/furnace.json");
	
	//Models Item
	public final static Identifier ITEM_WALL = BetterEnd.makeID("patterns/item/pattern_wall.json");
	public final static Identifier ITEM_FENCE = BetterEnd.makeID("patterns/item/pattern_fence.json");
	public final static Identifier ITEM_BUTTON = BetterEnd.makeID("patterns/item/pattern_button.json");
	public final static Identifier ITEM_CHEST = BetterEnd.makeID("patterns/item/pattern_chest.json");
	public final static Identifier ITEM_BLOCK = BetterEnd.makeID("patterns/item/pattern_block_item.json");
	public final static Identifier ITEM_GENERATED = BetterEnd.makeID("patterns/item/pattern_item_generated.json");
	public final static Identifier ITEM_HANDHELD = BetterEnd.makeID("patterns/item/pattern_item_handheld.json");
	public final static Identifier ITEM_SPAWN_EGG = BetterEnd.makeID("patterns/item/pattern_item_spawn_egg.json");
	
	public static String createJson(Reader data, String parent, String block) {
		try (BufferedReader buffer = new BufferedReader(data)) {
			return buffer.lines().collect(Collectors.joining())
					.replace("%parent%", parent)
					.replace("%block%", block);
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static String createJson(Identifier patternId, String parent, String block) {
		ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
		try (InputStream input = resourceManager.getResource(patternId).getInputStream()) {
			return createJson(new InputStreamReader(input, StandardCharsets.UTF_8), parent, block);
		} catch (Exception ex) {
			return null;
		}
	}

	public static String createJson(Identifier patternId, String texture) {
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%texture%", texture);
		return createJson(patternId, textures);
	}

	public static String createJson(Identifier patternId, Map<String, String> textures) {
		ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
		try (InputStream input = resourceManager.getResource(patternId).getInputStream()) {
			String json = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))
					.lines().collect(Collectors.joining());
			for (Entry<String, String> texture : textures.entrySet()) {
				json = json.replace(texture.getKey(), texture.getValue());
			}
			return json;
		} catch (Exception ex) {
			return null;
		}
	}
}
