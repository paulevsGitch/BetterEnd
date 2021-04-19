package ru.betterend.blocks.basis;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.EndTerrainBlock;
import ru.betterend.patterns.Patterns;

public class TripleTerrainBlock extends EndTerrainBlock {
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	
	public TripleTerrainBlock(MaterialColor color) {
		super(color);
		this.registerDefaultState(this.defaultBlockState().setValue(SHAPE, TripleShape.BOTTOM));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		Direction dir = ctx.getClickedFace();
		TripleShape shape = dir == Direction.UP ? TripleShape.BOTTOM : dir == Direction.DOWN ? TripleShape.TOP : TripleShape.MIDDLE;
		return this.defaultBlockState().setValue(SHAPE, shape);
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
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		TripleShape shape = state.getValue(SHAPE);
		if (shape == TripleShape.BOTTOM) {
			return super.use(state, world, pos, player, hand, hit);
		}
		return InteractionResult.FAIL;
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		TripleShape shape = state.getValue(SHAPE);
		if (shape == TripleShape.BOTTOM) {
			super.randomTick(state, world, pos, random);
			return;
		}
		else if (random.nextInt(16) == 0) {
			boolean bottom = canSurviveBottom(world, pos);
			if (shape == TripleShape.TOP) {
				if (!bottom) {
					world.setBlockAndUpdate(pos, Blocks.END_STONE.defaultBlockState());
				}
			}
			else {
				boolean top = canSurvive(state, world, pos) || isMiddle(world.getBlockState(pos.above()));
				if (!top && !bottom) {
					world.setBlockAndUpdate(pos, Blocks.END_STONE.defaultBlockState());
				}
				else if (top && !bottom) {
					world.setBlockAndUpdate(pos, state.setValue(SHAPE, TripleShape.BOTTOM));
				}
				else if (!top && bottom) {
					world.setBlockAndUpdate(pos, state.setValue(SHAPE, TripleShape.TOP));
				}
			}
		}
	}
	
	protected boolean canSurviveBottom(LevelReader world, BlockPos pos) {
		BlockPos blockPos = pos.below();
		BlockState blockState = world.getBlockState(blockPos);
		if (isMiddle(blockState)) {
			return true;
		}
		else if (blockState.getFluidState().getAmount() == 8) {
			return false;
		}
		else {
			return !blockState.isFaceSturdy(world, blockPos, Direction.UP);
		}
	}
	
	protected boolean isMiddle(BlockState state) {
		return state.is(this) && state.getValue(SHAPE) == TripleShape.MIDDLE;
	}
}
