package ru.betterend.blocks.basis;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.bclib.blocks.BaseBlockNotFull;
import ru.bclib.blocks.BlockProperties;
import ru.bclib.client.models.ModelsHelper;
import ru.betterend.blocks.EndBlockProperties;
import ru.betterend.blocks.EndBlockProperties.PedestalState;
import ru.betterend.blocks.InfusionPedestal;
import ru.betterend.blocks.entities.EndStoneSmelterBlockEntity;
import ru.betterend.blocks.entities.InfusionPedestalEntity;
import ru.betterend.blocks.entities.PedestalBlockEntity;
import ru.betterend.client.models.Patterns;
import ru.betterend.registry.EndBlockEntities;
import ru.betterend.registry.EndBlocks;
import ru.betterend.rituals.InfusionRitual;

@SuppressWarnings({"deprecation"})
public class PedestalBlock extends BaseBlockNotFull implements EntityBlock {
	public final static EnumProperty<PedestalState> STATE = EndBlockProperties.PEDESTAL_STATE;
	public static final BooleanProperty HAS_ITEM = EndBlockProperties.HAS_ITEM;
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
	 * @param name pedestal name
	 * @param source source block
	 * @return new Pedestal block with Better End id.
	 */
	public static Block registerPedestal(String name, Block source) {
		return EndBlocks.registerBlock(name, new PedestalBlock(source));
	}
	
	/**
	 * 
	 * Register new Pedestal block with specified mod id.
	 * 
	 * @param id pedestal id
	 * @param source source block
	 * @return new Pedestal block with specified id.
	 */
	public static Block registerPedestal(ResourceLocation id, Block source) {
		return EndBlocks.registerBlock(id, new PedestalBlock(source));
	}
	
	protected final Block parent;
	protected float height = 1.0F;
	
	public PedestalBlock(Block parent) {
		super(FabricBlockSettings.copyOf(parent).luminance(state -> state.getValue(HAS_LIGHT) ? 12 : 0));
		this.registerDefaultState(stateDefinition.any().setValue(STATE, PedestalState.DEFAULT).setValue(HAS_ITEM, false).setValue(HAS_LIGHT, false));
		this.parent = parent;
	}
	
