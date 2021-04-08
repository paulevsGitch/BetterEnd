package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties.HydraluxShape;
import ru.betterend.blocks.basis.UnderwaterPlantBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;
import ru.betterend.util.MHelper;

public class HydraluxBlock extends UnderwaterPlantBlock {
	public static final EnumProperty<HydraluxShape> SHAPE = BlockProperties.HYDRALUX_SHAPE;

	public HydraluxBlock() {
		super(FabricBlockSettings.of(Material.UNDERWATER_PLANT).breakByTool(FabricToolTags.SHEARS)
				.sounds(SoundType.WET_GRASS).breakByHand(true).luminance((state) -> {
					return state.getValue(SHAPE).hasGlow() ? 15 : 0;
				}).noCollision());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockState down = world.getBlockState(pos.below());
		HydraluxShape shape = state.getValue(SHAPE);
		if (shape == HydraluxShape.FLOWER_BIG_TOP || shape == HydraluxShape.FLOWER_SMALL_TOP) {
			return down.is(this);
		} else if (shape == HydraluxShape.ROOTS) {
			return down.is(EndBlocks.SULPHURIC_ROCK.stone) && world.getBlockState(pos.up()).is(this);
		} else {
			return down.is(this) && world.getBlockState(pos.up()).is(this);
		}
	}

	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return false;
	}

	@Override
	public boolean canGrow(Level world, Random random, BlockPos pos, BlockState state) {
		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(EndBlocks.HYDRALUX_SAPLING);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		HydraluxShape shape = state.getValue(SHAPE);
		if (shape == HydraluxShape.FLOWER_BIG_BOTTOM || shape == HydraluxShape.FLOWER_SMALL_BOTTOM) {
			return Lists.newArrayList(new ItemStack(EndItems.HYDRALUX_PETAL, MHelper.randRange(1, 4, MHelper.RANDOM)));
		} else if (shape == HydraluxShape.ROOTS) {
			return Lists
					.newArrayList(new ItemStack(EndBlocks.HYDRALUX_SAPLING, MHelper.randRange(1, 2, MHelper.RANDOM)));
		}
		return Collections.emptyList();
	}
}
