package ru.betterend.interfaces;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface MultiModelItem {
	void registerModelPredicate();
}
