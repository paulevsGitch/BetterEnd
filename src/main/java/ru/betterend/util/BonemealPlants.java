package ru.betterend.util;

import com.google.common.collect.Lists;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import ru.bclib.api.BonemealAPI;
import ru.bclib.api.TagAPI;
import ru.betterend.blocks.basis.EndTerrainBlock;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndBlocks;

import java.util.List;

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
		BonemealAPI.addLandGrass(EndBlocks.SMALL_JELLYSHROOM, EndBlocks.JUNGLE_MOSS, 0.1F);
		
		BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS.getID(), EndBlocks.BLOOMING_COOKSONIA, EndBlocks.END_MOSS);
		BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS.getID(), EndBlocks.VAIOLUSH_FERN, EndBlocks.END_MOSS);
		BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS.getID(), EndBlocks.FRACTURN, EndBlocks.END_MOSS);
		BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS.getID(), EndBlocks.SALTEAGO, EndBlocks.END_MOSS);
		
		BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS.getID(), EndBlocks.CREEPING_MOSS, EndBlocks.END_MOSS, 0.1F);
		BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS.getID(), EndBlocks.UMBRELLA_MOSS, EndBlocks.END_MOSS, 0.1F);
		BonemealAPI.addLandGrass(EndBiomes.GLOWING_GRASSLANDS.getID(), EndBlocks.TWISTED_UMBRELLA_MOSS, EndBlocks.END_MOSS, 0.1F);
		
		BonemealAPI.addLandGrass(EndBlocks.ORANGO, EndBlocks.RUTISCUS);
		BonemealAPI.addLandGrass(EndBlocks.AERIDIUM, EndBlocks.RUTISCUS, 0.2F);
		BonemealAPI.addLandGrass(EndBlocks.LUTEBUS, EndBlocks.RUTISCUS, 0.2F);
		BonemealAPI.addLandGrass(EndBlocks.LAMELLARIUM, EndBlocks.RUTISCUS);
		
		BonemealAPI.addLandGrass(EndBiomes.LANTERN_WOODS.getID(), EndBlocks.AERIDIUM, EndBlocks.RUTISCUS, 0.2F);
		BonemealAPI.addLandGrass(EndBiomes.LANTERN_WOODS.getID(), EndBlocks.LAMELLARIUM, EndBlocks.RUTISCUS);
		BonemealAPI.addLandGrass(EndBiomes.LANTERN_WOODS.getID(), EndBlocks.BOLUX_MUSHROOM, EndBlocks.RUTISCUS, 0.05F);
		
		BonemealAPI.addLandGrass(EndBlocks.GLOBULAGUS, EndBlocks.SANGNUM, EndBlocks.MOSSY_OBSIDIAN, EndBlocks.MOSSY_DRAGON_BONE);
		BonemealAPI.addLandGrass(EndBlocks.CLAWFERN, EndBlocks.SANGNUM, EndBlocks.MOSSY_OBSIDIAN, EndBlocks.MOSSY_DRAGON_BONE);
		BonemealAPI.addLandGrass(EndBlocks.SANGNUM, EndBlocks.SMALL_AMARANITA_MUSHROOM, 0.1F);
		BonemealAPI.addLandGrass(EndBlocks.SMALL_AMARANITA_MUSHROOM, EndBlocks.MOSSY_OBSIDIAN, 0.1F);
		BonemealAPI.addLandGrass(EndBlocks.SMALL_AMARANITA_MUSHROOM, EndBlocks.MOSSY_DRAGON_BONE, 0.1F);
		
		BonemealAPI.addLandGrass(EndBlocks.GLOBULAGUS, EndBlocks.MOSSY_DRAGON_BONE);
		BonemealAPI.addLandGrass(EndBlocks.CLAWFERN, EndBlocks.MOSSY_DRAGON_BONE);
		BonemealAPI.addLandGrass(EndBlocks.SMALL_AMARANITA_MUSHROOM, EndBlocks.MOSSY_DRAGON_BONE, 0.1F);
		
		BonemealAPI.addLandGrass(EndBlocks.GLOBULAGUS, EndBlocks.MOSSY_OBSIDIAN);
		BonemealAPI.addLandGrass(EndBlocks.CLAWFERN, EndBlocks.MOSSY_OBSIDIAN);
		BonemealAPI.addLandGrass(EndBlocks.SMALL_AMARANITA_MUSHROOM, EndBlocks.MOSSY_OBSIDIAN, 0.1F);
		
		Block[] charnias = new Block[] {
			EndBlocks.CHARNIA_CYAN,
			EndBlocks.CHARNIA_GREEN,
			EndBlocks.CHARNIA_ORANGE,
			EndBlocks.CHARNIA_LIGHT_BLUE,
			EndBlocks.CHARNIA_PURPLE,
			EndBlocks.CHARNIA_RED
		};
		List<Block> terrain = Lists.newArrayList();
		EndBlocks.getModBlocks().forEach(block -> {
			if (block instanceof EndTerrainBlock) {
				terrain.add(block);
			}
		});
		terrain.add(Blocks.END_STONE);
		terrain.add(EndBlocks.ENDSTONE_DUST);
		terrain.add(EndBlocks.CAVE_MOSS);
		terrain.add(EndBlocks.SULPHURIC_ROCK.stone);
		terrain.add(EndBlocks.VIOLECITE.stone);
		terrain.add(EndBlocks.FLAVOLITE.stone);
		terrain.add(EndBlocks.AZURE_JADESTONE.stone);
		terrain.add(EndBlocks.VIRID_JADESTONE.stone);
		terrain.add(EndBlocks.SANDY_JADESTONE.stone);
		terrain.add(EndBlocks.BRIMSTONE);
		Block[] terrainBlocks = terrain.toArray(new Block[terrain.size()]);
		for (Block charnia: charnias) {
			BonemealAPI.addWaterGrass(charnia, terrainBlocks);
		}
	}
}
