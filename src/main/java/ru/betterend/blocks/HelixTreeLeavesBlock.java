package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.MathHelper;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.interfaces.IColorProvider;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.MHelper;

public class HelixTreeLeavesBlock extends BlockBase implements IColorProvider {
	public static final IntProperty COLOR = BlockProperties.COLOR;
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(0);
	
	public HelixTreeLeavesBlock() {
		super(FabricBlockSettings.of(Material.LEAVES)
				.materialColor(MaterialColor.ORANGE)
				.breakByTool(FabricToolTags.SHEARS)
				.sounds(BlockSoundGroup.WART_BLOCK)
				.sounds(BlockSoundGroup.GRASS)
				.strength(0.2F));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(COLOR);
	}

	@Override
	public BlockColorProvider getProvider() {
		return (state, world, pos, tintIndex) -> {
			return MHelper.color(237, getGreen(state.get(COLOR)), 20);
		};
	}

	@Override
	public ItemColorProvider getItemProvider() {
		return (stack, tintIndex) -> {
			return MHelper.color(237, getGreen(4), 20);
		};
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		double px = ctx.getBlockPos().getX() * 0.1;
		double py = ctx.getBlockPos().getY() * 0.1;
		double pz = ctx.getBlockPos().getZ() * 0.1;
		return this.getDefaultState().with(COLOR, MHelper.floor(NOISE.eval(px, py, pz) * 3.5 + 4));
	}
	
	private int getGreen(int color) {
		float delta = color / 7F;
		return (int) MathHelper.lerp(delta, 80, 158);
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
				return Lists.newArrayList(new ItemStack(EndBlocks.HELIX_TREE_SAPLING));
			}
			return Lists.newArrayList();
		}
		return MHelper.RANDOM.nextInt(32) == 0 ? Lists.newArrayList(new ItemStack(EndBlocks.HELIX_TREE_SAPLING)) : Lists.newArrayList();
	}
}
