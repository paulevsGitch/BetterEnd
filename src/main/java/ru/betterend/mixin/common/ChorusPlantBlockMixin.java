package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusPlantBlock;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.BlockTagRegistry;
import ru.betterend.util.BlocksHelper;

@Mixin(value = ChorusPlantBlock.class, priority = 500)
public abstract class ChorusPlantBlockMixin extends Block {
	public ChorusPlantBlockMixin(Settings settings) {
		super(settings);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void beOnInit(AbstractBlock.Settings settings, CallbackInfo info) {
		this.setDefaultState(this.getDefaultState().with(BlocksHelper.ROOTS, false));
	}
	
	@Inject(method = "appendProperties", at = @At("TAIL"))
	private void beAddProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo info) {
		builder.add(BlocksHelper.ROOTS);
	}
	 
	@Inject(method = "canPlaceAt", at = @At("HEAD"), cancellable = true)
	private void beCanPlace(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
		if (world.getBlockState(pos.down()).isOf(BlockRegistry.CHORUS_NYLIUM)) {
			info.setReturnValue(true);
			info.cancel();
		}
	}
	
	@Inject(method = "withConnectionProperties", at = @At("RETURN"), cancellable = true)
	private void beConnectionProperties(BlockView world, BlockPos pos, CallbackInfoReturnable<BlockState> info) {
		BlockState plant = info.getReturnValue();
		if (plant.isOf(Blocks.CHORUS_PLANT)) {
			if (!plant.get(Properties.DOWN) && world.getBlockState(pos.down()).isIn(BlockTagRegistry.END_GROUND)) {
				info.setReturnValue(plant.with(Properties.DOWN, true).with(BlocksHelper.ROOTS, true));
			}
			else {
				info.setReturnValue(plant.with(BlocksHelper.ROOTS, false));
			}
		}
	}
}
