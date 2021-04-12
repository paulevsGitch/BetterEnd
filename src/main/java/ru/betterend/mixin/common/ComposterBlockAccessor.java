package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.ItemLike;

@Mixin(ComposterBlock.class)
public interface ComposterBlockAccessor {
	@Invoker
	static void callRegisterCompostableItem(float levelIncreaseChance, ItemLike item) {
		throw new AssertionError("@Invoker dummy body called");
	}
}
