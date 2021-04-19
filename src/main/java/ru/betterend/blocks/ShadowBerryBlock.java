package ru.betterend.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.betterend.blocks.basis.EndCropBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class ShadowBerryBlock extends EndCropBlock {
	private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 8, 15);
	
	public ShadowBerryBlock() {
		super(EndItems.SHADOW_BERRY_RAW, EndBlocks.SHADOW_GRASS);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return SHAPE;
	}
}
