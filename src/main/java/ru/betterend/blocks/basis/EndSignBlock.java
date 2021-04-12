package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.AbstractSignBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShapeContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.ItemPlacementContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.network.packet.s2c.play.SignEditorOpenS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.SignType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.core.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.entities.ESignBlockEntity;
import ru.betterend.interfaces.ISpetialItem;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;
import ru.betterend.util.BlocksHelper;

public class EndSignBlock extends AbstractSignBlock implements BlockPatterned, ISpetialItem {
	public static final IntegerProperty ROTATION = Properties.ROTATION;
	public static final BooleanProperty FLOOR = BooleanProperty.of("floor");
	private static final VoxelShape[] WALL_SHAPES = new VoxelShape[] {
			Block.createCuboidShape(0.0D, 4.5D, 14.0D, 16.0D, 12.5D, 16.0D),
			Block.createCuboidShape(0.0D, 4.5D, 0.0D, 2.0D, 12.5D, 16.0D),
			Block.createCuboidShape(0.0D, 4.5D, 0.0D, 16.0D, 12.5D, 2.0D),
			Block.createCuboidShape(14.0D, 4.5D, 0.0D, 16.0D, 12.5D, 16.0D) };

	private final Block parent;

	public EndSignBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).strength(1.0F, 1.0F).noCollision().nonOpaque(), SignType.OAK);
		this.setDefaultState(
				this.stateManager.defaultBlockState().with(ROTATION, 0).with(FLOOR, false).with(WATERLOGGED, false));
		this.parent = source;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ROTATION, FLOOR, WATERLOGGED);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return state.getValue(FLOOR) ? SHAPE : WALL_SHAPES[state.getValue(ROTATION) >> 2];
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new ESignBlockEntity();
	}

	@Override
	public void onPlaced(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (placer != null && placer instanceof Player) {
			ESignBlockEntity sign = (ESignBlockEntity) world.getBlockEntity(pos);
			if (!world.isClientSide) {
				sign.setEditor((Player) placer);
				((ServerPlayer) placer).networkHandler.sendPacket(new SignEditorOpenS2CPacket(pos));
			} else {
				sign.setEditable(true);
			}
		}
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world,
			BlockPos pos, BlockPos neighborPos) {
		if ((Boolean) state.getValue(WATERLOGGED)) {
			world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		if (!canPlaceAt(state, world, pos)) {
			return state.getValue(WATERLOGGED) ? state.getFluidState().getBlockState() : Blocks.AIR.defaultBlockState();
		}
		return super.updateShape(state, facing, neighborState, world, pos, neighborPos);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		if (!state.getValue(FLOOR)) {
			int index = (((state.getValue(ROTATION) >> 2) + 2)) & 3;
			return world.getBlockState(pos.relative(BlocksHelper.HORIZONTAL[index])).getMaterial().isSolid();
		} else {
			return world.getBlockState(pos.below()).getMaterial().isSolid();
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		if (ctx.getSide() == Direction.UP) {
			FluidState fluidState = ctx.getLevel().getFluidState(ctx.getBlockPos());
			return this.defaultBlockState().with(FLOOR, true)
					.with(ROTATION, Mth.floor((180.0 + ctx.getPlayerYaw() * 16.0 / 360.0) + 0.5 - 12) & 15)
					.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
		} else if (ctx.getSide() != Direction.DOWN) {
			BlockState blockState = this.defaultBlockState();
			FluidState fluidState = ctx.getLevel().getFluidState(ctx.getBlockPos());
			WorldView worldView = ctx.getLevel();
			BlockPos blockPos = ctx.getBlockPos();
			Direction[] directions = ctx.getPlacementDirections();

			for (int i = 0; i < directions.length; ++i) {
				Direction direction = directions[i];
				if (direction.getAxis().isHorizontal()) {
					Direction dir = direction.getOpposite();
					int rot = Mth.floor((180.0 + dir.asRotation() * 16.0 / 360.0) + 0.5 + 4) & 15;
					blockState = blockState.with(ROTATION, rot);
					if (blockState.canPlaceAt(worldView, blockPos)) {
						return blockState.with(FLOOR, false).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
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
		return (BlockState) state.with(ROTATION, rotation.rotate((Integer) state.getValue(ROTATION), 16));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return (BlockState) state.with(ROTATION, mirror.mirror((Integer) state.getValue(ROTATION), 16));
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}

	@Override
	public Fluid tryDrainFluid(LevelAccessor world, BlockPos pos, BlockState state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tryFillWithFluid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
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