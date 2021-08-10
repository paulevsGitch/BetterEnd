package ru.betterend.registry;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.impl.tool.attribute.ToolManagerImpl;
import net.fabricmc.fabric.impl.tool.attribute.handlers.ModdedToolsVanillaBlocksToolHandler;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockAccessor;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockSettingsAccessor;
import net.minecraft.core.Registry;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderConfiguration;
import net.minecraft.world.level.material.Material;
import ru.bclib.api.BonemealAPI;
import ru.bclib.api.TagAPI;
import ru.bclib.blocks.BaseVineBlock;
import ru.bclib.blocks.SimpleLeavesBlock;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.basis.EndTerrainBlock;
import ru.betterend.blocks.basis.PedestalBlock;
import ru.betterend.item.tool.EndHammerItem;
import ru.betterend.mixin.common.ComposterBlockAccessor;

import java.util.List;

public class EndTags {
	// Table with common (c) tags:
	// https://fabricmc.net/wiki/tutorial:tags
	
	// Block Tags
	public static final Tag.Named<Block> PEDESTALS = TagAPI.makeBlockTag(BetterEnd.MOD_ID, "pedestal");

	// Item Tags
	public static final Tag.Named<Item> ALLOYING_IRON = TagAPI.makeItemTag(BetterEnd.MOD_ID, "alloying_iron");
	public static final Tag.Named<Item> ALLOYING_GOLD = TagAPI.makeItemTag(BetterEnd.MOD_ID, "alloying_gold");
	public static final Tag.Named<Item> ALLOYING_COPPER = TagAPI.makeItemTag(BetterEnd.MOD_ID, "alloying_copper");
	
