package ru.betterend.mixin.common;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ComposterBlock.class)
public interface ComposterBlockAccessor {
	@Invoker
	static void callRegisterCompostableItem(float levelIncreaseChance, ItemLike item) {
		throw new AssertionError("@Invoker dummy body called");
	}
}
