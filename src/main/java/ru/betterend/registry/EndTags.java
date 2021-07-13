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
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderConfiguration;
import net.minecraft.world.level.material.Material;
import ru.bclib.api.BonemealAPI;
import ru.bclib.api.TagAPI;
import ru.bclib.blocks.BaseVineBlock;
import ru.bclib.blocks.SimpleLeavesBlock;
import ru.bclib.util.TagHelper;
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
	public static final Tag.Named<Block> END_STONES = TagAPI.makeCommonBlockTag("end_stones");
	
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
			
			if (material.equals(Material.STONE)) {
				TagHelper.addTag(TagAPI.MINEABLE_PICKAXE, block);
			}
			else if (material.equals(Material.WOOD)) {
				TagHelper.addTag(TagAPI.MINEABLE_AXE, block);
			}
			else if (material.equals(Material.LEAVES) || material.equals(Material.PLANT) || material.equals(Material.WATER_PLANT)) {
				TagHelper.addTag(TagAPI.MINEABLE_HOE, block);
			}
			else if (material.equals(Material.SAND)) {
				TagHelper.addTag(TagAPI.MINEABLE_HOE, block);
			}
			
			if (block instanceof EndTerrainBlock) {
				TagAPI.addEndGround(block);
				TagHelper.addTag(BlockTags.NYLIUM, block);
				BonemealAPI.addSpreadableBlock(block);
			}
			else if (block instanceof LeavesBlock || block instanceof SimpleLeavesBlock) {
				TagHelper.addTag(BlockTags.LEAVES, block);
				ComposterBlockAccessor.callAdd(0.3F, block);
			}
			else if (block instanceof BaseVineBlock) {
				TagHelper.addTag(BlockTags.CLIMBABLE, block);
			}
			else if (block instanceof PedestalBlock) {
				TagHelper.addTag(PEDESTALS, block);
			}
			
			Material mat = block.defaultBlockState().getMaterial();
			if (mat.equals(Material.PLANT) || mat.equals(Material.REPLACEABLE_PLANT)) {
				ComposterBlockAccessor.callAdd(0.1F, block);
			}
		});
		TagAPI.addEndGround(EndBlocks.CAVE_MOSS);
		TagHelper.addTag(BlockTags.NYLIUM, EndBlocks.CAVE_MOSS);
		BonemealAPI.addSpreadableBlock(EndBlocks.CAVE_MOSS);
		
		List<Item> hammers = Lists.newArrayList();
		EndItems.getModItems(BetterEnd.MOD_ID).forEach(item -> {
			if (item.isEdible()) {
				FoodProperties food = item.getFoodProperties();
				if (food != null) {
					float compost = food.getNutrition() * food.getSaturationModifier() * 0.18F;
					ComposterBlockAccessor.callAdd(compost, item);
				}
			}
			if (item instanceof EndHammerItem) {
				hammers.add(item);
			}
		});
		ToolManagerImpl.tag(TagAPI.HAMMERS).register(new ModdedToolsVanillaBlocksToolHandler(hammers));
		TagHelper.addTag(TagAPI.HAMMERS, EndItems.AETERNIUM_HAMMER);
		
		TagHelper.addTag(TagAPI.GEN_TERRAIN, EndBlocks.ENDER_ORE, EndBlocks.FLAVOLITE.stone, EndBlocks.VIOLECITE.stone, EndBlocks.SULPHURIC_ROCK.stone, EndBlocks.BRIMSTONE, EndBlocks.VIRID_JADESTONE.stone, EndBlocks.AZURE_JADESTONE.stone, EndBlocks.SANDY_JADESTONE.stone);
		TagHelper.addTag(TagAPI.END_GROUND, EndBlocks.SULPHURIC_ROCK.stone, EndBlocks.BRIMSTONE);
		TagHelper.addTag(BlockTags.ANVIL, EndBlocks.AETERNIUM_ANVIL);
		TagHelper.addTag(BlockTags.BEACON_BASE_BLOCKS, EndBlocks.AETERNIUM_BLOCK);
		TagHelper.addTag(ItemTags.BEACON_PAYMENT_ITEMS, EndItems.AETERNIUM_INGOT);
		TagHelper.addTag(TagAPI.DRAGON_IMMUNE, EndBlocks.ENDER_ORE, EndBlocks.ETERNAL_PEDESTAL, EndBlocks.FLAVOLITE_RUNED_ETERNAL, EndBlocks.FLAVOLITE_RUNED);
		TagHelper.addTag(TagAPI.IRON_INGOTS, EndBlocks.THALLASIUM.ingot);
		
		TagHelper.addTag(ALLOYING_IRON, Items.IRON_ORE, Items.DEEPSLATE_IRON_ORE, Items.RAW_IRON);
		TagHelper.addTag(ALLOYING_GOLD, Items.GOLD_ORE, Items.DEEPSLATE_GOLD_ORE, Items.RAW_GOLD);
		TagHelper.addTag(ALLOYING_COPPER, Items.COPPER_ORE, Items.DEEPSLATE_COPPER_ORE, Items.RAW_COPPER);
	}
	
	public static void addTerrainTags(Registry<Biome> biomeRegistry) {
		biomeRegistry.forEach((biome) -> {
			if (biome.getBiomeCategory() == BiomeCategory.THEEND) {
				SurfaceBuilderConfiguration config = biome.getGenerationSettings().getSurfaceBuilderConfig();
				Block under = config.getUnderMaterial().getBlock();
				Block surface = config.getTopMaterial().getBlock();
				TagHelper.addTag(TagAPI.GEN_TERRAIN, under, surface);
				TagHelper.addTag(TagAPI.END_GROUND, surface);
			}
		});
		END_STONES.getValues().forEach(TagAPI::addEndGround);
	}
}
