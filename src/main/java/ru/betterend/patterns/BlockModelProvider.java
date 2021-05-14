package ru.betterend.patterns;

import java.io.Reader;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;

public interface BlockModelProvider extends ModelProvider {
	String getStatesPattern(Reader data);
	ResourceLocation statePatternId();
	Pair<ResourceLocation, BlockModel> getBlockModel(BlockState blockState);
	MultiVariant getModelVariant(BlockState blockState);
}
