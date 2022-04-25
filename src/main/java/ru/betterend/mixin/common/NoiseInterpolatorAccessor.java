package ru.betterend.mixin.common;

import net.minecraft.world.level.levelgen.NoiseChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NoiseChunk.NoiseInterpolator.class)
public interface NoiseInterpolatorAccessor {
	@Accessor("slice0")
	double[][] be_getSlice0();

	@Accessor("slice1")
	double[][] be_getSlice1();
}
