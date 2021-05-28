package ru.betterend.blocks.basis;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.bclib.client.models.BlockModelProvider;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.ModelsHelper.MultiPartBuilder;
import ru.betterend.client.models.Patterns;

public class EndComposterBlock extends ComposterBlock implements BlockModelProvider {
	public EndComposterBlock(Block source) {
		super(FabricBlockSettings.copyOf(source));
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this.asItem()));
	}

	@Override
	public BlockModel getItemModel(ResourceLocation resourceLocation) {
		return getBlockModel(resourceLocation, defaultBlockState());
	}

	@Override
	public @Nullable BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_COMPOSTER, blockId.getPath());
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		ResourceLocation modelId = new ResourceLocation(stateId.getNamespace(), "block/" + stateId.getPath());
		registerBlockModel(stateId, modelId, blockState, modelCache);

		MultiPartBuilder builder = MultiPartBuilder.create(stateDefinition);
		LEVEL.getPossibleValues().forEach(level -> {
			if (level > 0) {
				ResourceLocation contentId;
				if (level > 7) {
					contentId = new ResourceLocation("block/composter_contents_ready");
				} else {
					contentId = new ResourceLocation("block/composter_contents" + level);
				}
				builder.part(contentId).setCondition(state -> state.getValue(LEVEL).equals(level)).add();
			}
		});
		builder.part(modelId).add();

		return builder.build();
	}
}