	public static void register() {
		TagAPI.addEndGround(EndBlocks.THALLASIUM.ore);
		TagAPI.addEndGround(EndBlocks.ENDSTONE_DUST);
		TagAPI.addEndGround(EndBlocks.AMBER_ORE);
		
		EndBlocks.getModBlocks().forEach(block -> {
			Properties properties = ((AbstractBlockAccessor) block).getSettings();
			Material material = ((AbstractBlockSettingsAccessor) properties).getMaterial();
			
			if (material.equals(Material.STONE) || material.equals(Material.METAL) || material.equals(Material.HEAVY_METAL)) {
				TagAPI.addTag(TagAPI.MINEABLE_PICKAXE, block);
			}
			else if (material.equals(Material.WOOD)) {
				TagAPI.addTag(TagAPI.MINEABLE_AXE, block);
			}
			else if (material.equals(Material.LEAVES) || material.equals(Material.PLANT) || material.equals(Material.WATER_PLANT) || material.equals(Material.SPONGE)) {
				TagAPI.addTag(TagAPI.MINEABLE_HOE, block);
			}
			else if (material.equals(Material.SAND)) {
				TagAPI.addTag(TagAPI.MINEABLE_SHOVEL, block);
			}
			
			if (block instanceof EndTerrainBlock) {
				TagAPI.addEndGround(block);
				TagAPI.addTag(BlockTags.NYLIUM, block);
				BonemealAPI.addSpreadableBlock(block, Blocks.END_STONE);
			}
			else if (block instanceof LeavesBlock || block instanceof SimpleLeavesBlock) {
				TagAPI.addTag(BlockTags.LEAVES, block);
				ComposterBlockAccessor.callAdd(0.3F, block);
			}
			else if (block instanceof BaseVineBlock) {
				TagAPI.addTag(BlockTags.CLIMBABLE, block);
			}
			else if (block instanceof PedestalBlock) {
				TagAPI.addTag(PEDESTALS, block);
			}
			
			Material mat = block.defaultBlockState().getMaterial();
			if (mat.equals(Material.PLANT) || mat.equals(Material.REPLACEABLE_PLANT)) {
				ComposterBlockAccessor.callAdd(0.1F, block);
			}
		});
		TagAPI.addEndGround(EndBlocks.CAVE_MOSS);
		TagAPI.addTag(BlockTags.NYLIUM, EndBlocks.CAVE_MOSS);
		BonemealAPI.addSpreadableBlock(EndBlocks.CAVE_MOSS, Blocks.END_STONE);
		BonemealAPI.addSpreadableBlock(EndBlocks.MOSSY_OBSIDIAN, Blocks.OBSIDIAN);
		BonemealAPI.addSpreadableBlock(EndBlocks.MOSSY_DRAGON_BONE, EndBlocks.DRAGON_BONE_BLOCK);
		
		List<Item> ITEM_HAMMERS = Lists.newArrayList();
		EndItems.getModItems(BetterEnd.MOD_ID).forEach(item -> {
			if (item.isEdible()) {
				FoodProperties food = item.getFoodProperties();
				if (food != null) {
					float compost = food.getNutrition() * food.getSaturationModifier() * 0.18F;
					ComposterBlockAccessor.callAdd(compost, item);
				}
			}
			if (item instanceof EndHammerItem) {
				ITEM_HAMMERS.add(item);
			}
		});
		ToolManagerImpl.tag(TagAPI.ITEM_HAMMERS).register(new ModdedToolsVanillaBlocksToolHandler(ITEM_HAMMERS));
		TagAPI.addTag(TagAPI.ITEM_HAMMERS, EndItems.AETERNIUM_HAMMER);
		
		TagAPI.addTag(
			TagAPI.BLOCK_GEN_TERRAIN,
			EndBlocks.ENDER_ORE,
			EndBlocks.BRIMSTONE
		);
		TagAPI.addTag(TagAPI.BLOCK_END_GROUND, EndBlocks.BRIMSTONE);
		TagAPI.addTag(BlockTags.ANVIL, EndBlocks.AETERNIUM_ANVIL);
		TagAPI.addTag(BlockTags.BEACON_BASE_BLOCKS, EndBlocks.AETERNIUM_BLOCK);
		TagAPI.addTag(ItemTags.BEACON_PAYMENT_ITEMS, EndItems.AETERNIUM_INGOT);
		TagAPI.addTag(
			TagAPI.BLOCK_DRAGON_IMMUNE,
			EndBlocks.ENDER_ORE,
			EndBlocks.ETERNAL_PEDESTAL,
			EndBlocks.FLAVOLITE_RUNED_ETERNAL,
			EndBlocks.FLAVOLITE_RUNED
		);
		TagAPI.addTag(TagAPI.ITEM_IRON_INGOTS, EndBlocks.THALLASIUM.ingot);
		
		TagAPI.addTag(ALLOYING_IRON, Items.IRON_ORE, Items.DEEPSLATE_IRON_ORE, Items.RAW_IRON);
		TagAPI.addTag(ALLOYING_GOLD, Items.GOLD_ORE, Items.DEEPSLATE_GOLD_ORE, Items.RAW_GOLD);
		TagAPI.addTag(ALLOYING_COPPER, Items.COPPER_ORE, Items.DEEPSLATE_COPPER_ORE, Items.RAW_COPPER);
	}
	
	public static void addTerrainTags(Registry<Biome> biomeRegistry) {
		biomeRegistry.forEach((biome) -> {
			if (biome.getBiomeCategory() == BiomeCategory.THEEND) {
				SurfaceBuilderConfiguration config = biome.getGenerationSettings().getSurfaceBuilderConfig();
				Block under = config.getUnderMaterial().getBlock();
				Block surface = config.getTopMaterial().getBlock();
				TagAPI.addTag(TagAPI.BLOCK_GEN_TERRAIN, under, surface);
				TagAPI.addTag(TagAPI.BLOCK_END_GROUND, surface);
			}
		});
		TagAPI.BLOCK_END_STONES.getValues().forEach(TagAPI::addEndGround);
	}
}
