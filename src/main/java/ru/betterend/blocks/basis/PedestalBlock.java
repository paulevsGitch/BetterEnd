package ru.betterend.blocks.basis;

import java.awt.Point;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockEntityProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ShapeContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemPlacementContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockProperties.PedestalState;
import ru.betterend.blocks.InfusionPedestal;
import ru.betterend.blocks.entities.PedestalBlockEntity;
import ru.betterend.patterns.Patterns;
import ru.betterend.registry.EndBlocks;
import ru.betterend.rituals.InfusionRitual;

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
	public static Block registerPedestal(ResourceLocation id, Block source) {
		return EndBlocks.registerBlock(id, new PedestalBlock(source));
	}

	protected final Block parent;
	protected float height = 1.0F;

	public PedestalBlock(Block parent) {
		super(FabricBlockSettings.copyOf(parent).luminance(state -> state.getValue(HAS_LIGHT) ? 12 : 0));
		this.setDefaultState(stateManager.defaultBlockState().with(STATE, PedestalState.DEFAULT).with(HAS_ITEM, false)
				.with(HAS_LIGHT, false));
		this.parent = parent;
	}

	public float getHeight(BlockState state) {
		if (state.getBlock() instanceof PedestalBlock && state.getValue(STATE) == PedestalState.PEDESTAL_TOP) {
			return this.height - 0.2F;
		}
		return this.height;
	}

	@Override
	public ActionResult onUse(BlockState state, Level world, BlockPos pos, Player player, Hand hand,
			BlockHitResult hit) {
		if (world.isClientSide || !state.is(this))
			return ActionResult.CONSUME;
		if (!isPlaceable(state)) {
			return ActionResult.PASS;
		}
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PedestalBlockEntity) {
			PedestalBlockEntity pedestal = (PedestalBlockEntity) blockEntity;
			if (pedestal.isEmpty()) {
				ItemStack itemStack = player.getStackInHand(hand);
				if (itemStack.isEmpty())
					return ActionResult.CONSUME;
				pedestal.setStack(0, itemStack.split(1));
				checkRitual(world, pos);
				return ActionResult.SUCCESS;
			} else {
				ItemStack itemStack = pedestal.getStack(0);
				if (player.giveItemStack(itemStack)) {
					pedestal.removeStack(0);
					checkRitual(world, pos);
					return ActionResult.SUCCESS;
				}
				return ActionResult.FAIL;
			}
		}
		return ActionResult.PASS;
	}

	public void checkRitual(Level world, BlockPos pos) {
		MutableBlockPos posMutable = new MutableBlockPos();
		for (Point point : InfusionRitual.getMap()) {
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
		Level world = context.getLevel();
		BlockPos pos = context.getBlockPos();
		BlockState upState = world.getBlockState(pos.up());
		BlockState downState = world.getBlockState(pos.below());
		boolean upSideSolid = upState.isSideSolidFullSquare(world, pos.up(), Direction.DOWN)
				|| upState.isIn(BlockTags.WALLS);
		boolean hasPedestalOver = upState.getBlock() instanceof PedestalBlock;
		boolean hasPedestalUnder = downState.getBlock() instanceof PedestalBlock;
		if (!hasPedestalOver && hasPedestalUnder && upSideSolid) {
			return getDefaultState().with(STATE, PedestalState.COLUMN_TOP);
		} else if (!hasPedestalOver && !hasPedestalUnder && upSideSolid) {
			return getDefaultState().with(STATE, PedestalState.COLUMN);
		} else if (hasPedestalUnder && hasPedestalOver) {
			return getDefaultState().with(STATE, PedestalState.PILLAR);
		} else if (hasPedestalUnder) {
			return getDefaultState().with(STATE, PedestalState.PEDESTAL_TOP);
		} else if (hasPedestalOver) {
			return getDefaultState().with(STATE, PedestalState.BOTTOM);
		}
		return getDefaultState();
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world,
			BlockPos pos, BlockPos posFrom) {
		BlockState updated = getUpdatedState(state, direction, newState, world, pos, posFrom);
		if (!updated.is(this))
			return updated;
		if (!isPlaceable(updated)) {
			moveStoredStack(world, updated, pos);
		}
		return updated;
	}

	private BlockState getUpdatedState(BlockState state, Direction direction, BlockState newState, LevelAccessor world,
			BlockPos pos, BlockPos posFrom) {
		if (!state.is(this))
			return state.updateShape(direction, newState, world, pos, posFrom);
		if (direction != Direction.UP && direction != Direction.DOWN)
			return state;
		BlockState upState = world.getBlockState(pos.up());
		BlockState downState = world.getBlockState(pos.below());
		boolean upSideSolid = upState.isSideSolidFullSquare(world, pos.up(), Direction.DOWN)
				|| upState.isIn(BlockTags.WALLS);
		boolean hasPedestalOver = upState.getBlock() instanceof PedestalBlock;
		boolean hasPedestalUnder = downState.getBlock() instanceof PedestalBlock;
		if (direction == Direction.UP) {
			upSideSolid = newState.isSideSolidFullSquare(world, posFrom, Direction.DOWN)
					|| newState.isIn(BlockTags.WALLS);
			hasPedestalOver = newState.getBlock() instanceof PedestalBlock;
		} else {
			hasPedestalUnder = newState.getBlock() instanceof PedestalBlock;
		}
		BlockState updatedState;
		if (!hasPedestalOver && hasPedestalUnder && upSideSolid) {
			updatedState = state.with(STATE, PedestalState.COLUMN_TOP);
		} else if (!hasPedestalOver && !hasPedestalUnder && upSideSolid) {
			updatedState = state.with(STATE, PedestalState.COLUMN);
		} else if (hasPedestalUnder && hasPedestalOver) {
			updatedState = state.with(STATE, PedestalState.PILLAR);
		} else if (hasPedestalUnder) {
			updatedState = state.with(STATE, PedestalState.PEDESTAL_TOP);
		} else if (hasPedestalOver) {
			updatedState = state.with(STATE, PedestalState.BOTTOM);
		} else {
			updatedState = state.with(STATE, PedestalState.DEFAULT);
		}
		if (!isPlaceable(updatedState)) {
			updatedState = updatedState.with(HAS_ITEM, false).with(HAS_LIGHT, false);
		}
		return updatedState;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drop = Lists.newArrayList(super.getDrops(state, builder));
		if (state.is(this)) {
			if (isPlaceable(state)) {
				BlockEntity blockEntity = builder.getNullable(LootContextParams.BLOCK_ENTITY);
				if (blockEntity instanceof PedestalBlockEntity) {
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

	private void moveStoredStack(LevelAccessor world, BlockState state, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PedestalBlockEntity && state.is(this)) {
			PedestalBlockEntity pedestal = (PedestalBlockEntity) blockEntity;
			ItemStack stack = pedestal.removeStack(0);
			if (!stack.isEmpty()) {
				moveStoredStack(blockEntity, world, stack, pos.up());
			}
		}
	}

	private void moveStoredStack(BlockEntity blockEntity, LevelAccessor world, ItemStack stack, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (!state.is(this)) {
			dropStoredStack(blockEntity, stack, pos);
		} else if (state.getValue(STATE).equals(PedestalState.PILLAR)) {
			moveStoredStack(blockEntity, world, stack, pos.up());
		} else if (!isPlaceable(state)) {
			dropStoredStack(blockEntity, stack, pos);
		} else if (blockEntity instanceof PedestalBlockEntity) {
			PedestalBlockEntity pedestal = (PedestalBlockEntity) blockEntity;
			if (pedestal.isEmpty()) {
				pedestal.setStack(0, stack);
			} else {
				dropStoredStack(blockEntity, stack, pos);
			}
		} else {
			dropStoredStack(blockEntity, stack, pos);
		}
	}

	private void dropStoredStack(BlockEntity blockEntity, ItemStack stack, BlockPos pos) {
		if (blockEntity != null && blockEntity.getLevel() != null) {
			Level world = blockEntity.getLevel();
			Block.dropStack(world, getDropPos(world, pos), stack);
		}
	}

	private BlockPos getDropPos(LevelAccessor world, BlockPos pos) {
		BlockPos dropPos;
		if (world.getBlockState(pos).isAir()) {
			return pos;
		}
		if (world.getBlockState(pos.up()).isAir()) {
			return pos.up();
		}
		for (int i = 2; i < Direction.values().length; i++) {
			dropPos = pos.relative(Direction.byId(i));
			if (world.getBlockState(dropPos).isAir()) {
				return dropPos.immutable();
			}
		}
		return getDropPos(world, pos.up());
	}

	public boolean isPlaceable(BlockState state) {
		if (!state.is(this))
			return false;
		PedestalState currentState = state.getValue(STATE);
		return currentState == PedestalState.DEFAULT || currentState == PedestalState.PEDESTAL_TOP;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (state.is(this)) {
			switch (state.getValue(STATE)) {
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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
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
	public int getComparatorOutput(BlockState state, Level world, BlockPos pos) {
		return state.getValue(HAS_ITEM) ? 15 : 0;
	}

	@Override
	public String getStatesPattern(Reader data) {
		String texture = Registry.BLOCK.getKey(this).getPath();
		return Patterns.createJson(data, texture, texture);
	}

	@Override
	public String getModelPattern(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(parent);
		String name = blockId.getPath();
		Map<String, String> textures = new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("%mod%", blockId.getNamespace());
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
	public ResourceLocation statePatternId() {
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
