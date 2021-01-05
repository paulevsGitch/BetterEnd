package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties.HydraluxShape;
import ru.betterend.blocks.basis.UnderwaterPlantBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;
import ru.betterend.util.MHelper;

public class HydraluxBlock extends UnderwaterPlantBlock {
	public static final EnumProperty<HydraluxShape> SHAPE = BlockProperties.HYDRALUX_SHAPE;
	
	public HydraluxBlock() {
		super(FabricBlockSettings.of(Material.UNDERWATER_PLANT)
				.breakByTool(FabricToolTags.SHEARS)
				.sounds(BlockSoundGroup.WET_GRASS)
				.breakByHand(true)
				.luminance((state) -> { return state.get(SHAPE).hasGlow() ? 15 : 0; })
				.noCollision());
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockState down = world.getBlockState(pos.down());
		HydraluxShape shape = state.get(SHAPE);
		if (shape == HydraluxShape.FLOWER_BIG_TOP || shape == HydraluxShape.FLOWER_SMALL_TOP) {
			return down.isOf(this);
		}
		else if (shape == HydraluxShape.ROOTS) {
			return down.isOf(EndBlocks.SULPHURIC_ROCK.stone) && world.getBlockState(pos.up()).isOf(this);
		}
		else {
			return down.isOf(this) && world.getBlockState(pos.up()).isOf(this);
		}
	}
	
	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return false;
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return false;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(EndBlocks.HYDRALUX_SAPLING);
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		HydraluxShape shape = state.get(SHAPE);
		if (shape == HydraluxShape.FLOWER_BIG_BOTTOM || shape == HydraluxShape.FLOWER_SMALL_BOTTOM) {
			return Lists.newArrayList(new ItemStack(EndItems.HYDRALUX_PETAL, MHelper.randRange(1, 4, MHelper.RANDOM)));
		}
		else if (shape == HydraluxShape.ROOTS) {
			return Lists.newArrayList(new ItemStack(EndBlocks.HYDRALUX_SAPLING, MHelper.randRange(1, 2, MHelper.RANDOM)));
		}
		return Collections.emptyList();
	}
}
