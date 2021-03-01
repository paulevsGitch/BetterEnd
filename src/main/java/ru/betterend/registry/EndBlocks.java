package ru.betterend.registry;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.AeterniumAnvil;
import ru.betterend.blocks.AeterniumBlock;
import ru.betterend.blocks.AmaranitaCapBlock;
import ru.betterend.blocks.AmaranitaHymenophoreBlock;
import ru.betterend.blocks.AmaranitaHyphaeBlock;
import ru.betterend.blocks.AmberBlock;
import ru.betterend.blocks.AncientEmeraldIceBlock;
import ru.betterend.blocks.AuroraCrystalBlock;
import ru.betterend.blocks.BlueVineBlock;
import ru.betterend.blocks.BlueVineLanternBlock;
import ru.betterend.blocks.BlueVineSeedBlock;
import ru.betterend.blocks.BrimstoneBlock;
import ru.betterend.blocks.BubbleCoralBlock;
import ru.betterend.blocks.BulbVineBlock;
import ru.betterend.blocks.BulbVineLanternBlock;
import ru.betterend.blocks.BulbVineLanternColoredBlock;
import ru.betterend.blocks.BulbVineSeedBlock;
import ru.betterend.blocks.ChandelierBlock;
import ru.betterend.blocks.CharcoalBlock;
import ru.betterend.blocks.CharniaBlock;
import ru.betterend.blocks.ChorusGrassBlock;
import ru.betterend.blocks.DenseEmeraldIceBlock;
import ru.betterend.blocks.DenseSnowBlock;
import ru.betterend.blocks.DragonTreeSaplingBlock;
import ru.betterend.blocks.EmeraldIceBlock;
import ru.betterend.blocks.EndLilyBlock;
import ru.betterend.blocks.EndLilySeedBlock;
import ru.betterend.blocks.EndLotusFlowerBlock;
import ru.betterend.blocks.EndLotusLeafBlock;
import ru.betterend.blocks.EndLotusSeedBlock;
import ru.betterend.blocks.EndLotusStemBlock;
import ru.betterend.blocks.EndPathBlock;
import ru.betterend.blocks.EndPortalBlock;
import ru.betterend.blocks.EndStoneSmelter;
import ru.betterend.blocks.EndTerrainBlock;
import ru.betterend.blocks.EnderBlock;
import ru.betterend.blocks.EndstoneDustBlock;
import ru.betterend.blocks.EternalPedestal;
import ru.betterend.blocks.EternalRunedFlavolite;
import ru.betterend.blocks.GlowingMossBlock;
import ru.betterend.blocks.GlowingPillarLuminophorBlock;
import ru.betterend.blocks.GlowingPillarRootsBlock;
import ru.betterend.blocks.GlowingPillarSeedBlock;
import ru.betterend.blocks.HelixTreeLeavesBlock;
import ru.betterend.blocks.HelixTreeSaplingBlock;
import ru.betterend.blocks.HydraluxBlock;
import ru.betterend.blocks.HydraluxPetalBlock;
import ru.betterend.blocks.HydraluxPetalColoredBlock;
import ru.betterend.blocks.HydraluxSaplingBlock;
import ru.betterend.blocks.HydrothermalVentBlock;
import ru.betterend.blocks.InfusionPedestal;
import ru.betterend.blocks.JellyshroomCapBlock;
import ru.betterend.blocks.LacugroveSaplingBlock;
import ru.betterend.blocks.LanceleafBlock;
import ru.betterend.blocks.LanceleafSeedBlock;
import ru.betterend.blocks.LargeAmaranitaBlock;
import ru.betterend.blocks.LumecornBlock;
import ru.betterend.blocks.LumecornSeedBlock;
import ru.betterend.blocks.MengerSpongeBlock;
import ru.betterend.blocks.MengerSpongeWetBlock;
import ru.betterend.blocks.MissingTileBlock;
import ru.betterend.blocks.MossyBoneBlock;
import ru.betterend.blocks.MossyGlowshroomCapBlock;
import ru.betterend.blocks.MossyGlowshroomHymenophoreBlock;
import ru.betterend.blocks.MossyGlowshroomSaplingBlock;
import ru.betterend.blocks.MossyObsidian;
import ru.betterend.blocks.MurkweedBlock;
import ru.betterend.blocks.NeedlegrassBlock;
import ru.betterend.blocks.PedestalVanilla;
import ru.betterend.blocks.PythadendronSaplingBlock;
import ru.betterend.blocks.RespawnObeliskBlock;
import ru.betterend.blocks.RunedFlavolite;
import ru.betterend.blocks.ShadowBerryBlock;
import ru.betterend.blocks.ShadowGrassBlock;
import ru.betterend.blocks.SilkMothNestBlock;
import ru.betterend.blocks.SmallAmaranitaBlock;
import ru.betterend.blocks.SmallJellyshroomBlock;
import ru.betterend.blocks.SulphurCrystalBlock;
import ru.betterend.blocks.TenaneaFlowersBlock;
import ru.betterend.blocks.TenaneaSaplingBlock;
import ru.betterend.blocks.TerrainPlantBlock;
import ru.betterend.blocks.TwistedUmbrellaMossBlock;
import ru.betterend.blocks.TwistedUmbrellaMossTallBlock;
import ru.betterend.blocks.UmbrellaMossBlock;
import ru.betterend.blocks.UmbrellaMossTallBlock;
import ru.betterend.blocks.UmbrellaTreeClusterBlock;
import ru.betterend.blocks.UmbrellaTreeClusterEmptyBlock;
import ru.betterend.blocks.UmbrellaTreeMembraneBlock;
import ru.betterend.blocks.UmbrellaTreeSaplingBlock;
import ru.betterend.blocks.VentBubbleColumnBlock;
import ru.betterend.blocks.basis.EndCropBlock;
import ru.betterend.blocks.basis.EndFurnaceBlock;
import ru.betterend.blocks.basis.EndLeavesBlock;
import ru.betterend.blocks.basis.EndOreBlock;
import ru.betterend.blocks.basis.EndUnderwaterWallPlantBlock;
import ru.betterend.blocks.basis.EndWallPlantBlock;
import ru.betterend.blocks.basis.FurBlock;
import ru.betterend.blocks.basis.SimpleLeavesBlock;
import ru.betterend.blocks.basis.StoneLanternBlock;
import ru.betterend.blocks.basis.VineBlock;
import ru.betterend.blocks.basis.WallMushroomBlock;
import ru.betterend.blocks.complex.ColoredMaterial;
import ru.betterend.blocks.complex.MetalMaterial;
import ru.betterend.blocks.complex.StoneMaterial;
import ru.betterend.blocks.complex.WoodenMaterial;
import ru.betterend.config.Configs;
import ru.betterend.item.material.EndArmorMaterial;
import ru.betterend.item.material.EndToolMaterial;

