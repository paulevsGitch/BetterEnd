package ru.betterend.registry;

import java.util.Arrays;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.impl.tool.attribute.ToolManagerImpl;
import net.fabricmc.fabric.impl.tool.attribute.handlers.ModdedToolsVanillaBlocksToolHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.tag.Tag.Identified;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.BlockTerrain;
import ru.betterend.util.TagHelper;

public class EndTags {
	// Block Tags
	public static final Tag.Identified<Block> END_GROUND = makeTag("end_ground");
	public static final Tag.Identified<Block> GEN_TERRAIN = makeTag("gen_terrain");
	
	// Item Tags
	public final static Tag<Item> HAMMERS = registerFabricItemTag("hammers");
	
	private static Tag.Identified<Block> makeTag(String name) {
		return (Identified<Block>) TagRegistry.block(BetterEnd.makeID(name));
	}
	
	public static void register() {
		addSurfaceBlock(EndBlocks.ENDSTONE_DUST);
		
		EndItems.getModBlocks().forEach((item) -> {
			Block block = ((BlockItem) item).getBlock();
			if (block instanceof BlockTerrain) {
				addSurfaceBlock(block);
				TagHelper.addTag(BlockTags.NYLIUM, block);
			}
		});
		
		TagHelper.addTag(GEN_TERRAIN, EndBlocks.ENDER_ORE, EndBlocks.FLAVOLITE.stone, EndBlocks.VIOLECITE.stone);
		
		ToolManagerImpl.tag(HAMMERS).register(new ModdedToolsVanillaBlocksToolHandler(
				Arrays.asList(
					EndItems.IRON_HAMMER,
					EndItems.GOLDEN_HAMMER,
					EndItems.DIAMOND_HAMMER,
					EndItems.NETHERITE_HAMMER,
					EndItems.TERMINITE_HAMMER,
					EndItems.AETERNIUM_HAMMER
				)
			));
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
	
	public static boolean validGenBlock(BlockState block) {
		return block.isIn(END_GROUND) || block.isIn(GEN_TERRAIN);
	}
	
	public static Tag<Item> registerItemTag(String name) {
		return TagRegistry.item(BetterEnd.makeID(name));
	}
	
	public static Tag<Item> registerFabricItemTag(String name) {
		return TagRegistry.item(new Identifier("fabric", name));
	}
}
