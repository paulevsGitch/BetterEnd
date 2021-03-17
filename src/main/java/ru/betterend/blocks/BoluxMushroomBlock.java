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
import net.minecraft.world.World;
import ru.betterend.blocks.basis.EndPlantBlock;
import ru.betterend.registry.EndBlocks;

public class BoluxMushroomBlock extends EndPlantBlock {
	private static final VoxelShape SHAPE = Block.createCuboidShape(1, 0, 1, 15, 9, 15);
	
	public BoluxMushroomBlock() {
		super(10);
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.isOf(EndBlocks.RUTISCUS);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return SHAPE;
	}

	@Override
	public AbstractBlock.OffsetType getOffsetType() {
		return AbstractBlock.OffsetType.NONE;
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
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Lists.newArrayList(new ItemStack(this));
	}
}