public class EndBlocks {
	// Terrain //
	public static final Block ENDSTONE_DUST = registerBlock("endstone_dust", new EndstoneDustBlock());
	public static final Block END_MYCELIUM = registerBlock("end_mycelium", new EndTerrainBlock(MaterialColor.LIGHT_BLUE));
	public static final Block END_MOSS = registerBlock("end_moss", new EndTerrainBlock(MaterialColor.CYAN));
	public static final Block CHORUS_NYLIUM = registerBlock("chorus_nylium", new EndTerrainBlock(MaterialColor.MAGENTA));
	public static final Block CAVE_MOSS = registerBlock("cave_moss", new EndTerrainBlock(MaterialColor.PURPLE));
	public static final Block CRYSTAL_MOSS = registerBlock("crystal_moss", new EndTerrainBlock(MaterialColor.PINK));
	public static final Block SHADOW_GRASS = registerBlock("shadow_grass", new ShadowGrassBlock());
	public static final Block PINK_MOSS = registerBlock("pink_moss", new EndTerrainBlock(MaterialColor.PINK));
	public static final Block AMBER_MOSS = registerBlock("amber_moss", new EndTerrainBlock(MaterialColor.ORANGE));
	public static final Block JUNGLE_MOSS = registerBlock("jungle_moss", new EndTerrainBlock(MaterialColor.GREEN));
	public static final Block SANGNUM = registerBlock("sangnum", new EndTerrainBlock(MaterialColor.RED));
	public static final Block RUTISCUS = registerBlock("rutiscus", new EndTerrainBlock(MaterialColor.ORANGE));
	
