package ru.betterend.blocks.basis;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import ru.bclib.util.MHelper;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;

public class EndLeavesBlock extends LeavesBlock implements BlockModelProvider, IRenderTypeable {
	private final Block sapling;
	
	public EndLeavesBlock(Block sapling, MaterialColor color) {
		super(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)
				.materialColor(color)
				.breakByTool(FabricToolTags.HOES)
				.breakByTool(FabricToolTags.SHEARS)
				.breakByHand(true)
				.isValidSpawn((state, world, pos, type) -> false)
				.isSuffocating((state, world, pos) -> false)
				.isViewBlocking((state, world, pos) -> false));
		this.sapling = sapling;
	}
	
	public EndLeavesBlock(Block sapling, MaterialColor color, int light) {
		super(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)
				.materialColor(color)
				.luminance(light)
				.breakByTool(FabricToolTags.HOES)
				.breakByTool(FabricToolTags.SHEARS)
				.isValidSpawn((state, world, pos, type) -> false)
				.isSuffocating((state, world, pos) -> false)
				.isViewBlocking((state, world, pos) -> false));
		this.sapling = sapling;
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.getParameter(LootContextParams.TOOL);
		if (tool != null) {
			if (tool.getItem().is(FabricToolTags.SHEARS) || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
				return Collections.singletonList(new ItemStack(this));
			}
			int fortune = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
			if (MHelper.RANDOM.nextInt(16) <= fortune) {
				return Lists.newArrayList(new ItemStack(sapling));
			}
			return Lists.newArrayList();
		}
		return MHelper.RANDOM.nextInt(16) == 0 ? Lists.newArrayList(new ItemStack(sapling)) : Lists.newArrayList();
	}

	@Override
	public BlockModel getItemModel(ResourceLocation resourceLocation) {
		return getBlockModel(resourceLocation, defaultBlockState());
	}
}
