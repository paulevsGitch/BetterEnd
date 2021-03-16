package ru.betterend.recipe.builders;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.BlastingRecipe;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.recipe.SmokingRecipe;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;
import ru.betterend.config.Configs;
import ru.betterend.recipe.EndRecipeManager;
import ru.betterend.util.RecipeHelper;

public class FurnaceRecipe {
	private static final FurnaceRecipe INSTANCE = new FurnaceRecipe();
	
	private ItemConvertible input;
	private ItemConvertible output;
	private boolean exist;
	private String group;
	private String name;
	private int count;
	private int time;
	private float xp;
	
	private FurnaceRecipe() {}
	
	public static FurnaceRecipe make(String name, ItemConvertible input, ItemConvertible output) {
		INSTANCE.name = name;
		INSTANCE.group = "";
		INSTANCE.input = input;
		INSTANCE.output = output;
		INSTANCE.count = 1;
		INSTANCE.time = 200;
		INSTANCE.xp = 0;
		INSTANCE.exist = Configs.RECIPE_CONFIG.getBoolean("furnace", name, true) && RecipeHelper.exists(output) && RecipeHelper.exists(input);
		return INSTANCE;
	}
	
	public FurnaceRecipe setGroup(String group) {
		this.group = group;
		return this;
	}
	
	public FurnaceRecipe setOutputCount(int count) {
		this.count = count;
		return this;
	}
	
	public FurnaceRecipe setXP(float xp) {
		this.xp = xp;
		return this;
	}
	
	public FurnaceRecipe setCookTime(int time) {
		this.time = time;
		return this;
	}
	
	public void build() {
		build(false, false, false);
	}
	
	public void buildWithBlasting() {
		build(true, false, false);
	}
	
	public void buildFoodlike() {
		build(false, true, true);
	}
	
	public void build(boolean blasting, boolean campfire, boolean smoker) {
		if (exist) {
			Identifier id = BetterEnd.makeID(name);
			SmeltingRecipe recipe = new SmeltingRecipe(id, group, Ingredient.ofItems(input), new ItemStack(output, count), xp, time);
			EndRecipeManager.addRecipe(RecipeType.SMELTING, recipe);
			
			if (blasting) {
				BlastingRecipe recipe2 = new BlastingRecipe(id, group, Ingredient.ofItems(input), new ItemStack(output, count), xp, time / 2);
				EndRecipeManager.addRecipe(RecipeType.BLASTING, recipe2);
			}
			
			if (campfire) {
				CampfireCookingRecipe recipe2 = new CampfireCookingRecipe(id, group, Ingredient.ofItems(input), new ItemStack(output, count), xp, time * 3);
				EndRecipeManager.addRecipe(RecipeType.CAMPFIRE_COOKING, recipe2);
			}
			
			if (smoker) {
				SmokingRecipe recipe2 = new SmokingRecipe(id, group, Ingredient.ofItems(input), new ItemStack(output, count), xp, time / 2);
				EndRecipeManager.addRecipe(RecipeType.SMOKING, recipe2);
			}
		}
		else {
			BetterEnd.LOGGER.debug("Furnace recipe {} couldn't be added", name);
		}
	}
}
