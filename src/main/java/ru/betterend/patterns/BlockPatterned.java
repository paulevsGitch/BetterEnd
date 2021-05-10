package ru.betterend.patterns;

import java.io.Reader;

import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public interface BlockPatterned extends Patterned {
	String getStatesPattern(Reader data);
	ResourceLocation statePatternId();
	@NotNull UnbakedModel getBlockModel(BlockState blockState);
}
