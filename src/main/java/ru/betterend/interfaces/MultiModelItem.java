package ru.betterend.interfaces;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public interface MultiModelItem {
	@Environment(EnvType.CLIENT)
	void registerModelPredicate();
}
