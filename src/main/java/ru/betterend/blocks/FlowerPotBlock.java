package ru.betterend.blocks;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import ru.bclib.blocks.BaseBlockNotFull;
import ru.bclib.client.models.BlockModelProvider;
import ru.betterend.client.models.MergedModel;
import ru.betterend.interfaces.PottablePlant;
import ru.betterend.registry.EndBlocks;

import java.util.List;
import java.util.Map;

public class FlowerPotBlock extends BaseBlockNotFull {
	private static final IntegerProperty PLANT_ID = EndBlockProperties.PLANT_ID;
	private final Block[] blocks;
	
	@Environment(EnvType.CLIENT)
	private UnbakedModel source;
	
	public FlowerPotBlock(Block source) {
		super(FabricBlockSettings.copyOf(source));
		List<Block> blocks = Lists.newArrayList();
		EndBlocks.getModBlocks().forEach(block -> {
			if (block instanceof PottablePlant) {
				blocks.add(block);
			}
		});
		this.blocks = blocks.toArray(new Block[] {});
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(PLANT_ID);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		int id = blockState.getValue(PLANT_ID);
		if (id == 0 || id > blocks.length) {
			if (id == 0) {
				source = super.getModelVariant(stateId, blockState, modelCache);
			}
			return source;
		}
		registerModel(stateId, blockState, modelCache);
		MergedModel model = new MergedModel(blockState, source);
		Block plant = blocks[id - 1];
		if (plant instanceof BlockModelProvider) {
			ResourceLocation location = Registry.BLOCK.getKey(plant);
			//model.addModel(((BlockModelProvider) plant).getBlockModel(location, plant.defaultBlockState()));
			model = new MergedModel(blockState, ((BlockModelProvider) plant).getBlockModel(location, plant.defaultBlockState()));
			System.out.println("Plant " + id + " is instance!");
		}
		return model;
	}
	
	private void registerModel(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		ResourceLocation modelId = new ResourceLocation(stateId.getNamespace(), "block/" + stateId.getPath());
		registerBlockModel(stateId, modelId, blockState, modelCache);
	}
}
