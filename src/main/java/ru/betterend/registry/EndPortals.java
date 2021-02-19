package ru.betterend.registry;

import java.io.File;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import ru.betterend.config.ConfigWriter;
import ru.betterend.util.JsonFactory;
import ru.betterend.util.MHelper;

public class EndPortals {
	private static PortalInfo[] portals;
	
	public static void loadPortals() {
		File file = new File(ConfigWriter.MOD_CONFIG_DIR, "portals.json");
		JsonObject json;
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			json = makeDefault(file);
		}
		else {
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
	
	public static ServerWorld getWorld(MinecraftServer server, int state) {
		if (state >= portals.length) {
			return server.getOverworld();
		}
		return portals[state].getWorld(server);
	}
	
	public static int getColor(int state) {
		return portals[state].color;
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
		return new PortalInfo(new Identifier("minecraft:overworld"), 255, 255, 255);
	}
	
	private static class PortalInfo {
		private final Identifier dimension;
		private final int color;
		private ServerWorld world;
		
		PortalInfo(JsonObject obj) {
			this(
				new Identifier(JsonFactory.getString(obj, "dimension", "minecraft:overworld")),
				JsonFactory.getInt(obj, "colorRed", 255),
				JsonFactory.getInt(obj, "colorGreen", 255),
				JsonFactory.getInt(obj, "colorBlue", 255)
			);
		}
		
		PortalInfo(Identifier dimension, int r, int g, int b) {
			this.dimension = dimension;
			this.color = MHelper.color(r, g, b);
		}
		
		ServerWorld getWorld(MinecraftServer server) {
			if (world != null) {
				return world;
			}
			Iterator<ServerWorld> iterator = server.getWorlds().iterator();
			while (iterator.hasNext()) {
				ServerWorld world = iterator.next();
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
			obj.addProperty("colorRed", (color >> 16) & 255);
			obj.addProperty("colorGreen", (color >> 8) & 255);
			obj.addProperty("colorBlue", color & 255);
			return obj;
		}
	}
}
