package ru.betterend.blocks;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockRenderType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FluidDrainable;
import net.minecraft.world.level.block.FluidFillable;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.ShapeContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.WorldView;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;

public class VentBubbleColumnBlock extends Block implements FluidDrainable, FluidFillable {
	public VentBubbleColumnBlock() {
		super(FabricBlockSettings.of(Material.BUBBLE_COLUMN).nonOpaque().noCollision().dropsNothing());
	}

	@Override
	public Fluid tryDrainFluid(LevelAccessor world, BlockPos pos, BlockState state) {
		world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState(), 11);
		return Fluids.WATER;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos.below());
		return blockState.is(this) || blockState.is(EndBlocks.HYDROTHERMAL_VENT);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world,
			BlockPos pos, BlockPos posFrom) {
		if (!state.canPlaceAt(world, pos)) {
			return Blocks.WATER.defaultBlockState();
		} else {
			BlockPos up = pos.up();
			if (world.getBlockState(up).is(Blocks.WATER)) {
				BlocksHelper.setWithoutUpdate(world, up, this);
				world.getBlockTickScheduler().schedule(up, this, 5);
			}
		}
		return state;
	}

	@Environment(EnvType.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
		if (random.nextInt(4) == 0) {
			double px = pos.getX() + random.nextDouble();
			double py = pos.getY() + random.nextDouble();
			double pz = pos.getZ() + random.nextDouble();
			world.addImportantParticle(ParticleTypes.BUBBLE_COLUMN_UP, px, py, pz, 0, 0.04, 0);
		}
		if (random.nextInt(200) == 0) {
			world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT,
					SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
		}
	}

	@Environment(EnvType.CLIENT)
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		BlockState blockState = world.getBlockState(pos.up());
		if (blockState.isAir()) {
			entity.onBubbleColumnSurfaceCollision(false);
			if (!world.isClientSide) {
				ServerLevel serverWorld = (ServerLevel) world;

				for (int i = 0; i < 2; ++i) {
					serverWorld.sendParticles(ParticleTypes.SPLASH, (double) pos.getX() + world.random.nextDouble(),
							(double) (pos.getY() + 1), (double) pos.getZ() + world.random.nextDouble(), 1, 0.0D, 0.0D,
							0.0D, 1.0D);
					serverWorld.sendParticles(ParticleTypes.BUBBLE, (double) pos.getX() + world.random.nextDouble(),
							(double) (pos.getY() + 1), (double) pos.getZ() + world.random.nextDouble(), 1, 0.0D, 0.01D,
							0.0D, 0.2D);
				}
			}
		} else {
			entity.onBubbleColumnCollision(false);
		}
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
	public FluidState getFluidState(BlockState state) {
		return Fluids.WATER.getStill(false);
	}
}
