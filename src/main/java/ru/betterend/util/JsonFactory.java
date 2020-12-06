package ru.betterend.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.resource.Resource;
import ru.betterend.BetterEnd;

public class JsonFactory {
	
	public final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	// Unused
	@Deprecated
	public static JsonObject getJsonObject(String path) throws IOException {
		try (InputStream is = JsonFactory.class.getResourceAsStream(path)) {
			Reader reader = new InputStreamReader(is);
			JsonObject jsonObject = loadJson(reader).getAsJsonObject();
			if (jsonObject == null) {
				return new JsonObject();
			}
			return jsonObject;
		}
	}
	
	// Unused
	@Deprecated
	public static JsonObject getJsonObject(Resource jsonSource) {
		if (jsonSource != null) {
			try (InputStream is = jsonSource.getInputStream()) {
				Reader reader = new InputStreamReader(is);
				JsonElement json = loadJson(reader);
				if (json != null && json.isJsonObject()) {
					JsonObject jsonObject = json.getAsJsonObject();
					return jsonObject != null ? jsonObject : new JsonObject();
				}
			}
			catch (IOException ex) {
				BetterEnd.LOGGER.catching(ex);
			}
		}
		return new JsonObject();
	}

	public static JsonObject getJsonObject(InputStream stream) {
		try {
			Reader reader = new InputStreamReader(stream);
			JsonElement json = loadJson(reader);
			if (json != null && json.isJsonObject()) {
				JsonObject jsonObject = json.getAsJsonObject();
				return jsonObject != null ? jsonObject : new JsonObject();
			}
		}
		catch (Exception ex) {
			BetterEnd.LOGGER.catching(ex);
		}
		return new JsonObject();
	}
	
	public static JsonObject getJsonObject(File jsonFile) {
		if (jsonFile.exists()) {
			JsonElement json = loadJson(jsonFile);
			if (json != null && json.isJsonObject()) {
				JsonObject jsonObject = json.getAsJsonObject();
				return jsonObject != null ? jsonObject : new JsonObject();
			}
		}
		
		return new JsonObject();
	}
	
	public static JsonElement loadJson(File jsonFile) {
		if (jsonFile.exists()) {
			try (Reader reader = new FileReader(jsonFile)) {
				return loadJson(reader);
			} catch (Exception ex) {
				BetterEnd.LOGGER.catching(ex);
			}
		}
		return null;
	}
	
	public static JsonElement loadJson(Reader reader) {
		return GSON.fromJson(reader, JsonElement.class);
	}
	
	public static void storeJson(File jsonFile, JsonElement jsonObject) {
		try(FileWriter writer = new FileWriter(jsonFile)) {			
			String json = GSON.toJson(jsonObject);
			writer.write(json);
			writer.flush();
		} catch (IOException ex) {
			BetterEnd.LOGGER.catching(ex);
		}
	}
	
	public static float getFloat(JsonObject object, String member, float def) {
		JsonElement elem = object.get(member);
		return elem == null ? def : elem.getAsFloat();
	}
	
	public static boolean getBoolean(JsonObject object, String member, boolean def) {
		JsonElement elem = object.get(member);
		return elem == null ? def : elem.getAsBoolean();
	}
	
	public static String getString(JsonObject object, String member, String def) {
		JsonElement elem = object.get(member);
		return elem == null ? def : elem.getAsString();
	}
}
