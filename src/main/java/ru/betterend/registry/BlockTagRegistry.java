package ru.betterend.registry;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.tag.Tag.Identified;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig;
import ru.betterend.BetterEnd;
import ru.betterend.util.TagHelper;

public class BlockTagRegistry {
	public static final Tag.Identified<Block> END_GROUND = makeTag("end_ground");
	public static final Tag.Identified<Block> GEN_TERRAIN = makeTag("gen_terrain");
	
	private static Tag.Identified<Block> makeTag(String name) {
		return (Identified<Block>) TagRegistry.block(BetterEnd.makeID(name));
	}
	
	public static void register() {
		TagHelper.addTag(END_GROUND, BlockRegistry.END_MOSS, BlockRegistry.END_MYCELIUM, BlockRegistry.ENDER_ORE);
		TagHelper.addTag(BlockTags.NYLIUM, BlockRegistry.END_MOSS, BlockRegistry.END_MYCELIUM, BlockRegistry.ENDER_ORE);
	}
	
	public static void addTerrainTags(Registry<Biome> biomeRegistry) {
		END_GROUND.values().forEach((block) -> {
			TagHelper.addTag(GEN_TERRAIN, block);
		});
		biomeRegistry.forEach((biome) -> {
			if (biome.getCategory() == Category.THEEND) {
				SurfaceConfig config = biome.getGenerationSettings().getSurfaceConfig();
				TagHelper.addTag(GEN_TERRAIN, config.getTopMaterial().getBlock(), config.getUnderMaterial().getBlock());
			}
		});
	}
}
