package ru.betterend.patterns;

import java.io.Reader;

import net.minecraft.util.Identifier;

public interface BlockPatterned extends Patterned {
	default String getStatesPattern(Reader data) {
		return null;
	}
	default Identifier statePatternId() {
		return null;
	}
}
