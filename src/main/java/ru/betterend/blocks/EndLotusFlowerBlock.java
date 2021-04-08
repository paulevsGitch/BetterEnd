package ru.betterend.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.AbstractBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.ShapeContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.core.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import ru.betterend.blocks.basis.EndPlantBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.MHelper;

public class EndLotusFlowerBlock extends EndPlantBlock {
	private static final VoxelShape SHAPE_OUTLINE = Block.createCuboidShape(2, 0, 2, 14, 14, 14);
	private static final VoxelShape SHAPE_COLLISION = Block.createCuboidShape(0, 0, 0, 16, 2, 16);

	public EndLotusFlowerBlock() {
		super(FabricBlockSettings.of(Material.PLANT).nonOpaque().luminance(15));
	}

	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(EndBlocks.END_LOTUS_STEM);
	}

	@Override
	public AbstractBlock.OffsetType getOffsetType() {
		return AbstractBlock.OffsetType.NONE;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return SHAPE_OUTLINE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return SHAPE_COLLISION;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		int count = MHelper.randRange(1, 2, MHelper.RANDOM);
		return Lists.newArrayList(new ItemStack(EndBlocks.END_LOTUS_SEED, count));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(EndBlocks.END_LOTUS_SEED);
	}
}
