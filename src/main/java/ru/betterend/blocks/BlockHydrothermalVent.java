package ru.betterend.blocks;

import java.util.Random;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.basis.BlockBaseNotFull;
import ru.betterend.blocks.entities.BlockEntityHydrothermalVent;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndParticles;
import ru.betterend.util.BlocksHelper;

public class BlockHydrothermalVent extends BlockBaseNotFull implements BlockEntityProvider, FluidFillable, Waterloggable {
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final BooleanProperty ACTIVATED = BlockProperties.ACTIVATED;
	private static final VoxelShape SHAPE = Block.createCuboidShape(1, 1, 1, 15, 16, 15);
	
	public BlockHydrothermalVent() {
		super(FabricBlockSettings.of(Material.STONE)
				.breakByTool(FabricToolTags.PICKAXES)
				.sounds(BlockSoundGroup.STONE)
				.noCollision()
				.requiresTool());
		this.setDefaultState(getDefaultState().with(WATERLOGGED, true).with(ACTIVATED, false));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
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
	public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		return false;
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		state = world.getBlockState(pos.down());
		return state.isOf(EndBlocks.SULPHURIC_ROCK.stone);
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!canPlaceAt(state, world, pos)) {
			return Blocks.WATER.getDefaultState();
		}
		else if (state.get(WATERLOGGED) && facing == Direction.UP && neighborState.isOf(Blocks.WATER)) {
			world.getBlockTickScheduler().schedule(pos, this, 20);
		}
		return state;
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		WorldAccess worldAccess = ctx.getWorld();
		BlockPos blockPos = ctx.getBlockPos();
		return this.getDefaultState().with(WATERLOGGED, worldAccess.getFluidState(blockPos).getFluid() == Fluids.WATER);
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return (Boolean) state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new BlockEntityHydrothermalVent();
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		BlockPos up = pos.up();
		if (world.getBlockState(up).isOf(Blocks.WATER)) {
			BlocksHelper.setWithoutUpdate(world, up, EndBlocks.VENT_BUBBLE_COLUMN);
			world.getBlockTickScheduler().schedule(up, EndBlocks.VENT_BUBBLE_COLUMN, 5);
		}
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (world instanceof ServerWorld && state.get(WATERLOGGED) && world.getBlockState(pos.up()).isOf(Blocks.WATER)) {
			scheduledTick(state,(ServerWorld) world, pos, world.random);
		}
	}
	
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (!state.get(ACTIVATED) && random.nextBoolean()) {
			super.randomDisplayTick(state, world, pos, random);
			double x = pos.getX() + random.nextDouble();
			double y = pos.getY() + 0.9 + random.nextDouble() * 0.3;
			double z = pos.getZ() + random.nextDouble();
			if (state.get(WATERLOGGED)) {
				world.addParticle(EndParticles.GEYSER_PARTICLE, x, y, z, 0, 0, 0);
			}
			else {
				world.addParticle(ParticleTypes.BUBBLE, x, y, z, 0, 0, 0);
			}
		}
	}
}
