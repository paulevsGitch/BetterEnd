package ru.betterend.registry;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.Lists;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import ru.bclib.api.BiomeAPI;
import ru.bclib.world.biomes.BCLBiome;
import ru.bclib.world.features.BCLFeature;
import ru.bclib.world.features.DefaultFeature;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.complex.StoneMaterial;
import ru.betterend.world.biome.BiomeDefinition;
import ru.betterend.world.biome.EndBiome;
import ru.betterend.world.features.BiomeIslandFeature;
import ru.betterend.world.features.BlueVineFeature;
import ru.betterend.world.features.CavePumpkinFeature;
import ru.betterend.world.features.CharniaFeature;
import ru.betterend.world.features.CrashedShipFeature;
import ru.betterend.world.features.DoublePlantFeature;
import ru.betterend.world.features.EndLilyFeature;
import ru.betterend.world.features.EndLotusFeature;
import ru.betterend.world.features.EndLotusLeafFeature;
import ru.betterend.world.features.FilaluxFeature;
import ru.betterend.world.features.GlowPillarFeature;
import ru.betterend.world.features.HydraluxFeature;
import ru.betterend.world.features.LanceleafFeature;
import ru.betterend.world.features.MengerSpongeFeature;
import ru.betterend.world.features.NeonCactusFeature;
import ru.betterend.world.features.SilkMothNestFeature;
import ru.betterend.world.features.SingleInvertedScatterFeature;
import ru.betterend.world.features.SinglePlantFeature;
import ru.betterend.world.features.UnderwaterPlantFeature;
import ru.betterend.world.features.VineFeature;
import ru.betterend.world.features.WallPlantFeature;
import ru.betterend.world.features.WallPlantOnLogFeature;
import ru.betterend.world.features.bushes.BushFeature;
import ru.betterend.world.features.bushes.BushWithOuterFeature;
import ru.betterend.world.features.bushes.LargeAmaranitaFeature;
import ru.betterend.world.features.bushes.Lumecorn;
import ru.betterend.world.features.bushes.TenaneaBushFeature;
import ru.betterend.world.features.terrain.BigAuroraCrystalFeature;
import ru.betterend.world.features.terrain.DesertLakeFeature;
import ru.betterend.world.features.terrain.EndLakeFeature;
import ru.betterend.world.features.terrain.FallenPillarFeature;
import ru.betterend.world.features.terrain.FloatingSpireFeature;
import ru.betterend.world.features.terrain.GeyserFeature;
import ru.betterend.world.features.terrain.IceStarFeature;
import ru.betterend.world.features.terrain.ObsidianBoulderFeature;
import ru.betterend.world.features.terrain.ObsidianPillarBasementFeature;
import ru.betterend.world.features.terrain.OreLayerFeature;
import ru.betterend.world.features.terrain.SingleBlockFeature;
import ru.betterend.world.features.terrain.SmaragdantCrystalFeature;
import ru.betterend.world.features.terrain.SpireFeature;
import ru.betterend.world.features.terrain.StalactiteFeature;
import ru.betterend.world.features.terrain.SulphurHillFeature;
import ru.betterend.world.features.terrain.SulphuricCaveFeature;
import ru.betterend.world.features.terrain.SulphuricLakeFeature;
import ru.betterend.world.features.terrain.SurfaceVentFeature;
import ru.betterend.world.features.terrain.caves.RoundCaveFeature;
import ru.betterend.world.features.terrain.caves.TunelCaveFeature;
import ru.betterend.world.features.trees.DragonTreeFeature;
import ru.betterend.world.features.trees.GiganticAmaranitaFeature;
import ru.betterend.world.features.trees.HelixTreeFeature;
import ru.betterend.world.features.trees.JellyshroomFeature;
import ru.betterend.world.features.trees.LacugroveFeature;
import ru.betterend.world.features.trees.LucerniaFeature;
import ru.betterend.world.features.trees.MossyGlowshroomFeature;
import ru.betterend.world.features.trees.PythadendronTreeFeature;
import ru.betterend.world.features.trees.TenaneaFeature;
import ru.betterend.world.features.trees.UmbrellaTreeFeature;
import ru.betterend.world.generator.GeneratorOptions;

