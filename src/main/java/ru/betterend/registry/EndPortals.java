package ru.betterend.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ibm.icu.impl.UResource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;
import ru.betterend.config.ConfigWriter;
import ru.betterend.util.JsonFactory;
import ru.betterend.util.MHelper;

import java.io.File;

public class EndPortals {
	private static PortalInfo[] portals;
	
	public static void loadPortals() {
		File file = new File(ConfigWriter.MOD_CONFIG_DIR, "portals.json");
		JsonObject json;
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			json = makeDefault(file);
		} else {
			json = JsonFactory.getJsonObject(file);
		}
		if (!json.has("portals") || !json.get("portals").isJsonArray()) {
			json = makeDefault(file);
		}
		JsonArray array = json.get("portals").getAsJsonArray();
		if (array.size() == 0) {
			json = makeDefault(file);
			array = json.get("portals").getAsJsonArray();
		}
		portals = new PortalInfo[array.size()];
		for (int i = 0; i < portals.length; i++) {
			portals[i] = new PortalInfo(array.get(i).getAsJsonObject());
		}
	}
	
	public static int getCount() {
		return MHelper.max(portals.length - 1, 1);
	}
	
	public static ServerWorld getWorld(MinecraftServer server, int state) {
		if (state >= portals.length) {
			return server.getOverworld();
		}
		return portals[state].getWorld(server);
	}
	
	public static int getPortalState(Identifier item) {
		for (int i = 0; i < portals.length; i++) {
			if (portals[i].item.equals(item)) {
				return i;
			}
		}
		return 0;
	}
	
	public static int getColor(int state) {
		return portals[state].color;
	}
	
	public static boolean isAvailableItem(Identifier item) {
		for (PortalInfo portal : portals) {
			if (portal.item.equals(item)) {
				return true;
			}
		}
		return false;
	}
	
	private static JsonObject makeDefault(File file) {
		JsonObject jsonObject = new JsonObject();
		JsonFactory.storeJson(file, jsonObject);
		JsonArray array = new JsonArray();
		jsonObject.add("portals", array);
		array.add(makeDefault().toJson());
		JsonFactory.storeJson(file, jsonObject);
		return jsonObject;
	}
	
	private static PortalInfo makeDefault() {
		return new PortalInfo(new Identifier("minecraft:overworld"), BetterEnd.makeID("eternal_crystal"), 255, 255, 255);
	}
	
	private static class PortalInfo {
		private final Identifier dimension;
		private final Identifier item;
		private final int color;
		private ServerWorld world;
		
		PortalInfo(JsonObject obj) {
			this(
				new Identifier(JsonFactory.getString(obj, "dimension", "minecraft:overworld")),
				new Identifier(JsonFactory.getString(obj, "item", "betterend:eternal_crystal")),
				JsonFactory.getInt(obj, "colorRed", 255),
				JsonFactory.getInt(obj, "colorGreen", 255),
				JsonFactory.getInt(obj, "colorBlue", 255)
			);
		}
		
		PortalInfo(Identifier dimension, Identifier item, int r, int g, int b) {
			this.dimension = dimension;
			this.item = item;
			this.color = MHelper.color(r, g, b);
		}
		
		ServerWorld getWorld(MinecraftServer server) {
			if (world != null) {
				return world;
			}
			for (ServerWorld world : server.getWorlds()) {
				if (world.getRegistryKey().getValue().equals(dimension)) {
					this.world = world;
					return world;
				}
			}
			return server.getOverworld();
		}
		
		JsonObject toJson() {
			JsonObject obj = new JsonObject();
			obj.addProperty("dimension", dimension.toString());
			obj.addProperty("item", item.toString());
			obj.addProperty("colorRed", (color >> 16) & 255);
			obj.addProperty("colorGreen", (color >> 8) & 255);
			obj.addProperty("colorBlue", color & 255);
			return obj;
		}
	}
}
