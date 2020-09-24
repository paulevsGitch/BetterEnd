package ru.betterend.recipe;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import ru.betterend.BetterEnd;

public class RecipeBuilder {
	private final String name;
	private final Item output;
	
	private String group = "";
	private RecipeType<?> type = RecipeType.CRAFTING;
	private boolean shaped = true;
	private String[] shape = new String[] {"#"};
	private Map<Character, Ingredient> materialKeys = Maps.newHashMap();
	private int count = 1;
	
	public RecipeBuilder(String name, Item output) {
		this.name = name;
		this.output = output;
	}
	
	public RecipeBuilder(String name, Block output) {
		this.name = name;
		this.output = output.asItem();
	}

	public RecipeBuilder setGroup(String group) {
		this.group = group;
		return this;
	}
	
	public RecipeBuilder setShape(String[] shape) {
		this.shape = shape;
		return this;
	}
	
	public RecipeBuilder setList(String shape) {
		this.shape = new String[] {shape};
		this.shaped = false;
		return this;
	}
	
	public RecipeBuilder addMaterial(char key, Tag<Item> value) {
		return addMaterial(key, Ingredient.fromTag(value));
	}
	
	public RecipeBuilder addMaterial(char key, ItemConvertible... values) {
		return addMaterial(key, Ingredient.ofItems(values));
	}
	
	public RecipeBuilder addMaterial(char key, Ingredient value) {
		materialKeys.put(key, value);
		return this;
	}
	
	public RecipeBuilder setOutputCount(int count) {
		this.count = count;
		return this;
	}
	
	private DefaultedList<Ingredient> getMaterials(int width, int height) {
		DefaultedList<Ingredient> materials = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
		int pos = 0;
		for (String line: shape) {
			for (int i = 0; i < width; i++) {
				char c = line.charAt(i);
				Ingredient material = materialKeys.get(c);
				materials.set(pos ++, material == null ? Ingredient.EMPTY : material);
			}
		}
		return materials;
	}
	
	public void build() {
		int height = shape.length;
		int width = shape[0].length();
		ItemStack result = new ItemStack(output, count);
		Identifier id = new Identifier(BetterEnd.MOD_ID, name);
		DefaultedList<Ingredient> materials = getMaterials(width, height);
		
		CraftingRecipe recipe = shaped ? new ShapedRecipe(id, group, width, height, materials, result) : new ShapelessRecipe(id, group, result, materials);
		EndRecipeManager.addRecipe(type, recipe); 
	}
}
