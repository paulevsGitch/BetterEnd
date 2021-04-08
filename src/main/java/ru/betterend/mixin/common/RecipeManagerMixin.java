package ru.betterend.mixin.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.gson.JsonElement;

import net.minecraft.inventory.Inventory;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.level.Level;
import ru.betterend.recipe.EndRecipeManager;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
	@Shadow
	private Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipes;

	@Inject(method = "apply", at = @At(value = "RETURN"))
	private void beSetRecipes(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager,
			Profiler profiler, CallbackInfo info) {
		recipes = EndRecipeManager.getMap(recipes);
	}

	@Shadow
	private <C extends Inventory, T extends Recipe<C>> Map<ResourceLocation, Recipe<C>> getAllOfType(
			RecipeType<T> type) {
		return null;
	}

	/**
	 * @author paulevs
	 * @reason Remove conflicts with vanilla tags Change recipe order to show mod
	 *         recipes first, helps when block have vanilla tag (example - mod stone
	 *         with vanilla tags and furnace from that stone)
	 */
	@Overwrite
	public <C extends Inventory, T extends Recipe<C>> Optional<T> getFirstMatch(RecipeType<T> type, C inventory,
			Level world) {
		Collection<Recipe<C>> values = getAllOfType(type).values();
		List<Recipe<C>> list = new ArrayList<Recipe<C>>(values);
		list.sort((v1, v2) -> {
			boolean b1 = v1.getId().getNamespace().equals("minecraft");
			boolean b2 = v2.getId().getNamespace().equals("minecraft");
			return b1 ^ b2 ? (b1 ? 1 : -1) : 0;
		});

		return list.stream().flatMap((recipe) -> {
			return Util.stream(type.get(recipe, world, inventory));
		}).findFirst();
	}
}