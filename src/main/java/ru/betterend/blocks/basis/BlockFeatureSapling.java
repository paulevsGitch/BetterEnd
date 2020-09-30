package ru.betterend.blocks.basis;

import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.client.ERenderLayer;
import ru.betterend.client.IRenderTypeable;
import ru.betterend.registry.BlockTagRegistry;
import ru.betterend.util.BlocksHelper;
import ru.betterend.world.features.EndFeature;

public class BlockFeatureSapling extends BlockBaseNotFull implements Fertilizable, IRenderTypeable {
	private static final VoxelShape SHAPE = Block.createCuboidShape(4, 2, 4, 12, 16, 12);
	private final EndFeature feature;
	
	public BlockFeatureSapling(EndFeature feature) {
		super(FabricBlockSettings.of(Material.PLANT)
				.breakByHand(true)
				.collidable(false)
				.breakInstantly()
				.sounds(BlockSoundGroup.GRASS)
				.ticksRandomly());
		this.feature = feature;
	}
	
	public BlockFeatureSapling(EndFeature feature, int light) {
		super(FabricBlockSettings.of(Material.PLANT)
				.breakByHand(true)
				.collidable(false)
				.breakInstantly()
				.sounds(BlockSoundGroup.GRASS)
				.lightLevel(light)
				.ticksRandomly());
		this.feature = feature;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return SHAPE;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.down()).isIn(BlockTagRegistry.END_GROUND);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!canPlaceAt(state, world, pos))
			return Blocks.AIR.getDefaultState();
		else
			return state;
	}

	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return random.nextInt(16) == 0;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		BlocksHelper.setWithoutUpdate(world, pos, Blocks.AIR.getDefaultState());
		feature.getFeature().generate(world, world.getChunkManager().getChunkGenerator(), random, pos, DefaultFeatureConfig.INSTANCE);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		if (canGrow(world, random, pos, state)) {
			grow(world, random, pos, state);
		}
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
}
