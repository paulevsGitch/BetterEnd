package ru.betterend.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldGenRegion.class)
public class WorldGenRegionMixin {
	@Final
	@Shadow
	private ChunkAccess center;
	
	@Inject(method = "ensureCanWrite", at = @At("HEAD"), cancellable = true)
	private void be_alterBlockCheck(BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
		int x = blockPos.getX() >> 4;
		int z = blockPos.getZ() >> 4;
		WorldGenRegion region = (WorldGenRegion) (Object) this;
		info.setReturnValue(Math.abs(x - center.x) < 2 && Math.abs(z - center.z) < 2);
	}
}
