package ru.betterend.mixin.common;

import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseChunk.NoiseFiller;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.betterend.world.generator.EndNoiseFiller;

@Mixin(NoiseChunk.NoiseInterpolator.class)
public class NoiseInterpolatorMixin {
	/*@Redirect(
		method = "fillSlice([[DI)V",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/level/levelgen/NoiseChunk$NoiseInterpolator;noiseFiller:Lnet/minecraft/world/level/levelgen/NoiseChunk$NoiseFiller;",
			opcode = Opcodes.GETFIELD
		)
	)
	private NoiseFiller getFiller(NoiseChunk.NoiseInterpolator interpolator) {
		return EndNoiseFiller.INSTANCE;
	}*/
}
