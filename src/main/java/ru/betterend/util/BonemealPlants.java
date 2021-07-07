package ru.betterend.util;

import ru.bclib.api.BonemealAPI;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndBlocks;

public class BonemealPlants {
	public static void init() {
		BonemealAPI.addLandGrass(EndBlocks.CREEPING_MOSS, EndBlocks.END_MOSS);
		BonemealAPI.addLandGrass(EndBlocks.UMBRELLA_MOSS, EndBlocks.END_MOSS);
		BonemealAPI.addLandGrass(EndBlocks.CREEPING_MOSS, EndBlocks.END_MYCELIUM);
		BonemealAPI.addLandGrass(EndBlocks.UMBRELLA_MOSS, EndBlocks.END_MYCELIUM);

		BonemealAPI.addLandGrass(EndBlocks.CAVE_GRASS, EndBlocks.CAVE_MOSS);
		BonemealAPI.addLandGrass(EndBlocks.CHORUS_GRASS, EndBlocks.CHORUS_NYLIUM);
		BonemealAPI.addLandGrass(EndBlocks.CRYSTAL_GRASS, EndBlocks.CRYSTAL_MOSS);
		BonemealAPI.addLandGrass(EndBlocks.SHADOW_PLANT, EndBlocks.SHADOW_GRASS);
		BonemealAPI.addLandGrass(EndBlocks.BUSHY_GRASS, EndBlocks.PINK_MOSS);
		BonemealAPI.addLandGrass(EndBlocks.AMBER_GRASS, EndBlocks.AMBER_MOSS);

		BonemealAPI.addLandGrass(EndBlocks.JUNGLE_GRASS, EndBlocks.JUNGLE_MOSS);
		BonemealAPI.addLandGrass(EndBlocks.TWISTED_UMBRELLA_MOSS, EndBlocks.JUNGLE_MOSS);
		BonemealAPI.addLandGrass(EndBlocks.JUNGLE_MOSS, EndBlocks.SMALL_JELLYSHROOM, 0.1F);

		BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS.getID(), EndBlocks.BLOOMING_COOKSONIA, EndBlocks.END_MOSS);
		BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS.getID(), EndBlocks.VAIOLUSH_FERN, EndBlocks.END_MOSS);
		BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS.getID(), EndBlocks.FRACTURN, EndBlocks.END_MOSS);
		BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS.getID(), EndBlocks.SALTEAGO, EndBlocks.END_MOSS);

		BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS.getID(), EndBlocks.END_MOSS, EndBlocks.CREEPING_MOSS, 0.1F);
		BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS.getID(), EndBlocks.END_MOSS, EndBlocks.UMBRELLA_MOSS, 0.1F);
		BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS.getID(), EndBlocks.END_MOSS, EndBlocks.TWISTED_UMBRELLA_MOSS, 0.1F);

		BonemealAPI.addLandGrass(EndBlocks.ORANGO, EndBlocks.RUTISCUS);
		BonemealAPI.addLandGrass(EndBlocks.RUTISCUS, EndBlocks.AERIDIUM, 0.2F);
		BonemealAPI.addLandGrass(EndBlocks.RUTISCUS, EndBlocks.LUTEBUS, 0.2F);
		BonemealAPI.addLandGrass(EndBlocks.RUTISCUS, EndBlocks.LAMELLARIUM);

		BonemealAPI.addLandGrass(EndBiomes.LANTERN_WOODS.getID(), EndBlocks.RUTISCUS, EndBlocks.AERIDIUM, 0.2F);
		BonemealAPI.addLandGrass(EndBiomes.LANTERN_WOODS.getID(), EndBlocks.LAMELLARIUM, EndBlocks.RUTISCUS);
		BonemealAPI.addLandGrass(EndBiomes.LANTERN_WOODS.getID(), EndBlocks.RUTISCUS, EndBlocks.BOLUX_MUSHROOM, 0.05F);

		BonemealAPI.addLandGrass(EndBlocks.GLOBULAGUS, EndBlocks.SANGNUM, EndBlocks.MOSSY_OBSIDIAN, EndBlocks.MOSSY_DRAGON_BONE);
		BonemealAPI.addLandGrass(EndBlocks.CLAWFERN, EndBlocks.SANGNUM, EndBlocks.MOSSY_OBSIDIAN, EndBlocks.MOSSY_DRAGON_BONE);
		BonemealAPI.addLandGrass(EndBlocks.SANGNUM, EndBlocks.SMALL_AMARANITA_MUSHROOM, 0.1F);
		BonemealAPI.addLandGrass(EndBlocks.MOSSY_OBSIDIAN, EndBlocks.SMALL_AMARANITA_MUSHROOM, 0.1F);
		BonemealAPI.addLandGrass(EndBlocks.MOSSY_DRAGON_BONE, EndBlocks.SMALL_AMARANITA_MUSHROOM, 0.1F);

		BonemealAPI.addLandGrass(EndBlocks.GLOBULAGUS, EndBlocks.MOSSY_DRAGON_BONE);
		BonemealAPI.addLandGrass(EndBlocks.CLAWFERN, EndBlocks.MOSSY_DRAGON_BONE);
		BonemealAPI.addLandGrass(EndBlocks.MOSSY_DRAGON_BONE, EndBlocks.SMALL_AMARANITA_MUSHROOM, 0.1F);

		BonemealAPI.addLandGrass(EndBlocks.GLOBULAGUS, EndBlocks.MOSSY_OBSIDIAN);
		BonemealAPI.addLandGrass(EndBlocks.CLAWFERN, EndBlocks.MOSSY_OBSIDIAN);
		BonemealAPI.addLandGrass(EndBlocks.MOSSY_OBSIDIAN, EndBlocks.SMALL_AMARANITA_MUSHROOM, 0.1F);
	}
}
