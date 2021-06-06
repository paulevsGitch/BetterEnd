package ru.betterend.blocks;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.bclib.util.BlocksHelper;
import ru.betterend.registry.EndBlocks;

public class VentBubbleColumnBlock extends Block implements BucketPickup, LiquidBlockContainer {
	public VentBubbleColumnBlock() {
		super(FabricBlockSettings.of(Material.BUBBLE_COLUMN).noOcclusion().noCollission().noDrops());
	}

	@Override
	public Fluid takeLiquid(LevelAccessor world, BlockPos pos, BlockState state) {
		world.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
		return Fluids.WATER;
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos.below());
		return blockState.is(this) || blockState.is(EndBlocks.HYDROTHERMAL_VENT);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		if (!state.canSurvive(world, pos)) {
			return Blocks.WATER.defaultBlockState();
		}
		else {
			BlockPos up = pos.above();
			if (world.getBlockState(up).is(Blocks.WATER)) {
				BlocksHelper.setWithoutUpdate(world, up, this);
				world.getBlockTicks().scheduleTick(up, this, 5);
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
			world.addAlwaysVisibleParticle(ParticleTypes.BUBBLE_COLUMN_UP, px, py, pz, 0, 0.04, 0);
		}
		if (random.nextInt(200) == 0) {
			world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
		}
	}

	@Environment(EnvType.CLIENT)
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		BlockState blockState = world.getBlockState(pos.above());
		if (blockState.isAir()) {
			entity.onAboveBubbleCol(false);
			if (!world.isClientSide) {
				ServerLevel serverWorld = (ServerLevel) world;

				for (int i = 0; i < 2; ++i) {
					serverWorld.sendParticles(ParticleTypes.SPLASH, (double) pos.getX() + world.random.nextDouble(), (double) (pos.getY() + 1), (double) pos.getZ() + world.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 1.0D);
					serverWorld.sendParticles(ParticleTypes.BUBBLE, (double) pos.getX() + world.random.nextDouble(), (double) (pos.getY() + 1), (double) pos.getZ() + world.random.nextDouble(), 1, 0.0D, 0.01D, 0.0D, 0.2D);
				}
			}
		}
		else {
			entity.onInsideBubbleColumn(false);
		}
	}
	
	@Override
	public boolean canPlaceLiquid(BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
		return false;
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return Fluids.WATER.getSource(false);
	}
}
