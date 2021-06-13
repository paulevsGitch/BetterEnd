package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.bclib.api.TagAPI;
import ru.bclib.blocks.UnderwaterPlantBlock;
import ru.bclib.util.MHelper;
import ru.betterend.blocks.EndBlockProperties.HydraluxShape;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class HydraluxBlock extends UnderwaterPlantBlock {

	public static final EnumProperty<HydraluxShape> SHAPE = EndBlockProperties.HYDRALUX_SHAPE;
	
	public HydraluxBlock() {
		super(FabricBlockSettings.of(Material.WATER_PLANT)
				.breakByTool(FabricToolTags.SHEARS)
				.breakByHand(true)
				.sound(SoundType.WET_GRASS)
				.lightLevel((state) -> state.getValue(SHAPE).hasGlow() ? 15 : 0)
				.noCollission());
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		BlockState down = world.getBlockState(pos.below());
		HydraluxShape shape = state.getValue(SHAPE);
		if (shape == HydraluxShape.FLOWER_BIG_TOP || shape == HydraluxShape.FLOWER_SMALL_TOP) {
			return down.is(this);
		}
		else if (shape == HydraluxShape.ROOTS) {
			return down.is(EndBlocks.SULPHURIC_ROCK.stone) && world.getBlockState(pos.above()).is(this);
		}
		else {
			return down.is(this) && world.getBlockState(pos.above()).is(this);
		}
	}

	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(TagAPI.END_GROUND);
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
		return false;
	}

	@Override
	public boolean isBonemealSuccess(Level world, Random random, BlockPos pos, BlockState state) {
		return false;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
		return new ItemStack(EndBlocks.HYDRALUX_SAPLING);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		HydraluxShape shape = state.getValue(SHAPE);
		if (shape == HydraluxShape.FLOWER_BIG_BOTTOM || shape == HydraluxShape.FLOWER_SMALL_BOTTOM) {
			return Lists.newArrayList(new ItemStack(EndItems.HYDRALUX_PETAL, MHelper.randRange(1, 4, MHelper.RANDOM)));
		}
		else if (shape == HydraluxShape.ROOTS) {
			return Lists.newArrayList(new ItemStack(EndBlocks.HYDRALUX_SAPLING, MHelper.randRange(1, 2, MHelper.RANDOM)));
		}
		return Collections.emptyList();
	}
}
