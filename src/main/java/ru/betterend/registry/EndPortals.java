package ru.betterend.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import ru.bclib.util.ColorUtil;
import ru.bclib.util.JsonFactory;
import ru.bclib.util.MHelper;
import ru.betterend.BetterEnd;

import java.io.File;

public class EndPortals {

	public final static ResourceLocation OVERWORLD_ID = Level.OVERWORLD.location();

	private static PortalInfo[] portals;

	public static void loadPortals() {
		File file = new File(FabricLoader.getInstance().getConfigDir().toString(), "betterend/portals.json");
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

	public static int getCount() {
		return MHelper.max(portals.length - 1, 1);
	}

	public static ServerLevel getWorld(MinecraftServer server, int portalId) {
		if (portalId < 0 || portalId >= portals.length) {
			return server.overworld();
		}
		return portals[portalId].getWorld(server);
	}

	public static ResourceLocation getWorldId(int portalId) {
		if (portalId < 0 || portalId >= portals.length) {
			return OVERWORLD_ID;
		}
		return portals[portalId].dimension;
	}

	public static int getPortalIdByItem(ResourceLocation item) {
		for (int i = 0; i < portals.length; i++) {
			if (portals[i].item.equals(item)) {
				return i;
			}
		}
		return 0;
	}

	public static int getPortalIdByWorld(ResourceLocation world) {
		for (int i = 0; i < portals.length; i++) {
			if (portals[i].dimension.equals(world)) {
				return i;
			}
		}
		return 0;
	}

	public static int getColor(int state) {
		return portals[state].color;
	}

	public static boolean isAvailableItem(ResourceLocation item) {
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
		return new PortalInfo(new ResourceLocation("minecraft:overworld"), BetterEnd.makeID("eternal_crystal"), 255, 255, 255);
	}

	private static class PortalInfo {
		private final ResourceLocation dimension;
		private final ResourceLocation item;
		private final int color;
		private ServerLevel world;

		PortalInfo(JsonObject obj) {
			this(
					new ResourceLocation(JsonFactory.getString(obj, "dimension", "minecraft:overworld")),
					new ResourceLocation(JsonFactory.getString(obj, "item", "betterend:eternal_crystal")),
					JsonFactory.getInt(obj, "colorRed", 255),
					JsonFactory.getInt(obj, "colorGreen", 255),
					JsonFactory.getInt(obj, "colorBlue", 255)
			);
		}

		PortalInfo(ResourceLocation dimension, ResourceLocation item, int r, int g, int b) {
			this.dimension = dimension;
			this.item = item;
			this.color = ColorUtil.color(r, g, b);
		}

		ServerLevel getWorld(MinecraftServer server) {
			if (world != null) {
				return world;
			}
			for (ServerLevel world : server.getAllLevels()) {
				if (world.dimension().location().equals(dimension)) {
					this.world = world;
					return world;
				}
			}
			return server.overworld();
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
