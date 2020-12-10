package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.MathHelper;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.interfaces.IColorProvider;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.MHelper;

public class BlockHelixTreeLeaves extends BlockBase implements IColorProvider {
	public static final IntProperty COLOR = IntProperty.of("color", 0, 7);
	
	public BlockHelixTreeLeaves() {
		super(FabricBlockSettings.of(Material.LEAVES)
				.strength(0.2F)
				.sounds(BlockSoundGroup.GRASS)
				.materialColor(MaterialColor.ORANGE));
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(EndBlocks.HELIX_TREE_SAPLING));
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
		return this.getDefaultState().with(COLOR, MHelper.randRange(3, 5, ctx.getWorld().getRandom()));
	}
	
	private int getGreen(int color) {
		float delta = color / 7F;
		return (int) MathHelper.lerp(delta, 35, 102);
	}
}