	// Roads //
	public static final Block END_MYCELIUM_PATH = registerBlock("end_mycelium_path", new EndPathBlock(END_MYCELIUM));
	public static final Block END_MOSS_PATH = registerBlock("end_moss_path", new EndPathBlock(END_MOSS));
	public static final Block CHORUS_NYLIUM_PATH = registerBlock("chorus_nylium_path", new EndPathBlock(CHORUS_NYLIUM));
	public static final Block CAVE_MOSS_PATH = registerBlock("cave_moss_path", new EndPathBlock(CAVE_MOSS));
	public static final Block CRYSTAL_MOSS_PATH = registerBlock("crystal_moss_path", new EndPathBlock(CRYSTAL_MOSS));
	public static final Block SHADOW_GRASS_PATH = registerBlock("shadow_grass_path", new EndPathBlock(SHADOW_GRASS));
	public static final Block PINK_MOSS_PATH = registerBlock("pink_moss_path", new EndPathBlock(PINK_MOSS));
	public static final Block AMBER_MOSS_PATH = registerBlock("amber_moss_path", new EndPathBlock(AMBER_MOSS));
	public static final Block JUNGLE_MOSS_PATH = registerBlock("jungle_moss_path", new EndPathBlock(JUNGLE_MOSS));
	public static final Block SANGNUM_PATH = registerBlock("sangnum_path", new EndPathBlock(SANGNUM));
	public static final Block RUTISCUS_PATH = registerBlock("rutiscus_path", new EndPathBlock(RUTISCUS));
	
	public static final Block MOSSY_OBSIDIAN = registerBlock("mossy_obsidian", new MossyObsidian());
	public static final Block MOSSY_BONE = registerBlock("mossy_bone", new MossyBoneBlock());
	
	// Rocks //
	public static final StoneMaterial FLAVOLITE = new StoneMaterial("flavolite", MaterialColor.SAND);
	public static final StoneMaterial VIOLECITE = new StoneMaterial("violecite", MaterialColor.PURPLE);
	public static final StoneMaterial SULPHURIC_ROCK = new StoneMaterial("sulphuric_rock", MaterialColor.BROWN);
	public static final Block BRIMSTONE = registerBlock("brimstone", new BrimstoneBlock());
	public static final Block SULPHUR_CRYSTAL = registerBlock("sulphur_crystal", new SulphurCrystalBlock());
	public static final Block MISSING_TILE = registerBlock("missing_tile", new MissingTileBlock());
	
	public static final Block FLAVOLITE_RUNED = registerBlock("flavolite_runed", new RunedFlavolite());
	public static final Block FLAVOLITE_RUNED_ETERNAL = registerBlock("flavolite_runed_eternal", new EternalRunedFlavolite());
	
	public static final Block ANDESITE_PEDESTAL = registerBlock("andesite_pedestal", new PedestalVanilla(Blocks.ANDESITE));
	public static final Block DIORITE_PEDESTAL = registerBlock("diorite_pedestal", new PedestalVanilla(Blocks.DIORITE));
	public static final Block GRANITE_PEDESTAL = registerBlock("granite_pedestal", new PedestalVanilla(Blocks.GRANITE));
	public static final Block QUARTZ_PEDESTAL = registerBlock("quartz_pedestal", new PedestalVanilla(Blocks.QUARTZ_BLOCK));
	public static final Block PURPUR_PEDESTAL = registerBlock("purpur_pedestal", new PedestalVanilla(Blocks.PURPUR_BLOCK));
	
