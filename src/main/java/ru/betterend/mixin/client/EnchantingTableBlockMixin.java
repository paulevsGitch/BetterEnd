package ru.betterend.mixin.client;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.betterend.registry.EndTags;

@Mixin(EnchantingTableBlock.class)
public abstract class EnchantingTableBlockMixin extends Block {
	public EnchantingTableBlockMixin(Settings settings) {
		super(settings);
	}

	@Inject(method = "randomDisplayTick", at = @At(value = "TAIL"))
	private void be_onRandomDisplayTick(BlockState state, World world, BlockPos pos, Random random, CallbackInfo info) {
		for (int px = -2; px <= 2; ++px) {
			for (int pz = -2; pz <= 2; ++pz) {
				if (px > -2 && px < 2 && pz == -1) {
					pz = 2;
				}
				if (random.nextInt(16) == 0) {
					for (int py = 0; py <= 1; ++py) {
						BlockPos blockPos = pos.add(px, py, pz);
						if (world.getBlockState(blockPos).isIn(EndTags.BOOKSHELVES)) {
							if (!world.isAir(pos.add(px / 2, 0, pz / 2))) {
								break;
							}
							world.addParticle(ParticleTypes.ENCHANT, pos.getX() + 0.5, pos.getY() + 2.0, pos.getZ() + 0.5, px + random.nextFloat() - 0.5, py - random.nextFloat() - 1.0, pz + random.nextFloat() - 0.5);
						}
					}
				}
			}
		}

	}
}
