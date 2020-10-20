package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import ru.betterend.blocks.basis.BlockPlant;
import ru.betterend.registry.BlockRegistry;

public class BlockEndLotusFlower extends BlockPlant {
	private static final VoxelShape SHAPE_OUTLINE = Block.createCuboidShape(2, 0, 2, 14, 14, 14);
	private static final VoxelShape SHAPE_COLLISION = Block.createCuboidShape(0, 0, 0, 16, 2, 16);
	
	public BlockEndLotusFlower() {
		super(FabricBlockSettings.of(Material.PLANT).lightLevel(15));
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.isOf(BlockRegistry.END_LOTUS_FLOWER);
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
}
