package ru.betterend.blocks;

import java.util.Random;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockEntityProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FluidFillable;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.ShapeContext;
import net.minecraft.world.level.block.Waterloggable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.item.ItemPlacementContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.basis.BlockBaseNotFull;
import ru.betterend.blocks.entities.BlockEntityHydrothermalVent;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndParticles;
import ru.betterend.util.BlocksHelper;

public class HydrothermalVentBlock extends BlockBaseNotFull
		implements BlockEntityProvider, FluidFillable, Waterloggable {
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final BooleanProperty ACTIVATED = BlockProperties.ACTIVE;
	private static final VoxelShape SHAPE = Block.createCuboidShape(1, 1, 1, 15, 16, 15);

	public HydrothermalVentBlock() {
		super(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).sounds(SoundType.STONE)
				.noCollision().requiresTool());
		this.setDefaultState(getDefaultState().with(WATERLOGGED, true).with(ACTIVATED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, ACTIVATED);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return SHAPE;
	}

	@Override
	public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	public boolean tryFillWithFluid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
		return false;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		state = world.getBlockState(pos.below());
		return state.is(EndBlocks.SULPHURIC_ROCK.stone);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world,
			BlockPos pos, BlockPos neighborPos) {
		if (!canPlaceAt(state, world, pos)) {
			return Blocks.WATER.defaultBlockState();
		} else if (state.getValue(WATERLOGGED) && facing == Direction.UP && neighborState.is(Blocks.WATER)) {
			world.getBlockTickScheduler().schedule(pos, this, 20);
		}
		return state;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		LevelAccessor worldAccess = ctx.getLevel();
		BlockPos blockPos = ctx.getBlockPos();
		return this.defaultBlockState().with(WATERLOGGED,
				worldAccess.getFluidState(blockPos).getFluid() == Fluids.WATER);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (Boolean) state.getValue(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new BlockEntityHydrothermalVent();
	}

	@Override
	public void scheduledTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		BlockPos up = pos.up();
		if (world.getBlockState(up).is(Blocks.WATER)) {
			BlocksHelper.setWithoutUpdate(world, up, EndBlocks.VENT_BUBBLE_COLUMN);
			world.getBlockTickScheduler().schedule(up, EndBlocks.VENT_BUBBLE_COLUMN, 5);
		}
	}

	@Override
	public void onPlaced(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
			ItemStack itemStack) {
		if (world instanceof ServerLevel && state.getValue(WATERLOGGED)
				&& world.getBlockState(pos.up()).is(Blocks.WATER)) {
			scheduledTick(state, (ServerLevel) world, pos, world.random);
		}
	}

	@Environment(EnvType.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
		if (!state.getValue(ACTIVATED) && random.nextBoolean()) {
			super.animateTick(state, world, pos, random);
			double x = pos.getX() + random.nextDouble();
			double y = pos.getY() + 0.9 + random.nextDouble() * 0.3;
			double z = pos.getZ() + random.nextDouble();
			if (state.getValue(WATERLOGGED)) {
				world.addParticle(EndParticles.GEYSER_PARTICLE, x, y, z, 0, 0, 0);
			} else {
				world.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0, 0);
			}
		}
	}
}
