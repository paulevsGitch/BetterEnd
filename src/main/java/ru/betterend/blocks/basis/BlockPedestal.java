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
import ru.betterend.blocks.BlockProperties.State;
import ru.betterend.blocks.entities.PedestalBlockEntity;
import ru.betterend.util.BlocksHelper;

public class BlockPedestal extends BlockBaseNotFull implements BlockEntityProvider {
	public final static EnumProperty<State> STATE = BlockProperties.STATE;
	public static final BooleanProperty HAS_ITEM = BlockProperties.HAS_ITEM;
	
	private static final VoxelShape SHAPE_DEFAULT = VoxelShapes.cuboid(0, 0, 0, 16, 14, 16);
	private static final VoxelShape SHAPE_PILLAR = VoxelShapes.cuboid(3, 0, 3, 13, 16, 13);
	private static final VoxelShape SHAPE_BOTTOM;
	private static final VoxelShape SHAPE_TOP;
	
	public BlockPedestal(Block parent) {
		super(FabricBlockSettings.copyOf(parent));
		this.setDefaultState(stateManager.getDefaultState().with(STATE, State.DEFAULT).with(HAS_ITEM, false));
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient || !state.isOf(this)) return ActionResult.CONSUME;
		State currentState = state.get(STATE);
		if (currentState.equals(State.BOTTOM) || currentState.equals(State.PILLAR)) {
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
		BlockPos pos = context.getBlockPos();
		Block down = context.getWorld().getBlockState(pos.down()).getBlock();
		Block up = context.getWorld().getBlockState(pos.up()).getBlock();
		if (down instanceof BlockPedestal && up instanceof BlockPedestal) {
			return this.getDefaultState().with(STATE, State.PILLAR);
		} else if (down instanceof BlockPedestal) {
			return this.getDefaultState().with(STATE, State.TOP);
		} else if (up instanceof BlockPedestal) {
			return this.getDefaultState().with(STATE, State.BOTTOM);
		}
		return this.getDefaultState();
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (newState.getBlock() instanceof BlockPedestal) {
			if (direction.equals(Direction.DOWN)) {
				if (world.getBlockState(pos.up()).getBlock() instanceof BlockPedestal) {
					this.moveStoredStack(world, state, pos);
					return state.with(STATE, State.PILLAR);
				}
				return state.with(STATE, State.TOP);
			} else if (direction.equals(Direction.UP)) {
				this.moveStoredStack(world, state, pos);
				if (world.getBlockState(pos.down()).getBlock() instanceof BlockPedestal) {
					return state.with(STATE, State.PILLAR);
				}
				return state.with(STATE, State.BOTTOM);
			}
		} else {
			if (direction.equals(Direction.DOWN)) {
				if (world.getBlockState(pos.up()).getBlock() instanceof BlockPedestal) {
					this.moveStoredStack(world, state, pos);
					return state.with(STATE, State.BOTTOM);
				}
				return state.with(STATE, State.DEFAULT);
			} else if (direction.equals(Direction.UP)) {
				if (world.getBlockState(pos.down()).getBlock() instanceof BlockPedestal) {
					return state.with(STATE, State.TOP);
				}
				return state.with(STATE, State.DEFAULT);
			}
		}
		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drop = super.getDroppedStacks(state, builder);
		if (state.isOf(this)) {
			State currentState = state.get(STATE);
			if (currentState.equals(State.BOTTOM) || currentState.equals(State.PILLAR)) {
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
		if (state.get(STATE).equals(State.PILLAR)) {
			BlockPos upPos = pos.up();
			this.moveStoredStack(world, stack, world.getBlockState(upPos), upPos);
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
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (state.isOf(this)) {
			switch(state.get(STATE)) {
				case BOTTOM: {
					return SHAPE_BOTTOM;
				}
				case TOP: {
					return SHAPE_TOP;
				}
				case PILLAR: {
					return SHAPE_PILLAR;
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
		VoxelShape basin = VoxelShapes.cuboid(0, 0, 0, 16, 4, 16);
		VoxelShape top = VoxelShapes.cuboid(1, 12, 1, 15, 14, 15);
		VoxelShape pillar = VoxelShapes.cuboid(3, 0, 3, 13, 14, 13);
		SHAPE_BOTTOM = VoxelShapes.union(basin, SHAPE_PILLAR);
		SHAPE_TOP = VoxelShapes.union(top, pillar);
	}
}
