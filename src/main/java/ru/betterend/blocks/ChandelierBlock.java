package ru.betterend.blocks;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.bclib.client.models.BlockModelProvider;
import ru.bclib.client.models.ModelsHelper;
import ru.betterend.blocks.basis.AttachedBlock;
import ru.betterend.client.models.Patterns;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;

public class ChandelierBlock extends AttachedBlock implements IRenderTypeable, BlockModelProvider {
	private static final EnumMap<Direction, VoxelShape> BOUNDING_SHAPES = Maps.newEnumMap(Direction.class);
	
	public ChandelierBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).luminance(15).noCollission().noOcclusion().requiresCorrectToolForDrops());
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return BOUNDING_SHAPES.get(state.getValue(FACING));
	}

	@Override
	public BlockModel getItemModel(ResourceLocation blockId) {
		return ModelsHelper.createItemModel(blockId);
	}

	@Override
	public @Nullable BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
		Optional<String> pattern;
		switch (blockState.getValue(FACING)) {
			case UP:
				pattern = Patterns.createJson(Patterns.BLOCK_CHANDELIER_FLOOR, resourceLocation.getPath());
				break;
			case DOWN:
				pattern = Patterns.createJson(Patterns.BLOCK_CHANDELIER_CEIL, resourceLocation.getPath());
				break;
			default:
				pattern = Patterns.createJson(Patterns.BLOCK_CHANDELIER_WALL, resourceLocation.getPath());
		}
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		String state = "_wall";
		BlockModelRotation rotation = BlockModelRotation.X0_Y0;
		switch (blockState.getValue(FACING)) {
			case UP:
				state = "_floor";
				break;
			case DOWN:
				state = "_ceil";
				break;
			case EAST:
				rotation = BlockModelRotation.X0_Y270;
				break;
			case NORTH:
				rotation = BlockModelRotation.X0_Y180;
				break;
			case WEST:
				rotation = BlockModelRotation.X0_Y90;
				break;
			default:
				break;
		}
		ResourceLocation modelId = new ResourceLocation(stateId.getNamespace(), "block/" + stateId.getPath() + state);
		registerBlockModel(stateId, modelId, blockState, modelCache);
		return ModelsHelper.createMultiVariant(modelId, rotation.getRotation(), false);
	}

	static {
		BOUNDING_SHAPES.put(Direction.UP, Block.box(5, 0, 5, 11, 13, 11));
		BOUNDING_SHAPES.put(Direction.DOWN, Block.box(5, 3, 5, 11, 16, 11));
		BOUNDING_SHAPES.put(Direction.NORTH, Shapes.box(0.0, 0.0, 0.5, 1.0, 1.0, 1.0));
		BOUNDING_SHAPES.put(Direction.SOUTH, Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 0.5));
		BOUNDING_SHAPES.put(Direction.WEST, Shapes.box(0.5, 0.0, 0.0, 1.0, 1.0, 1.0));
		BOUNDING_SHAPES.put(Direction.EAST, Shapes.box(0.0, 0.0, 0.0, 0.5, 1.0, 1.0));
	}
}
