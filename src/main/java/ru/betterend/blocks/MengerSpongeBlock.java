package ru.betterend.blocks;

import java.util.Queue;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import ru.betterend.blocks.basis.BaseBlockNotFull;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.registry.EndBlocks;

public class MengerSpongeBlock extends BaseBlockNotFull implements IRenderTypeable {
	public MengerSpongeBlock() {
		super(FabricBlockSettings.copyOf(Blocks.SPONGE).nonOpaque());
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (absorbWater(world, pos)) {
			world.setBlockState(pos, EndBlocks.MENGER_SPONGE_WET.getDefaultState());
		}
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (absorbWater(world, pos)) {
			return EndBlocks.MENGER_SPONGE_WET.getDefaultState();
		}
		return state;
	}

	private boolean absorbWater(WorldAccess world, BlockPos pos) {
		Queue<Pair<BlockPos, Integer>> queue = Lists.newLinkedList();
		queue.add(new Pair<BlockPos, Integer>(pos, 0));
		int i = 0;

		while (!queue.isEmpty()) {
			Pair<BlockPos, Integer> pair = queue.poll();
			BlockPos blockPos = (BlockPos) pair.getLeft();
			int j = (Integer) pair.getRight();
			Direction[] var8 = Direction.values();
			int var9 = var8.length;

			for (int var10 = 0; var10 < var9; ++var10) {
				Direction direction = var8[var10];
				BlockPos blockPos2 = blockPos.offset(direction);
				BlockState blockState = world.getBlockState(blockPos2);
				FluidState fluidState = world.getFluidState(blockPos2);
				Material material = blockState.getMaterial();
				if (fluidState.isIn(FluidTags.WATER)) {
					if (blockState.getBlock() instanceof FluidDrainable && ((FluidDrainable) blockState.getBlock()).tryDrainFluid(world, blockPos2, blockState) != Fluids.EMPTY) {
						++i;
						if (j < 6) {
							queue.add(new Pair<BlockPos, Integer>(blockPos2, j + 1));
						}
					}
					else if (blockState.getBlock() instanceof FluidBlock) {
						world.setBlockState(blockPos2, Blocks.AIR.getDefaultState(), 3);
						++i;
						if (j < 6) {
							queue.add(new Pair<BlockPos, Integer>(blockPos2, j + 1));
						}
					}
					else if (material == Material.UNDERWATER_PLANT || material == Material.REPLACEABLE_UNDERWATER_PLANT) {
						BlockEntity blockEntity = blockState.getBlock().hasBlockEntity() ? world.getBlockEntity(blockPos2) : null;
						dropStacks(blockState, world, blockPos2, blockEntity);
						world.setBlockState(blockPos2, Blocks.AIR.getDefaultState(), 3);
						++i;
						if (j < 6) {
							queue.add(new Pair<BlockPos, Integer>(blockPos2, j + 1));
						}
					}
				}
			}

			if (i > 64) {
				break;
			}
		}

		return i > 0;
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
}
