package ru.betterend.registry;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockAccessor;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockSettingsAccessor;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import ru.bclib.api.BonemealAPI;
import ru.bclib.api.ComposterAPI;
import ru.bclib.api.tag.CommonItemTags;
import ru.bclib.api.tag.NamedBlockTags;
import ru.bclib.api.tag.NamedCommonBlockTags;
import ru.bclib.api.tag.NamedCommonItemTags;
import ru.bclib.api.tag.NamedItemTags;
import ru.bclib.api.tag.NamedMineableTags;
import ru.bclib.api.tag.TagAPI;
import ru.bclib.blocks.BaseVineBlock;
import ru.bclib.blocks.SimpleLeavesBlock;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.basis.EndTerrainBlock;
import ru.betterend.blocks.basis.PedestalBlock;
import ru.betterend.item.tool.EndHammerItem;
import ru.betterend.world.biome.EndBiome;

import java.util.List;

public class EndTags {
	// Table with common (c) tags:
	// https://fabricmc.net/wiki/tutorial:tags
	
	// Block Tags
	public static final TagKey<Block> PEDESTALS = TagAPI.makeBlockTag(BetterEnd.MOD_ID, "pedestal");

	// Item Tags
	public static final TagKey<Item> ALLOYING_IRON = TagAPI.makeItemTag(BetterEnd.MOD_ID, "alloying_iron");
	public static final TagKey<Item> ALLOYING_GOLD = TagAPI.makeItemTag(BetterEnd.MOD_ID, "alloying_gold");
	public static final TagKey<Item> ALLOYING_COPPER = TagAPI.makeItemTag(BetterEnd.MOD_ID, "alloying_copper");
	
