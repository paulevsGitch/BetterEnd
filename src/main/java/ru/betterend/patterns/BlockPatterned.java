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
