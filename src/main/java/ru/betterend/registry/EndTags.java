package ru.betterend.registry;

import java.util.Arrays;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.impl.tool.attribute.ToolManagerImpl;
import net.fabricmc.fabric.impl.tool.attribute.handlers.ModdedToolsVanillaBlocksToolHandler;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.Tag.Named;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderConfiguration;
import net.minecraft.world.level.material.Material;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.EndTerrainBlock;
import ru.betterend.blocks.basis.PedestalBlock;
import ru.betterend.blocks.basis.SimpleLeavesBlock;
import ru.betterend.blocks.basis.VineBlock;
import ru.betterend.mixin.common.ComposterBlockAccessor;
import ru.betterend.util.TagHelper;

public class EndTags {
	// Table with common (c) tags:
	// https://fabricmc.net/wiki/tutorial:tags
	
	// Block Tags
	public static final Tag.Named<Block> BOOKSHELVES = makeCommonBlockTag("bookshelves");
	public static final Tag.Named<Block> GEN_TERRAIN = makeBlockTag("gen_terrain");
	public static final Tag.Named<Block> END_GROUND = makeBlockTag("end_ground");
	public static final Tag.Named<Block> PEDESTALS = makeBlockTag("pedestal");
	public static final Tag.Named<Block> BLOCK_CHEST = makeCommonBlockTag("chest");
	public static final Tag.Named<Block> END_STONES = makeCommonBlockTag("end_stones");
	public static final Tag.Named<Block> DRAGON_IMMUNE = getMCBlockTag("dragon_immune");
	
	// Item Tags
	public static final Tag.Named<Item> ITEM_CHEST = makeCommonItemTag("chest");
	public static final Tag.Named<Item> IRON_INGOTS = makeCommonItemTag("iron_ingots");
	public static final Tag.Named<Item> FURNACES = makeCommonItemTag("furnaces");
	public final static Tag<Item> HAMMERS = registerFabricItemTag("hammers");
	
	public static Tag.Named<Block> makeBlockTag(String name) {
		ResourceLocation id = BetterEnd.makeID(name);
		Tag<Block> tag = BlockTags.getAllTags().getTag(id);
		return tag == null ? (Named<Block>) TagRegistry.block(id) : (Named<Block>) tag;
	}
	
	public static Tag.Named<Item> makeItemTag(String name) {
		ResourceLocation id = BetterEnd.makeID(name);
		Tag<Item> tag = ItemTags.getAllTags().getTag(id);
		return tag == null ? (Named<Item>) TagRegistry.item(id) : (Named<Item>) tag;
	}
	
	public static Tag.Named<Block> makeCommonBlockTag(String name) {
		ResourceLocation id = new ResourceLocation("c", name);
		Tag<Block> tag = BlockTags.getAllTags().getTag(id);
		return tag == null ? (Named<Block>) TagRegistry.block(id) : (Named<Block>) tag;
	}
	
	public static Tag.Named<Item> makeCommonItemTag(String name) {
		ResourceLocation id = new ResourceLocation("c", name);
		Tag<Item> tag = ItemTags.getAllTags().getTag(id);
		return tag == null ? (Named<Item>) TagRegistry.item(id) : (Named<Item>) tag;
	}
	
	public static Tag.Named<Block> getMCBlockTag(String name) {
		ResourceLocation id = new ResourceLocation(name);
		Tag<Block> tag = BlockTags.getAllTags().getTag(id);
		return tag == null ? (Named<Block>) TagRegistry.block(id) : (Named<Block>) tag;
	}
	
	public static void register() {
		addSurfaceBlock(Blocks.END_STONE);
		addSurfaceBlock(EndBlocks.THALLASIUM.ore);
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
			
			Material mat = block.defaultBlockState().getMaterial();
			if (mat.equals(Material.PLANT) || mat.equals(Material.REPLACEABLE_PLANT)) {
				ComposterBlockAccessor.callRegisterCompostableItem(0.1F, block);
			}
		});
		
		EndItems.getModItems().forEach((item) -> {
			if (item.isEdible()) {
				FoodProperties food = item.getFoodProperties();
				float compost = food.getNutrition() * food.getSaturationModifier() * 0.18F;
				ComposterBlockAccessor.callRegisterCompostableItem(compost, item);
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
				EndItems.AETERNIUM_HAMMER,
				EndBlocks.THALLASIUM.hammer,
				EndBlocks.TERMINITE.hammer
			)
		));
		
		TagHelper.addTag(FURNACES, Blocks.FURNACE);
		TagHelper.addTag(BlockTags.ANVIL, EndBlocks.AETERNIUM_ANVIL);
		
		TagHelper.addTag(BlockTags.BEACON_BASE_BLOCKS, EndBlocks.AETERNIUM_BLOCK);
		TagHelper.addTag(ItemTags.BEACON_PAYMENT_ITEMS, EndItems.AETERNIUM_INGOT);
		
		TagHelper.addTag(EndTags.DRAGON_IMMUNE, EndBlocks.ENDER_ORE, EndBlocks.ETERNAL_PEDESTAL, EndBlocks.FLAVOLITE_RUNED_ETERNAL, EndBlocks.FLAVOLITE_RUNED);
	}
	
	public static void addSurfaceBlock(Block block) {
		TagHelper.addTag(END_GROUND, block);
		TagHelper.addTag(GEN_TERRAIN, block);
	}
	
	public static void addTerrainTags(Registry<Biome> biomeRegistry) {
		biomeRegistry.forEach((biome) -> {
			if (biome.getBiomeCategory() == BiomeCategory.THEEND) {
				SurfaceBuilderConfiguration config = biome.getGenerationSettings().getSurfaceBuilderConfig();
				Block under = config.getUnderMaterial().getBlock();
				Block surface = config.getTopMaterial().getBlock();
				TagHelper.addTag(GEN_TERRAIN, under, surface);
				TagHelper.addTag(END_GROUND, surface);
			}
		});
		END_STONES.getValues().forEach(EndTags::addSurfaceBlock);
	}
	
	public static Tag<Item> registerFabricItemTag(String name) {
		return TagRegistry.item(new ResourceLocation("fabric", name));
	}
}
