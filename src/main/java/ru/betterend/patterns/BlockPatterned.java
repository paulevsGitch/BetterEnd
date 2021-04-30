<<<<<<< Updated upstream
package ru.betterend.patterns;

import java.io.Reader;

import net.minecraft.resources.ResourceLocation;

public interface BlockPatterned extends Patterned {
	default String getStatesPattern(Reader data) {
		return null;
	}
	default ResourceLocation statePatternId() {
		return null;
	}
}
=======
package ru.betterend.patterns;

import java.io.Reader;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockPatterned extends Patterned {
	BlockModel getBlockModel(BlockState state);
	default String getStatesPattern(Reader data) {
		return null;
	}
	default ResourceLocation statePatternId() {
		return null;
	}
}
>>>>>>> Stashed changes
