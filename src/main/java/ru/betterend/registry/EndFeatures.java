package ru.betterend.registry;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.Lists;

import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import ru.betterend.BetterEnd;
import ru.betterend.world.biome.land.BiomeDefinition;
import ru.betterend.world.biome.land.EndBiome;
import ru.betterend.world.features.BlueVineFeature;
import ru.betterend.world.features.CharniaFeature;
import ru.betterend.world.features.CrashedShipFeature;
import ru.betterend.world.features.DefaultFeature;
import ru.betterend.world.features.DoublePlantFeature;
import ru.betterend.world.features.EndFeature;
import ru.betterend.world.features.EndLilyFeature;
import ru.betterend.world.features.EndLotusFeature;
import ru.betterend.world.features.EndLotusLeafFeature;
import ru.betterend.world.features.GlowPillarFeature;
import ru.betterend.world.features.HydraluxFeature;
import ru.betterend.world.features.LanceleafFeature;
import ru.betterend.world.features.MengerSpongeFeature;
import ru.betterend.world.features.OverworldIslandFeature;
import ru.betterend.world.features.SilkMothNestFeature;
import ru.betterend.world.features.SingleInvertedScatterFeature;
import ru.betterend.world.features.SinglePlantFeature;
import ru.betterend.world.features.UnderwaterPlantFeature;
import ru.betterend.world.features.VineFeature;
import ru.betterend.world.features.WallPlantFeature;
import ru.betterend.world.features.WallPlantOnLogFeature;
import ru.betterend.world.features.bushes.BushFeature;
import ru.betterend.world.features.bushes.LargeAmaranitaFeature;
import ru.betterend.world.features.bushes.Lumecorn;
import ru.betterend.world.features.bushes.TenaneaBushFeature;
import ru.betterend.world.features.terrain.BigAuroraCrystalFeature;
import ru.betterend.world.features.terrain.EndLakeFeature;
import ru.betterend.world.features.terrain.FallenPillarFeature;
import ru.betterend.world.features.terrain.FloatingSpireFeature;
import ru.betterend.world.features.terrain.GeyserFeature;
import ru.betterend.world.features.terrain.IceStarFeature;
import ru.betterend.world.features.terrain.ObsidianBoulderFeature;
import ru.betterend.world.features.terrain.ObsidianPillarBasementFeature;
import ru.betterend.world.features.terrain.SmaragdantCrystalFeature;
import ru.betterend.world.features.terrain.SmaragdantCrystalShardFeature;
import ru.betterend.world.features.terrain.SpireFeature;
import ru.betterend.world.features.terrain.SulphurHillFeature;
import ru.betterend.world.features.terrain.SulphuricCaveFeature;
import ru.betterend.world.features.terrain.SulphuricLakeFeature;
import ru.betterend.world.features.terrain.SurfaceVentFeature;
import ru.betterend.world.features.terrain.caves.RoundCaveFeature;
import ru.betterend.world.features.trees.DragonTreeFeature;
import ru.betterend.world.features.trees.GiganticAmaranitaFeature;
import ru.betterend.world.features.trees.HelixTreeFeature;
import ru.betterend.world.features.trees.JellyshroomFeature;
import ru.betterend.world.features.trees.LacugroveFeature;
import ru.betterend.world.features.trees.MossyGlowshroomFeature;
import ru.betterend.world.features.trees.PythadendronTreeFeature;
import ru.betterend.world.features.trees.TenaneaFeature;
import ru.betterend.world.features.trees.UmbrellaTreeFeature;
import ru.betterend.world.generator.GeneratorOptions;

public class EndFeatures {
	// Trees //
	public static final EndFeature MOSSY_GLOWSHROOM = new EndFeature("mossy_glowshroom", new MossyGlowshroomFeature(), 3);
	public static final EndFeature PYTHADENDRON_TREE = new EndFeature("pythadendron_tree", new PythadendronTreeFeature(), 2);
	public static final EndFeature LACUGROVE = new EndFeature("lacugrove", new LacugroveFeature(), 4);
	public static final EndFeature DRAGON_TREE = new EndFeature("dragon_tree", new DragonTreeFeature(), 3);
	public static final EndFeature TENANEA = new EndFeature("tenanea", new TenaneaFeature(), 3);
	public static final EndFeature HELIX_TREE = new EndFeature("helix_tree", new HelixTreeFeature(), 2);
	public static final EndFeature UMBRELLA_TREE = new EndFeature("umbrella_tree", new UmbrellaTreeFeature(), 4);
	public static final EndFeature JELLYSHROOM = new EndFeature("jellyshroom", new JellyshroomFeature(), 3);
	public static final EndFeature GIGANTIC_AMARANITA = new EndFeature("gigantic_amaranita", new GiganticAmaranitaFeature(), 1);
	