public class EndFeatures {
	// Trees //
	public static final BCLFeature MOSSY_GLOWSHROOM = redisterVegetation("mossy_glowshroom", new MossyGlowshroomFeature(), 3);
	public static final BCLFeature PYTHADENDRON_TREE = redisterVegetation("pythadendron_tree", new PythadendronTreeFeature(), 2);
	public static final BCLFeature LACUGROVE = redisterVegetation("lacugrove", new LacugroveFeature(), 4);
	public static final BCLFeature DRAGON_TREE = redisterVegetation("dragon_tree", new DragonTreeFeature(), 3);
	public static final BCLFeature TENANEA = redisterVegetation("tenanea", new TenaneaFeature(), 3);
	public static final BCLFeature HELIX_TREE = redisterVegetation("helix_tree", new HelixTreeFeature(), 2);
	public static final BCLFeature UMBRELLA_TREE = redisterVegetation("umbrella_tree", new UmbrellaTreeFeature(), 4);
	public static final BCLFeature JELLYSHROOM = redisterVegetation("jellyshroom", new JellyshroomFeature(), 3);
	public static final BCLFeature GIGANTIC_AMARANITA = redisterVegetation("gigantic_amaranita", new GiganticAmaranitaFeature(), 1);
	public static final BCLFeature LUCERNIA = redisterVegetation("lucernia", new LucerniaFeature(), 3);
	
	// Bushes //
	public static final BCLFeature PYTHADENDRON_BUSH = redisterVegetation("pythadendron_bush", new BushFeature(EndBlocks.PYTHADENDRON_LEAVES, EndBlocks.PYTHADENDRON.bark), 4);
	public static final BCLFeature DRAGON_TREE_BUSH = redisterVegetation("dragon_tree_bush", new BushFeature(EndBlocks.DRAGON_TREE_LEAVES, EndBlocks.DRAGON_TREE.bark), 15);
	public static final BCLFeature TENANEA_BUSH = redisterVegetation("tenanea_bush", new TenaneaBushFeature(), 10);
	public static final BCLFeature LUMECORN = redisterVegetation("lumecorn", new Lumecorn(), 5);
	public static final BCLFeature LARGE_AMARANITA = redisterVegetation("large_amaranita", new LargeAmaranitaFeature(), 5);
	public static final BCLFeature LUCERNIA_BUSH = redisterVegetation("lucernia_bush", new BushWithOuterFeature(EndBlocks.LUCERNIA_LEAVES, EndBlocks.LUCERNIA_OUTER_LEAVES, EndBlocks.LUCERNIA.bark), 10);
	public static final BCLFeature LUCERNIA_BUSH_RARE = redisterVegetation("lucernia_bush_rare", new BushWithOuterFeature(EndBlocks.LUCERNIA_LEAVES, EndBlocks.LUCERNIA_OUTER_LEAVES, EndBlocks.LUCERNIA.bark), 1);
	public static final BCLFeature NEON_CACTUS = redisterVegetation("neon_cactus", new NeonCactusFeature(), 2);
	
