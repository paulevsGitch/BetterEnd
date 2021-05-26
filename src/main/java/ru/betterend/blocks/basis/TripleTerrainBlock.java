package ru.betterend.blocks.basis;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.client.models.ModelsHelper;
import ru.betterend.client.models.Patterns;

public class TripleTerrainBlock extends EndTerrainBlock {
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	
	public TripleTerrainBlock(MaterialColor color) {
		super(color);
		this.registerDefaultState(this.defaultBlockState().setValue(SHAPE, TripleShape.BOTTOM));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		Direction dir = ctx.getClickedFace();
		TripleShape shape = dir == Direction.UP ? TripleShape.BOTTOM : dir == Direction.DOWN ? TripleShape.TOP : TripleShape.MIDDLE;
		return this.defaultBlockState().setValue(SHAPE, shape);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		TripleShape shape = state.getValue(SHAPE);
		if (shape == TripleShape.BOTTOM) {
			return super.use(state, world, pos, player, hand, hit);
		}
		return InteractionResult.FAIL;
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		TripleShape shape = state.getValue(SHAPE);
		if (shape == TripleShape.BOTTOM) {
			super.randomTick(state, world, pos, random);
		} else if (random.nextInt(16) == 0) {
			boolean bottom = canStayBottom(world, pos);
			if (shape == TripleShape.TOP) {
				if (!bottom) {
					world.setBlockAndUpdate(pos, Blocks.END_STONE.defaultBlockState());
				}
			}
			else {
				boolean top = canStay(state, world, pos) || isMiddle(world.getBlockState(pos.above()));
				if (!top && !bottom) {
					world.setBlockAndUpdate(pos, Blocks.END_STONE.defaultBlockState());
				} else if (top && !bottom) {
					world.setBlockAndUpdate(pos, state.setValue(SHAPE, TripleShape.BOTTOM));
				} else if (!top) {
					world.setBlockAndUpdate(pos, state.setValue(SHAPE, TripleShape.TOP));
				}
			}
		}
	}
	
	protected boolean canStayBottom(LevelReader world, BlockPos pos) {
		BlockPos blockPos = pos.below();
		BlockState blockState = world.getBlockState(blockPos);
		if (isMiddle(blockState)) {
			return true;
		} else if (blockState.getFluidState().getAmount() == 8) {
			return false;
		} else {
			return !blockState.isFaceSturdy(world, blockPos, Direction.UP);
		}
	}

	@Override
	public BlockModel getItemModel(ResourceLocation blockId) {
		return getBlockModel(blockId, defaultBlockState());
	}

	@Override
	public @Nullable BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
		String name = resourceLocation.getPath();
		Optional<String> pattern;
		if (isMiddle(blockState)) {
			pattern = Patterns.createBlockSimple(name + "_top");
		} else {
			Map<String, String> textures = Maps.newHashMap();
			textures.put("%top%", "betterend:block/" + name + "_top");
			textures.put("%side%", "betterend:block/" + name + "_side");
			textures.put("%bottom%", "minecraft:block/end_stone");
			pattern = Patterns.createJson(Patterns.BLOCK_TOP_SIDE_BOTTOM, textures);
		}
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		boolean isMiddle = isMiddle(blockState);
		String middle = isMiddle ? "_middle" : "";
		ResourceLocation modelId = new ResourceLocation(stateId.getNamespace(),
				"block/" + stateId.getPath() + middle);
		registerBlockModel(stateId, modelId, blockState, modelCache);
		if (isMiddle) {
			List<Variant> variants = Lists.newArrayList();
			for (BlockModelRotation rotation : BlockModelRotation.values()) {
				variants.add(new Variant(modelId, rotation.getRotation(), false, 1));
			}
			return new MultiVariant(variants);
		} else if (blockState.getValue(SHAPE) == TripleShape.TOP) {
			return new MultiVariant(Lists.newArrayList(
				new Variant(modelId, BlockModelRotation.X180_Y0.getRotation(), false, 1),
				new Variant(modelId, BlockModelRotation.X180_Y90.getRotation(), false, 1),
				new Variant(modelId, BlockModelRotation.X180_Y180.getRotation(), false, 1),
				new Variant(modelId, BlockModelRotation.X180_Y270.getRotation(), false, 1)
			));
		}
		return ModelsHelper.createRandomTopModel(modelId);
	}

	protected boolean isMiddle(BlockState blockState) {
		return blockState.is(this) && blockState.getValue(SHAPE) == TripleShape.MIDDLE;
	}
}
