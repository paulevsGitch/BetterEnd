package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fertilizable;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.ShapeContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.WorldView;
import net.minecraft.world.level.levelgen.feature.Feature;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.Patterns;
import ru.betterend.registry.EndTags;

public abstract class FeatureSaplingBlock extends BlockBaseNotFull implements Fertilizable, IRenderTypeable {
	private static final VoxelShape SHAPE = Block.createCuboidShape(4, 0, 4, 12, 14, 12);

	public FeatureSaplingBlock() {
		super(FabricBlockSettings.of(Material.PLANT).breakByHand(true).collidable(false).breakInstantly()
				.sounds(SoundType.GRASS).ticksRandomly());
	}

	public FeatureSaplingBlock(int light) {
		super(FabricBlockSettings.of(Material.PLANT).breakByHand(true).collidable(false).breakInstantly()
				.sounds(SoundType.GRASS).luminance(light).ticksRandomly());
	}

	protected abstract Feature<?> getFeature();

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return SHAPE;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.below()).isIn(EndTags.END_GROUND);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world,
			BlockPos pos, BlockPos neighborPos) {
		if (!canPlaceAt(state, world, pos))
			return Blocks.AIR.defaultBlockState();
		else
			return state;
	}

	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canGrow(Level world, Random random, BlockPos pos, BlockState state) {
		return random.nextInt(16) == 0;
	}

	@Override
	public void grow(ServerLevel world, Random random, BlockPos pos, BlockState state) {
		getFeature().place(world, world.getChunkManager().getChunkGenerator(), random, pos, null);
	}

	@Override
	public void scheduledTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		if (canGrow(world, random, pos, state)) {
			grow(world, random, pos, state);
		}
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}

	@Override
	public String getStatesPattern(Reader data) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		return Patterns.createJson(data, blockId.getPath(), blockId.getPath());
	}

	@Override
	public String getModelPattern(String block) {
		if (block.contains("item")) {
			block = block.split("/")[1];
			return Patterns.createJson(Patterns.ITEM_BLOCK, block);
		}
		return Patterns.createJson(Patterns.BLOCK_CROSS, block);
	}

	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_SAPLING;
	}
}
