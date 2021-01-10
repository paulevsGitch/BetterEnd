package ru.betterend.mixin.common;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
	private static Direction[] HORIZONTAL;
	
	@Inject(method = "findRespawnPosition", at = @At(value = "HEAD"), cancellable = true)
	private static void be_statueRespawn(ServerWorld world, BlockPos pos, float f, boolean bl, boolean bl2, CallbackInfoReturnable<Optional<Vec3d>> info) {
		BlockState blockState = world.getBlockState(pos);
		if (blockState.isOf(EndBlocks.RESPAWN_OBELISK)) {
			info.setReturnValue(beObeliskRespawnPosition(world, pos, blockState));
			info.cancel();
		}
	}

	private static Optional<Vec3d> beObeliskRespawnPosition(ServerWorld world, BlockPos pos, BlockState state) {
		if (state.get(BlockProperties.TRIPLE_SHAPE) == TripleShape.TOP) {
			pos = pos.down(2);
		}
		else if (state.get(BlockProperties.TRIPLE_SHAPE) == TripleShape.MIDDLE) {
			pos = pos.down();
		}
		if (HORIZONTAL == null) {
			HORIZONTAL = BlocksHelper.makeHorizontal();
		}
		MHelper.shuffle(HORIZONTAL, world.getRandom());
		for (Direction dir: HORIZONTAL) {
			BlockPos p = pos.offset(dir);
			BlockState state2 = world.getBlockState(p);
			if (!state2.getMaterial().blocksMovement() && state2.getCollisionShape(world, pos).isEmpty()) {
				return Optional.of(Vec3d.of(p).add(0.5, 0, 0.5));
			}
		}
		return Optional.empty();
	}
}