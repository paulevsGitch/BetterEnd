package ru.betterend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.apache.commons.lang3.mutable.MutableInt;
import ru.bclib.api.WorldDataAPI;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.util.Logger;
import ru.betterend.api.BetterEndPlugin;
import ru.betterend.config.Configs;
import ru.betterend.effects.EndPotions;
import ru.betterend.integration.Integrations;
import ru.betterend.recipe.AlloyingRecipes;
import ru.betterend.recipe.AnvilRecipes;
import ru.betterend.recipe.CraftingRecipes;
import ru.betterend.recipe.FurnaceRecipes;
import ru.betterend.recipe.InfusionRecipes;
import ru.betterend.recipe.SmithingRecipes;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndBlockEntities;
import ru.betterend.registry.EndEnchantments;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndPortals;
import ru.betterend.registry.EndSounds;
import ru.betterend.registry.EndStructures;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BonemealPlants;
import ru.betterend.util.LootTableUtil;
import ru.betterend.world.generator.GeneratorOptions;
import ru.betterend.world.generator.TerrainGenerator;

public class BetterEnd implements ModInitializer {
	public static final String MOD_ID = "betterend";
	public static final Logger LOGGER = new Logger(MOD_ID);
	public static final boolean RUNS_FALL_FLYING_LIB = FabricLoader.getInstance().getModContainer("fallflyinglib").isPresent();

	@Override
	public void onInitialize() {
		WorldDataAPI.registerModCache(MOD_ID);
		EndPortals.loadPortals();
		EndSounds.register();
		EndBlockEntities.register();
		EndFeatures.register();
		EndEntities.register();
		EndBiomes.register();
		EndTags.register();
		EndEnchantments.register();
		EndPotions.register();
		CraftingRecipes.register();
		FurnaceRecipes.register();
		AlloyingRecipes.register();
		AnvilRecipes.register();
		SmithingRecipes.register();
		InfusionRecipes.register();
		EndStructures.register();
		BonemealPlants.init();
		GeneratorOptions.init();
		LootTableUtil.init();
		FabricLoader.getInstance().getEntrypoints("betterend", BetterEndPlugin.class).forEach(BetterEndPlugin::register);
		Integrations.init();
		Configs.saveConfigs();

		if (GeneratorOptions.useNewGenerator()) {
			ru.bclib.world.generator.GeneratorOptions.setFarEndBiomes(GeneratorOptions.getIslandDistBlock() > 250000L);
			ru.bclib.world.generator.GeneratorOptions.setEndLandFunction((pos) -> TerrainGenerator.isLand(pos.x, pos.y));
		}

		BiomeAPI.registerEndBiomeModification((biomeID, biome) -> {
			//EndStructures.addBiomeStructures(biomeID, biome);
			//TODO: 1.18 disabled to test feature-sorting of BE biomes
			//EndFeatures.addBiomeFeatures(biomeID, biome);
		});
	List<Biome> biomes = new LinkedList<>();
		biomes.add(net.minecraft.data.worldgen.biome.EndBiomes.endBarrens());
		biomes.add(net.minecraft.data.worldgen.biome.EndBiomes.endHighlands());
		biomes.add(net.minecraft.data.worldgen.biome.EndBiomes.endMidlands());
		biomes.add(net.minecraft.data.worldgen.biome.EndBiomes.theEnd());
		biomes.add(net.minecraft.data.worldgen.biome.EndBiomes.smallEndIslands());

		List<Biome> lBiomes = new LinkedList<>();
		lBiomes.add(EndBiomes.FOGGY_MUSHROOMLAND.getBiome());
		lBiomes.add(EndBiomes.CHORUS_FOREST.getBiome());
		lBiomes.add(EndBiomes.DUST_WASTELANDS.getBiome());
		lBiomes.add(EndBiomes.MEGALAKE.getBiome());
		lBiomes.add(EndBiomes.MEGALAKE_GROVE.getBiome());
		lBiomes.add(EndBiomes.CRYSTAL_MOUNTAINS.getBiome());
		lBiomes.add(EndBiomes.PAINTED_MOUNTAINS.getBiome());
		lBiomes.add(EndBiomes.SHADOW_FOREST.getBiome());
		lBiomes.add(EndBiomes.AMBER_LAND.getBiome());
		lBiomes.add(EndBiomes.BLOSSOMING_SPIRES.getBiome());
		lBiomes.add(EndBiomes.SULPHUR_SPRINGS.getBiome());
		lBiomes.add(EndBiomes.UMBRELLA_JUNGLE.getBiome());
		lBiomes.add(EndBiomes.GLOWING_GRASSLANDS.getBiome());
		lBiomes.add(EndBiomes.DRAGON_GRAVEYARDS.getBiome());
		lBiomes.add(EndBiomes.DRY_SHRUBLAND.getBiome());
		lBiomes.add(EndBiomes.LANTERN_WOODS.getBiome());
		lBiomes.add(EndBiomes.NEON_OASIS.getBiome());
		lBiomes.add(EndBiomes.UMBRA_VALLEY.getBiome());

		lBiomes.forEach(biome -> {
			BiomeAPI.sortBiomeFeatures(biome);
		});
		biomes.addAll(lBiomes);

		//buildFeaturesPerStep(lBiomes, true);
	}
	public static ResourceLocation makeID(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
	
}
