package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.Patterns;
import ru.betterend.registry.EndTags;

public abstract class FeatureSaplingBlock extends BlockBaseNotFull implements BonemealableBlock, IRenderTypeable {
	private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 14, 12);
	
	public FeatureSaplingBlock() {
		super(FabricBlockSettings.of(Material.PLANT)
				.breakByHand(true)
				.collidable(false)
				.instabreak()
				.sound(SoundType.GRASS)
				.randomTicks());
	}
	
	public FeatureSaplingBlock(int light) {
		super(FabricBlockSettings.of(Material.PLANT)
				.breakByHand(true)
				.collidable(false)
				.luminance(light)
				.instabreak()
				.sound(SoundType.GRASS)
				.randomTicks());
	}
	
	protected abstract Feature<?> getFeature();

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return SHAPE;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return world.getBlockState(pos.below()).is(EndTags.END_GROUND);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (!canSurvive(state, world, pos))
			return Blocks.AIR.defaultBlockState();
		else
			return state;
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean isBonemealSuccess(Level world, Random random, BlockPos pos, BlockState state) {
		return random.nextInt(16) == 0;
	}

	@Override
	public void performBonemeal(ServerLevel world, Random random, BlockPos pos, BlockState state) {
		getFeature().place(world, world.getChunkSource().getGenerator(), random, pos, null);
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		super.tick(state, world, pos, random);
		if (isBonemealSuccess(world, random, pos, state)) {
			performBonemeal(world, random, pos, state);
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
	public String getModelString(String block) {
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
