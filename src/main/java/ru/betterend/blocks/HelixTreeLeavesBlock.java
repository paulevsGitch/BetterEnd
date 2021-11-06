package ru.betterend.blocks;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import ru.bclib.api.TagAPI;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.interfaces.CustomColorProvider;
import ru.bclib.util.ColorUtil;
import ru.bclib.util.MHelper;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;

import java.util.Collections;
import java.util.List;

public class HelixTreeLeavesBlock extends BaseBlock implements CustomColorProvider {
	public static final IntegerProperty COLOR = EndBlockProperties.COLOR;
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(0);
	
	public HelixTreeLeavesBlock() {
		super(FabricBlockSettings
			.of(Material.LEAVES)
			.mapColor(MaterialColor.COLOR_ORANGE)
			.breakByTool(FabricToolTags.SHEARS)
			.sound(SoundType.WART_BLOCK)
			.sound(SoundType.GRASS)
			.strength(0.2F)
		);

		TagAPI.addTags(this, TagAPI.BLOCK_LEAVES);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(COLOR);
	}
	
	@Override
	public BlockColor getProvider() {
		return (state, world, pos, tintIndex) -> {
			return ColorUtil.color(237, getGreen(state.getValue(COLOR)), 20);
		};
	}
	
	@Override
	public ItemColor getItemProvider() {
		return (stack, tintIndex) -> {
			return ColorUtil.color(237, getGreen(4), 20);
		};
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		double px = ctx.getClickedPos().getX() * 0.1;
		double py = ctx.getClickedPos().getY() * 0.1;
		double pz = ctx.getClickedPos().getZ() * 0.1;
		return this.defaultBlockState().setValue(COLOR, MHelper.floor(NOISE.eval(px, py, pz) * 3.5 + 4));
	}
	
	private int getGreen(int color) {
		float delta = color / 7F;
		return (int) Mth.lerp(delta, 80, 158);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.getParameter(LootContextParams.TOOL);
		if (tool != null) {
			if (tool.is(FabricToolTags.SHEARS) || tool.isCorrectToolForDrops(state) || EnchantmentHelper.getItemEnchantmentLevel(
				Enchantments.SILK_TOUCH,
				tool
			) > 0) {
				return Collections.singletonList(new ItemStack(this));
			}
			int fortune = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
			if (MHelper.RANDOM.nextInt(16) <= fortune) {
				return Lists.newArrayList(new ItemStack(EndBlocks.HELIX_TREE_SAPLING));
			}
			return Lists.newArrayList();
		}
		return MHelper.RANDOM.nextInt(32) == 0 ? Lists.newArrayList(new ItemStack(EndBlocks.HELIX_TREE_SAPLING)) : Lists
			.newArrayList();
	}
}
