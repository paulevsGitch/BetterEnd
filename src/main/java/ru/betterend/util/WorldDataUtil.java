package ru.betterend.util;

import java.io.File;
import java.io.IOException;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import ru.betterend.BetterEnd;

public class WorldDataUtil {
	private static CompoundTag root;
	private static File saveFile;
	
	public static void load(File file) {
		saveFile = file;
		if (file.exists()) {
			try {
				root = NbtIo.readCompressed(file);
			}
			catch (IOException e) {
				BetterEnd.LOGGER.error("World data loading failed", e);
				root = new CompoundTag();
			}
			return;
		}
		root = new CompoundTag();
	}
	
	public static CompoundTag getRootTag() {
		return root;
	}
	
	public static CompoundTag getCompoundTag(String path) {
		String[] parts = path.split("\\.");
		CompoundTag tag = root;
		for (String part: parts) {
			if (tag.contains(part)) {
				tag = tag.getCompound(part);
			}
			else {
				CompoundTag t = new CompoundTag();
				tag.put(part, t);
				tag = t;
			}
		}
		return tag;
	}
	
	public static void saveFile() {
		try {
			NbtIo.writeCompressed(root, saveFile);
		}
		catch (IOException e) {
			BetterEnd.LOGGER.error("World data saving failed", e);
		}
	}
}
