package ru.betterend.util;

import ru.bclib.api.BonemealAPI;
import ru.betterend.registry.EndBlocks;

public class BonemealPlants {
	public static void init() {
		BonemealAPI.addLandGrass(EndBlocks.END_MOSS, EndBlocks.CREEPING_MOSS);
		BonemealAPI.addLandGrass(EndBlocks.END_MOSS, EndBlocks.UMBRELLA_MOSS);
		BonemealAPI.addLandGrass(EndBlocks.END_MYCELIUM, EndBlocks.CREEPING_MOSS);
		BonemealAPI.addLandGrass(EndBlocks.END_MYCELIUM, EndBlocks.UMBRELLA_MOSS);
		
		BonemealAPI.addLandGrass(EndBlocks.CAVE_MOSS, EndBlocks.CAVE_GRASS);
		BonemealAPI.addLandGrass(EndBlocks.CHORUS_NYLIUM, EndBlocks.CHORUS_GRASS);
		BonemealAPI.addLandGrass(EndBlocks.CRYSTAL_MOSS, EndBlocks.CRYSTAL_GRASS);
		BonemealAPI.addLandGrass(EndBlocks.SHADOW_GRASS, EndBlocks.SHADOW_PLANT);
		BonemealAPI.addLandGrass(EndBlocks.PINK_MOSS, EndBlocks.BUSHY_GRASS);
		BonemealAPI.addLandGrass(EndBlocks.AMBER_MOSS, EndBlocks.AMBER_GRASS);
		
		BonemealAPI.addLandGrass(EndBlocks.JUNGLE_MOSS, EndBlocks.JUNGLE_GRASS);
		BonemealAPI.addLandGrass(EndBlocks.JUNGLE_MOSS, EndBlocks.TWISTED_UMBRELLA_MOSS);
		BonemealAPI.addLandGrass(EndBlocks.JUNGLE_MOSS, EndBlocks.SMALL_JELLYSHROOM, 0.1F);
		
		// Wait for Lib changes
		//BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS, EndBlocks.END_MOSS, EndBlocks.BLOOMING_COOKSONIA);
		//BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS, EndBlocks.END_MOSS, EndBlocks.VAIOLUSH_FERN);
		//BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS, EndBlocks.END_MOSS, EndBlocks.FRACTURN);
		//BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS, EndBlocks.END_MOSS, EndBlocks.SALTEAGO);
		
		//BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS, EndBlocks.END_MOSS, EndBlocks.CREEPING_MOSS, 0.1F);
		//BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS, EndBlocks.END_MOSS, EndBlocks.UMBRELLA_MOSS, 0.1F);
		//BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS, EndBlocks.END_MOSS, EndBlocks.TWISTED_UMBRELLA_MOSS, 0.1F);
		
		BonemealAPI.addLandGrass(EndBlocks.RUTISCUS, EndBlocks.ORANGO);
		BonemealAPI.addLandGrass(EndBlocks.RUTISCUS, EndBlocks.AERIDIUM, 0.2F);
		BonemealAPI.addLandGrass(EndBlocks.RUTISCUS, EndBlocks.LUTEBUS, 0.2F);
		BonemealAPI.addLandGrass(EndBlocks.RUTISCUS, EndBlocks.LAMELLARIUM);
		
		//BonemealAPI.addLandGrass(EndBiomes.LANTERN_WOODS, EndBlocks.RUTISCUS, EndBlocks.AERIDIUM, 0.2F);
		//BonemealAPI.addLandGrass(EndBiomes.LANTERN_WOODS, EndBlocks.RUTISCUS, EndBlocks.LAMELLARIUM);
		//BonemealAPI.addLandGrass(EndBiomes.LANTERN_WOODS, EndBlocks.RUTISCUS, EndBlocks.BOLUX_MUSHROOM, 0.05F);
		
		BonemealAPI.addLandGrass(EndBlocks.SANGNUM, EndBlocks.GLOBULAGUS);
		BonemealAPI.addLandGrass(EndBlocks.SANGNUM, EndBlocks.CLAWFERN);
		BonemealAPI.addLandGrass(EndBlocks.SANGNUM, EndBlocks.SMALL_AMARANITA_MUSHROOM, 0.1F);
		
		BonemealAPI.addLandGrass(EndBlocks.MOSSY_DRAGON_BONE, EndBlocks.GLOBULAGUS);
		BonemealAPI.addLandGrass(EndBlocks.MOSSY_DRAGON_BONE, EndBlocks.CLAWFERN);
		BonemealAPI.addLandGrass(EndBlocks.MOSSY_DRAGON_BONE, EndBlocks.SMALL_AMARANITA_MUSHROOM, 0.1F);
		
		BonemealAPI.addLandGrass(EndBlocks.MOSSY_OBSIDIAN, EndBlocks.GLOBULAGUS);
		BonemealAPI.addLandGrass(EndBlocks.MOSSY_OBSIDIAN, EndBlocks.CLAWFERN);
		BonemealAPI.addLandGrass(EndBlocks.MOSSY_OBSIDIAN, EndBlocks.SMALL_AMARANITA_MUSHROOM, 0.1F);
	}
}
