package ru.betterend.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import ru.betterend.blocks.basis.EndPlantBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.util.BlocksHelper;

public class SmallAmaranitaBlock extends EndPlantBlock {
	private static final VoxelShape SHAPE = Block.createCuboidShape(4, 0, 4, 12, 10, 12);
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.isOf(EndBlocks.SANGNUM) || state.isOf(EndBlocks.MOSSY_OBSIDIAN) || state.isOf(EndBlocks.MOSSY_BONE);
	}
	
	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		BlockPos bigPos = growBig(world, pos);
		if (bigPos != null) {
			if (EndFeatures.GIGANTIC_AMARANITA.getFeature().generate(world, null, random, bigPos, null)) {
				replaceMushroom(world, bigPos);
				replaceMushroom(world, bigPos.south());
				replaceMushroom(world, bigPos.east());
				replaceMushroom(world, bigPos.south().east());
			}
			return;
		}
		EndFeatures.LARGE_AMARANITA.getFeature().generate(world, null, random, pos, null);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		Vec3d vec3d = state.getModelOffset(view, pos);
		return SHAPE.offset(vec3d.x, vec3d.y, vec3d.z);
	}
	
	private BlockPos growBig(ServerWorld world, BlockPos pos) {
		for (int x = -1; x < 2; x++) {
			for (int z = -1; z < 2; z++) {
				BlockPos p = pos.add(x, 0, z);
				if (checkFrame(world, p)) {
					return p;
				}
			}
		}
		return null;
	}
	
	private boolean checkFrame(ServerWorld world, BlockPos pos) {
		return world.getBlockState(pos).isOf(this) &&
				world.getBlockState(pos.south()).isOf(this) &&
				world.getBlockState(pos.east()).isOf(this) &&
				world.getBlockState(pos.south().east()).isOf(this);
	}
	
	private void replaceMushroom(ServerWorld world, BlockPos pos) {
		if (world.getBlockState(pos).isOf(this)) {
			BlocksHelper.setWithUpdate(world, pos, Blocks.AIR);
		}
	}
	
	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return random.nextInt(8) == 0;
	}
}