	// Plants //
	public static final BCLFeature UMBRELLA_MOSS = redisterVegetation("umbrella_moss", new DoublePlantFeature(EndBlocks.UMBRELLA_MOSS, EndBlocks.UMBRELLA_MOSS_TALL, 5), 5);
	public static final BCLFeature CREEPING_MOSS = redisterVegetation("creeping_moss", new SinglePlantFeature(EndBlocks.CREEPING_MOSS, 5), 5);
	public static final BCLFeature BLUE_VINE = redisterVegetation("blue_vine", new BlueVineFeature(), 1);
	public static final BCLFeature CHORUS_GRASS = redisterVegetation("chorus_grass", new SinglePlantFeature(EndBlocks.CHORUS_GRASS, 4), 5);
	public static final BCLFeature CRYSTAL_GRASS = redisterVegetation("crystal_grass", new SinglePlantFeature(EndBlocks.CRYSTAL_GRASS, 8, false), 5);
	public static final BCLFeature SHADOW_PLANT = redisterVegetation("shadow_plant", new SinglePlantFeature(EndBlocks.SHADOW_PLANT, 6), 9);
	public static final BCLFeature MURKWEED = redisterVegetation("murkweed", new SinglePlantFeature(EndBlocks.MURKWEED, 3), 2);
	public static final BCLFeature NEEDLEGRASS = redisterVegetation("needlegrass", new SinglePlantFeature(EndBlocks.NEEDLEGRASS, 3), 2);
	public static final BCLFeature SHADOW_BERRY = redisterVegetation("shadow_berry", new SinglePlantFeature(EndBlocks.SHADOW_BERRY, 2), 1);
	public static final BCLFeature BUSHY_GRASS = redisterVegetation("bushy_grass", new SinglePlantFeature(EndBlocks.BUSHY_GRASS, 8, false), 20);
	public static final BCLFeature BUSHY_GRASS_WG = redisterVegetation("bushy_grass_wg", new SinglePlantFeature(EndBlocks.BUSHY_GRASS, 5), 10);
	public static final BCLFeature AMBER_GRASS = redisterVegetation("amber_grass", new SinglePlantFeature(EndBlocks.AMBER_GRASS, 6), 9);
	public static final BCLFeature LANCELEAF = redisterVegetation("lanceleaf", new LanceleafFeature(), 3);
	public static final BCLFeature GLOW_PILLAR = redisterVegetation("glow_pillar", new GlowPillarFeature(), 1);
	public static final BCLFeature TWISTED_UMBRELLA_MOSS = redisterVegetation("twisted_umbrella_moss", new DoublePlantFeature(EndBlocks.TWISTED_UMBRELLA_MOSS, EndBlocks.TWISTED_UMBRELLA_MOSS_TALL, 6), 5);
	public static final BCLFeature JUNGLE_GRASS = redisterVegetation("jungle_grass", new SinglePlantFeature(EndBlocks.JUNGLE_GRASS, 7, 3), 8);
	public static final BCLFeature SMALL_JELLYSHROOM_FLOOR = redisterVegetation("small_jellyshroom_floor", new SinglePlantFeature(EndBlocks.SMALL_JELLYSHROOM, 5, 5), 4);
	public static final BCLFeature BLOSSOM_BERRY = redisterVegetation("blossom_berry", new SinglePlantFeature(EndBlocks.BLOSSOM_BERRY, 3, 3), 2);
	public static final BCLFeature BLOOMING_COOKSONIA = redisterVegetation("blooming_cooksonia", new SinglePlantFeature(EndBlocks.BLOOMING_COOKSONIA, 5), 5);
	public static final BCLFeature SALTEAGO = redisterVegetation("salteago", new SinglePlantFeature(EndBlocks.SALTEAGO, 5), 5);
	public static final BCLFeature VAIOLUSH_FERN = redisterVegetation("vaiolush_fern", new SinglePlantFeature(EndBlocks.VAIOLUSH_FERN, 5), 5);
	public static final BCLFeature FRACTURN = redisterVegetation("fracturn", new SinglePlantFeature(EndBlocks.FRACTURN, 5), 5);
	public static final BCLFeature UMBRELLA_MOSS_RARE = redisterVegetation("umbrella_moss_rare", new SinglePlantFeature(EndBlocks.UMBRELLA_MOSS, 3), 2);
	public static final BCLFeature CREEPING_MOSS_RARE = redisterVegetation("creeping_moss_rare", new SinglePlantFeature(EndBlocks.CREEPING_MOSS, 3), 2);
	public static final BCLFeature TWISTED_UMBRELLA_MOSS_RARE = redisterVegetation("twisted_umbrella_moss_rare", new SinglePlantFeature(EndBlocks.TWISTED_UMBRELLA_MOSS, 3), 2);
	public static final BCLFeature ORANGO = redisterVegetation("orango", new SinglePlantFeature(EndBlocks.ORANGO, 5), 6);
	public static final BCLFeature AERIDIUM = redisterVegetation("aeridium", new SinglePlantFeature(EndBlocks.AERIDIUM, 5, 4), 5);
	public static final BCLFeature LUTEBUS = redisterVegetation("lutebus", new SinglePlantFeature(EndBlocks.LUTEBUS, 5, 2), 5);
	public static final BCLFeature LAMELLARIUM = redisterVegetation("lamellarium", new SinglePlantFeature(EndBlocks.LAMELLARIUM, 5), 6);
	public static final BCLFeature SMALL_AMARANITA = redisterVegetation("small_amaranita", new SinglePlantFeature(EndBlocks.SMALL_AMARANITA_MUSHROOM, 5, 5), 4);
	public static final BCLFeature GLOBULAGUS = redisterVegetation("globulagus", new SinglePlantFeature(EndBlocks.GLOBULAGUS, 5, 3), 6);
	public static final BCLFeature CLAWFERN = redisterVegetation("clawfern", new SinglePlantFeature(EndBlocks.CLAWFERN, 5, 4), 5);
	public static final BCLFeature BOLUX_MUSHROOM = redisterVegetation("bolux_mushroom", new SinglePlantFeature(EndBlocks.BOLUX_MUSHROOM, 5, 5), 2);
	public static final BCLFeature CHORUS_MUSHROOM = redisterVegetation("chorus_mushroom", new SinglePlantFeature(EndBlocks.CHORUS_MUSHROOM, 5, 5), 1);
	public static final BCLFeature AMBER_ROOT = redisterVegetation("amber_root", new SinglePlantFeature(EndBlocks.AMBER_ROOT, 5, 5), 1);
	//public static final BCLFeature PEARLBERRY = redisterVegetation("pearlberry", new SinglePlantFeature(EndBlocks.PEARLBERRY, 5, 5), 1);
	
