package ru.betterend.mixin.common;

import java.util.Random;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.block.ChorusPlantBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.BlockTagRegistry;
import ru.betterend.util.BlocksHelper;

@Mixin(ChorusFlowerBlock.class)
public class ChorusFlowerBlockMixin {
	@Shadow
	@Final
	private ChorusPlantBlock plantBlock;
	
	@Inject(method = "canPlaceAt", at = @At("HEAD"), cancellable = true)
	private void canPlace(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
		if (world.getBlockState(pos.down()).isOf(BlockRegistry.CHORUS_NYLIUM)) {
			info.setReturnValue(true);
			info.cancel();
		}
	}
	
	@Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
	private void onTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo info) {
		if (world.getBlockState(pos.down()).isIn(BlockTagRegistry.END_GROUND)) {
			BlockPos up = pos.up();
			if (world.isAir(up) && up.getY() < 256) {
				int i = state.get(ChorusFlowerBlock.AGE);
				if (i < 5) {
					this.grow(world, up, i + 1);
					BlocksHelper.setWithoutUpdate(world, pos, plantBlock.getDefaultState().with(ChorusPlantBlock.UP, true).with(ChorusPlantBlock.DOWN, true).with(BlocksHelper.ROOTS, true));
					info.cancel();
				}
			}
		}
	}
	
	@Shadow
	private static boolean isSurroundedByAir(WorldView world, BlockPos pos, @Nullable Direction exceptDirection) { return false; }
	
	@Shadow
	private void grow(World world, BlockPos pos, int age) {}
	
	@Shadow
	private void die(World world, BlockPos pos) {}
}
