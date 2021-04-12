package ru.betterend.blocks.basis;

import java.io.Reader;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalFacingBlock;
import net.minecraft.world.level.block.ShapeContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.ItemPlacementContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.WorldView;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;
import ru.betterend.util.BlocksHelper;

public class EndLadderBlock extends BlockBaseNotFull implements IRenderTypeable, BlockPatterned {
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
	protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
	protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);

	public EndLadderBlock(Block block) {
		super(FabricBlockSettings.copyOf(block).nonOpaque());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(FACING);
		stateManager.add(WATERLOGGED);
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		switch (state.getValue(FACING)) {
		case SOUTH:
			return SOUTH_SHAPE;
		case WEST:
			return WEST_SHAPE;
		case EAST:
			return EAST_SHAPE;
		default:
			return NORTH_SHAPE;
		}
	}

	private boolean canPlaceOn(BlockView world, BlockPos pos, Direction side) {
		BlockState blockState = world.getBlockState(pos);
		return !blockState.emitsRedstonePower() && blockState.isSideSolidFullSquare(world, pos, side);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = (Direction) state.getValue(FACING);
		return this.canPlaceOn(world, pos.relative(direction.getOpposite()), direction);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world,
			BlockPos pos, BlockPos neighborPos) {
		if (facing.getOpposite() == state.getValue(FACING) && !state.canPlaceAt(world, pos)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			if ((Boolean) state.getValue(WATERLOGGED)) {
				world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
			}

			return super.updateShape(state, facing, neighborState, world, pos, neighborPos);
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState blockState2;
		if (!ctx.canReplaceExisting()) {
			blockState2 = ctx.getLevel().getBlockState(ctx.getBlockPos().offset(ctx.getSide().getOpposite()));
			if (blockState2.getBlock() == this && blockState2.get(FACING) == ctx.getSide()) {
				return null;
			}
		}

		blockState2 = this.defaultBlockState();
		WorldView worldView = ctx.getLevel();
		BlockPos blockPos = ctx.getBlockPos();
		FluidState fluidState = ctx.getLevel().getFluidState(ctx.getBlockPos());
		Direction[] var6 = ctx.getPlacementDirections();
		int var7 = var6.length;

		for (int var8 = 0; var8 < var7; ++var8) {
			Direction direction = var6[var8];
			if (direction.getAxis().isHorizontal()) {
				blockState2 = (BlockState) blockState2.with(FACING, direction.getOpposite());
				if (blockState2.canPlaceAt(worldView, blockPos)) {
					return (BlockState) blockState2.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
				}
			}
		}

		return null;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return BlocksHelper.rotateHorizontal(state, rotation, FACING);
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return BlocksHelper.mirrorHorizontal(state, mirror, FACING);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (Boolean) state.getValue(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}

	@Override
	public String getStatesPattern(Reader data) {
		String blockId = Registry.BLOCK.getKey(this).getPath();
		return Patterns.createJson(data, blockId, blockId);
	}

	@Override
	public String getModelPattern(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		if (block.contains("item")) {
			return Patterns.createJson(Patterns.ITEM_BLOCK, blockId.getPath());
		}
		return Patterns.createJson(Patterns.BLOCK_LADDER, blockId.getPath());
	}

	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_LADDER;
	}
}