	// Vines //
	public static final BCLFeature DENSE_VINE = redisterVegetation("dense_vine", new VineFeature(EndBlocks.DENSE_VINE, 24), 3);
	public static final BCLFeature TWISTED_VINE = redisterVegetation("twisted_vine", new VineFeature(EndBlocks.TWISTED_VINE, 24), 3);
	public static final BCLFeature BULB_VINE = redisterVegetation("bulb_vine", new VineFeature(EndBlocks.BULB_VINE, 24), 5);
	public static final BCLFeature JUNGLE_VINE = redisterVegetation("jungle_vine", new VineFeature(EndBlocks.JUNGLE_VINE, 24), 5);
	
	// Ceil plants
	public static final BCLFeature SMALL_JELLYSHROOM_CEIL = redisterVegetation("small_jellyshroom_ceil", new SingleInvertedScatterFeature(EndBlocks.SMALL_JELLYSHROOM, 8), 8);
	
	// Wall Plants //
	public static final BCLFeature PURPLE_POLYPORE = redisterVegetation("purple_polypore", new WallPlantOnLogFeature(EndBlocks.PURPLE_POLYPORE, 3), 5);
	public static final BCLFeature AURANT_POLYPORE = redisterVegetation("aurant_polypore", new WallPlantOnLogFeature(EndBlocks.AURANT_POLYPORE, 3), 5);
	public static final BCLFeature TAIL_MOSS = redisterVegetation("tail_moss", new WallPlantFeature(EndBlocks.TAIL_MOSS, 3), 15);
	public static final BCLFeature CYAN_MOSS = redisterVegetation("cyan_moss", new WallPlantFeature(EndBlocks.CYAN_MOSS, 3), 15);
	public static final BCLFeature TAIL_MOSS_WOOD = redisterVegetation("tail_moss_wood", new WallPlantOnLogFeature(EndBlocks.TAIL_MOSS, 4), 25);
	public static final BCLFeature CYAN_MOSS_WOOD = redisterVegetation("cyan_moss_wood", new WallPlantOnLogFeature(EndBlocks.CYAN_MOSS, 4), 25);
	public static final BCLFeature TWISTED_MOSS = redisterVegetation("twisted_moss", new WallPlantFeature(EndBlocks.TWISTED_MOSS, 6), 15);
	public static final BCLFeature TWISTED_MOSS_WOOD = redisterVegetation("twisted_moss_wood", new WallPlantOnLogFeature(EndBlocks.TWISTED_MOSS, 6), 25);
	public static final BCLFeature BULB_MOSS = redisterVegetation("bulb_moss", new WallPlantFeature(EndBlocks.BULB_MOSS, 6), 1);
	public static final BCLFeature BULB_MOSS_WOOD = redisterVegetation("bulb_moss_wood", new WallPlantOnLogFeature(EndBlocks.BULB_MOSS, 6), 15);
	public static final BCLFeature SMALL_JELLYSHROOM_WALL = redisterVegetation("small_jellyshroom_wall", new WallPlantFeature(EndBlocks.SMALL_JELLYSHROOM, 4), 4);
	public static final BCLFeature SMALL_JELLYSHROOM_WOOD = redisterVegetation("small_jellyshroom_wood", new WallPlantOnLogFeature(EndBlocks.SMALL_JELLYSHROOM, 4), 8);
	public static final BCLFeature JUNGLE_FERN_WOOD = redisterVegetation("jungle_fern_wood", new WallPlantOnLogFeature(EndBlocks.JUNGLE_FERN, 3), 12);
	public static final BCLFeature RUSCUS = redisterVegetation("ruscus", new WallPlantFeature(EndBlocks.RUSCUS, 6), 10);
	public static final BCLFeature RUSCUS_WOOD = redisterVegetation("ruscus_wood", new WallPlantOnLogFeature(EndBlocks.RUSCUS, 6), 10);
	