	public static final Block HYDROTHERMAL_VENT = registerBlock("hydrothermal_vent", new HydrothermalVentBlock());
	public static final Block VENT_BUBBLE_COLUMN = registerBlockNI("vent_bubble_column", new VentBubbleColumnBlock());
	
	public static final Block DENSE_SNOW = registerBlock("dense_snow", new DenseSnowBlock());
	public static final Block EMERALD_ICE = registerBlock("emerald_ice", new EmeraldIceBlock());
	public static final Block DENSE_EMERALD_ICE = registerBlock("dense_emerald_ice", new DenseEmeraldIceBlock());
	public static final Block ANCIENT_EMERALD_ICE = registerBlock("ancient_emerald_ice", new AncientEmeraldIceBlock());
	
	// Wooden Materials And Trees //
	public static final Block MOSSY_GLOWSHROOM_SAPLING = registerBlock("mossy_glowshroom_sapling", new MossyGlowshroomSaplingBlock());
	public static final Block MOSSY_GLOWSHROOM_CAP = registerBlock("mossy_glowshroom_cap", new MossyGlowshroomCapBlock());
	public static final Block MOSSY_GLOWSHROOM_HYMENOPHORE = registerBlock("mossy_glowshroom_hymenophore", new MossyGlowshroomHymenophoreBlock());
	public static final Block MOSSY_GLOWSHROOM_FUR = registerBlock("mossy_glowshroom_fur", new FurBlock(MOSSY_GLOWSHROOM_SAPLING, 15, 16));
	public static final WoodenMaterial MOSSY_GLOWSHROOM = new WoodenMaterial("mossy_glowshroom", MaterialColor.GRAY, MaterialColor.WOOD);
	
	public static final Block PYTHADENDRON_SAPLING = registerBlock("pythadendron_sapling", new PythadendronSaplingBlock());
	public static final Block PYTHADENDRON_LEAVES = registerBlock("pythadendron_leaves", new EndLeavesBlock(PYTHADENDRON_SAPLING, MaterialColor.MAGENTA));
	public static final WoodenMaterial PYTHADENDRON = new WoodenMaterial("pythadendron", MaterialColor.MAGENTA, MaterialColor.PURPLE);
	
	public static final Block END_LOTUS_SEED = registerBlock("end_lotus_seed", new EndLotusSeedBlock());
	public static final Block END_LOTUS_STEM = registerBlock("end_lotus_stem", new EndLotusStemBlock());
	public static final Block END_LOTUS_LEAF = registerBlockNI("end_lotus_leaf", new EndLotusLeafBlock());
	public static final Block END_LOTUS_FLOWER = registerBlockNI("end_lotus_flower", new EndLotusFlowerBlock());
	public static final WoodenMaterial END_LOTUS = new WoodenMaterial("end_lotus", MaterialColor.LIGHT_BLUE, MaterialColor.CYAN);
	
	public static final Block LACUGROVE_SAPLING = registerBlock("lacugrove_sapling", new LacugroveSaplingBlock());
	public static final Block LACUGROVE_LEAVES = registerBlock("lacugrove_leaves", new EndLeavesBlock(LACUGROVE_SAPLING, MaterialColor.CYAN));
	public static final WoodenMaterial LACUGROVE = new WoodenMaterial("lacugrove", MaterialColor.BROWN, MaterialColor.YELLOW);
	
	public static final Block DRAGON_TREE_SAPLING = registerBlock("dragon_tree_sapling", new DragonTreeSaplingBlock());
	public static final Block DRAGON_TREE_LEAVES = registerBlock("dragon_tree_leaves", new EndLeavesBlock(DRAGON_TREE_SAPLING, MaterialColor.MAGENTA));
	public static final WoodenMaterial DRAGON_TREE = new WoodenMaterial("dragon_tree", MaterialColor.BLACK, MaterialColor.MAGENTA);
	
