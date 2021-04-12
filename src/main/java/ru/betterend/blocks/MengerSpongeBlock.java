package ru.betterend.blocks;

import java.util.Queue;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FluidBlock;
import net.minecraft.world.level.block.FluidDrainable;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import ru.betterend.blocks.basis.BlockBaseNotFull;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.registry.EndBlocks;

public class MengerSpongeBlock extends BlockBaseNotFull implements IRenderTypeable {
	public MengerSpongeBlock() {
		super(FabricBlockSettings.copyOf(Blocks.SPONGE).nonOpaque());
	}

	@Override
	public void onBlockAdded(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		if (absorbWater(world, pos)) {
			world.setBlockAndUpdate(pos, EndBlocks.MENGER_SPONGE_WET.defaultBlockState());
		}
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world,
			BlockPos pos, BlockPos neighborPos) {
		if (absorbWater(world, pos)) {
			return EndBlocks.MENGER_SPONGE_WET.defaultBlockState();
		}
		return state;
	}

	private boolean absorbWater(LevelAccessor world, BlockPos pos) {
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
					if (blockState.getBlock() instanceof FluidDrainable && ((FluidDrainable) blockState.getBlock())
							.tryDrainFluid(world, blockPos2, blockState) != Fluids.EMPTY) {
						++i;
						if (j < 6) {
							queue.add(new Pair<BlockPos, Integer>(blockPos2, j + 1));
						}
					} else if (blockState.getBlock() instanceof FluidBlock) {
						world.setBlockAndUpdate(blockPos2, Blocks.AIR.defaultBlockState(), 3);
						++i;
						if (j < 6) {
							queue.add(new Pair<BlockPos, Integer>(blockPos2, j + 1));
						}
					} else if (material == Material.UNDERWATER_PLANT
							|| material == Material.REPLACEABLE_UNDERWATER_PLANT) {
						BlockEntity blockEntity = blockState.getBlock().hasBlockEntity()
								? world.getBlockEntity(blockPos2)
								: null;
						dropStacks(blockState, world, blockPos2, blockEntity);
						world.setBlockAndUpdate(blockPos2, Blocks.AIR.defaultBlockState(), 3);
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
