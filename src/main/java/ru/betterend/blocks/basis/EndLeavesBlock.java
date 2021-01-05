package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.MaterialColor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;
import ru.betterend.util.MHelper;

public class EndLeavesBlock extends LeavesBlock implements BlockPatterned, IRenderTypeable {
	private final Block sapling;
	
	public EndLeavesBlock(Block sapling, MaterialColor color) {
		super(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)
				.allowsSpawning((state, world, pos, type) -> { return false; })
				.suffocates((state, world, pos) -> { return false; })
				.blockVision((state, world, pos) -> { return false; })
				.materialColor(color)
				.breakByTool(FabricToolTags.SHEARS));
		this.sapling = sapling;
	}
	
	public EndLeavesBlock(Block sapling, MaterialColor color, int light) {
		super(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)
				.allowsSpawning((state, world, pos, type) -> { return false; })
				.suffocates((state, world, pos) -> { return false; })
				.blockVision((state, world, pos) -> { return false; })
				.materialColor(color)
				.luminance(light)
				.breakByTool(FabricToolTags.SHEARS));
		this.sapling = sapling;
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		String blockId = Registry.BLOCK.getId(this).getPath();
		return Patterns.createJson(data, blockId, blockId);
	}
	
	@Override
	public String getModelPattern(String block) {
		String blockId = Registry.BLOCK.getId(this).getPath();
		return Patterns.createJson(Patterns.BLOCK_BASE, blockId, blockId);
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_SIMPLE;
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.get(LootContextParameters.TOOL);
		if (tool != null) {
			if (tool.getItem().isIn(FabricToolTags.SHEARS) || tool.isEffectiveOn(state) || EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, tool) > 0) {
				return Collections.singletonList(new ItemStack(this));
			}
			int fortune = EnchantmentHelper.getLevel(Enchantments.FORTUNE, tool);
			if (MHelper.RANDOM.nextInt(16) <= fortune) {
				return Lists.newArrayList(new ItemStack(sapling));
			}
			return Lists.newArrayList();
		}
		return MHelper.RANDOM.nextInt(16) == 0 ? Lists.newArrayList(new ItemStack(sapling)) : Lists.newArrayList();
	}
}
