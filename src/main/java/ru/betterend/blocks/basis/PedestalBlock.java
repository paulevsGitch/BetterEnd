package ru.betterend.blocks.basis;

import java.awt.Point;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

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
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockProperties.PedestalState;
import ru.betterend.blocks.InfusionPedestal;
import ru.betterend.blocks.entities.PedestalBlockEntity;
import ru.betterend.patterns.Patterns;
import ru.betterend.registry.EndBlocks;
import ru.betterend.rituals.InfusionRitual;
import ru.betterend.util.BlocksHelper;

public class PedestalBlock extends BlockBaseNotFull implements BlockEntityProvider {
	public final static EnumProperty<PedestalState> STATE = BlockProperties.PEDESTAL_STATE;
	public static final BooleanProperty HAS_ITEM = BlockProperties.HAS_ITEM;
	public static final BooleanProperty HAS_LIGHT = BlockProperties.HAS_LIGHT;
	
	private static final VoxelShape SHAPE_DEFAULT;
	private static final VoxelShape SHAPE_COLUMN;
	private static final VoxelShape SHAPE_PILLAR;
	private static final VoxelShape SHAPE_PEDESTAL_TOP;
	private static final VoxelShape SHAPE_COLUMN_TOP;
	private static final VoxelShape SHAPE_BOTTOM;
	
	/**
	 * 
	 * Register new Pedestal block with Better End mod id.
	 * 
	 * @param name
	 * @param source
	 * @return new Pedestal block with Better End id.
	 */
	public static Block registerPedestal(String name, Block source) {
		return EndBlocks.registerBlock(name, new PedestalBlock(source));
	}
	
	/**
	 * 
	 * Register new Pedestal block with specified mod id.
	 * 
	 * @param id
	 * @param source
	 * @return new Pedestal block with specified id.
	 */
	public static Block registerPedestal(Identifier id, Block source) {
		return EndBlocks.registerBlock(id, new PedestalBlock(source));
	}
	
	protected final Block parent;
	protected float height = 1.0F;
	
	public PedestalBlock(Block parent) {
		super(FabricBlockSettings.copyOf(parent).luminance(state -> {
			return state.get(HAS_LIGHT) ? 12 : 0;
		}));
		this.setDefaultState(stateManager.getDefaultState().with(STATE, PedestalState.DEFAULT).with(HAS_ITEM, false).with(HAS_LIGHT, false));
		this.parent = parent;
	}
	