	// Bushes //
	public static final EndFeature PYTHADENDRON_BUSH = new EndFeature("pythadendron_bush", new BushFeature(EndBlocks.PYTHADENDRON_LEAVES, EndBlocks.PYTHADENDRON.bark), 4);
	public static final EndFeature DRAGON_TREE_BUSH = new EndFeature("dragon_tree_bush", new BushFeature(EndBlocks.DRAGON_TREE_LEAVES, EndBlocks.DRAGON_TREE.bark), 15);
	public static final EndFeature TENANEA_BUSH = new EndFeature("tenanea_bush", new TenaneaBushFeature(), 10);
	public static final EndFeature LUMECORN = new EndFeature("lumecorn", new Lumecorn(), 5);
	public static final EndFeature LARGE_AMARANITA = new EndFeature("large_amaranita", new LargeAmaranitaFeature(), 5);
	
	// Plants //
	public static final EndFeature UMBRELLA_MOSS = new EndFeature("umbrella_moss", new DoublePlantFeature(EndBlocks.UMBRELLA_MOSS, EndBlocks.UMBRELLA_MOSS_TALL, 5), 5);
	public static final EndFeature CREEPING_MOSS = new EndFeature("creeping_moss", new SinglePlantFeature(EndBlocks.CREEPING_MOSS, 5), 5);
	public static final EndFeature BLUE_VINE = new EndFeature("blue_vine", new BlueVineFeature(), 1);
	public static final EndFeature CHORUS_GRASS = new EndFeature("chorus_grass", new SinglePlantFeature(EndBlocks.CHORUS_GRASS, 4), 5);
	public static final EndFeature CRYSTAL_GRASS = new EndFeature("crystal_grass", new SinglePlantFeature(EndBlocks.CRYSTAL_GRASS, 8, false), 5);
	public static final EndFeature SHADOW_PLANT = new EndFeature("shadow_plant", new SinglePlantFeature(EndBlocks.SHADOW_PLANT, 6), 9);
	public static final EndFeature MURKWEED = new EndFeature("murkweed", new SinglePlantFeature(EndBlocks.MURKWEED, 3), 2);
	public static final EndFeature NEEDLEGRASS = new EndFeature("needlegrass", new SinglePlantFeature(EndBlocks.NEEDLEGRASS, 3), 2);
	public static final EndFeature SHADOW_BERRY = new EndFeature("shadow_berry", new SinglePlantFeature(EndBlocks.SHADOW_BERRY, 2), 1);
	public static final EndFeature BUSHY_GRASS = new EndFeature("bushy_grass", new SinglePlantFeature(EndBlocks.BUSHY_GRASS, 8, false), 20);
	public static final EndFeature BUSHY_GRASS_WG = new EndFeature("bushy_grass_wg", new SinglePlantFeature(EndBlocks.BUSHY_GRASS, 5), 10);
	public static final EndFeature AMBER_GRASS = new EndFeature("amber_grass", new SinglePlantFeature(EndBlocks.AMBER_GRASS, 6), 9);
	public static final EndFeature LANCELEAF = new EndFeature("lanceleaf", new LanceleafFeature(), 3);
	public static final EndFeature GLOW_PILLAR = new EndFeature("glow_pillar", new GlowPillarFeature(), 1);
	public static final EndFeature TWISTED_UMBRELLA_MOSS = new EndFeature("twisted_umbrella_moss", new DoublePlantFeature(EndBlocks.TWISTED_UMBRELLA_MOSS, EndBlocks.TWISTED_UMBRELLA_MOSS_TALL, 6), 5);
	public static final EndFeature JUNGLE_GRASS = new EndFeature("jungle_grass", new SinglePlantFeature(EndBlocks.JUNGLE_GRASS, 7, 3), 8);
	public static final EndFeature SMALL_JELLYSHROOM_FLOOR = new EndFeature("small_jellyshroom_floor", new SinglePlantFeature(EndBlocks.SMALL_JELLYSHROOM, 5, 5), 4);
	public static final EndFeature BLOSSOM_BERRY = new EndFeature("blossom_berry", new SinglePlantFeature(EndBlocks.BLOSSOM_BERRY, 3, 3), 2);
	public static final EndFeature BLOOMING_COOKSONIA = new EndFeature("blooming_cooksonia", new SinglePlantFeature(EndBlocks.BLOOMING_COOKSONIA, 5), 5);
	public static final EndFeature SALTEAGO = new EndFeature("salteago", new SinglePlantFeature(EndBlocks.SALTEAGO, 5), 5);
	public static final EndFeature VAIOLUSH_FERN = new EndFeature("vaiolush_fern", new SinglePlantFeature(EndBlocks.VAIOLUSH_FERN, 5), 5);
	public static final EndFeature FRACTURN = new EndFeature("fracturn", new SinglePlantFeature(EndBlocks.FRACTURN, 5), 5);
	public static final EndFeature UMBRELLA_MOSS_RARE = new EndFeature("umbrella_moss_rare", new SinglePlantFeature(EndBlocks.UMBRELLA_MOSS, 3), 2);
	public static final EndFeature CREEPING_MOSS_RARE = new EndFeature("creeping_moss_rare", new SinglePlantFeature(EndBlocks.CREEPING_MOSS, 3), 2);
	public static final EndFeature TWISTED_UMBRELLA_MOSS_RARE = new EndFeature("twisted_umbrella_moss_rare", new SinglePlantFeature(EndBlocks.TWISTED_UMBRELLA_MOSS, 3), 2);
	public static final EndFeature ORANGO = new EndFeature("orango", new SinglePlantFeature(EndBlocks.ORANGO, 5), 6);
	public static final EndFeature AERIDIUM = new EndFeature("aeridium", new SinglePlantFeature(EndBlocks.AERIDIUM, 5, 4), 5);
	public static final EndFeature LUTEBUS = new EndFeature("lutebus", new SinglePlantFeature(EndBlocks.LUTEBUS, 5, 2), 5);
	public static final EndFeature LAMELLARIUM = new EndFeature("lamellarium", new SinglePlantFeature(EndBlocks.LAMELLARIUM, 5), 6);
	public static final EndFeature SMALL_AMARANITA = new EndFeature("small_amaranita", new SinglePlantFeature(EndBlocks.SMALL_AMARANITA_MUSHROOM, 5, 5), 4);
	public static final EndFeature GLOBULAGUS = new EndFeature("globulagus", new SinglePlantFeature(EndBlocks.GLOBULAGUS, 5, 3), 6);
	public static final EndFeature CLAWFERN = new EndFeature("clawfern", new SinglePlantFeature(EndBlocks.CLAWFERN, 5, 4), 5);
	