	public static final Block TENANEA_SAPLING = registerBlock("tenanea_sapling", new TenaneaSaplingBlock());
	public static final Block TENANEA_LEAVES = registerBlock("tenanea_leaves", new EndLeavesBlock(TENANEA_SAPLING, MaterialColor.PINK));
	public static final Block TENANEA_FLOWERS = registerBlock("tenanea_flowers", new TenaneaFlowersBlock());
	public static final Block TENANEA_OUTER_LEAVES = registerBlock("tenanea_outer_leaves", new FurBlock(TENANEA_SAPLING, 32));
	public static final WoodenMaterial TENANEA = new WoodenMaterial("tenanea", MaterialColor.BROWN, MaterialColor.PINK);
	
	public static final Block HELIX_TREE_SAPLING = registerBlock("helix_tree_sapling", new HelixTreeSaplingBlock());
	public static final Block HELIX_TREE_LEAVES = registerBlock("helix_tree_leaves", new HelixTreeLeavesBlock());
	public static final WoodenMaterial HELIX_TREE = new WoodenMaterial("helix_tree", MaterialColor.GRAY, MaterialColor.ORANGE);
	
	public static final Block UMBRELLA_TREE_SAPLING = registerBlock("umbrella_tree_sapling", new UmbrellaTreeSaplingBlock());
	public static final Block UMBRELLA_TREE_MEMBRANE = registerBlock("umbrella_tree_membrane", new UmbrellaTreeMembraneBlock());
	public static final Block UMBRELLA_TREE_CLUSTER = registerBlock("umbrella_tree_cluster", new UmbrellaTreeClusterBlock());
	public static final Block UMBRELLA_TREE_CLUSTER_EMPTY = registerBlock("umbrella_tree_cluster_empty", new UmbrellaTreeClusterEmptyBlock());
	public static final WoodenMaterial UMBRELLA_TREE = new WoodenMaterial("umbrella_tree", MaterialColor.BLUE, MaterialColor.GREEN);
	
	public static final Block JELLYSHROOM_CAP_PURPLE = registerBlock("jellyshroom_cap_purple", new JellyshroomCapBlock(217, 142, 255, 164, 0, 255));
	public static final WoodenMaterial JELLYSHROOM = new WoodenMaterial("jellyshroom", MaterialColor.PURPLE, MaterialColor.LIGHT_BLUE);
	
	// Small Plants //
	public static final Block UMBRELLA_MOSS = registerBlock("umbrella_moss", new UmbrellaMossBlock());
	public static final Block UMBRELLA_MOSS_TALL = registerBlock("umbrella_moss_tall", new UmbrellaMossTallBlock());
	public static final Block CREEPING_MOSS = registerBlock("creeping_moss", new GlowingMossBlock(11));
	public static final Block CHORUS_GRASS = registerBlock("chorus_grass", new ChorusGrassBlock());
	public static final Block CAVE_GRASS = registerBlock("cave_grass", new TerrainPlantBlock(CAVE_MOSS));
	public static final Block CRYSTAL_GRASS = registerBlock("crystal_grass", new TerrainPlantBlock(CRYSTAL_MOSS));
	public static final Block SHADOW_PLANT = registerBlock("shadow_plant", new TerrainPlantBlock(SHADOW_GRASS));
	public static final Block BUSHY_GRASS = registerBlock("bushy_grass", new TerrainPlantBlock(PINK_MOSS));
	public static final Block AMBER_GRASS = registerBlock("amber_grass", new TerrainPlantBlock(AMBER_MOSS));
	public static final Block TWISTED_UMBRELLA_MOSS = registerBlock("twisted_umbrella_moss", new TwistedUmbrellaMossBlock());
	public static final Block TWISTED_UMBRELLA_MOSS_TALL = registerBlock("twisted_umbrella_moss_tall", new TwistedUmbrellaMossTallBlock());
	public static final Block JUNGLE_GRASS = registerBlock("jungle_grass", new TerrainPlantBlock(JUNGLE_MOSS));
	public static final Block BLOOMING_COOKSONIA = registerBlock("blooming_cooksonia", new TerrainPlantBlock(END_MOSS));
	public static final Block SALTEAGO = registerBlock("salteago", new TerrainPlantBlock(END_MOSS));
	public static final Block VAIOLUSH_FERN = registerBlock("vaiolush_fern", new TerrainPlantBlock(END_MOSS));
	public static final Block FRACTURN = registerBlock("fracturn", new TerrainPlantBlock(END_MOSS));
	public static final Block CLAWFERN = registerBlock("clawfern", new TerrainPlantBlock(SANGNUM, MOSSY_OBSIDIAN, MOSSY_BONE));
	public static final Block GLOBULAGUS = registerBlock("globulagus", new TerrainPlantBlock(SANGNUM, MOSSY_OBSIDIAN, MOSSY_BONE));
	public static final Block ORANGO = registerBlock("orango", new TerrainPlantBlock(RUTISCUS));
	public static final Block AERIDIUM = registerBlock("aeridium", new TerrainPlantBlock(RUTISCUS));
	public static final Block LUTEBUS = registerBlock("lutebus", new TerrainPlantBlock(RUTISCUS));
	public static final Block LAMELLARIUM = registerBlock("lamellarium", new TerrainPlantBlock(RUTISCUS));
	
