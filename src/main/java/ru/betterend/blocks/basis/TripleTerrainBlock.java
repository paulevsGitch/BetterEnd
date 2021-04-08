package ru.betterend.blocks.basis;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.item.ItemPlacementContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.EndTerrainBlock;
import ru.betterend.patterns.Patterns;

public class TripleTerrainBlock extends EndTerrainBlock {
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;

	public TripleTerrainBlock(MaterialColor color) {
		super(color);
		this.setDefaultState(this.defaultBlockState().with(SHAPE, TripleShape.BOTTOM));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction dir = ctx.getSide();
		TripleShape shape = dir == Direction.UP ? TripleShape.BOTTOM
				: dir == Direction.DOWN ? TripleShape.TOP : TripleShape.MIDDLE;
		return this.defaultBlockState().with(SHAPE, shape);
	}

	@Override
	public String getModelPattern(String block) {
		String name = Registry.BLOCK.getKey(this).getPath();
		if (block.endsWith("_middle")) {
			return Patterns.createJson(Patterns.BLOCK_BASE, name + "_top", name + "_top");
		}
		Map<String, String> map = Maps.newHashMap();
		map.put("%top%", "betterend:block/" + name + "_top");
		map.put("%side%", "betterend:block/" + name + "_side");
		map.put("%bottom%", "minecraft:block/end_stone");
		return Patterns.createJson(Patterns.BLOCK_TOP_SIDE_BOTTOM, map);
	}

	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_TRIPLE_ROTATED_TOP;
	}

	@Override
	public ActionResult onUse(BlockState state, Level world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult hit) {
		TripleShape shape = state.getValue(SHAPE);
		if (shape == TripleShape.BOTTOM) {
			return super.onUse(state, world, pos, player, hand, hit);
		}
		return ActionResult.FAIL;
	}

	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		TripleShape shape = state.getValue(SHAPE);
		if (shape == TripleShape.BOTTOM) {
			super.randomTick(state, world, pos, random);
			return;
		} else if (random.nextInt(16) == 0) {
			boolean bottom = canSurviveBottom(world, pos);
			if (shape == TripleShape.TOP) {
				if (!bottom) {
					world.setBlockAndUpdate(pos, Blocks.END_STONE.defaultBlockState());
				}
			} else {
				boolean top = canSurvive(state, world, pos) || isMiddle(world.getBlockState(pos.up()));
				if (!top && !bottom) {
					world.setBlockAndUpdate(pos, Blocks.END_STONE.defaultBlockState());
				} else if (top && !bottom) {
					world.setBlockAndUpdate(pos, state.with(SHAPE, TripleShape.BOTTOM));
				} else if (!top && bottom) {
					world.setBlockAndUpdate(pos, state.with(SHAPE, TripleShape.TOP));
				}
			}
		}
	}

	protected boolean canSurviveBottom(WorldView world, BlockPos pos) {
		BlockPos blockPos = pos.below();
		BlockState blockState = world.getBlockState(blockPos);
		if (isMiddle(blockState)) {
			return true;
		} else if (blockState.getFluidState().getLevel() == 8) {
			return false;
		} else {
			return !blockState.isSideSolidFullSquare(world, blockPos, Direction.UP);
		}
	}

	protected boolean isMiddle(BlockState state) {
		return state.is(this) && state.getValue(SHAPE) == TripleShape.MIDDLE;
	}
}
