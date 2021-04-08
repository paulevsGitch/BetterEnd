package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.level.block.AbstractBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.item.ItemPlacementContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.Properties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.WorldView;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(value = ChorusPlantBlock.class, priority = 100)
public abstract class ChorusPlantBlockMixin extends Block {
	public ChorusPlantBlockMixin(Properties settings) {
		super(settings);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void beOnInit(AbstractBlock.Properties settings, CallbackInfo info) {
		if (GeneratorOptions.changeChorusPlant()) {
			this.setDefaultState(this.defaultBlockState().with(BlocksHelper.ROOTS, false));
		}
	}

	@Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
	private void beAddProperties(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo info) {
		GeneratorOptions.init();
		if (GeneratorOptions.changeChorusPlant()) {
			builder.add(BlocksHelper.ROOTS);
		}
	}

	@Inject(method = "withConnectionProperties", at = @At("RETURN"), cancellable = true)
	private void beConnectionProperties(BlockView world, BlockPos pos, CallbackInfoReturnable<BlockState> info) {
		BlockState plant = info.getReturnValue();
		if (plant.is(Blocks.CHORUS_PLANT)) {
			if (world.getBlockState(pos.below()).isIn(EndTags.END_GROUND)) {
				if (GeneratorOptions.changeChorusPlant()) {
					info.setReturnValue(plant.with(Properties.DOWN, true).with(BlocksHelper.ROOTS, true));
				} else {
					info.setReturnValue(plant.with(Properties.DOWN, true));
				}
				info.cancel();
			} else {
				if (GeneratorOptions.changeChorusPlant()) {
					info.setReturnValue(plant.with(BlocksHelper.ROOTS, false));
				}
				info.cancel();
			}
		}
	}

	@Inject(method = "canPlaceAt", at = @At("HEAD"), cancellable = true)
	private void beCanPlace(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
		BlockState down = world.getBlockState(pos.below());
		if (down.is(EndBlocks.CHORUS_NYLIUM) || down.is(Blocks.END_STONE)) {
			info.setReturnValue(true);
			info.cancel();
		}
	}

	@Inject(method = "updateShape", at = @At("RETURN"), cancellable = true)
	private void beStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState,
			LevelAccessor world, BlockPos pos, BlockPos posFrom, CallbackInfoReturnable<BlockState> info) {
		BlockState plant = info.getReturnValue();
		if (plant.is(Blocks.CHORUS_PLANT)) {
			if (world.getBlockState(pos.below()).isIn(EndTags.END_GROUND)) {
				if (GeneratorOptions.changeChorusPlant()) {
					plant = plant.with(Properties.DOWN, true).with(BlocksHelper.ROOTS, true);
				} else {
					plant = plant.with(Properties.DOWN, true);
				}
				info.cancel();
			} else {
				if (GeneratorOptions.changeChorusPlant()) {
					plant = plant.with(BlocksHelper.ROOTS, false);
				}
				info.cancel();
			}
			info.setReturnValue(plant);
			info.cancel();
		}
	}

	@Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
	private void beGetPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> info) {
		BlockPos pos = ctx.getBlockPos();
		Level world = ctx.getLevel();
		BlockState plant = info.getReturnValue();
		if (ctx.canPlace() && plant.is(Blocks.CHORUS_PLANT)
				&& world.getBlockState(pos.below()).isIn(EndTags.END_GROUND)) {
			if (GeneratorOptions.changeChorusPlant()) {
				info.setReturnValue(plant.with(BlocksHelper.ROOTS, true).with(Properties.DOWN, true));
			} else {
				info.setReturnValue(plant.with(Properties.DOWN, true));
			}
			info.cancel();
		}
	}
}
