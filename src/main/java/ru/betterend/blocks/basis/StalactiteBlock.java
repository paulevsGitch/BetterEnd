package ru.betterend.blocks.basis;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.Patterns;

public class StalactiteBlock extends BlockBaseNotFull implements Waterloggable, FluidFillable, IRenderTypeable {
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final BooleanProperty IS_FLOOR = BlockProperties.IS_FLOOR;
	public static final IntProperty SIZE = BlockProperties.SIZE;
	private static final VoxelShape[] SHAPES;

	public StalactiteBlock(Block source) {
		super(FabricBlockSettings.copy(source).nonOpaque());
		this.setDefaultState(getStateManager().getDefaultState().with(SIZE, 0).with(IS_FLOOR, true).with(WATERLOGGED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(WATERLOGGED, IS_FLOOR, SIZE);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return SHAPES[state.get(SIZE)];
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		WorldView world = ctx.getWorld();
		BlockPos pos = ctx.getBlockPos();
		Direction dir = ctx.getSide();
		boolean water = world.getFluidState(pos).getFluid() == Fluids.WATER;
		
		if (dir == Direction.DOWN) {
			if (isThis(world, pos.up()) || sideCoversSmallSquare(world, pos.up(), Direction.DOWN)) {
				return getDefaultState().with(IS_FLOOR, false).with(WATERLOGGED, water);
			}
			else if (isThis(world, pos.down()) || sideCoversSmallSquare(world, pos.down(), Direction.UP)) {
				return getDefaultState().with(IS_FLOOR, true).with(WATERLOGGED, water);
			}
			else {
				return null;
			}
		}
		else {
			if (isThis(world, pos.down()) || sideCoversSmallSquare(world, pos.down(), Direction.UP)) {
				return getDefaultState().with(IS_FLOOR, true).with(WATERLOGGED, water);
			}
			else if (isThis(world, pos.up()) || sideCoversSmallSquare(world, pos.up(), Direction.DOWN)) {
				return getDefaultState().with(IS_FLOOR, false).with(WATERLOGGED, water);
			}
			else {
				return null;
			}
		}
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		boolean hasUp = isThis(world, pos.up());
		boolean hasDown = isThis(world, pos.down());
		Mutable mut = new Mutable();
		if (hasUp && hasDown) {
			boolean floor = state.get(IS_FLOOR);
			BlockPos second = floor ? pos.up() : pos.down();
			BlockState bState = world.getBlockState(second);
			world.setBlockState(pos, state.with(SIZE, 1).with(IS_FLOOR, floor));
			world.setBlockState(second, bState.with(SIZE, 1).with(IS_FLOOR, !floor));
			
			bState = state;
			int startSize = floor ? 1 : 2;
			mut.set(pos.getX(), pos.getY() + 1, pos.getZ());
			for (int i = 0; i < 8 && isThis(bState); i++) {
				world.setBlockState(mut, bState.with(SIZE, startSize++).with(IS_FLOOR, false));
				mut.setY(mut.getY() + 1);
				bState = world.getBlockState(mut);
			}
			
			bState = state;
			startSize = floor ? 2 : 1;
			mut.set(pos.getX(), pos.getY() - 1, pos.getZ());
			for (int i = 0; i < 8 && isThis(bState); i++) {
				world.setBlockState(mut, bState.with(SIZE, startSize++).with(IS_FLOOR, true));
				mut.setY(mut.getY() - 1);
				bState = world.getBlockState(mut);
			}
		}
		else if (hasDown) {
			mut.setX(pos.getX());
			mut.setZ(pos.getZ());
			for (int i = 1; i < 8; i++) {
				mut.setY(pos.getY() - i);
				if (isThis(world, mut)) {
					BlockState state2 = world.getBlockState(mut);
					int size = state2.get(SIZE);
					if (size < i) {
						world.setBlockState(mut, state2.with(SIZE, i).with(IS_FLOOR, true));
					}
					else {
						break;
					}
				}
				else {
					break;
				}
			}
		}
		else if (hasUp) {
			mut.setX(pos.getX());
			mut.setZ(pos.getZ());
			for (int i = 1; i < 8; i++) {
				mut.setY(pos.getY() + i);
				if (isThis(world, mut)) {
					BlockState state2 = world.getBlockState(mut);
					int size = state2.get(SIZE);
					if (size < i) {
						world.setBlockState(mut, state2.with(SIZE, i).with(IS_FLOOR, false));
					}
					else {
						break;
					}
				}
				else {
					break;
				}
			}
		}
	}
	
	private boolean isThis(WorldView world, BlockPos pos) {
		return isThis(world.getBlockState(pos));
	}
	
	private boolean isThis(BlockState state) {
		return state.getBlock() instanceof StalactiteBlock;
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!canPlaceAt(state, world, pos)) {
			return Blocks.AIR.getDefaultState();
		}
		return state;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		int size = state.get(SIZE);
		return checkUp(world, pos, size) || checkDown(world, pos, size);
	}
	
	private boolean checkUp(BlockView world, BlockPos pos, int size) {
		BlockPos p = pos.up();
		BlockState state = world.getBlockState(p);
		return (isThis(state) && state.get(SIZE) >= size) || state.isFullCube(world, p);
	}
	
	private boolean checkDown(BlockView world, BlockPos pos, int size) {
		BlockPos p = pos.down();
		BlockState state = world.getBlockState(p);
		return (isThis(state) && state.get(SIZE) >= size) || state.isFullCube(world, p);
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		if (block.contains("item")) {
			return Patterns.createJson(Patterns.ITEM_GENERATED, "item/" + blockId.getPath());
		}
		return Patterns.createJson(Patterns.BLOCK_CROSS_SHADED, block);
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_STALACTITE;
	}
	
	@Override
	public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		return false;
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : Fluids.EMPTY.getDefaultState();
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}

	static {
		float end = 2F / 8F;
		float start = 5F / 8F;
		SHAPES = new VoxelShape[8];
		for (int i = 0; i < 8; i++) {
			int side = MathHelper.floor(MathHelper.lerp(i / 7F, start, end) * 8F + 0.5F);
			SHAPES[i] = Block.createCuboidShape(side, 0, side, 16 - side, 16, 16 - side);
		}
	}
}