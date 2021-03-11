package ru.betterend.blocks.basis;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.patterns.Patterns;
import ru.betterend.blocks.EndTerrainBlock;

public class TripleTerrainBlock extends EndTerrainBlock {
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	
	public TripleTerrainBlock(MaterialColor color) {
		super(color);
		this.setDefaultState(this.getDefaultState().with(SHAPE, TripleShape.BOTTOM));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction dir = ctx.getSide();
		TripleShape shape = dir == Direction.UP ? TripleShape.BOTTOM : dir == Direction.DOWN ? TripleShape.TOP : TripleShape.MIDDLE;
		return this.getDefaultState().with(SHAPE, shape);
	}
	
	@Override
	public String getModelPattern(String block) {
		System.out.println(block);
		String name = Registry.BLOCK.getId(this).getPath();
		if (block.endsWith("_middle")) {
			return Patterns.createJson(Patterns.BLOCK_BASE, name + "_top");
		}
		Map<String, String> map = Maps.newHashMap();
		map.put("%top%", "betterend:block/" + name + "_top");
		map.put("%side%", "betterend:block/" + name + "_side");
		map.put("%bottom%", "minecraft:block/end_stone");
		return Patterns.createJson(Patterns.BLOCK_TOP_SIDE_BOTTOM, map);
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_TRIPLE_ROTATED_TOP;
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		TripleShape shape = state.get(SHAPE);
		if (shape == TripleShape.TOP) {
			super.randomTick(state, world, pos, random);
			return;
		}
		else if (random.nextInt(16) == 0) {
			boolean bottom = canSurviveBottom(world, pos);
			if (shape == TripleShape.BOTTOM) {
				if (!bottom) {
					world.setBlockState(pos, Blocks.END_STONE.getDefaultState());
				}
			}
			else {
				boolean top = canSurvive(state, world, pos);
				if (!top && !bottom) {
					world.setBlockState(pos, Blocks.END_STONE.getDefaultState());
				}
				else if (top && !bottom) {
					world.setBlockState(pos, state.with(SHAPE, TripleShape.TOP));
				}
				else if (!top && bottom) {
					world.setBlockState(pos, state.with(SHAPE, TripleShape.BOTTOM));
				}
			}
		}
	}
	
	protected boolean canSurviveBottom(WorldView world, BlockPos pos) {
		BlockPos blockPos = pos.down();
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.getFluidState().getLevel() == 8) {
			return false;
		}
		else {
			return !blockState.isSideSolidFullSquare(world, blockPos, Direction.UP);
		}
	}
}