	// Vines //
	public static final EndFeature DENSE_VINE = new EndFeature("dense_vine", new VineFeature(EndBlocks.DENSE_VINE, 24), 3);
	public static final EndFeature TWISTED_VINE = new EndFeature("twisted_vine", new VineFeature(EndBlocks.TWISTED_VINE, 24), 3);
	public static final EndFeature BULB_VINE = new EndFeature("bulb_vine", new VineFeature(EndBlocks.BULB_VINE, 24), 5);
	public static final EndFeature JUNGLE_VINE = new EndFeature("jungle_vine", new VineFeature(EndBlocks.JUNGLE_VINE, 24), 5);
	
	// Ceil plants
	public static final EndFeature SMALL_JELLYSHROOM_CEIL = new EndFeature("small_jellyshroom_ceil", new SingleInvertedScatterFeature(EndBlocks.SMALL_JELLYSHROOM, 8), 8);
	
	// Wall Plants //
	public static final EndFeature PURPLE_POLYPORE = new EndFeature("purple_polypore", new WallPlantOnLogFeature(EndBlocks.PURPLE_POLYPORE, 3), 5);
	public static final EndFeature TAIL_MOSS = new EndFeature("tail_moss", new WallPlantFeature(EndBlocks.TAIL_MOSS, 3), 15);
	public static final EndFeature CYAN_MOSS = new EndFeature("cyan_moss", new WallPlantFeature(EndBlocks.CYAN_MOSS, 3), 15);
	public static final EndFeature TAIL_MOSS_WOOD = new EndFeature("tail_moss_wood", new WallPlantOnLogFeature(EndBlocks.TAIL_MOSS, 4), 25);
	public static final EndFeature CYAN_MOSS_WOOD = new EndFeature("cyan_moss_wood", new WallPlantOnLogFeature(EndBlocks.CYAN_MOSS, 4), 25);
	public static final EndFeature TWISTED_MOSS = new EndFeature("twisted_moss", new WallPlantFeature(EndBlocks.TWISTED_MOSS, 6), 15);
	public static final EndFeature TWISTED_MOSS_WOOD = new EndFeature("twisted_moss_wood", new WallPlantOnLogFeature(EndBlocks.TWISTED_MOSS, 6), 25);
	public static final EndFeature BULB_MOSS = new EndFeature("bulb_moss", new WallPlantFeature(EndBlocks.BULB_MOSS, 6), 1);
	public static final EndFeature BULB_MOSS_WOOD = new EndFeature("bulb_moss_wood", new WallPlantOnLogFeature(EndBlocks.BULB_MOSS, 6), 15);
	public static final EndFeature SMALL_JELLYSHROOM_WALL = new EndFeature("small_jellyshroom_wall", new WallPlantFeature(EndBlocks.SMALL_JELLYSHROOM, 4), 4);
	public static final EndFeature SMALL_JELLYSHROOM_WOOD = new EndFeature("small_jellyshroom_wood", new WallPlantOnLogFeature(EndBlocks.SMALL_JELLYSHROOM, 4), 8);
	public static final EndFeature JUNGLE_FERN_WOOD = new EndFeature("jungle_fern_wood", new WallPlantOnLogFeature(EndBlocks.JUNGLE_FERN, 3), 12);
	