	// Sky plants
	public static final BCLFeature FILALUX = redisterVegetation("filalux", new FilaluxFeature(), 1);
	
	// Water //
	public static final BCLFeature BUBBLE_CORAL = redisterVegetation("bubble_coral", new UnderwaterPlantFeature(EndBlocks.BUBBLE_CORAL, 6), 10);
	public static final BCLFeature BUBBLE_CORAL_RARE = redisterVegetation("bubble_coral_rare", new UnderwaterPlantFeature(EndBlocks.BUBBLE_CORAL, 3), 4);
	public static final BCLFeature END_LILY = redisterVegetation("end_lily", new EndLilyFeature(6), 10);
	public static final BCLFeature END_LILY_RARE = redisterVegetation("end_lily_rare", new EndLilyFeature(3), 4);
	public static final BCLFeature END_LOTUS = redisterVegetation("end_lotus", new EndLotusFeature(7), 5);
	public static final BCLFeature END_LOTUS_LEAF = redisterVegetation("end_lotus_leaf", new EndLotusLeafFeature(20), 25);
	public static final BCLFeature HYDRALUX = redisterVegetation("hydralux", new HydraluxFeature(5), 5);
	public static final BCLFeature POND_ANEMONE = redisterVegetation("pond_anemone", new UnderwaterPlantFeature(EndBlocks.POND_ANEMONE, 6), 10);
	
