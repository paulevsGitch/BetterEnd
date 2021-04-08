package ru.betterend.blocks;

import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShapeContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import ru.betterend.blocks.basis.EndPlantBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.util.BlocksHelper;

public class SmallAmaranitaBlock extends EndPlantBlock {
	private static final VoxelShape SHAPE = Block.createCuboidShape(4, 0, 4, 12, 10, 12);

	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(EndBlocks.SANGNUM) || state.is(EndBlocks.MOSSY_OBSIDIAN)
				|| state.is(EndBlocks.MOSSY_DRAGON_BONE);
	}

	@Override
	public void grow(ServerLevel world, Random random, BlockPos pos, BlockState state) {
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

	private BlockPos growBig(ServerLevel world, BlockPos pos) {
		for (int x = -1; x < 2; x++) {
			for (int z = -1; z < 2; z++) {
				BlockPos p = pos.offset(x, 0, z);
				if (checkFrame(world, p)) {
					return p;
				}
			}
		}
		return null;
	}

	private boolean checkFrame(ServerLevel world, BlockPos pos) {
		return world.getBlockState(pos).is(this) && world.getBlockState(pos.south()).is(this)
				&& world.getBlockState(pos.east()).is(this) && world.getBlockState(pos.south().east()).is(this);
	}

	private void replaceMushroom(ServerLevel world, BlockPos pos) {
		if (world.getBlockState(pos).is(this)) {
			BlocksHelper.setWithUpdate(world, pos, Blocks.AIR);
		}
	}

	@Override
	public boolean canGrow(Level world, Random random, BlockPos pos, BlockState state) {
		return random.nextInt(8) == 0;
	}
}