	// Water //
	public static final EndFeature BUBBLE_CORAL = new EndFeature("bubble_coral", new UnderwaterPlantFeature(EndBlocks.BUBBLE_CORAL, 6), 10);
	public static final EndFeature BUBBLE_CORAL_RARE = new EndFeature("bubble_coral_rare", new UnderwaterPlantFeature(EndBlocks.BUBBLE_CORAL, 3), 4);
	public static final EndFeature END_LILY = new EndFeature("end_lily", new EndLilyFeature(6), 10);
	public static final EndFeature END_LILY_RARE = new EndFeature("end_lily_rare", new EndLilyFeature(3), 4);
	public static final EndFeature END_LOTUS = new EndFeature("end_lotus", new EndLotusFeature(7), 5);
	public static final EndFeature END_LOTUS_LEAF = new EndFeature("end_lotus_leaf", new EndLotusLeafFeature(20), 25);
	public static final EndFeature HYDRALUX = new EndFeature("hydralux", new HydraluxFeature(5), 5);
	
	public static final EndFeature CHARNIA_RED = new EndFeature("charnia_red", new CharniaFeature(EndBlocks.CHARNIA_RED), 10);
	public static final EndFeature CHARNIA_PURPLE = new EndFeature("charnia_purple", new CharniaFeature(EndBlocks.CHARNIA_PURPLE), 10);
	public static final EndFeature CHARNIA_CYAN = new EndFeature("charnia_cyan", new CharniaFeature(EndBlocks.CHARNIA_CYAN), 10);
	public static final EndFeature CHARNIA_LIGHT_BLUE = new EndFeature("charnia_light_blue", new CharniaFeature(EndBlocks.CHARNIA_LIGHT_BLUE), 10);
	public static final EndFeature CHARNIA_ORANGE = new EndFeature("charnia_orange", new CharniaFeature(EndBlocks.CHARNIA_ORANGE), 10);
	public static final EndFeature CHARNIA_GREEN = new EndFeature("charnia_green", new CharniaFeature(EndBlocks.CHARNIA_GREEN), 10);
	public static final EndFeature MENGER_SPONGE = new EndFeature("menger_sponge", new MengerSpongeFeature(5), 1);
	public static final EndFeature CHARNIA_RED_RARE = new EndFeature("charnia_red_rare", new CharniaFeature(EndBlocks.CHARNIA_RED), 2);
	public static final EndFeature OVERWORLD_ISLAND = EndFeature.makeFetureConfigured("overworld_island", new OverworldIslandFeature());
	