	public static final BCLFeature CHARNIA_RED = redisterVegetation("charnia_red", new CharniaFeature(EndBlocks.CHARNIA_RED), 10);
	public static final BCLFeature CHARNIA_PURPLE = redisterVegetation("charnia_purple", new CharniaFeature(EndBlocks.CHARNIA_PURPLE), 10);
	public static final BCLFeature CHARNIA_CYAN = redisterVegetation("charnia_cyan", new CharniaFeature(EndBlocks.CHARNIA_CYAN), 10);
	public static final BCLFeature CHARNIA_LIGHT_BLUE = redisterVegetation("charnia_light_blue", new CharniaFeature(EndBlocks.CHARNIA_LIGHT_BLUE), 10);
	public static final BCLFeature CHARNIA_ORANGE = redisterVegetation("charnia_orange", new CharniaFeature(EndBlocks.CHARNIA_ORANGE), 10);
	public static final BCLFeature CHARNIA_GREEN = redisterVegetation("charnia_green", new CharniaFeature(EndBlocks.CHARNIA_GREEN), 10);
	public static final BCLFeature MENGER_SPONGE = redisterVegetation("menger_sponge", new MengerSpongeFeature(5), 1);
	public static final BCLFeature CHARNIA_RED_RARE = redisterVegetation("charnia_red_rare", new CharniaFeature(EndBlocks.CHARNIA_RED), 2);
	public static final BCLFeature BIOME_ISLAND = BCLFeature.makeFeatureConfigured(BetterEnd.makeID("overworld_island"), new BiomeIslandFeature());
	public static final BCLFeature FLAMAEA = redisterVegetation("flamaea", new SinglePlantFeature(EndBlocks.FLAMAEA, 12, false, 5), 20);
	
	// Terrain //
	public static final BCLFeature END_LAKE= registerLake("end_lake", new EndLakeFeature(), 4);
	public static final BCLFeature END_LAKE_NORMAL= registerLake("end_lake_normal", new EndLakeFeature(), 20);
	public static final BCLFeature END_LAKE_RARE= registerLake("end_lake_rare", new EndLakeFeature(), 40);
	public static final BCLFeature DESERT_LAKE= registerLake("desert_lake", new DesertLakeFeature(), 8);
	public static final BCLFeature ROUND_CAVE = registerRawGen("round_cave", new RoundCaveFeature(), 2);
	public static final BCLFeature SPIRE = registerRawGen("spire", new SpireFeature(), 2);
	public static final BCLFeature FLOATING_SPIRE = registerRawGen("floating_spire", new FloatingSpireFeature(), 8);
	public static final BCLFeature GEYSER = registerRawGen("geyser", new GeyserFeature(), 8);
	public static final BCLFeature SULPHURIC_LAKE= registerLake("sulphuric_lake", new SulphuricLakeFeature(), 8);
	public static final BCLFeature SULPHURIC_CAVE = BCLFeature.makeCountRawFeature(BetterEnd.makeID("sulphuric_cave"), new SulphuricCaveFeature(), 2);
	public static final BCLFeature ICE_STAR = registerRawGen("ice_star", new IceStarFeature(5, 15, 10, 25), 15);
	public static final BCLFeature ICE_STAR_SMALL = registerRawGen("ice_star_small", new IceStarFeature(3, 5, 7, 12), 8);
	public static final BCLFeature SURFACE_VENT = registerChanced("surface_vent", new SurfaceVentFeature(), 4);
	public static final BCLFeature SULPHUR_HILL = registerChanced("sulphur_hill", new SulphurHillFeature(), 8);
	public static final BCLFeature OBSIDIAN_PILLAR_BASEMENT = registerChanced("obsidian_pillar_basement", new ObsidianPillarBasementFeature(), 8);
	public static final BCLFeature OBSIDIAN_BOULDER = registerChanced("obsidian_boulder", new ObsidianBoulderFeature(), 10);
	public static final BCLFeature FALLEN_PILLAR = registerChanced("fallen_pillar", new FallenPillarFeature(), 20);
	public static final BCLFeature TUNEL_CAVE = BCLFeature.makeChunkFeature(BetterEnd.makeID("tunel_cave"), new TunelCaveFeature());
	
