package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChorusPlantBlock;

@Mixin(value = ChorusPlantBlock.class, priority = 100)
public abstract class ChorusPlantBlockMixin extends Block {
	public ChorusPlantBlockMixin(Properties settings) {
		super(settings);
	}

	/*@Inject(method = "<init>*", at = @At("TAIL"))
	private void beOnInit(BlockBehaviour.Properties settings, CallbackInfo info) {
		if (GeneratorOptions.changeChorusPlant()) {
			this.registerDefaultState(this.defaultBlockState().setValue(BlockProperties.ROOTS, false));
		}
	}
	
	@Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
	private void be_createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo info) {
		GeneratorOptions.init();
		if (GeneratorOptions.changeChorusPlant()) {
			builder.add(BlockProperties.ROOTS);
		}
	}

	@Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
	private void be_getStateForPlacement(BlockPlaceContext ctx, CallbackInfoReturnable<BlockState> info) {
		BlockPos pos = ctx.getClickedPos();
		Level world = ctx.getLevel();
		BlockState plant = info.getReturnValue();
		if (ctx.canPlace() && plant.is(Blocks.CHORUS_PLANT) && world.getBlockState(pos.below()).is(TagAPI.END_GROUND)) {
			if (GeneratorOptions.changeChorusPlant()) {
				info.setReturnValue(plant.setValue(BlockProperties.ROOTS, true).setValue(BlockStateProperties.DOWN, true));
			}
			else {
				info.setReturnValue(plant.setValue(BlockStateProperties.DOWN, true));
			}
			info.cancel();
		}
	}
	
	@Inject(method = "Lnet/minecraft/world/level/block/ChorusPlantBlock;getStateForPlacement" +
			"(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)" +
			"Lnet/minecraft/world/level/block/state/BlockState;",
			at = @At("RETURN"), cancellable = true)
	private void be_getStateForPlacement(BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<BlockState> info) {
		BlockState plant = info.getReturnValue();
		if (plant.is(Blocks.CHORUS_PLANT)) {
			if (blockGetter.getBlockState(blockPos.below()).is(TagAPI.END_GROUND)) {
				if (GeneratorOptions.changeChorusPlant()) {
					info.setReturnValue(plant.setValue(BlockStateProperties.DOWN, true).setValue(BlockProperties.ROOTS, true));
				}
				else {
					info.setReturnValue(plant.setValue(BlockStateProperties.DOWN, true));
				}
				info.cancel();
			}
			else {
				if (GeneratorOptions.changeChorusPlant()) {
					info.setReturnValue(plant.setValue(BlockProperties.ROOTS, false));
				}
				info.cancel();
			}
		}
	}
	
	@Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
	private void be_canSurvive(BlockState state, LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
		BlockState down = world.getBlockState(pos.below());
		if (down.is(EndBlocks.CHORUS_NYLIUM) || down.is(Blocks.END_STONE)) {
			info.setReturnValue(true);
			info.cancel();
		}
	}
	
	@Inject(method = "updateShape", at = @At("RETURN"), cancellable = true)
	private void be_updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom, CallbackInfoReturnable<BlockState> info) {
		BlockState plant = info.getReturnValue();
		if (plant.is(Blocks.CHORUS_PLANT)) {
			if (world.getBlockState(pos.below()).is(TagAPI.END_GROUND)) {
				if (GeneratorOptions.changeChorusPlant()) {
					plant = plant.setValue(BlockStateProperties.DOWN, true).setValue(BlockProperties.ROOTS, true);
				}
				else {
					plant = plant.setValue(BlockStateProperties.DOWN, true);
				}
				info.cancel();
			}
			else {
				if (GeneratorOptions.changeChorusPlant()) {
					plant = plant.setValue(BlockProperties.ROOTS, false);
				}
				info.cancel();
			}
			info.setReturnValue(plant);
			info.cancel();
		}
	}*/
}