	public float getHeight(BlockState state) {
		if (state.getBlock() instanceof PedestalBlock && state.getValue(STATE) == PedestalState.PEDESTAL_TOP) {
			return this.height - 0.2F;
		}
		return this.height;
	}
	
	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide || !state.is(this)) return InteractionResult.CONSUME;
		if (!isPlaceable(state)) {
			return InteractionResult.PASS;
		}
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PedestalBlockEntity) {
			PedestalBlockEntity pedestal = (PedestalBlockEntity) blockEntity;
			if (pedestal.isEmpty()) {
				ItemStack itemStack = player.getItemInHand(hand);
				if (itemStack.isEmpty()) return InteractionResult.CONSUME;
				pedestal.setItem(0, itemStack);
				checkRitual(world, pos);
				return InteractionResult.SUCCESS;
			} else {
				ItemStack itemStack = pedestal.getItem(0);
				if (player.addItem(itemStack)) {
					pedestal.removeItemNoUpdate(0);
					checkRitual(world, pos);
					return InteractionResult.SUCCESS;
				}
				return InteractionResult.FAIL;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public void destroy(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
		MutableBlockPos posMutable = new MutableBlockPos();
		for (Point point: InfusionRitual.getMap()) {
			posMutable.set(blockPos).move(point.x, 0, point.y);
			BlockState state = levelAccessor.getBlockState(posMutable);
			if (state.getBlock() instanceof InfusionPedestal) {
				BlockEntity blockEntity = levelAccessor.getBlockEntity(posMutable);
				if (blockEntity instanceof InfusionPedestalEntity) {
					InfusionPedestalEntity pedestal = (InfusionPedestalEntity) blockEntity;
					if (pedestal.hasRitual()) {
						pedestal.getRitual().markDirty();
					}
				}
				break;
			}
		}
	}

	public void checkRitual(Level world, BlockPos pos) {
		MutableBlockPos posMutable = new MutableBlockPos();
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
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState upState = world.getBlockState(pos.above());
		BlockState downState = world.getBlockState(pos.below());
		boolean upSideSolid = upState.isFaceSturdy(world, pos.above(), Direction.DOWN) || upState.is(BlockTags.WALLS);
		boolean hasPedestalOver = upState.getBlock() instanceof PedestalBlock;
		boolean hasPedestalUnder = downState.getBlock() instanceof PedestalBlock;
		if (!hasPedestalOver && hasPedestalUnder && upSideSolid) {
			return defaultBlockState().setValue(STATE, PedestalState.COLUMN_TOP);
		} else if (!hasPedestalOver && !hasPedestalUnder && upSideSolid) {
			return defaultBlockState().setValue(STATE, PedestalState.COLUMN);
		} else if (hasPedestalUnder && hasPedestalOver) {
			return defaultBlockState().setValue(STATE, PedestalState.PILLAR);
		} else if (hasPedestalUnder) {
			return defaultBlockState().setValue(STATE, PedestalState.PEDESTAL_TOP);
		} else if (hasPedestalOver) {
			return defaultBlockState().setValue(STATE, PedestalState.BOTTOM);
		}
		return defaultBlockState();
	}
	
	@Override
	@Deprecated
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		BlockState updated = getUpdatedState(state, direction, newState, world, pos, posFrom);
		if (!updated.is(this)) return updated;
		if (!isPlaceable(updated)) {
			moveStoredStack(world, updated, pos);
		}
		return updated;
	}
	
	private BlockState getUpdatedState(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		if (!state.is(this)) return state.updateShape(direction, newState, world, pos, posFrom);
		if (direction != Direction.UP && direction != Direction.DOWN) return state;
		BlockState upState = world.getBlockState(pos.above());
		BlockState downState = world.getBlockState(pos.below());
		boolean upSideSolid = upState.isFaceSturdy(world, pos.above(), Direction.DOWN) || upState.is(BlockTags.WALLS);
		boolean hasPedestalOver = upState.getBlock() instanceof PedestalBlock;
		boolean hasPedestalUnder = downState.getBlock() instanceof PedestalBlock;
		if (direction == Direction.UP) {
			upSideSolid = newState.isFaceSturdy(world, posFrom, Direction.DOWN) || newState.is(BlockTags.WALLS);
			hasPedestalOver = newState.getBlock() instanceof PedestalBlock;
		} else {
			hasPedestalUnder = newState.getBlock() instanceof PedestalBlock;
		}
		BlockState updatedState;
		if (!hasPedestalOver && hasPedestalUnder && upSideSolid) {
			updatedState = state.setValue(STATE, PedestalState.COLUMN_TOP);
		} else if (!hasPedestalOver && !hasPedestalUnder && upSideSolid) {
			updatedState = state.setValue(STATE, PedestalState.COLUMN);
		} else if (hasPedestalUnder && hasPedestalOver) {
			updatedState = state.setValue(STATE, PedestalState.PILLAR);
		} else if (hasPedestalUnder) {
			updatedState = state.setValue(STATE, PedestalState.PEDESTAL_TOP);
		} else if (hasPedestalOver) {
			updatedState = state.setValue(STATE, PedestalState.BOTTOM);
		} else {
			updatedState = state.setValue(STATE, PedestalState.DEFAULT);
		}
		if (!isPlaceable(updatedState)) {
			updatedState = updatedState.setValue(HAS_ITEM, false).setValue(HAS_LIGHT, false);
		}
		return updatedState;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drop = Lists.newArrayList(super.getDrops(state, builder));
		if (state.is(this)) {
			if (isPlaceable(state)) {
				BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
				if (blockEntity instanceof PedestalBlockEntity) {
					PedestalBlockEntity pedestal = (PedestalBlockEntity) blockEntity;
					if (!pedestal.isEmpty()) {
						drop.add(pedestal.getItem(0));
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
			ItemStack stack = pedestal.removeItemNoUpdate(0);
			if (!stack.isEmpty()) {
				moveStoredStack(blockEntity, world, stack, pos.above());
			}
		}
	}
	
	private void moveStoredStack(BlockEntity blockEntity, LevelAccessor world, ItemStack stack, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (!state.is(this)) {
			dropStoredStack(blockEntity, stack, pos);
		} else if (state.getValue(STATE).equals(PedestalState.PILLAR)) {
			moveStoredStack(blockEntity, world, stack, pos.above());
		} else if (!isPlaceable(state)) {
			dropStoredStack(blockEntity, stack, pos);
		} else if (blockEntity instanceof PedestalBlockEntity) {
			PedestalBlockEntity pedestal = (PedestalBlockEntity) blockEntity;
			if (pedestal.isEmpty()) {
				pedestal.setItem(0, stack);
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
			Block.popResource(world, getDropPos(world, pos), stack);
		}
	}
	
	private BlockPos getDropPos(LevelAccessor world, BlockPos pos) {
		BlockPos dropPos;
		if (world.getBlockState(pos).isAir()) {
			return pos;
		}
		if (world.getBlockState(pos.above()).isAir()) {
			return pos.above();
		}
		for(int i = 2; i < Direction.values().length; i++) {
			dropPos = pos.relative(Direction.from3DDataValue(i));
			if (world.getBlockState(dropPos).isAir()) {
				return dropPos.immutable();
			}
		}
		return getDropPos(world, pos.above());
	}
	
	public boolean isPlaceable(BlockState state) {
		if (!state.is(this)) return false;
		PedestalState currentState = state.getValue(STATE);
		return currentState == PedestalState.DEFAULT ||
			   currentState == PedestalState.PEDESTAL_TOP;
	}
	
	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (state.is(this)) {
			switch(state.getValue(STATE)) {
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
		return super.getShape(state, world, pos, context);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(STATE, HAS_ITEM, HAS_LIGHT);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new PedestalBlockEntity(blockPos, blockState);
	}

	public boolean hasUniqueEntity() {
		return false;
	}
	
	@Override
	@Deprecated
	public boolean hasAnalogOutputSignal(BlockState state) {
		return state.getBlock() instanceof PedestalBlock;
	}
	
	@Override
	@Deprecated
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return state.getValue(HAS_ITEM) ? 15 : 0;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation blockId) {
		return getBlockModel(blockId, defaultBlockState());
	}

	@Override
	@Environment(EnvType.CLIENT)
	public @Nullable BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
		Map<String, String> textures = createTexturesMap();
		PedestalState state = blockState.getValue(STATE);
		Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_PEDESTAL_DEFAULT, textures);
		switch (state) {
			case COLUMN_TOP:
				pattern = Patterns.createJson(Patterns.BLOCK_PEDESTAL_COLUMN_TOP, textures);
				break;
			case COLUMN:
				pattern = Patterns.createJson(Patterns.BLOKC_PEDESTAL_COLUMN, textures);
				break;
			case PEDESTAL_TOP:
				pattern = Patterns.createJson(Patterns.BLOCK_PEDESTAL_TOP, textures);
				break;
			case BOTTOM:
				pattern = Patterns.createJson(Patterns.BLOCK_PEDESTAL_BOTTOM, textures);
				break;
			case PILLAR:
				pattern = Patterns.createJson(Patterns.BLOCK_PEDESTAL_PILLAR, textures);
				break;
			default: break;
		}
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		PedestalState state = blockState.getValue(STATE);
		ResourceLocation modelId = new ResourceLocation(stateId.getNamespace(),
				"block/" + stateId.getPath() + "_" + state);
		registerBlockModel(stateId, modelId, blockState, modelCache);
		return ModelsHelper.createBlockSimple(modelId);
	}

	protected Map<String, String> createTexturesMap() {
		ResourceLocation blockId = Registry.BLOCK.getKey(parent);
		String name = blockId.getPath();
		return new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("%mod%", blockId.getNamespace() );
				put("%top%", name + "_top");
				put("%base%", name + "_base");
				put("%pillar%", name + "_pillar");
				put("%bottom%", name + "_bottom");
			}
		};
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockEntityType<T> blockEntityType) {
		return level.isClientSide() ? null : createTickerHelper(blockEntityType, EndBlockEntities.PEDESTAL, PedestalBlockEntity::tick);
	}

	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityType, BlockEntityType<E> blockEntityType2, BlockEntityTicker<? super E> blockEntityTicker) {
		return blockEntityType2 == blockEntityType ? (BlockEntityTicker<A>) blockEntityTicker : null;
	}
	
	static {
		VoxelShape basinUp = Block.box(2, 3, 2, 14, 4, 14);
		VoxelShape basinDown = Block.box(0, 0, 0, 16, 3, 16);
		VoxelShape columnTopUp = Block.box(1, 14, 1, 15, 16, 15);
		VoxelShape columnTopDown = Block.box(2, 13, 2, 14, 14, 14);
		VoxelShape pedestalTop = Block.box(1, 8, 1, 15, 10, 15);
		VoxelShape pedestalDefault = Block.box(1, 12, 1, 15, 14, 15);
		VoxelShape pillar = Block.box(3, 0, 3, 13, 8, 13);
		VoxelShape pillarDefault = Block.box(3, 0, 3, 13, 12, 13);
		VoxelShape columnTop = Shapes.or(columnTopDown, columnTopUp);
		VoxelShape basin = Shapes.or(basinDown, basinUp);
		SHAPE_PILLAR = Block.box(3, 0, 3, 13, 16, 13);
		SHAPE_DEFAULT = Shapes.or(basin, pillarDefault, pedestalDefault);
		SHAPE_PEDESTAL_TOP = Shapes.or(pillar, pedestalTop);
		SHAPE_COLUMN_TOP = Shapes.or(SHAPE_PILLAR, columnTop);
		SHAPE_COLUMN = Shapes.or(basin, SHAPE_PILLAR, columnTop);
		SHAPE_BOTTOM = Shapes.or(basin, SHAPE_PILLAR);
	}
}
