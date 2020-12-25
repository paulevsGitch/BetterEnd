package ru.betterend.util;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import ru.betterend.registry.EndBlocks;

public class BonemealUtil {
	private static final Map<Block, GrassList> GRASS_TYPES = Maps.newHashMap();
	
	public static void init() {
		addBonemealGrass(EndBlocks.END_MOSS, EndBlocks.CREEPING_MOSS);
		addBonemealGrass(EndBlocks.END_MOSS, EndBlocks.UMBRELLA_MOSS);
		addBonemealGrass(EndBlocks.END_MYCELIUM, EndBlocks.CREEPING_MOSS);
		addBonemealGrass(EndBlocks.END_MYCELIUM, EndBlocks.UMBRELLA_MOSS);
		addBonemealGrass(EndBlocks.CAVE_MOSS, EndBlocks.CAVE_GRASS);
		addBonemealGrass(EndBlocks.CHORUS_NYLIUM, EndBlocks.CHORUS_GRASS);
		addBonemealGrass(EndBlocks.CRYSTAL_MOSS, EndBlocks.CRYSTAL_GRASS);
		addBonemealGrass(EndBlocks.SHADOW_GRASS, EndBlocks.SHADOW_PLANT);
		addBonemealGrass(EndBlocks.PINK_MOSS, EndBlocks.BUSHY_GRASS);
		addBonemealGrass(EndBlocks.AMBER_MOSS, EndBlocks.AMBER_GRASS);
		addBonemealGrass(EndBlocks.JUNGLE_MOSS, EndBlocks.JUNGLE_GRASS);
	}
	
	public static void addBonemealGrass(Block terrain, Block plant) {
		addBonemealGrass(terrain, plant, 1F);
	}
	
	public static void addBonemealGrass(Block terrain, Block plant, float chance) {
		GrassList list = GRASS_TYPES.get(terrain);
		if (list == null) {
			list = new GrassList();
			GRASS_TYPES.put(terrain, list);
		}
		list.addGrass(plant, chance);
	}
	
	public static Block getGrass(Block terrain, Random random) {
		GrassList list = GRASS_TYPES.get(terrain);
		return list == null ? null : list.getGrass(random);
	}
	
	private static final class GrassInfo {
		final Block grass;
		float chance;
		
		public GrassInfo(Block grass, float chance) {
			this.grass = grass;
			this.chance = chance;
		}
		
		public float addChance(float chance) {
			this.chance += chance;
			return this.chance;
		}
	}
	
	private static final class GrassList {
		final List<GrassInfo> list = Lists.newArrayList();
		float maxChance = 0;
		
		public void addGrass(Block grass, float chance) {
			GrassInfo info = new GrassInfo(grass, chance);
			maxChance = info.addChance(maxChance);
			list.add(info);
		}
		
		public Block getGrass(Random random) {
			if (maxChance == 0 || list.isEmpty()) {
				return null;
			}
			float chance = random.nextFloat() * maxChance;
			for (GrassInfo info: list) {
				if (chance <= info.chance) {
					return info.grass;
				}
			}
			return null;
		}
	}
}
