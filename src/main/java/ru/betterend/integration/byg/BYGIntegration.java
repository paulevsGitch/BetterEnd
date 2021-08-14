package ru.betterend.integration.byg;

import net.minecraft.world.level.block.Block;
import ru.bclib.api.TagAPI;
import ru.bclib.integration.ModIntegration;
import ru.betterend.integration.EndBiomeIntegration;
import ru.betterend.integration.Integrations;
import ru.betterend.integration.byg.biomes.BYGBiomes;
import ru.betterend.integration.byg.features.BYGFeatures;

public class BYGIntegration extends ModIntegration implements EndBiomeIntegration {
	public BYGIntegration() {
		super("byg");
	}
	
	@Override
	public void init() {
		Block block = Integrations.BYG.getBlock("ivis_phylium");
		if (block != null) {
			TagAPI.addTags(block, TagAPI.BLOCK_END_GROUND, TagAPI.BLOCK_GEN_TERRAIN);
		}
		BYGBlocks.register();
		BYGFeatures.register();
		BYGBiomes.register();
	}
	
	@Override
	public void addBiomes() {
		BYGBiomes.addBiomes();
		
		//Class<?> biomeClass = this.getClass("corgiaoc.byg.common.world.biome.BYGEndBiome");
		//List<Object> biomes = this.getStaticFieldValue(biomeClass, "BYG_END_BIOMES");
		
		/*if (biomes != null && biomeClass != null) {
			biomes.forEach((obj) -> {
				Biome biome = this.getAndExecuteRuntime(biomeClass, obj, "getBiome");
				if (biome != null) {
					ResourceLocation biomeID = BuiltinRegistries.BIOME.getKey(biome);
					BCLBiome endBiome = BiomeAPI.getBiome(biomeID);
					Biome edge = this.getAndExecuteRuntime(biomeClass, obj, "getEdge");
					if (edge != null) {
						ResourceLocation edgeID = BuiltinRegistries.BIOME.getKey(edge);
						EndBiomes.LAND_BIOMES.removeMutableBiome(edgeID);
						EndBiomes.VOID_BIOMES.removeMutableBiome(edgeID);
						BCLBiome edgeBiome = BiomeAPI.getBiome(edgeID);
						endBiome.setEdge(edgeBiome);
					}
					else {
						Boolean isVoid = this.getAndExecuteRuntime(biomeClass, obj, "isVoid");
						if (isVoid != null && isVoid.booleanValue()) {
							EndBiomes.LAND_BIOMES.removeMutableBiome(biomeID);
							EndBiomes.VOID_BIOMES.addBiomeMutable(endBiome);
						}
						ShufflingList<ResourceLocation> subBiomes = this.getAndExecuteRuntime(
							biomeClass,
							obj,
							"getHills"
						);
						if (subBiomes != null) {
							subBiomes.stream().collect(Collectors.toList()).forEach((id) -> {
								BCLBiome subBiome = BiomeAPI.getBiome(id);
								EndBiomes.LAND_BIOMES.removeMutableBiome(id);
								EndBiomes.VOID_BIOMES.removeMutableBiome(id);
								if (!endBiome.containsSubBiome(subBiome)) {
									EndBiomes.SUBBIOMES.add(subBiome);
									endBiome.addSubBiome(subBiome);
								}
							});
						}
					}
				}
			});
		}*/
	}
}
