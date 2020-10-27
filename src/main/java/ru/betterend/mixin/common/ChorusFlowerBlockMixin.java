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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.block.ChorusPlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;

@Mixin(value = ChorusFlowerBlock.class, priority = 100)
public abstract class ChorusFlowerBlockMixin extends Block {
	private static final VoxelShape SHAPE_FULL = Block.createCuboidShape(0, 0, 0, 16, 16, 16);
	private static final VoxelShape SHAPE_HALF = Block.createCuboidShape(0, 0, 0, 16, 4, 16);
	
	public ChorusFlowerBlockMixin(Settings settings) {
		super(settings);
	}

	@Shadow
	@Final
	private ChorusPlantBlock plantBlock;
	
	@Inject(method = "canPlaceAt", at = @At("HEAD"), cancellable = true)
	private void beCanPlace(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
		if (world.getBlockState(pos.down()).isOf(EndBlocks.CHORUS_NYLIUM)) {
			info.setReturnValue(true);
			info.cancel();
		}
	}
	
	@Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
	private void beOnTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo info) {
		if (world.getBlockState(pos.down()).isIn(EndTags.END_GROUND)) {
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
	
	@Inject(method = "generate", at = @At("RETURN"), cancellable = true)
	private static void beOnGenerate(WorldAccess world, BlockPos pos, Random random, int size, CallbackInfo info) {
		BlockState state = world.getBlockState(pos);
		if (state.isOf(Blocks.CHORUS_PLANT)) {
			BlocksHelper.setWithoutUpdate(world, pos, state.with(BlocksHelper.ROOTS, true));
		}
	}
	
	@Shadow
	private static boolean isSurroundedByAir(WorldView world, BlockPos pos, @Nullable Direction exceptDirection) { return false; }
	
	@Shadow
	private void grow(World world, BlockPos pos, int age) {}
	
	@Shadow
	private void die(World world, BlockPos pos) {}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return state.get(ChorusFlowerBlock.AGE) == 5 ? SHAPE_HALF : SHAPE_FULL;
	}
}