	public static void register() {
		addEndGround(EndBlocks.THALLASIUM.ore);
		addEndGround(EndBlocks.ENDSTONE_DUST);
		addEndGround(EndBlocks.AMBER_ORE);
		
		EndBlocks.getModBlocks().forEach(block -> {
			Properties properties = ((AbstractBlockAccessor) block).getSettings();
			Material material = ((AbstractBlockSettingsAccessor) properties).getMaterial();
			final Item item = block.asItem();
			
			if (material.equals(Material.STONE) || material.equals(Material.METAL) || material.equals(Material.HEAVY_METAL)) {
				TagAPI.addBlockTag(NamedMineableTags.PICKAXE, block);
			}
			else if (material.equals(Material.WOOD)) {
				TagAPI.addBlockTag(NamedMineableTags.AXE, block);
			}
			else if (material.equals(Material.LEAVES) || material.equals(Material.PLANT) || material.equals(Material.WATER_PLANT) || material.equals(Material.SPONGE)) {
				TagAPI.addBlockTag(NamedMineableTags.HOE, block);
			}
			else if (material.equals(Material.SAND)) {
				TagAPI.addBlockTag(NamedMineableTags.SHOVEL, block);
			}
			
			if (block instanceof EndTerrainBlock) {
				addEndGround(block);
				TagAPI.addBlockTag(NamedBlockTags.NYLIUM, block);
				BonemealAPI.addSpreadableBlock(block, Blocks.END_STONE);
			}
			else if (block instanceof LeavesBlock || block instanceof SimpleLeavesBlock) {
				TagAPI.addBlockTag(NamedBlockTags.LEAVES, block);
				ComposterAPI.allowCompost(0.3f, item);
			}
			else if (block instanceof BaseVineBlock) {
				TagAPI.addBlockTag(NamedBlockTags.CLIMBABLE, block);
			}
			else if (block instanceof PedestalBlock) {
				TagAPI.addBlockTag(PEDESTALS, block);
			}
			
			Material mat = block.defaultBlockState().getMaterial();
			if (mat.equals(Material.PLANT) || mat.equals(Material.REPLACEABLE_PLANT)) {
				ComposterAPI.allowCompost(0.1F, item);
			}
		});
		addEndGround(EndBlocks.CAVE_MOSS);
		TagAPI.addBlockTag(NamedBlockTags.NYLIUM, EndBlocks.CAVE_MOSS);
		BonemealAPI.addSpreadableBlock(EndBlocks.CAVE_MOSS, Blocks.END_STONE);
		BonemealAPI.addSpreadableBlock(EndBlocks.MOSSY_OBSIDIAN, Blocks.OBSIDIAN);
		BonemealAPI.addSpreadableBlock(EndBlocks.MOSSY_DRAGON_BONE, EndBlocks.DRAGON_BONE_BLOCK);
		
		List<Item> ITEM_HAMMERS = Lists.newArrayList();
		EndItems.getModItems().forEach(item -> {
			if (item.isEdible()) {
				FoodProperties food = item.getFoodProperties();
				if (food != null) {
					float compost = food.getNutrition() * food.getSaturationModifier() * 0.18F;
					ComposterAPI.allowCompost(compost, item);
				}
			}
			if (item instanceof EndHammerItem) {
				ITEM_HAMMERS.add(item);
			}
		});
		ToolManagerImpl.tag(CommonItemTags.HAMMERS).register(new ModdedToolsVanillaBlocksToolHandler(ITEM_HAMMERS));
		
		TagAPI.addBlockTag(
			NamedCommonBlockTags.GEN_END_STONES,
			EndBlocks.ENDER_ORE,
			EndBlocks.BRIMSTONE
		);
		TagAPI.addBlockTag(NamedCommonBlockTags.END_STONES, EndBlocks.BRIMSTONE);
		TagAPI.addBlockTag(NamedBlockTags.ANVIL, EndBlocks.AETERNIUM_ANVIL);
		TagAPI.addBlockTag(NamedBlockTags.BEACON_BASE_BLOCKS, EndBlocks.AETERNIUM_BLOCK);
		TagAPI.addItemTag(NamedItemTags.BEACON_PAYMENT_ITEMS, EndItems.AETERNIUM_INGOT);
		TagAPI.addBlockTag(
			NamedCommonBlockTags.DRAGON_IMMUNE,
			EndBlocks.ENDER_ORE,
			EndBlocks.ETERNAL_PEDESTAL,
			EndBlocks.FLAVOLITE_RUNED_ETERNAL,
			EndBlocks.FLAVOLITE_RUNED
		);
		TagAPI.addItemTag(NamedCommonItemTags.IRON_INGOTS, EndBlocks.THALLASIUM.ingot);
		
		TagAPI.addItemTag(ALLOYING_IRON.getName(), Items.IRON_ORE, Items.DEEPSLATE_IRON_ORE, Items.RAW_IRON);
		TagAPI.addItemTag(ALLOYING_GOLD.getName(), Items.GOLD_ORE, Items.DEEPSLATE_GOLD_ORE, Items.RAW_GOLD);
		TagAPI.addItemTag(ALLOYING_COPPER.getName(), Items.COPPER_ORE, Items.DEEPSLATE_COPPER_ORE, Items.RAW_COPPER);
	}
	
	public static void addEndGround(Block bl){
		TagAPI.addBlockTag(NamedCommonBlockTags.END_STONES, bl);
	}
	
	public static void addBiomeSurfaceToEndGroup(EndBiome b){
		addEndGround(b.getTopMaterial().getBlock());
		addEndGround(b.getAltTopMaterial().getBlock());
		addEndGround(b.getUnderMaterial().getBlock());
	}
	
	// TODO make getter for biome top blocks
	public static void addTerrainTags(Registry<Biome> biomeRegistry) {
		/*biomeRegistry.forEach((biome) -> {
			if (biome.getBiomeCategory() == BiomeCategory.THEEND) {
				SurfaceBuilderConfiguration config = biome.getGenerationSettings().getSurfaceBuilderConfig();
				Block under = config.getUnderMaterial().getBlock();
				Block surface = config.getTopMaterial().getBlock();
				TagAPI.addTag(CommonBlockTags.GEN_END_STONES, under, surface);
				TagAPI.addTag(CommonBlockTags.END_STONES, surface);
			}
		});
		TagAPI.BLOCK_END_STONES.getValues().forEach(TagAPI::addEndGround);*/
	}
}
