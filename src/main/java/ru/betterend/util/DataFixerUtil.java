package ru.betterend.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.storage.RegionFile;

public class DataFixerUtil {
	private static final Map<String, String> REPLACEMENT = Maps.newHashMap();
	
	public static void init() {
		//addFix("minecraft:stone", "minecraft:glowstone"); // Example
	}
	
	public static void fixData(File dir) {
		if (REPLACEMENT.isEmpty()) {
			return;
		}
		
		List<File> regions = getAllRegions(dir, null);
		regions.parallelStream().forEach((file) -> {
			try {
				System.out.println("Fixing " + file);
				boolean[] changed = new boolean[1];
				RegionFile region = new RegionFile(file, file.getParentFile(), true);
				for (int x = 0; x < 32; x++) {
					for (int z = 0; z < 32; z++) {
						ChunkPos pos = new ChunkPos(x, z);
						changed[0] = false;
						if (region.hasChunk(pos)) {
							DataInputStream input = region.getChunkDataInputStream(pos);
							CompoundTag root = NbtIo.read(input);
							input.close();
							ListTag sections = root.getCompound("Level").getList("Sections", 10);
							sections.forEach((tag) -> {
								ListTag palette = ((CompoundTag) tag).getList("Palette", 10);
								palette.forEach((blockTag) -> {
									CompoundTag blockTagCompound = ((CompoundTag) blockTag);
									String name = blockTagCompound.getString("Name");
									String replace = REPLACEMENT.get(name);
									if (replace != null) {
										blockTagCompound.putString("Name", replace);
										changed[0] = true;
									}
								});
							});
							if (changed[0]) {
								System.out.println("Write!");
								DataOutputStream output = region.getChunkDataOutputStream(pos);
								NbtIo.write(root, output);
								output.close();
							}
						}
					}
				}
				region.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	protected static void addFix(String result, String... names) {
		for (String name: names) {
			REPLACEMENT.put(name, result);
		}
	}
	
	private static List<File> getAllRegions(File dir, List<File> list) {
		if (list == null) {
			list = Lists.newArrayList();
		}
		for (File file: dir.listFiles()) {
			if (file.isDirectory()) {
				getAllRegions(file, list);
			}
			else if (file.isFile() && file.getName().endsWith(".mca")) {
				list.add(file);
			}
		}
		return list;
	}
}
