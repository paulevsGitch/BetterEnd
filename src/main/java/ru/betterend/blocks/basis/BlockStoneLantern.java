package ru.betterend.blocks.basis;

import java.io.Reader;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.AuroraCrystalBlock;
import ru.betterend.interfaces.IColorProvider;
import ru.betterend.interfaces.Patterned;
import ru.betterend.util.MHelper;

public class BlockStoneLantern extends BlockBaseNotFull implements IColorProvider, Waterloggable {
	public static final BooleanProperty IS_FLOOR = BooleanProperty.of("is_floor");
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	
	private static final VoxelShape SHAPE_CEIL = Block.createCuboidShape(3, 1, 3, 13, 16, 13);
	private static final VoxelShape SHAPE_FLOOR = Block.createCuboidShape(3, 0, 3, 13, 15, 13);
	private static final Vec3i[] COLORS = AuroraCrystalBlock.COLORS;
	
	public BlockStoneLantern(Block source) {
		super(FabricBlockSettings.copyOf(source).luminance(15));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(IS_FLOOR, WATERLOGGED);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		WorldView worldView = ctx.getWorld();
		BlockPos blockPos = ctx.getBlockPos();
		Direction dir = ctx.getSide();
		if (dir == Direction.DOWN) {
			if (sideCoversSmallSquare(worldView, blockPos.up(), Direction.DOWN)) {
				boolean water = worldView.getFluidState(blockPos).getFluid() == Fluids.WATER;
				return getDefaultState().with(IS_FLOOR, false).with(WATERLOGGED, water);
			}
		}
		else {
			if (sideCoversSmallSquare(worldView, blockPos.down(), Direction.UP)) {
				boolean water = worldView.getFluidState(blockPos).getFluid() == Fluids.WATER;
				return getDefaultState().with(IS_FLOOR, true).with(WATERLOGGED, water);
			}
		}
		return null;
	}
	
	@Override
	public BlockColorProvider getProvider() {
		return (state, world, pos, tintIndex) -> {
			long i = (long) pos.getX() + (long) pos.getY() + (long) pos.getZ();
			double delta = i * 0.1;
			int index = MHelper.floor(delta);
			int index2 = (index + 1) & 3;
			delta -= index;
			index &= 3;
			
			Vec3i color1 = COLORS[index];
			Vec3i color2 = COLORS[index2];
			
			int r = MHelper.floor(MathHelper.lerp(delta, color1.getX(), color2.getX()));
			int g = MHelper.floor(MathHelper.lerp(delta, color1.getY(), color2.getY()));
			int b = MHelper.floor(MathHelper.lerp(delta, color1.getZ(), color2.getZ()));
			
			return MHelper.color(r, g, b);
		};
	}

	@Override
	public ItemColorProvider getItemProvider() {
		return (stack, tintIndex) -> {
			return MHelper.color(COLORS[3].getX(), COLORS[3].getY(), COLORS[3].getZ());
		};
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return state.get(IS_FLOOR) ? SHAPE_FLOOR : SHAPE_CEIL;
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		if (state.get(IS_FLOOR)) {
			return sideCoversSmallSquare(world, pos.down(), Direction.UP);
		}
		else {
			return sideCoversSmallSquare(world, pos.up(), Direction.DOWN);
		}
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		Boolean water = state.get(WATERLOGGED);
		if (water) {
			world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		if (!canPlaceAt(state, world, pos)) {
			return water ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
		}
		else {
			return state;
		}
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterned.STATE_STONE_LANTERN;
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		if (block.contains("ceil")) {
			return Patterned.createJson(Patterned.BLOCK_STONE_LANTERN_CEIL, blockId, blockId.getPath());
		}
		return Patterned.createJson(Patterned.BLOCK_STONE_LANTERN_FLOOR, blockId, blockId.getPath());
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterned.createJson(data, blockId, blockId.getPath());
	}
}
