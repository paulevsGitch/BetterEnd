package ru.betterend.blocks.basis;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockProperties.PedestalState;
import ru.betterend.blocks.entities.PedestalBlockEntity;
import ru.betterend.util.BlocksHelper;

public class BlockPedestal extends BlockBaseNotFull implements BlockEntityProvider {
	public final static EnumProperty<PedestalState> STATE = BlockProperties.PEDESTAL_STATE;
	public static final BooleanProperty HAS_ITEM = BlockProperties.HAS_ITEM;
	
	private static final VoxelShape SHAPE_PILLAR = Block.createCuboidShape(3, 0, 3, 13, 16, 13);
	private static final VoxelShape SHAPE_DEFAULT;
	private static final VoxelShape SHAPE_COLUMN;
	private static final VoxelShape SHAPE_PEDESTAL_TOP;
	private static final VoxelShape SHAPE_COLUMN_TOP;
	private static final VoxelShape SHAPE_BOTTOM;
	
	public BlockPedestal(Block parent) {
		super(FabricBlockSettings.copyOf(parent));
		this.setDefaultState(stateManager.getDefaultState().with(STATE, PedestalState.DEFAULT).with(HAS_ITEM, false));
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient || !state.isOf(this)) return ActionResult.CONSUME;
		PedestalState currentState = state.get(STATE);
		if (currentState.equals(PedestalState.BOTTOM) || currentState.equals(PedestalState.PILLAR)) {
			return ActionResult.PASS;
		}
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PedestalBlockEntity) {
			PedestalBlockEntity pedestal = (PedestalBlockEntity) blockEntity;
			if (pedestal.isEmpty()) {
				ItemStack itemStack = player.getStackInHand(hand);
				if (itemStack.isEmpty()) return ActionResult.CONSUME;
				world.setBlockState(pos, state.with(HAS_ITEM, true));
				pedestal.setStack(0, itemStack.split(1));
				return ActionResult.SUCCESS;
			} else {
				ItemStack itemStack = pedestal.getStack(0);
				if (player.giveItemStack(itemStack)) {
					world.setBlockState(pos, state.with(HAS_ITEM, false));
					pedestal.removeStack(0);
					return ActionResult.SUCCESS;
				}
				return ActionResult.FAIL;
			}
		}
		return ActionResult.PASS;
	}
	
	@Override
	@Nullable
	public BlockState getPlacementState(ItemPlacementContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState upState = world.getBlockState(pos.up());
		Block down = world.getBlockState(pos.down()).getBlock();
		boolean upSideSolid = upState.isSideSolidFullSquare(world, pos.up(), Direction.DOWN);
		boolean hasPedestalOver = upState.getBlock() instanceof BlockPedestal;
		boolean hasPedestalUnder = down instanceof BlockPedestal;
		if (!hasPedestalOver && hasPedestalUnder && upSideSolid) {
			return this.getDefaultState().with(STATE, PedestalState.COLUMN_TOP);
		} else if (!hasPedestalUnder && upSideSolid) {
			return this.getDefaultState().with(STATE, PedestalState.COLUMN);
		} else if (hasPedestalUnder && hasPedestalOver) {
			return this.getDefaultState().with(STATE, PedestalState.PILLAR);
		} else if (hasPedestalUnder) {
			return this.getDefaultState().with(STATE, PedestalState.PEDESTAL_TOP);
		} else if (hasPedestalOver) {
			return this.getDefaultState().with(STATE, PedestalState.BOTTOM);
		}
		return this.getDefaultState();
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		BlockState updated = this.getUpdatedState(state, direction, newState, world, pos, posFrom);
		if (!updated.isOf(this)) return updated;
		if (!this.isPlaceable(updated)) {
			this.moveStoredStack(world, updated, pos);
		}
		return updated;
	}
	
	private BlockState getUpdatedState(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (!state.isOf(this)) return state.getStateForNeighborUpdate(direction, newState, world, pos, posFrom);
		PedestalState currentState = state.get(STATE);
		if (newState.getBlock() instanceof BlockPedestal) {
			if (direction.equals(Direction.DOWN)) {
				if (currentState == PedestalState.BOTTOM) {
					return state.with(STATE, PedestalState.PILLAR);
				} else if (currentState == PedestalState.COLUMN) {
					return state.with(STATE, PedestalState.COLUMN_TOP);
				}
				return state.with(STATE, PedestalState.PEDESTAL_TOP);
			} else if (direction.equals(Direction.UP)) {
				if (currentState == PedestalState.PEDESTAL_TOP) {
					return state.with(STATE, PedestalState.PILLAR);
				}
				return state.with(STATE, PedestalState.BOTTOM);
			}
		} else {
			if (direction.equals(Direction.DOWN)) {
				if (currentState == PedestalState.COLUMN_TOP) {
					return state.with(STATE, PedestalState.COLUMN);
				}
				if (currentState == PedestalState.PILLAR) {
					return state.with(STATE, PedestalState.BOTTOM);
				}
				return state.with(STATE, PedestalState.DEFAULT);
			} else if (direction.equals(Direction.UP)) {
				boolean upSideSolid = newState.isSideSolidFullSquare(world, posFrom, Direction.DOWN);
				if (currentState == PedestalState.PEDESTAL_TOP && upSideSolid) {
					return state.with(STATE, PedestalState.COLUMN_TOP);
				} else if (currentState == PedestalState.COLUMN_TOP || currentState == PedestalState.PILLAR) {
					return state.with(STATE, PedestalState.PEDESTAL_TOP);
				} else if (upSideSolid) {
					return state.with(STATE, PedestalState.COLUMN);
				}
				return state.with(STATE, PedestalState.DEFAULT);
			}
		}
		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drop = super.getDroppedStacks(state, builder);
		if (state.isOf(this)) {
			PedestalState currentState = state.get(STATE);
			if (currentState.equals(PedestalState.BOTTOM) || currentState.equals(PedestalState.PILLAR)) {
				return drop;
			} else {
				BlockEntity blockEntity = builder.getNullable(LootContextParameters.BLOCK_ENTITY);
				if (blockEntity != null && blockEntity instanceof PedestalBlockEntity) {
					PedestalBlockEntity pedestal = (PedestalBlockEntity) blockEntity;
					if (!pedestal.isEmpty()) {
						drop.add(pedestal.getStack(0));
					}
				}
			}
		}
		return drop;
	}
	
	private void moveStoredStack(WorldAccess world, BlockState state, BlockPos pos) {
		ItemStack stack = ItemStack.EMPTY;
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PedestalBlockEntity && state.isOf(this)) {
			PedestalBlockEntity pedestal = (PedestalBlockEntity) blockEntity;
			stack = pedestal.getStack(0);
			pedestal.clear();
			BlocksHelper.setWithoutUpdate(world, pos, state.with(HAS_ITEM, false));
		}
		if (!stack.isEmpty()) {
			BlockPos upPos = pos.up();
			this.moveStoredStack(world, stack, world.getBlockState(upPos), upPos);
		}
	}
	
	private void moveStoredStack(WorldAccess world, ItemStack stack, BlockState state, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (state.get(STATE).equals(PedestalState.PILLAR)) {
			BlockPos upPos = pos.up();
			this.moveStoredStack(world, stack, world.getBlockState(upPos), upPos);
		} else if (!this.isPlaceable(state)) {
			this.dropStoredStack(world, stack, pos);
		} else if (blockEntity instanceof PedestalBlockEntity) {
			PedestalBlockEntity pedestal = (PedestalBlockEntity) blockEntity;
			if (pedestal.isEmpty()) {
				pedestal.setStack(0, stack);
				BlocksHelper.setWithoutUpdate(world, pos, state.with(HAS_ITEM, true));
			} else {
				this.dropStoredStack(world, stack, pos);
			}
		} else {
			this.dropStoredStack(world, stack, pos);
		}
	}
	
	private void dropStoredStack(WorldAccess world, ItemStack stack, BlockPos pos) {
		Block.dropStack((World) world, this.getDropPos(world, pos), stack);
	}
	
	private BlockPos getDropPos(WorldAccess world, BlockPos pos) {
		BlockPos dropPos;
		for(int i = 2; i < Direction.values().length; i++) {
			dropPos = pos.offset(Direction.byId(i));
			if (world.getBlockState(dropPos).isAir()) {
				return dropPos.toImmutable();
			}
		}
		return this.getDropPos(world, pos.up());
	}
	
	protected boolean isPlaceable(BlockState state) {
		if (!state.isOf(this)) return false;
		PedestalState currentState = state.get(STATE);
		return currentState != PedestalState.BOTTOM &&
			   currentState != PedestalState.COLUMN &&
			   currentState != PedestalState.PILLAR &&
			   currentState != PedestalState.COLUMN_TOP;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (state.isOf(this)) {
			switch(state.get(STATE)) {
				case BOTTOM: {
					return SHAPE_BOTTOM;
				}
				case PEDESTAL_TOP: {
					return SHAPE_PEDESTAL_TOP;
				}
				case COLUMN_TOP: {
					return SHAPE_COLUMN_TOP;
				}
				case PILLAR: {
					return SHAPE_PILLAR;
				}
				case COLUMN: {
					return SHAPE_COLUMN;
				}
				default: {
					return SHAPE_DEFAULT;
				}
			}
		}
		return super.getOutlineShape(state, world, pos, context);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(STATE, HAS_ITEM);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new PedestalBlockEntity();
	}
	
	static {
		VoxelShape basin = Block.createCuboidShape(0, 0, 0, 16, 4, 16);
		VoxelShape pedestal_top = Block.createCuboidShape(1, 12, 1, 15, 14, 15);
		VoxelShape column_top = Block.createCuboidShape(1, 14, 1, 15, 16, 15);
		VoxelShape pillar = Block.createCuboidShape(3, 0, 3, 13, 14, 13);
		SHAPE_DEFAULT = VoxelShapes.union(basin, pillar, pedestal_top);
		SHAPE_PEDESTAL_TOP = VoxelShapes.union(pillar, pedestal_top);
		SHAPE_COLUMN_TOP = VoxelShapes.union(SHAPE_PILLAR, column_top);
		SHAPE_COLUMN = VoxelShapes.union(basin, SHAPE_PILLAR, column_top);
		SHAPE_BOTTOM = VoxelShapes.union(basin, SHAPE_PILLAR);
	}
}
