package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.Patterns;

public class EndDoorBlock extends DoorBlock implements IRenderTypeable, BlockModelProvider {
	public EndDoorBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).strength(3F, 3F).noOcclusion());
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (state.getValue(HALF) == DoubleBlockHalf.LOWER)
			return Collections.singletonList(new ItemStack(this.asItem()));
		else
			return Collections.emptyList();
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		String blockId = Registry.BLOCK.getKey(this).getPath();
		return Patterns.createJson(data, blockId, blockId);
	}

	@Override
	public Optional<String> getModelString(String block) {
		String blockId = Registry.BLOCK.getKey(this).getPath();
		if (block.contains("item")) {
			return Patterns.createJson(Patterns.ITEM_GENERATED, block);
		}
		if (block.contains("top_hinge")) {
			return Patterns.createJson(Patterns.BLOCK_DOOR_TOP_HINGE, blockId, blockId);
		}
		if (block.contains("bottom_hinge")) {
			return Patterns.createJson(Patterns.BLOCK_DOOR_BOTTOM_HINGE, blockId, blockId);
		}
		if (block.contains("top")) {
			return Patterns.createJson(Patterns.BLOCK_DOOR_TOP, blockId, blockId);
		}
		return Patterns.createJson(Patterns.BLOCK_DOOR_BOTTOM, blockId, blockId);
	}

	@Override
	public BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
		String blockName = resourceLocation.getPath();
		DoorType doorType = getDoorType(blockState);
		Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_DOOR_BOTTOM, blockName, blockName);
		switch (doorType) {
			case TOP_HINGE: {
				pattern = Patterns.createJson(Patterns.BLOCK_DOOR_TOP_HINGE, blockName, blockName);
				break;
			}
			case BOTTOM_HINGE: {
				pattern = Patterns.createJson(Patterns.BLOCK_DOOR_BOTTOM_HINGE, blockName, blockName);
				break;
			}
			case TOP: {
				pattern = Patterns.createJson(Patterns.BLOCK_DOOR_TOP, blockName, blockName);
				break;
			}
		}
		return pattern.map(BlockModel::fromString).orElse(null);
	}

	@Override
	public MultiVariant getModelVariant(ResourceLocation resourceLocation, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		Direction facing = blockState.getValue(FACING);
		DoorType doorType = getDoorType(blockState);
		boolean open = blockState.getValue(OPEN);
		boolean hinge = doorType.isHinge();
		BlockModelRotation rotation = BlockModelRotation.X0_Y0;
		switch (facing) {
			case EAST: {
				if (hinge && open) {
					rotation = BlockModelRotation.by(0, 90);
				} else if (open) {
					rotation = BlockModelRotation.by(0, 270);
				}
				break;
			}
			case SOUTH: {
				if (!hinge && !open || hinge && !open) {
					rotation = BlockModelRotation.by(0, 90);
				} else if (hinge) {
					rotation = BlockModelRotation.by(0, 180);
				}
				break;
			}
			case WEST: {
				if (!hinge && !open || hinge && !open) {
					rotation = BlockModelRotation.by(0, 180);
				} else if (hinge) {
					rotation = BlockModelRotation.by(0, 270);
				} else {
					rotation = BlockModelRotation.by(0, 90);
				}
				break;
			}
			case NORTH: {
				if (!hinge && !open || hinge && !open) {
					rotation = BlockModelRotation.by(0, 270);
				} else if (!hinge) {
					rotation = BlockModelRotation.by(0, 180);
				}
				break;
			}
		}
		ResourceLocation modelId = new ResourceLocation(resourceLocation.getNamespace(),
				"block/" + resourceLocation.getPath() + "_" + doorType);
		registerBlockModel(resourceLocation, modelId, blockState, modelCache);
		Variant variant = new Variant(modelId, rotation.getRotation(), false, 1);
		return new MultiVariant(Lists.newArrayList(variant));
	}

	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_DOOR;
	}

	protected DoorType getDoorType(BlockState blockState) {
		boolean isHinge = isHinge(blockState.getValue(HINGE), blockState.getValue(OPEN));
		switch (blockState.getValue(HALF)) {
			case UPPER: {
				return isHinge ? DoorType.TOP_HINGE : DoorType.TOP;
			}
			case LOWER: {
				return isHinge ? DoorType.BOTTOM_HINGE : DoorType.BOTTOM;
			}
		}
		return DoorType.BOTTOM;
	}

	private boolean isHinge(DoorHingeSide hingeSide, boolean open) {
		boolean isHinge = hingeSide == DoorHingeSide.RIGHT;
		return isHinge && !open || !isHinge && open;
	}

	protected enum DoorType implements StringRepresentable {
		BOTTOM_HINGE("bottom_hinge"),
		TOP_HINGE("top_hinge"),
		BOTTOM("bottom"),
		TOP("top");

		private final String name;

		DoorType(String name) {
			this.name = name;
		}

		public boolean isHinge() {
			return this == BOTTOM_HINGE ||
					this == TOP_HINGE;
		}

		@Override
		public String toString() {
			return getSerializedName();
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
