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
				root = NbtIo.read(file);
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
	
	public static void saveFile() {
		try {
			NbtIo.write(root, saveFile);
		}
		catch (IOException e) {
			BetterEnd.LOGGER.error("World data saving failed", e);
		}
	}
}
