package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.betterend.blocks.entities.ESignBlockEntity;
import ru.betterend.interfaces.ISpetialItem;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;
import ru.betterend.util.BlocksHelper;

public class EndSignBlock extends SignBlock implements BlockPatterned, ISpetialItem {
	public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
	public static final BooleanProperty FLOOR = BooleanProperty.create("floor");
	private static final VoxelShape[] WALL_SHAPES = new VoxelShape[] {
		Block.box(0.0D, 4.5D, 14.0D, 16.0D, 12.5D, 16.0D),
		Block.box(0.0D, 4.5D, 0.0D, 2.0D, 12.5D, 16.0D),
		Block.box(0.0D, 4.5D, 0.0D, 16.0D, 12.5D, 2.0D),
		Block.box(14.0D, 4.5D, 0.0D, 16.0D, 12.5D, 16.0D)
	};

	private final Block parent;
	
	public EndSignBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).strength(1.0F, 1.0F).noCollission().noOcclusion(), WoodType.OAK);
		this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, 0).setValue(FLOOR, false).setValue(WATERLOGGED, false));
		this.parent = source;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ROTATION, FLOOR, WATERLOGGED);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return state.getValue(FLOOR) ? SHAPE : WALL_SHAPES[state.getValue(ROTATION) >> 2];
	}

	@Override
	public BlockEntity newBlockEntity(BlockGetter world) {
		return new ESignBlockEntity();
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (placer != null && placer instanceof Player) {
			ESignBlockEntity sign = (ESignBlockEntity) world.getBlockEntity(pos);
			if (!world.isClientSide) {
				sign.setAllowedPlayerEditor((Player) placer);
				((ServerPlayer) placer).connection.send(new ClientboundOpenSignEditorPacket(pos));
			}
			else {
				sign.setEditable(true);
			}
		}
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if ((Boolean) state.getValue(WATERLOGGED)) {
			world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		if (!canSurvive(state, world, pos)) {
			return state.getValue(WATERLOGGED) ? state.getFluidState().createLegacyBlock() : Blocks.AIR.defaultBlockState();
		}
		return super.updateShape(state, facing, neighborState, world, pos, neighborPos);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		if (!state.getValue(FLOOR)) {
			int index = (((state.getValue(ROTATION) >> 2) + 2)) & 3;
			return world.getBlockState(pos.relative(BlocksHelper.HORIZONTAL[index])).getMaterial().isSolid();
		}
		else {
			return world.getBlockState(pos.below()).getMaterial().isSolid();
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		if (ctx.getClickedFace() == Direction.UP) {
			FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
			return this.defaultBlockState().setValue(FLOOR, true)
					.setValue(ROTATION, Mth.floor((180.0 + ctx.getRotation() * 16.0 / 360.0) + 0.5 - 12) & 15)
					.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
		}
		else if (ctx.getClickedFace() != Direction.DOWN) {
			BlockState blockState = this.defaultBlockState();
			FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
			LevelReader worldView = ctx.getLevel();
			BlockPos blockPos = ctx.getClickedPos();
			Direction[] directions = ctx.getNearestLookingDirections();

			for (int i = 0; i < directions.length; ++i) {
				Direction direction = directions[i];
				if (direction.getAxis().isHorizontal()) {
					Direction dir = direction.getOpposite();
					int rot = Mth.floor((180.0 + dir.toYRot() * 16.0 / 360.0) + 0.5 + 4) & 15;
					blockState = blockState.setValue(ROTATION, rot);
					if (blockState.canSurvive(worldView, blockPos)) {
						return blockState.setValue(FLOOR, false).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
					}
				}
			}
		}

		return null;
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		return Patterns.createJson(data, parentId.getPath(), blockId.getPath());
	}
	
	@Override
	public String getModelPattern(String path) {
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		if (path.contains("item")) {
			return Patterns.createJson(Patterns.ITEM_GENERATED, path);
		}
		return Patterns.createJson(Patterns.BLOCK_EMPTY, parentId.getPath());
	}
	
	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_SIMPLE;
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return (BlockState) state.setValue(ROTATION, rotation.rotate((Integer) state.getValue(ROTATION), 16));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return (BlockState) state.setValue(ROTATION, mirror.mirror((Integer) state.getValue(ROTATION), 16));
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}

	@Override
	public Fluid takeLiquid(LevelAccessor world, BlockPos pos, BlockState state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canPlaceLiquid(BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getStackSize() {
		return 16;
	}

	@Override
	public boolean canPlaceOnWater() {
		return false;
	}
}