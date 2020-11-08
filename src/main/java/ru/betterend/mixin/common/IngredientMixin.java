package ru.betterend.mixin.common;

import java.util.Arrays;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Ingredient;

import ru.betterend.interfaces.CompoundSerializer;

@Mixin(Ingredient.class)
public abstract class IngredientMixin implements CompoundSerializer<Ingredient> {

	@Shadow
	private ItemStack[] matchingStacks;
	
	@Shadow
	protected abstract void cacheMatchingStacks();
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		this.cacheMatchingStacks();
		int i = 0; 
		tag.putInt("length", matchingStacks.length);
		for (ItemStack stack : matchingStacks) {
			String key = Integer.toString(i);
			tag.put(key, stack.toTag(new CompoundTag()));
			i++;
		}
		return tag;
	}

	@Override
	public Ingredient fromTag(CompoundTag tag) {
		int length = tag.getInt("length");
		ItemStack[] stacks = new ItemStack[length];
		for (int i = 0; i < length; i++) {
			String key = Integer.toString(i);
			stacks[i] = ItemStack.fromTag(tag.getCompound(key));
		}
		return Ingredient.ofStacks(Arrays.stream(stacks));
	}
}
