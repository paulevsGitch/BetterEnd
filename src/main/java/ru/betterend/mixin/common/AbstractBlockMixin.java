package ru.betterend.mixin.common;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Lists;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.math.MathHelper;
import ru.betterend.item.EndHammerItem;
import ru.betterend.util.MHelper;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {
	
	@Inject(method = "getDroppedStacks", at = @At("HEAD"), cancellable = true)
	public void getDroppedStacks(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> info) {
		if (state.isOf(Blocks.GLOWSTONE)) {
			ItemStack tool = builder.get(LootContextParameters.TOOL);
			if (tool != null && tool.getItem() instanceof EndHammerItem) {
				int min = 3;
				int max = 4;
				int count = 0;
				int fortune = EnchantmentHelper.getLevel(Enchantments.FORTUNE, tool);
				if (fortune > 0) {
					fortune /= Enchantments.FORTUNE.getMaxLevel();
					min = MathHelper.clamp(min + fortune, min, max);
					if (min == max) {
						info.setReturnValue(Lists.newArrayList(new ItemStack(Items.GLOWSTONE_DUST, max)));
						info.cancel();
					}
				}
				count = MHelper.randRange(min, max, MHelper.RANDOM);
				info.setReturnValue(Lists.newArrayList(new ItemStack(Items.GLOWSTONE_DUST, count)));
				info.cancel();
			}
		}
	}
}