	// Ores //
	public static final BCLFeature THALLASIUM_ORE = registerOre("thallasium_ore", EndBlocks.THALLASIUM.ore, 12, 6, 0, 16, 128);
	public static final BCLFeature ENDER_ORE = registerOre("ender_ore", EndBlocks.ENDER_ORE, 8, 3, 0, 16, 128);
	public static final BCLFeature AMBER_ORE = registerOre("amber_ore", EndBlocks.AMBER_ORE, 12, 6, 0, 16, 128);
	public static final BCLFeature VIOLECITE_LAYER = registerLayer("violecite_layer", EndBlocks.VIOLECITE, 15, 16, 128, 8);
	public static final BCLFeature FLAVOLITE_LAYER = registerLayer("flavolite_layer", EndBlocks.FLAVOLITE, 12, 16, 128, 6);
	
	// Buildings
	public static final BCLFeature CRASHED_SHIP = registerChanced("crashed_ship", new CrashedShipFeature(), 500);
	
	// Mobs
	public static final BCLFeature SILK_MOTH_NEST = registerChanced("silk_moth_nest", new SilkMothNestFeature(), 2);
	
	// Caves
	public static final DefaultFeature SMARAGDANT_CRYSTAL = new SmaragdantCrystalFeature();
	public static final DefaultFeature SMARAGDANT_CRYSTAL_SHARD = new SingleBlockFeature(EndBlocks.SMARAGDANT_CRYSTAL_SHARD);
	public static final DefaultFeature BIG_AURORA_CRYSTAL = new BigAuroraCrystalFeature();
	public static final DefaultFeature CAVE_BUSH = new BushFeature(EndBlocks.CAVE_BUSH, EndBlocks.CAVE_BUSH);
	public static final DefaultFeature CAVE_GRASS = new SingleBlockFeature(EndBlocks.CAVE_GRASS);
	public static final DefaultFeature RUBINEA = new VineFeature(EndBlocks.RUBINEA, 8);
	public static final DefaultFeature MAGNULA = new VineFeature(EndBlocks.MAGNULA, 8);
	public static final DefaultFeature END_STONE_STALACTITE = new StalactiteFeature(true, EndBlocks.END_STONE_STALACTITE, Blocks.END_STONE);
	public static final DefaultFeature END_STONE_STALAGMITE = new StalactiteFeature(false, EndBlocks.END_STONE_STALACTITE, Blocks.END_STONE);
	public static final DefaultFeature END_STONE_STALACTITE_CAVEMOSS = new StalactiteFeature(true, EndBlocks.END_STONE_STALACTITE_CAVEMOSS, Blocks.END_STONE, EndBlocks.CAVE_MOSS);
	public static final DefaultFeature END_STONE_STALAGMITE_CAVEMOSS = new StalactiteFeature(false, EndBlocks.END_STONE_STALACTITE_CAVEMOSS, EndBlocks.CAVE_MOSS);
	public static final DefaultFeature CAVE_PUMPKIN = new CavePumpkinFeature();
	
	private static BCLFeature redisterVegetation(String name, Feature<NoneFeatureConfiguration> feature, int density) {
		return BCLFeature.makeVegetationFeature(BetterEnd.makeID(name), feature, density);
	}
	
	private static BCLFeature registerRawGen(String name, Feature<NoneFeatureConfiguration> feature, int chance) {
		return BCLFeature.makeRawGenFeature(BetterEnd.makeID(name), feature, chance);
	}
	
	private static BCLFeature registerLake(String name, Feature<NoneFeatureConfiguration> feature, int chance) {
		return BCLFeature.makeLakeFeature(BetterEnd.makeID(name), feature, chance);
	}
	
