package ru.betterend.mixin.common;

import java.util.List;
import java.util.Random;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentLevelEntry;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.core.Registry;
import ru.betterend.registry.EndTags;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin extends ScreenHandler {
	@Shadow
	@Final
	private Inventory inventory;

	@Shadow
	@Final
	private ScreenHandlerContext context;

	@Shadow
	@Final
	private Random random;

	@Shadow
	@Final
	private Property seed;

	@Shadow
	@Final
	public int[] enchantmentPower;

	@Shadow
	@Final
	public int[] enchantmentId;

	@Shadow
	@Final
	public int[] enchantmentLevel;

	protected EnchantmentScreenHandlerMixin(ScreenHandlerType<?> type, int syncId) {
		super(type, syncId);
	}

	@Inject(method = "onContentChanged", at = @At("HEAD"), cancellable = true)
	private void beOnContentChanged(Inventory inventory, CallbackInfo info) {
		if (inventory == this.inventory) {
			ItemStack itemStack = inventory.getStack(0);
			if (!itemStack.isEmpty() && itemStack.isEnchantable()) {
				this.context.run((world, blockPos) -> {
					int i = 0;

					int j;
					for (j = -1; j <= 1; ++j) {
						for (int k = -1; k <= 1; ++k) {
							if ((j != 0 || k != 0) && world.isAir(blockPos.add(k, 0, j))
									&& world.isAir(blockPos.add(k, 1, j))) {
								if (world.getBlockState(blockPos.add(k * 2, 0, j * 2)).isIn(EndTags.BOOKSHELVES)) {
									++i;
								}

								if (world.getBlockState(blockPos.add(k * 2, 1, j * 2)).isIn(EndTags.BOOKSHELVES)) {
									++i;
								}

								if (k != 0 && j != 0) {
									if (world.getBlockState(blockPos.add(k * 2, 0, j)).isIn(EndTags.BOOKSHELVES)) {
										++i;
									}

									if (world.getBlockState(blockPos.add(k * 2, 1, j)).isIn(EndTags.BOOKSHELVES)) {
										++i;
									}

									if (world.getBlockState(blockPos.add(k, 0, j * 2)).isIn(EndTags.BOOKSHELVES)) {
										++i;
									}

									if (world.getBlockState(blockPos.add(k, 1, j * 2)).isIn(EndTags.BOOKSHELVES)) {
										++i;
									}
								}
							}
						}
					}

					this.random.setSeed((long) this.seed.get());

					for (j = 0; j < 3; ++j) {
						this.enchantmentPower[j] = EnchantmentHelper.calculateRequiredExperienceLevel(this.random, j, i,
								itemStack);
						this.enchantmentId[j] = -1;
						this.enchantmentLevel[j] = -1;
						if (this.enchantmentPower[j] < j + 1) {
							this.enchantmentPower[j] = 0;
						}
					}

					for (j = 0; j < 3; ++j) {
						if (this.enchantmentPower[j] > 0) {
							List<EnchantmentLevelEntry> list = this.generateEnchantments(itemStack, j,
									this.enchantmentPower[j]);
							if (list != null && !list.isEmpty()) {
								EnchantmentLevelEntry enchantmentLevelEntry = (EnchantmentLevelEntry) list
										.get(this.random.nextInt(list.size()));
								this.enchantmentId[j] = Registry.ENCHANTMENT
										.getRawId(enchantmentLevelEntry.enchantment);
								this.enchantmentLevel[j] = enchantmentLevelEntry.level;
							}
						}
					}

					this.sendContentUpdates();
				});
			} else {
				for (int i = 0; i < 3; ++i) {
					this.enchantmentPower[i] = 0;
					this.enchantmentId[i] = -1;
					this.enchantmentLevel[i] = -1;
				}
			}
			info.cancel();
		}
	}

	@Shadow
	private List<EnchantmentLevelEntry> generateEnchantments(ItemStack stack, int slot, int level) {
		return null;
	}
}
