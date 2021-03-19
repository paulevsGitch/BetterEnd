package ru.betterend.util;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndBlocks;
import ru.betterend.world.biome.land.EndBiome;

public class BonemealUtil {
	private static final Map<Identifier, Map<Block, GrassList>> GRASS_BIOMES = Maps.newHashMap();
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
		addBonemealGrass(EndBlocks.JUNGLE_MOSS, EndBlocks.TWISTED_UMBRELLA_MOSS);
		addBonemealGrass(EndBlocks.JUNGLE_MOSS, EndBlocks.SMALL_JELLYSHROOM, 0.1F);
		
		addBonemealGrass(EndBiomes.GLOWING_GRASSLANDS, EndBlocks.END_MOSS, EndBlocks.BLOOMING_COOKSONIA);
		addBonemealGrass(EndBiomes.GLOWING_GRASSLANDS, EndBlocks.END_MOSS, EndBlocks.VAIOLUSH_FERN);
		addBonemealGrass(EndBiomes.GLOWING_GRASSLANDS, EndBlocks.END_MOSS, EndBlocks.FRACTURN);
		addBonemealGrass(EndBiomes.GLOWING_GRASSLANDS, EndBlocks.END_MOSS, EndBlocks.SALTEAGO);
		
		addBonemealGrass(EndBiomes.GLOWING_GRASSLANDS, EndBlocks.END_MOSS, EndBlocks.CREEPING_MOSS, 0.1F);
		addBonemealGrass(EndBiomes.GLOWING_GRASSLANDS, EndBlocks.END_MOSS, EndBlocks.UMBRELLA_MOSS, 0.1F);
		addBonemealGrass(EndBiomes.GLOWING_GRASSLANDS, EndBlocks.END_MOSS, EndBlocks.TWISTED_UMBRELLA_MOSS, 0.1F);
		
		addBonemealGrass(EndBlocks.RUTISCUS, EndBlocks.ORANGO);
		addBonemealGrass(EndBlocks.RUTISCUS, EndBlocks.AERIDIUM, 0.2F);
		addBonemealGrass(EndBlocks.RUTISCUS, EndBlocks.LUTEBUS, 0.2F);
		addBonemealGrass(EndBlocks.RUTISCUS, EndBlocks.LAMELLARIUM);
		
		addBonemealGrass(EndBiomes.LANTERN_WOODS, EndBlocks.RUTISCUS, EndBlocks.AERIDIUM, 0.2F);
		addBonemealGrass(EndBiomes.LANTERN_WOODS, EndBlocks.RUTISCUS, EndBlocks.LAMELLARIUM);
		addBonemealGrass(EndBiomes.LANTERN_WOODS, EndBlocks.RUTISCUS, EndBlocks.BOLUX_MUSHROOM, 0.05F);
		
		addBonemealGrass(EndBlocks.SANGNUM, EndBlocks.GLOBULAGUS);
		addBonemealGrass(EndBlocks.SANGNUM, EndBlocks.CLAWFERN);
		addBonemealGrass(EndBlocks.SANGNUM, EndBlocks.SMALL_AMARANITA_MUSHROOM, 0.1F);
		
		addBonemealGrass(EndBlocks.MOSSY_DRAGON_BONE, EndBlocks.GLOBULAGUS);
		addBonemealGrass(EndBlocks.MOSSY_DRAGON_BONE, EndBlocks.CLAWFERN);
		addBonemealGrass(EndBlocks.MOSSY_DRAGON_BONE, EndBlocks.SMALL_AMARANITA_MUSHROOM, 0.1F);
		
		addBonemealGrass(EndBlocks.MOSSY_OBSIDIAN, EndBlocks.GLOBULAGUS);
		addBonemealGrass(EndBlocks.MOSSY_OBSIDIAN, EndBlocks.CLAWFERN);
		addBonemealGrass(EndBlocks.MOSSY_OBSIDIAN, EndBlocks.SMALL_AMARANITA_MUSHROOM, 0.1F);
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
	
	public static void addBonemealGrass(EndBiome biome, Block terrain, Block plant) {
		addBonemealGrass(biome, terrain, plant, 1F);
	}
	
	public static void addBonemealGrass(EndBiome biome, Block terrain, Block plant, float chance) {
		Map<Block, GrassList> map = GRASS_BIOMES.get(biome.getID());
		if (map == null) {
			map = Maps.newHashMap();
			GRASS_BIOMES.put(biome.getID(), map);
		}
		GrassList list = map.get(terrain);
		if (list == null) {
			list = new GrassList();
			map.put(terrain, list);
		}
		list.addGrass(plant, chance);
	}
	
	public static Block getGrass(Identifier biomeID, Block terrain, Random random) {
		Map<Block, GrassList> map = GRASS_BIOMES.get(biomeID);
		GrassList list = null;
		if (map != null) {
			list = map.get(terrain);
			if (list == null) {
				list = GRASS_TYPES.get(terrain);
			}
		}
		else {
			list = GRASS_TYPES.get(terrain);
		}
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
