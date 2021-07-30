package ru.betterend.interfaces;

public interface PottableTerrain {
	default boolean canBePotted() {
		return true;
	}
}
