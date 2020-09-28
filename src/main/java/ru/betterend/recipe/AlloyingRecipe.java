package ru.betterend.recipe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import ru.betterend.BetterEnd;
import ru.betterend.registry.BlockRegistry;

public class AlloyingRecipe implements Recipe<Inventory> {
	
	public final static String GROUP = "alloying";
	public final static RecipeType<AlloyingRecipe> TYPE = registerType(GROUP);
	public final static AlloyingRecipeSerializer SERIALIZER = registerSerializer(GROUP, new AlloyingRecipeSerializer());
	
	protected final RecipeType<?> type;
	protected final Identifier id;
	protected final Ingredient primaryInput;
	protected final Ingredient secondaryInput;
	protected final ItemStack output;
	protected final float experience;
	protected final int smeltTime;

	
	public AlloyingRecipe(Identifier id, Ingredient primaryInput, Ingredient secondaryInput,
			ItemStack output, float experience, int smeltTime) {
		
		this.id = id;
		this.primaryInput = primaryInput;
		this.secondaryInput = secondaryInput;
		this.output = output;
		this.experience = experience;
		this.smeltTime = smeltTime;
		this.type = TYPE;
	}
	
	public float getExperience() {
		return this.experience;
	}
	
	public int getSmeltTime() {
		return this.smeltTime;
	}

	public DefaultedList<Ingredient> getPreviewInputs() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(primaryInput);
		defaultedList.add(secondaryInput);
		
		return defaultedList;
	}

	@Override
	public boolean matches(Inventory inv, World world) {
		return this.primaryInput.test(inv.getStack(0)) && this.secondaryInput.test(inv.getStack(1)) ||
			   this.primaryInput.test(inv.getStack(1)) && this.secondaryInput.test(inv.getStack(0));
	}

	@Override
	public ItemStack craft(Inventory inv) {
		return this.output.copy();
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getOutput() {
		return this.output;
	}

	@Override
	public Identifier getId() {
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return this.type;
	}
	
	@Environment(EnvType.CLIENT)
	public String getGroup() {
		return GROUP;
	}
	
	@Environment(EnvType.CLIENT)
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(BlockRegistry.END_STONE_SMELTER);
	}
	
	private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerSerializer(String id, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(BetterEnd.MOD_ID, id), serializer);
	}
	
	private static <T extends Recipe<?>> RecipeType<T> registerType(String name) {
		return Registry.register(Registry.RECIPE_TYPE, new Identifier(BetterEnd.MOD_ID, name), new RecipeType<T>() {
			public String toString() {
				return name;
			}
	    });
	}
}
