package ru.betterend.mixin.common;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

@Mixin(Player.class)
public abstract class PlayerMixin {
	private static Direction[] horizontal;
	
	@Inject(method = "findRespawnPositionAndUseSpawnBlock", at = @At(value = "HEAD"), cancellable = true)
	private static void be_findRespawnPositionAndUseSpawnBlock(ServerLevel world, BlockPos pos, float f, boolean bl, boolean bl2, CallbackInfoReturnable<Optional<Vec3>> info) {
		BlockState blockState = world.getBlockState(pos);
		if (blockState.is(EndBlocks.RESPAWN_OBELISK)) {
			info.setReturnValue(be_obeliskRespawnPosition(world, pos, blockState));
			info.cancel();
		}
	}

	private static Optional<Vec3> be_obeliskRespawnPosition(ServerLevel world, BlockPos pos, BlockState state) {
		if (state.getValue(BlockProperties.TRIPLE_SHAPE) == TripleShape.TOP) {
			pos = pos.below(2);
		}
		else if (state.getValue(BlockProperties.TRIPLE_SHAPE) == TripleShape.MIDDLE) {
			pos = pos.below();
		}
		if (horizontal == null) {
			horizontal = BlocksHelper.makeHorizontal();
		}
		MHelper.shuffle(horizontal, world.getRandom());
		for (Direction dir: horizontal) {
			BlockPos p = pos.relative(dir);
			BlockState state2 = world.getBlockState(p);
			if (!state2.getMaterial().blocksMotion() && state2.getCollisionShape(world, pos).isEmpty()) {
				return Optional.of(Vec3.atLowerCornerOf(p).add(0.5, 0, 0.5));
			}
		}
		return Optional.empty();
	}
}