	public static final Block BLUE_VINE_SEED = registerBlock("blue_vine_seed", new BlueVineSeedBlock());
	public static final Block BLUE_VINE = registerBlockNI("blue_vine", new BlueVineBlock());
	public static final Block BLUE_VINE_LANTERN = registerBlock("blue_vine_lantern", new BlueVineLanternBlock());
	public static final Block BLUE_VINE_FUR = registerBlock("blue_vine_fur", new FurBlock(BLUE_VINE_SEED, 15, 3));
	
	public static final Block LANCELEAF_SEED = registerBlock("lanceleaf_seed", new LanceleafSeedBlock());
	public static final Block LANCELEAF = registerBlockNI("lanceleaf", new LanceleafBlock());
	
	public static final Block GLOWING_PILLAR_SEED = registerBlock("glowing_pillar_seed", new GlowingPillarSeedBlock());
	public static final Block GLOWING_PILLAR_ROOTS = registerBlockNI("glowing_pillar_roots", new GlowingPillarRootsBlock());
	public static final Block GLOWING_PILLAR_LUMINOPHOR = registerBlock("glowing_pillar_luminophor", new GlowingPillarLuminophorBlock());
	public static final Block GLOWING_PILLAR_LEAVES = registerBlock("glowing_pillar_leaves", new FurBlock(GLOWING_PILLAR_SEED, 15, 3));
	
	public static final Block SMALL_JELLYSHROOM = registerBlock("small_jellyshroom", new SmallJellyshroomBlock());
	
	public static final Block LUMECORN_SEED = registerBlock("lumecorn_seed", new LumecornSeedBlock());
	public static final Block LUMECORN = registerBlockNI("lumecorn", new LumecornBlock());
	
	public static final Block SMALL_AMARANITA_MUSHROOM = registerBlock("small_amaranita_mushroom", new SmallAmaranitaBlock());
	public static final Block LARGE_AMARANITA_MUSHROOM = registerBlockNI("large_amaranita_mushroom", new LargeAmaranitaBlock());
	public static final Block AMARANITA_HYPHAE = registerBlock("amaranita_hyphae", new AmaranitaHyphaeBlock());
	public static final Block AMARANITA_HYMENOPHORE = registerBlock("amaranita_hymenophore", new AmaranitaHymenophoreBlock());
	public static final Block AMARANITA_FUR = registerBlock("amaranita_fur", new FurBlock(MOSSY_GLOWSHROOM_SAPLING, 15, 4));
	public static final Block AMARANITA_CAP = registerBlock("amaranita_cap", new AmaranitaCapBlock());
	
	// Crops
	public static final Block BLOSSOM_BERRY = registerBlock("blossom_berry_seed", new EndCropBlock(EndItems.BLOSSOM_BERRY, PINK_MOSS));
	
