package ru.betterend.blocks.basis;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.ModelsHelper;
import ru.betterend.client.models.Patterns;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;

public class EndTrapdoorBlock extends TrapDoorBlock implements IRenderTypeable, BlockModelProvider {
	public EndTrapdoorBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).strength(3.0F, 3.0F).noOcclusion());
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}

	@Override
	public BlockModel getItemModel(ResourceLocation resourceLocation) {
		return getBlockModel(resourceLocation, defaultBlockState());
	}

	@Override
	public @Nullable BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
		String name = resourceLocation.getPath();
		Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_TRAPDOOR, new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("%block%", name);
				put("%texture%", name.replace("trapdoor", "door_side"));
			}
		});
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		ResourceLocation modelId = new ResourceLocation(stateId.getNamespace(), "block/" + stateId.getPath());
		registerBlockModel(stateId, modelId, blockState, modelCache);
		boolean isTop = blockState.getValue(HALF) == Half.TOP;
		boolean isOpen = blockState.getValue(OPEN);
		int y = 0;
		int x = (isTop && isOpen) ? 270 : isTop ? 180 : isOpen ? 90 : 0;
		switch (blockState.getValue(FACING)) {
			case EAST:
				y = (isTop && isOpen) ? 270 : 90;
				break;
			case NORTH:
				if (isTop && isOpen) y = 180;
				break;
			case SOUTH:
				y = (isTop && isOpen) ? 0 : 180;
				break;
			case WEST:
				y = (isTop && isOpen) ? 90 : 270;
				break;
		}
		BlockModelRotation rotation = BlockModelRotation.by(x, y);
		return ModelsHelper.createMultiVariant(modelId, rotation.getRotation(), false);
	}
}
