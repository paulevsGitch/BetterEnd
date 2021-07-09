package ru.betterend.interfaces;

import net.minecraft.resources.ResourceLocation;

public interface FallFlyingItem {
	ResourceLocation getModelTexture();

	double getMovementFactor();
}
