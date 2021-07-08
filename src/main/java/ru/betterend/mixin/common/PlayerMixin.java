package ru.betterend.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.bclib.blocks.BlockProperties;
import ru.bclib.blocks.BlockProperties.TripleShape;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.betterend.interfaces.FallFlyingItem;
import ru.betterend.registry.EndBlocks;

import java.util.Optional;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	private static Direction[] horizontal;

	@Inject(method = "findRespawnPositionAndUseSpawnBlock", at = @At(value = "HEAD"), cancellable = true)
	private static void be_findRespawnPositionAndUseSpawnBlock(ServerLevel world, BlockPos pos, float f, boolean bl, boolean bl2, CallbackInfoReturnable<Optional<Vec3>> info) {
		BlockState blockState = world.getBlockState(pos);
		if (blockState.is(EndBlocks.RESPAWN_OBELISK)) {
			info.setReturnValue(be_obeliskRespawnPosition(world, pos, blockState));
			info.cancel();
		}
	}

	@Inject(method = "tryToStartFallFlying", at = @At("HEAD"), cancellable = true)
	public void be_tryToStartFlying(CallbackInfoReturnable<Boolean> info) {
		if (!onGround && !isFallFlying() && !isInWater() && !hasEffect(MobEffects.LEVITATION)) {
			ItemStack itemStack = getItemBySlot(EquipmentSlot.CHEST);
			if (itemStack.getItem() instanceof FallFlyingItem && ElytraItem.isFlyEnabled(itemStack)) {
				setSharedFlag(7, true);
				info.setReturnValue(true);
			}
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
		for (Direction dir : horizontal) {
			BlockPos p = pos.relative(dir);
			BlockState state2 = world.getBlockState(p);
			if (!state2.getMaterial().blocksMotion() && state2.getCollisionShape(world, pos).isEmpty()) {
				return Optional.of(Vec3.atLowerCornerOf(p).add(0.5, 0, 0.5));
			}
		}
		return Optional.empty();
	}
}