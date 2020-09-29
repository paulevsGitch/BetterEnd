package ru.betterend.recipe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import ru.betterend.BetterEnd;
import ru.betterend.registry.BlockRegistry;

public class AlloyingRecipe implements Recipe<Inventory> {
	
	public final static String GROUP = "alloying";
	public final static RecipeType<AlloyingRecipe> TYPE = EndRecipeManager.registerType(GROUP);
	public final static AlloyingRecipeSerializer SERIALIZER = EndRecipeManager.registerSerializer(GROUP, new AlloyingRecipeSerializer());
	
	protected final RecipeType<?> type;
	protected final Identifier id;
	protected final Ingredient primaryInput;
	protected final Ingredient secondaryInput;
	protected final ItemStack output;
	protected final String group;
	protected final float experience;
	protected final int smeltTime;

	
	public AlloyingRecipe(Identifier id, Ingredient primaryInput, Ingredient secondaryInput,
			ItemStack output, float experience, int smeltTime) {
		
		this.group = String.format("%s:%s", GROUP, id.getPath());
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
		return this.group;
	}
	
	@Environment(EnvType.CLIENT)
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(BlockRegistry.END_STONE_SMELTER);
	}
	
	public static class Builder {
		private final static Builder INSTANCE = new Builder();
		
		public static Builder create(String id) {
			INSTANCE.id = BetterEnd.getResId(id);
			INSTANCE.primaryInput = null;
			INSTANCE.secondaryInput = null;
			INSTANCE.output = null;
			INSTANCE.experience = 0.0F;
			INSTANCE.smeltTime = 350;
			
			return INSTANCE;
		}
		
		private Identifier id;
		private Ingredient primaryInput;
		private Ingredient secondaryInput;
		private ItemStack output;
		private float experience;
		private int smeltTime;
		
		private Builder() {}
		
		public Builder setPrimaryInput(ItemConvertible... inputs) {
			this.primaryInput = Ingredient.ofItems(inputs);
			return this;
		}
		
		public Builder setSecondaryInput(ItemConvertible... inputs) {
			this.secondaryInput = Ingredient.ofItems(inputs);
			return this;
		}
		
		public Builder setPrimaryInput(Tag<Item> input) {
			this.primaryInput = Ingredient.fromTag(input);
			return this;
		}
		
		public Builder setSecondaryInput(Tag<Item> input) {
			this.secondaryInput = Ingredient.fromTag(input);
			return this;
		}
		
		public Builder setInput(ItemConvertible primaryInput, ItemConvertible secondaryInput) {
			this.setPrimaryInput(primaryInput);
			this.setSecondaryInput(secondaryInput);
			return this;
		}
		
		public Builder setInput(Tag<Item> primaryInput, Tag<Item> secondaryInput) {
			this.setPrimaryInput(primaryInput);
			this.setSecondaryInput(secondaryInput);
			return this;
		}
		
		public Builder setInput(Ingredient primaryInput, Ingredient secondaryInput) {
			this.primaryInput = primaryInput;
			this.secondaryInput = secondaryInput;
			return this;
		}
		
		public Builder setOutput(ItemConvertible output, int amount) {
			this.output = new ItemStack(output, amount);
			return this;
		}
		
		public Builder setExpiriense(float amount) {
			this.experience = amount;
			return this;
		}
		
		public Builder setSmeltTime(int time) {
			this.smeltTime = time;
			return this;
		}
		
		public void build() {
			if (primaryInput == null) {
				throw new IllegalArgumentException("Primary input can't be null!");
			} else if(secondaryInput == null) {
				throw new IllegalArgumentException("Secondary input can't be null!");
			} else if(output == null) {
				throw new IllegalArgumentException("Output can't be null!");
			}
			EndRecipeManager.addRecipe(AlloyingRecipe.TYPE, new AlloyingRecipe(id, primaryInput, secondaryInput, output, experience, smeltTime));
		}
	}
}
