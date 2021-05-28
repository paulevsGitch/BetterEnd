package ru.betterend.blocks;

import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import ru.bclib.client.models.ModelsHelper;
import ru.betterend.blocks.basis.EndAnvilBlock;
import ru.betterend.client.models.Patterns;
import ru.betterend.item.material.EndToolMaterial;
import ru.betterend.registry.EndBlocks;

public class AeterniumAnvil extends EndAnvilBlock {
	private static final IntegerProperty DESTRUCTION_LONG = BlockProperties.DESTRUCTION_LONG;
	
	public AeterniumAnvil() {
		super(EndBlocks.AETERNIUM_BLOCK.defaultMaterialColor(), EndToolMaterial.AETERNIUM.getLevel());
	}
	
	@Override
	public IntegerProperty getDestructionProperty() {
		return DESTRUCTION_LONG;
	}

	@Override
	public @Nullable BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		String name = blockId.getPath();
		int damage = getDamageState(blockState);
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%anvil%", name);
		textures.put("%top%", name + "_top_" + damage);
		Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_ANVIL, textures);
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		int damage = getDamageState(blockState);
		String modId = stateId.getNamespace();
		String modelId = "block/" + stateId.getPath() + "_top_" + damage;
		ResourceLocation modelLocation = new ResourceLocation(modId, modelId);
		registerBlockModel(stateId, modelLocation, blockState, modelCache);
		return ModelsHelper.createFacingModel(modelLocation, blockState.getValue(FACING), false, false);
	}

	private int getDamageState(BlockState blockState) {
		IntegerProperty destructionProperty = getDestructionProperty();
		int damage = blockState.getValue(destructionProperty);
		return damage < 3 ? 0 : damage < 6 ? 1 : 2;
	}
}