	public float getHeight(BlockState state) {
		if (state.getBlock() instanceof PedestalBlock && state.get(STATE) == PedestalState.PEDESTAL_TOP) {
			return this.height - 0.2F;
		}
		return this.height;
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient || !state.isOf(this)) return ActionResult.CONSUME;
		if (!this.isPlaceable(state)) {
			return ActionResult.PASS;
		}
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PedestalBlockEntity) {
			PedestalBlockEntity pedestal = (PedestalBlockEntity) blockEntity;
			if (pedestal.isEmpty()) {
				ItemStack itemStack = player.getStackInHand(hand);
				if (itemStack.isEmpty()) return ActionResult.CONSUME;
				pedestal.setStack(0, itemStack.split(1));
				this.checkRitual(world, pos);
				return ActionResult.SUCCESS;
			} else {
				ItemStack itemStack = pedestal.getStack(0);
				if (player.giveItemStack(itemStack)) {
					pedestal.removeStack(world, state);
					return ActionResult.SUCCESS;
				}
				return ActionResult.FAIL;
			}
		}
		return ActionResult.PASS;
	}
	
	public void checkRitual(World world, BlockPos pos) {
		Mutable posMutable = new Mutable();
		for (Point point: InfusionRitual.getMap()) {
			posMutable.set(pos).move(point.x, 0, point.y);
			BlockState state = world.getBlockState(posMutable);
			if (state.getBlock() instanceof InfusionPedestal) {
				((InfusionPedestal) state.getBlock()).checkRitual(world, posMutable);
				break;
			}
		}
	}
	
	@Override
	@Nullable
	public BlockState getPlacementState(ItemPlacementContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState upState = world.getBlockState(pos.up());
		BlockState downState = world.getBlockState(pos.down());
		boolean upSideSolid = upState.isSideSolidFullSquare(world, pos.up(), Direction.DOWN) || upState.isIn(BlockTags.WALLS);
		boolean hasPedestalOver = upState.getBlock() instanceof PedestalBlock;
		boolean hasPedestalUnder = downState.getBlock() instanceof PedestalBlock;
		if (!hasPedestalOver && hasPedestalUnder && upSideSolid) {
			return this.getDefaultState().with(STATE, PedestalState.COLUMN_TOP);
		} else if (!hasPedestalOver && !hasPedestalUnder && upSideSolid) {
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
		if (direction != Direction.UP && direction != Direction.DOWN) return state;
		BlockState upState = world.getBlockState(pos.up());
		BlockState downState = world.getBlockState(pos.down());
		boolean upSideSolid = upState.isSideSolidFullSquare(world, pos.up(), Direction.DOWN) || upState.isIn(BlockTags.WALLS);
		boolean hasPedestalOver = upState.getBlock() instanceof PedestalBlock;
		boolean hasPedestalUnder = downState.getBlock() instanceof PedestalBlock;
		if (direction == Direction.UP) {
			upSideSolid = newState.isSideSolidFullSquare(world, posFrom, Direction.DOWN) || newState.isIn(BlockTags.WALLS);
			hasPedestalOver = newState.getBlock() instanceof PedestalBlock;
		} else if (direction == Direction.DOWN) {
			hasPedestalUnder = newState.getBlock() instanceof PedestalBlock;
		}
		if (!hasPedestalOver && hasPedestalUnder && upSideSolid) {
			return state.with(STATE, PedestalState.COLUMN_TOP);
		} else if (!hasPedestalOver && !hasPedestalUnder && upSideSolid) {
			return state.with(STATE, PedestalState.COLUMN);
		} else if (hasPedestalUnder && hasPedestalOver) {
			return state.with(STATE, PedestalState.PILLAR);
		} else if (hasPedestalUnder) {
			return state.with(STATE, PedestalState.PEDESTAL_TOP);
		} else if (hasPedestalOver) {
			return state.with(STATE, PedestalState.BOTTOM);
		}
		return state.with(STATE, PedestalState.DEFAULT);
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drop = Lists.newArrayList(super.getDroppedStacks(state, builder));
		if (state.isOf(this)) {
			if (isPlaceable(state)) {
				BlockEntity blockEntity = builder.getNullable(LootContextParameters.BLOCK_ENTITY);
				if (blockEntity != null && blockEntity instanceof PedestalBlockEntity) {
					PedestalBlockEntity pedestal = (PedestalBlockEntity) blockEntity;
					if (!pedestal.isEmpty()) {
						drop.add(pedestal.getStack(0));
					}
				}
			} else {
				return drop;
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
		if (!state.isOf(this)) {
			this.dropStoredStack(blockEntity, stack, pos);
		} else if (state.get(STATE).equals(PedestalState.PILLAR)) {
			BlockPos upPos = pos.up();
			this.moveStoredStack(world, stack, world.getBlockState(upPos), upPos);
		} else if (!this.isPlaceable(state)) {
			this.dropStoredStack(blockEntity, stack, pos);
		} else if (blockEntity instanceof PedestalBlockEntity) {
			PedestalBlockEntity pedestal = (PedestalBlockEntity) blockEntity;
			if (pedestal.isEmpty()) {
				pedestal.setStack(0, stack);
			} else {
				this.dropStoredStack(blockEntity, stack, pos);
			}
		} else {
			this.dropStoredStack(blockEntity, stack, pos);
		}
	}
	
	private void dropStoredStack(BlockEntity blockEntity, ItemStack stack, BlockPos pos) {
		if (blockEntity != null && blockEntity.getWorld() != null) {
			World world = blockEntity.getWorld();
			Block.dropStack(world, this.getDropPos(world, pos), stack);
		}
	}
	
	private BlockPos getDropPos(WorldAccess world, BlockPos pos) {
		BlockPos dropPos;
		for(int i = 2; i < Direction.values().length; i++) {
			dropPos = pos.offset(Direction.byId(i));
			if (world.getBlockState(dropPos).isAir()) {
				return dropPos.toImmutable();
			}
		}
		if (world.getBlockState(pos.up()).isAir()) {
			return pos.up();
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
		stateManager.add(STATE, HAS_ITEM, HAS_LIGHT);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new PedestalBlockEntity();
	}
	
	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return state.getBlock() instanceof PedestalBlock;
	}
	
	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return state.get(HAS_ITEM) ? 15 : 0;
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		String texture = Registry.BLOCK.getId(this).getPath();
		return Patterns.createJson(data, texture, texture);
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(parent);
		String name = blockId.getPath();
		Map<String, String> textures = new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("%mod%", blockId.getNamespace() );
				put("%top%", name + "_top");
				put("%base%", name + "_base");
				put("%pillar%", name + "_pillar");
				put("%bottom%", name + "_bottom");
			}
		};
		if (block.contains("column_top")) {
			return Patterns.createJson(Patterns.BLOCK_PEDESTAL_COLUMN_TOP, textures);
		} else if (block.contains("column")) {
			return Patterns.createJson(Patterns.BLOKC_PEDESTAL_COLUMN, textures);
		} else if (block.contains("top")) {
			return Patterns.createJson(Patterns.BLOCK_PEDESTAL_TOP, textures);
		} else if (block.contains("bottom")) {
			return Patterns.createJson(Patterns.BLOCK_PEDESTAL_BOTTOM, textures);
		} else if (block.contains("pillar")) {
			return Patterns.createJson(Patterns.BLOCK_PEDESTAL_PILLAR, textures);
		}
		return Patterns.createJson(Patterns.BLOCK_PEDESTAL_DEFAULT, textures);
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_PEDESTAL;
	}
	
	static {
		VoxelShape basinUp = Block.createCuboidShape(2, 3, 2, 14, 4, 14);
		VoxelShape basinDown = Block.createCuboidShape(0, 0, 0, 16, 3, 16);
		VoxelShape columnTopUp = Block.createCuboidShape(1, 14, 1, 15, 16, 15);
		VoxelShape columnTopDown = Block.createCuboidShape(2, 13, 2, 14, 14, 14);
		VoxelShape pedestalTop = Block.createCuboidShape(1, 8, 1, 15, 10, 15);
		VoxelShape pedestalDefault = Block.createCuboidShape(1, 12, 1, 15, 14, 15);
		VoxelShape pillar = Block.createCuboidShape(3, 0, 3, 13, 8, 13);
		VoxelShape pillarDefault = Block.createCuboidShape(3, 0, 3, 13, 12, 13);
		VoxelShape columnTop = VoxelShapes.union(columnTopDown, columnTopUp);
		VoxelShape basin = VoxelShapes.union(basinDown, basinUp);
		SHAPE_PILLAR = Block.createCuboidShape(3, 0, 3, 13, 16, 13);
		SHAPE_DEFAULT = VoxelShapes.union(basin, pillarDefault, pedestalDefault);
		SHAPE_PEDESTAL_TOP = VoxelShapes.union(pillar, pedestalTop);
		SHAPE_COLUMN_TOP = VoxelShapes.union(SHAPE_PILLAR, columnTop);
		SHAPE_COLUMN = VoxelShapes.union(basin, SHAPE_PILLAR, columnTop);
		SHAPE_BOTTOM = VoxelShapes.union(basin, SHAPE_PILLAR);
	}
}
