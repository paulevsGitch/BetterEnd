package ru.betterend.recipe.builders;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.tags.Tag;
import net.minecraft.resources.ResourceLocation;
import ru.betterend.BetterEnd;
import ru.betterend.config.Configs;
import ru.betterend.recipe.EndRecipeManager;
import ru.betterend.util.RecipeHelper;

public class SmithingTableRecipe {

	private final static SmithingTableRecipe BUILDER = new SmithingTableRecipe();
	private final static RecipeType<SmithingRecipe> TYPE = RecipeType.SMITHING;

	public static SmithingTableRecipe create(String name) {
		return create(BetterEnd.makeID(name));
	}

	public static SmithingTableRecipe create(ResourceLocation id) {
		BUILDER.id = id;
		BUILDER.base = null;
		BUILDER.addition = null;
		BUILDER.result = null;
		BUILDER.alright = true;

		return BUILDER;
	}

	private ResourceLocation id;
	private Ingredient base;
	private Ingredient addition;
	private ItemStack result;
	private boolean alright;

	private SmithingTableRecipe() {
	}

	public SmithingTableRecipe setResult(ItemLike item) {
		return this.setResult(item, 1);
	}

	public SmithingTableRecipe setResult(ItemLike item, int count) {
		this.alright &= RecipeHelper.exists(item);
		this.result = new ItemStack(item, count);
		return this;
	}

	public SmithingTableRecipe setBase(ItemLike... items) {
		this.alright &= RecipeHelper.exists(items);
		this.base = Ingredient.of(items);
		return this;
	}

	public SmithingTableRecipe setBase(Tag<Item> tag) {
		this.base = (Ingredient.fromTag(tag));
		return this;
	}

	public SmithingTableRecipe setAddition(ItemLike... items) {
		this.alright &= RecipeHelper.exists(items);
		this.addition = Ingredient.of(items);
		return this;
	}

	public SmithingTableRecipe setAddition(Tag<Item> tag) {
		this.addition = (Ingredient.fromTag(tag));
		return this;
	}

	public void build() {
		if (Configs.RECIPE_CONFIG.getBoolean("smithing", id.getPath(), true)) {
			if (base == null) {
				BetterEnd.LOGGER.warning("Base input for Smithing recipe can't be 'null', recipe {} will be ignored!",
						id);
				return;
			}
			if (addition == null) {
				BetterEnd.LOGGER
						.warning("Addition input for Smithing recipe can't be 'null', recipe {} will be ignored!", id);
				return;
			}
			if (result == null) {
				BetterEnd.LOGGER.warning("Result for Smithing recipe can't be 'null', recipe {} will be ignored!", id);
				return;
			}
			if (EndRecipeManager.getRecipe(TYPE, id) != null) {
				BetterEnd.LOGGER.warning("Can't add Smithing recipe! Id {} already exists!", id);
				return;
			}
			if (!alright) {
				BetterEnd.LOGGER.debug("Can't add Smithing recipe {}! Ingeredients or output not exists.", id);
				return;
			}
			EndRecipeManager.addRecipe(TYPE, new SmithingRecipe(id, base, addition, result));
		}
	}
}
