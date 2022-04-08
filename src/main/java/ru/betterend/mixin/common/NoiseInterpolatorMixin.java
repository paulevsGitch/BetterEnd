package ru.betterend.mixin.common;

import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.betterend.interfaces.TargetChecker;
import ru.betterend.world.generator.TerrainGenerator;

import java.util.List;

@Mixin(NoiseChunk.NoiseInterpolator.class)
public interface NoiseInterpolatorMixin {
	@Accessor("slice0")
	public double[][] be_getSlice0();

	@Accessor("slice1")
	public double[][] be_getSlice1();
}