	// Water plants
	public static final Block BUBBLE_CORAL = registerBlock("bubble_coral", new BubbleCoralBlock());
	public static final Block MENGER_SPONGE = registerBlock("menger_sponge", new MengerSpongeBlock());
	public static final Block MENGER_SPONGE_WET = registerBlock("menger_sponge_wet", new MengerSpongeWetBlock());
	public static final Block CHARNIA_RED = registerBlock("charnia_red", new CharniaBlock());
	public static final Block CHARNIA_PURPLE = registerBlock("charnia_purple", new CharniaBlock());
	public static final Block CHARNIA_ORANGE = registerBlock("charnia_orange", new CharniaBlock());
	public static final Block CHARNIA_LIGHT_BLUE = registerBlock("charnia_light_blue", new CharniaBlock());
	public static final Block CHARNIA_CYAN = registerBlock("charnia_cyan", new CharniaBlock());
	public static final Block CHARNIA_GREEN = registerBlock("charnia_green", new CharniaBlock());
	
	public static final Block END_LILY = registerBlockNI("end_lily", new EndLilyBlock());
	public static final Block END_LILY_SEED = registerBlock("end_lily_seed", new EndLilySeedBlock());
	
	public static final Block HYDRALUX_SAPLING = registerBlock("hydralux_sapling", new HydraluxSaplingBlock());	
	public static final Block HYDRALUX = registerBlockNI("hydralux", new HydraluxBlock());
	public static final Block HYDRALUX_PETAL_BLOCK = registerBlock("hydralux_petal_block", new HydraluxPetalBlock());
	public static final ColoredMaterial HYDRALUX_PETAL_BLOCK_COLORED = new ColoredMaterial(HydraluxPetalColoredBlock::new, HYDRALUX_PETAL_BLOCK, true);
	
	public static final Block CAVE_BUSH = registerBlock("cave_bush", new SimpleLeavesBlock(MaterialColor.MAGENTA));
	
	public static final Block MURKWEED = registerBlock("murkweed", new MurkweedBlock());
	public static final Block NEEDLEGRASS = registerBlock("needlegrass", new NeedlegrassBlock());
	
	// Wall Plants //
	public static final Block PURPLE_POLYPORE = registerBlock("purple_polypore", new WallMushroomBlock(13));
	public static final Block TAIL_MOSS = registerBlock("tail_moss", new EndWallPlantBlock());
	public static final Block CYAN_MOSS = registerBlock("cyan_moss", new EndWallPlantBlock());
	public static final Block TWISTED_MOSS = registerBlock("twisted_moss", new EndWallPlantBlock());
	public static final Block TUBE_WORM = registerBlock("tube_worm", new EndUnderwaterWallPlantBlock());
	public static final Block BULB_MOSS = registerBlock("bulb_moss", new EndWallPlantBlock(12));
	public static final Block JUNGLE_FERN = registerBlock("jungle_fern", new EndWallPlantBlock());
	
	// Crops //
	public static final Block SHADOW_BERRY = registerBlock("shadow_berry", new ShadowBerryBlock());
	
	// Vines //
	public static final Block DENSE_VINE = registerBlock("dense_vine", new VineBlock(15, true));
	public static final Block TWISTED_VINE = registerBlock("twisted_vine", new VineBlock());
	public static final Block BULB_VINE_SEED = registerBlock("bulb_vine_seed", new BulbVineSeedBlock());
	public static final Block BULB_VINE = registerBlock("bulb_vine", new BulbVineBlock());
	public static final Block JUNGLE_VINE = registerBlock("jungle_vine", new VineBlock());
	
	// Mob-Related
	public static final Block SILK_MOTH_NEST = registerBlock("silk_moth_nest", new SilkMothNestBlock()); 
	
	// Ores //
	public static final Block ENDER_ORE = registerBlock("ender_ore", new EndOreBlock(EndItems.ENDER_SHARD, 1, 3, 5));
	public static final Block AMBER_ORE = registerBlock("amber_ore", new EndOreBlock(EndItems.RAW_AMBER, 1, 2, 4));
	
