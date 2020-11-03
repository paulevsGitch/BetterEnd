package ru.betterend.blocks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import ru.betterend.blocks.basis.BlockPlantWithAge;
import ru.betterend.registry.EndItems;
import ru.betterend.util.MHelper;

public class BlockShadowBerry extends BlockPlantWithAge {
	private static final VoxelShape SHAPE = Block.createCuboidShape(1, 0, 1, 15, 8, 15);
	
	@Override
	public void growAdult(StructureWorldAccess world, Random random, BlockPos pos) {}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		if (state.get(AGE) < 3) {
			return Lists.newArrayList(new ItemStack(this));
		}
		else {
			return Lists.newArrayList(new ItemStack(this), new ItemStack(EndItems.SHADOW_BERRY_RAW, MHelper.randRange(1, 3, MHelper.RANDOM)));
		}
	}
	
	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return state.get(AGE) < 3;
	}
	
	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return state.get(AGE) < 3;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return SHAPE;
	}
	
	@Override
	public AbstractBlock.OffsetType getOffsetType() {
		return AbstractBlock.OffsetType.NONE;
	}
}