	private static BCLFeature registerChanced(String name, Feature<NoneFeatureConfiguration> feature, int chance) {
		return BCLFeature.makeChansedFeature(BetterEnd.makeID(name), feature, chance);
	}
	
	private static BCLFeature registerOre(String name, Block blockOre, int veins, int veinSize, int offset, int minY, int maxY) {
		return BCLFeature.makeOreFeature(BetterEnd.makeID(name), blockOre, veins, veinSize, offset, minY, maxY);
	}
	
	private static BCLFeature registerLayer(String name, Block block, float radius, int minY, int maxY, int count) {
		OreLayerFeature layer = new OreLayerFeature(block.defaultBlockState(), radius, minY, maxY);
		ConfiguredFeature<?, ?> configured = layer.configured(FeatureConfiguration.NONE).decorated(FeatureDecorator.COUNT.configured(new CountConfiguration(count)));
		return new BCLFeature(BetterEnd.makeID(name), layer, GenerationStep.Decoration.UNDERGROUND_ORES, configured);
	}
	
	private static BCLFeature registerLayer(String name, StoneMaterial material, float radius, int minY, int maxY, int count) {
		return registerLayer(name, material.stone, radius, minY, maxY, count);
	}
	
	public static void registerBiomeFeatures(ResourceLocation id, Biome biome, List<List<Supplier<ConfiguredFeature<?, ?>>>> features) {
		if (id.getNamespace().equals(BetterEnd.MOD_ID)) {
			return;
		}
		
		if (GeneratorOptions.removeChorusFromVanillaBiomes()) {
			if (id.getNamespace().equals("minecraft")) {
				String path = id.getPath();
				if (path.equals("end_highlands") || path.equals("end_midlands") || path.equals("small_end_islands")) {
					int pos = GenerationStep.Decoration.VEGETAL_DECORATION.ordinal();
					if (pos < features.size()) {
						List<Supplier<ConfiguredFeature<?, ?>>> list = features.get(pos);
						// If only chorus plants are enabled
						if (list.size() == 1) {
							features.get(pos).clear();
						}
					}
				}
			}
		}
		
		addFeature(FLAVOLITE_LAYER, features);
		addFeature(THALLASIUM_ORE, features);
		addFeature(ENDER_ORE, features);
		addFeature(CRASHED_SHIP, features);
		
		BCLBiome bclbiome = BiomeAPI.getBiome(id);
		if (bclbiome instanceof EndBiome) {
			EndBiome endBiome = (EndBiome) bclbiome;
			if (endBiome.hasCaves() && !EndBiomes.VOID_BIOMES.containsImmutable(id)) {
				addFeature(ROUND_CAVE, features);
				addFeature(TUNEL_CAVE, features);
			}
		}
		
		BCLFeature feature = BiomeAPI.getBiome(id).getStructuresFeature();
		if (feature != null) {
			addFeature(feature, features);
		}
	}
	
	public static void addDefaultFeatures(BiomeDefinition def) {
		def.addFeature(FLAVOLITE_LAYER);
		def.addFeature(THALLASIUM_ORE);
		def.addFeature(ENDER_ORE);
		def.addFeature(CRASHED_SHIP);
		
		if (def.hasCaves()) {
			def.addFeature(ROUND_CAVE);
			def.addFeature(TUNEL_CAVE);
		}
	}
	
	private static void addFeature(BCLFeature feature, List<List<Supplier<ConfiguredFeature<?, ?>>>> features) {
		int index = feature.getFeatureStep().ordinal();
		if (features.size() > index) {
			features.get(index).add(() -> {
				return feature.getFeatureConfigured();
			});
		}
		else {
			List<Supplier<ConfiguredFeature<?, ?>>> newFeature = Lists.newArrayList();
			newFeature.add(() -> {
				return feature.getFeatureConfigured();
			});
			features.add(newFeature);
		}
	}
	
	public static void register() {}
}
