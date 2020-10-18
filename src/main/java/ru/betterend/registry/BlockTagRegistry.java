package ru.betterend.registry;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
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
		addSurfaceBlock(BlockRegistry.END_MOSS);
		addSurfaceBlock(BlockRegistry.END_MYCELIUM);
		addSurfaceBlock(BlockRegistry.CHORUS_NYLIUM);
		
		TagHelper.addTag(GEN_TERRAIN, BlockRegistry.ENDER_ORE, BlockRegistry.FLAVOLITE.stone, BlockRegistry.VIOLECITE.stone);
	}
	
	public static void addSurfaceBlock(Block block) {
		TagHelper.addTag(END_GROUND, block);
		TagHelper.addTag(GEN_TERRAIN, block);
	}
	
	public static void addTerrainTags(Registry<Biome> biomeRegistry) {
		biomeRegistry.forEach((biome) -> {
			if (biome.getCategory() == Category.THEEND) {
				SurfaceConfig config = biome.getGenerationSettings().getSurfaceConfig();
				TagHelper.addTag(GEN_TERRAIN, config.getTopMaterial().getBlock(), config.getUnderMaterial().getBlock());
			}
		});
	}
}
