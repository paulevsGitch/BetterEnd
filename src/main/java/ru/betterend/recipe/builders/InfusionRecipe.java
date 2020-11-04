package ru.betterend.recipe.builders;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import ru.betterend.rituals.InfusionRitual;

public class InfusionRecipe implements Recipe<InfusionRitual> {

	@Override
	public boolean matches(InfusionRitual inv, World world) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack craft(InfusionRitual inv) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean fits(int width, int height) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack getOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Identifier getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RecipeType<?> getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