	// Materials //
	public static final MetalMaterial THALLASIUM = MetalMaterial.makeNormal("thallasium", MaterialColor.BLUE, EndToolMaterial.THALLASIUM, EndArmorMaterial.THALLASIUM);
	public static final MetalMaterial TERMINITE = MetalMaterial.makeOreless("terminite", MaterialColor.field_25708, 7F, 9F, EndToolMaterial.TERMINITE, EndArmorMaterial.TERMINITE);
	public static final Block AETERNIUM_BLOCK = registerBlock("aeternium_block", new AeterniumBlock());
	public static final Block CHARCOAL_BLOCK = registerBlock("charcoal_block", new CharcoalBlock());
	
	public static final Block ENDER_BLOCK = registerBlock("ender_block", new EnderBlock());
	public static final Block AURORA_CRYSTAL = registerBlock("aurora_crystal", new AuroraCrystalBlock());
	public static final Block AMBER_BLOCK = registerBlock("amber_block", new AmberBlock());
	
	public static final Block RESPAWN_OBELISK = registerBlock("respawn_obelisk", new RespawnObeliskBlock());
	
	// Lanterns
	public static final Block ANDESITE_LANTERN = registerBlock("andesite_lantern", new StoneLanternBlock(Blocks.ANDESITE));
	public static final Block DIORITE_LANTERN = registerBlock("diorite_lantern", new StoneLanternBlock(Blocks.DIORITE));
	public static final Block GRANITE_LANTERN = registerBlock("granite_lantern", new StoneLanternBlock(Blocks.GRANITE));
	public static final Block QUARTZ_LANTERN = registerBlock("quartz_lantern", new StoneLanternBlock(Blocks.QUARTZ_BLOCK));
	public static final Block PURPUR_LANTERN = registerBlock("purpur_lantern", new StoneLanternBlock(Blocks.PURPUR_BLOCK));
	public static final Block END_STONE_LANTERN = registerBlock("end_stone_lantern", new StoneLanternBlock(Blocks.END_STONE));
	public static final Block BLACKSTONE_LANTERN = registerBlock("blackstone_lantern", new StoneLanternBlock(Blocks.BLACKSTONE));
	
	public static final Block IRON_BULB_LANTERN = registerBlock("iron_bulb_lantern", new BulbVineLanternBlock());
	public static final ColoredMaterial IRON_BULB_LANTERN_COLORED = new ColoredMaterial(BulbVineLanternColoredBlock::new, IRON_BULB_LANTERN, false);
	
	public static final Block IRON_CHANDELIER = EndBlocks.registerBlock("iron_chandelier", new ChandelierBlock(Blocks.GOLD_BLOCK));
	public static final Block GOLD_CHANDELIER = EndBlocks.registerBlock("gold_chandelier", new ChandelierBlock(Blocks.GOLD_BLOCK));
	
	// Blocks With Entity //
	public static final Block END_STONE_FURNACE = registerBlock("end_stone_furnace", new EndFurnaceBlock(Blocks.END_STONE));
	public static final Block END_STONE_SMELTER = registerBlock("end_stone_smelter", new EndStoneSmelter());
	public static final Block ETERNAL_PEDESTAL = registerBlock("eternal_pedestal", new EternalPedestal());
	public static final Block INFUSION_PEDESTAL = registerBlock("infusion_pedestal", new InfusionPedestal());
	public static final Block AETERNIUM_ANVIL = registerBlock("aeternium_anvil", new AeterniumAnvil());
	
	// Technical
	public static final Block END_PORTAL_BLOCK = registerBlockNI("end_portal_block", new EndPortalBlock());
	
	public static void register() {}
	
	public static Block registerBlock(Identifier id, Block block) {
		if (!Configs.BLOCK_CONFIG.getBooleanRoot(id.getPath(), true)) {
			return block;
		}
		Registry.register(Registry.BLOCK, id, block);
		EndItems.registerBlockItem(id, new BlockItem(block, EndItems.makeBlockItemSettings()));
		return block;
	}
	
	public static Block registerBlock(String name, Block block) {
		return registerBlock(BetterEnd.makeID(name), block);
	}
	
	public static Block registerBlockNI(String name, Block block) {
		return Registry.register(Registry.BLOCK, BetterEnd.makeID(name), block);
	}
}
