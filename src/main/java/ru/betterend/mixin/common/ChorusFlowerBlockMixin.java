package ru.betterend.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.bclib.api.TagAPI;
import ru.bclib.util.BlocksHelper;
import ru.betterend.registry.EndBlocks;
import ru.betterend.world.generator.GeneratorOptions;

import java.util.Random;

@Mixin(value = ChorusFlowerBlock.class, priority = 100)
public abstract class ChorusFlowerBlockMixin extends Block {
	private static final VoxelShape SHAPE_FULL = Block.box(0, 0, 0, 16, 16, 16);
	private static final VoxelShape SHAPE_HALF = Block.box(0, 0, 0, 16, 4, 16);
	
	public ChorusFlowerBlockMixin(Properties settings) {
		super(settings);
	}
	
	@Final
	@Shadow
	private ChorusPlantBlock plant;
	
	@Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
	private void be_canSurvive(BlockState state, LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
		if (world.getBlockState(pos.below()).is(EndBlocks.CHORUS_NYLIUM)) {
			info.setReturnValue(true);
			info.cancel();
		}
	}
	
	@Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
	private void be_randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random, CallbackInfo info) {
		if (world.getBlockState(pos.below()).is(TagAPI.BLOCK_END_GROUND)) {
			BlockPos up = pos.above();
			if (world.isEmptyBlock(up) && up.getY() < 256) {
				int i = state.getValue(ChorusFlowerBlock.AGE);
				if (i < 5) {
					this.placeGrownFlower(world, up, i + 1);
					BlocksHelper.setWithoutUpdate(world, pos, plant.defaultBlockState().setValue(ChorusPlantBlock.UP, true).setValue(ChorusPlantBlock.DOWN, true));
					info.cancel();
				}
			}
		}
	}
	
	@Shadow
	private void placeGrownFlower(Level world, BlockPos pos, int age) {}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (GeneratorOptions.changeChorusPlant()) {
			return state.getValue(ChorusFlowerBlock.AGE) == 5 ? SHAPE_HALF : SHAPE_FULL;
		}
		else {
			return super.getShape(state, world, pos, context);
		}
	}
	
	@Inject(method = "placeDeadFlower", at = @At("HEAD"), cancellable = true)
	private void be_placeDeadFlower(Level world, BlockPos pos, CallbackInfo info) {
		BlockState down = world.getBlockState(pos.below());
		if (down.is(Blocks.CHORUS_PLANT) || down.is(TagAPI.BLOCK_GEN_TERRAIN)) {
			world.setBlock(pos, this.defaultBlockState().setValue(BlockStateProperties.AGE_5, 5), 2);
			world.levelEvent(1034, pos, 0);
		}
		info.cancel();
	}
}