	// Terrain //
	public static final EndFeature END_LAKE = EndFeature.makeLakeFeature("end_lake", new EndLakeFeature(), 4);
	public static final EndFeature END_LAKE_RARE = EndFeature.makeLakeFeature("end_lake_rare", new EndLakeFeature(), 40);
	public static final EndFeature ROUND_CAVE = EndFeature.makeRawGenFeature("round_cave", new RoundCaveFeature(), 2);
	public static final EndFeature SPIRE = EndFeature.makeRawGenFeature("spire", new SpireFeature(), 2);
	public static final EndFeature FLOATING_SPIRE = EndFeature.makeRawGenFeature("floating_spire", new FloatingSpireFeature(), 8);
	public static final EndFeature GEYSER = EndFeature.makeRawGenFeature("geyser", new GeyserFeature(), 8);
	public static final EndFeature SULPHURIC_LAKE = EndFeature.makeLakeFeature("sulphuric_lake", new SulphuricLakeFeature(), 8);
	public static final EndFeature SULPHURIC_CAVE = EndFeature.makeCountRawFeature("sulphuric_cave", new SulphuricCaveFeature(), 2);
	public static final EndFeature ICE_STAR = EndFeature.makeRawGenFeature("ice_star", new IceStarFeature(5, 15, 10, 25), 15);
	public static final EndFeature ICE_STAR_SMALL = EndFeature.makeRawGenFeature("ice_star_small", new IceStarFeature(3, 5, 7, 12), 8);
	public static final EndFeature SURFACE_VENT = EndFeature.makeChansedFeature("surface_vent", new SurfaceVentFeature(), 4);
	public static final EndFeature SULPHUR_HILL = EndFeature.makeChansedFeature("sulphur_hill", new SulphurHillFeature(), 8);
	public static final EndFeature OBSIDIAN_PILLAR_BASEMENT = EndFeature.makeChansedFeature("obsidian_pillar_basement", new ObsidianPillarBasementFeature(), 8);
	public static final EndFeature OBSIDIAN_BOULDER = EndFeature.makeChansedFeature("obsidian_boulder", new ObsidianBoulderFeature(), 10);
	public static final EndFeature FALLEN_PILLAR = EndFeature.makeChansedFeature("fallen_pillar", new FallenPillarFeature(), 20);
	
	// Ores //
	public static final EndFeature THALLASIUM_ORE = EndFeature.makeOreFeature("thallasium_ore", EndBlocks.THALLASIUM.ore, 12, 6, 0, 16, 128);
	public static final EndFeature ENDER_ORE = EndFeature.makeOreFeature("ender_ore", EndBlocks.ENDER_ORE, 8, 3, 0, 16, 128);
	public static final EndFeature AMBER_ORE = EndFeature.makeOreFeature("amber_ore", EndBlocks.AMBER_ORE, 12, 6, 0, 16, 128);
	public static final EndFeature VIOLECITE_LAYER = EndFeature.makeLayerFeature("violecite_layer", EndBlocks.VIOLECITE, 15, 16, 128, 8);
	public static final EndFeature FLAVOLITE_LAYER = EndFeature.makeLayerFeature("flavolite_layer", EndBlocks.FLAVOLITE, 12, 16, 128, 6);
	
	// Buildings
	public static final EndFeature CRASHED_SHIP = EndFeature.makeChansedFeature("crashed_ship", new CrashedShipFeature(), 500);
	
	// Mobs
	public static final EndFeature SILK_MOTH_NEST = EndFeature.makeChansedFeature("silk_moth_nest", new SilkMothNestFeature(), 2);
	
	// Caves
	public static final DefaultFeature SMARAGDANT_CRYSTAL = new SmaragdantCrystalFeature();
	public static final DefaultFeature SMARAGDANT_CRYSTAL_SHARD = new SmaragdantCrystalShardFeature();
	public static final DefaultFeature BIG_AURORA_CRYSTAL = new BigAuroraCrystalFeature();
	
	public static void registerBiomeFeatures(Identifier id, Biome biome, List<List<Supplier<ConfiguredFeature<?, ?>>>> features) {
		if (id.getNamespace().equals(BetterEnd.MOD_ID)) {
			return;
		}
		
		if (GeneratorOptions.removeChorusFromVanillaBiomes()) {
			if (id.getNamespace().equals("minecraft")) {
				String path = id.getPath();
				if (path.equals("end_highlands") || path.equals("end_midlands") || path.equals("small_end_islands")) {
					int pos = GenerationStep.Feature.VEGETAL_DECORATION.ordinal();
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
		
		if (EndBiomes.getBiome(id).hasCaves()) {
			addFeature(ROUND_CAVE, features);
		}
		
		EndBiome endBiome = EndBiomes.getBiome(id);
		EndFeature feature = endBiome.getStructuresFeature();
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
		}
	}
	
	private static void addFeature(EndFeature feature, List<List<Supplier<ConfiguredFeature<?, ?>>>> features) {
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
