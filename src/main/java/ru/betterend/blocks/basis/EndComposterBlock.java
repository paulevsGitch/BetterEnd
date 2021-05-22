package ru.betterend.blocks.basis;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.ModelsHelper;
import ru.betterend.client.models.ModelsHelper.MultiPartBuilder;
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
	public Optional<String> getModelString(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		String blockName = blockId.getPath();
		return Patterns.createJson(Patterns.BLOCK_COMPOSTER, blockName);
	}

	@Override
	public BlockModel getModel(ResourceLocation resourceLocation) {
		return getBlockModel(resourceLocation, defaultBlockState());
	}

	@Override
	public @Nullable BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_COMPOSTER, blockId.getPath());
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	public UnbakedModel getModelVariant(ResourceLocation resourceLocation, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		ResourceLocation modelId = new ResourceLocation(resourceLocation.getNamespace(), "block/" + resourceLocation.getPath());
		registerBlockModel(resourceLocation, modelId, blockState, modelCache);

		ResourceLocation content_1 = new ResourceLocation("block/composter_contents1");
		ResourceLocation content_2 = new ResourceLocation("block/composter_contents2");
		ResourceLocation content_3 = new ResourceLocation("block/composter_contents3");
		ResourceLocation content_4 = new ResourceLocation("block/composter_contents4");
		ResourceLocation content_5 = new ResourceLocation("block/composter_contents5");
		ResourceLocation content_6 = new ResourceLocation("block/composter_contents6");
		ResourceLocation content_7 = new ResourceLocation("block/composter_contents7");
		ResourceLocation content_8 = new ResourceLocation("block/composter_contents_ready");

		MultiPartBuilder builder = MultiPartBuilder.create(stateDefinition);
		builder.part(content_1).setCondition(state -> state.getValue(LEVEL) == 1).add();
		builder.part(content_2).setCondition(state -> state.getValue(LEVEL) == 2).add();
		builder.part(content_3).setCondition(state -> state.getValue(LEVEL) == 3).add();
		builder.part(content_4).setCondition(state -> state.getValue(LEVEL) == 4).add();
		builder.part(content_5).setCondition(state -> state.getValue(LEVEL) == 5).add();
		builder.part(content_6).setCondition(state -> state.getValue(LEVEL) == 6).add();
		builder.part(content_7).setCondition(state -> state.getValue(LEVEL) == 7).add();
		builder.part(content_8).setCondition(state -> state.getValue(LEVEL) == 8).add();
		builder.part(modelId).add();

		return builder.build();
	}
}
