package ru.betterend.registry;

import java.util.Arrays;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.impl.tool.attribute.ToolManagerImpl;
import net.fabricmc.fabric.impl.tool.attribute.handlers.ModdedToolsVanillaBlocksToolHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.tag.Tag.Identified;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.EndTerrainBlock;
import ru.betterend.blocks.basis.PedestalBlock;
import ru.betterend.blocks.basis.SimpleLeavesBlock;
import ru.betterend.blocks.basis.VineBlock;
import ru.betterend.mixin.common.ComposterBlockAccessor;
import ru.betterend.util.TagHelper;

public class EndTags {
	// Block Tags
	public static final Tag.Identified<Block> BOOKSHELVES = makeCommonBlockTag("bookshelves");
	public static final Tag.Identified<Block> GEN_TERRAIN = makeBlockTag("gen_terrain");
	public static final Tag.Identified<Block> END_GROUND = makeBlockTag("end_ground");
	public static final Tag.Identified<Block> PEDESTALS = makeBlockTag("pedestal");
	public static final Tag.Identified<Block> BLOCK_CHEST = makeCommonBlockTag("chest");
	public static final Tag.Identified<Block> END_STONES = makeCommonBlockTag("end_stones");
	
	// Item Tags
	public static final Tag.Identified<Item> ITEM_CHEST = makeCommonItemTag("chest");
	public final static Tag<Item> HAMMERS = registerFabricItemTag("hammers");
	
	public static Tag.Identified<Block> makeBlockTag(String name) {
		Identifier id = BetterEnd.makeID(name);
		Tag<Block> tag = BlockTags.getTagGroup().getTag(id);
		return tag == null ? (Identified<Block>) TagRegistry.block(id) : (Identified<Block>) tag;
	}
	
	public static Tag.Identified<Item> makeItemTag(String name) {
		Identifier id = BetterEnd.makeID(name);
		Tag<Item> tag = ItemTags.getTagGroup().getTag(id);
		return tag == null ? (Identified<Item>) TagRegistry.item(id) : (Identified<Item>) tag;
	}
	
	public static Tag.Identified<Block> makeCommonBlockTag(String name) {
		Identifier id = new Identifier("c", name);
		Tag<Block> tag = BlockTags.getTagGroup().getTag(id);
		return tag == null ? (Identified<Block>) TagRegistry.block(id) : (Identified<Block>) tag;
	}
	
	public static Tag.Identified<Item> makeCommonItemTag(String name) {
		Identifier id = new Identifier("c", name);
		Tag<Item> tag = ItemTags.getTagGroup().getTag(id);
		return tag == null ? (Identified<Item>) TagRegistry.item(id) : (Identified<Item>) tag;
	}
	
	public static void register() {
		addSurfaceBlock(Blocks.END_STONE);
		addSurfaceBlock(EndBlocks.THALASSIUM_ORE);
		addSurfaceBlock(EndBlocks.ENDSTONE_DUST);
		addSurfaceBlock(EndBlocks.AMBER_ORE);
		
		EndItems.getModBlocks().forEach((item) -> {
			Block block = ((BlockItem) item).getBlock();
			if (block instanceof EndTerrainBlock) {
				addSurfaceBlock(block);
				TagHelper.addTag(BlockTags.NYLIUM, block);
			}
			else if (block instanceof LeavesBlock || block instanceof SimpleLeavesBlock) {
				TagHelper.addTag(BlockTags.LEAVES, block);
				ComposterBlockAccessor.callRegisterCompostableItem(0.3F, block);
			}
			else if (block instanceof VineBlock) {
				TagHelper.addTag(BlockTags.CLIMBABLE, block);
			}
			else if (block instanceof PedestalBlock) {
				TagHelper.addTag(PEDESTALS, block);
			}
			if (block.getDefaultState().getMaterial().equals(Material.PLANT)) {
				ComposterBlockAccessor.callRegisterCompostableItem(0.1F, block);
			}
		});
		
		TagHelper.addTag(GEN_TERRAIN, EndBlocks.ENDER_ORE, EndBlocks.FLAVOLITE.stone, EndBlocks.VIOLECITE.stone, EndBlocks.SULPHURIC_ROCK.stone, EndBlocks.BRIMSTONE);
		TagHelper.addTag(END_GROUND, EndBlocks.SULPHURIC_ROCK.stone, EndBlocks.BRIMSTONE);
		
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
				Block under = config.getUnderMaterial().getBlock();
				Block surface = config.getTopMaterial().getBlock();
				TagHelper.addTag(GEN_TERRAIN, under, surface);
				TagHelper.addTag(END_GROUND, surface);
			}
		});
		END_STONES.values().forEach((block) -> {
			addSurfaceBlock(block);
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
