package ru.betterend.patterns;

public interface Patterned {
	default String getModelPattern(String name) {
		return null;
	}
}