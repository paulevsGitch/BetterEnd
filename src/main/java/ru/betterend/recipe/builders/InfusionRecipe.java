package ru.betterend.recipe.builders;

import java.util.Arrays;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import ru.betterend.BetterEnd;
import ru.betterend.recipe.EndRecipeManager;
import ru.betterend.rituals.InfusionRitual;
import ru.betterend.util.ItemUtil;

public class InfusionRecipe implements Recipe<InfusionRitual> {
	
	public final static String GROUP = "infusion";
	public final static RecipeType<InfusionRecipe> TYPE = EndRecipeManager.registerType(GROUP);
	public final static Serializer SERIALIZER = EndRecipeManager.registerSerializer(GROUP, new Serializer());
	public final static Identifier ID = BetterEnd.makeID(GROUP);
	
	private final Identifier id;
	private Ingredient input;
	private ItemStack output;
	private int time = 1;
	private Ingredient[] catalysts = new Ingredient[8];
	
	private InfusionRecipe(Identifier id) {
		this(id, null, null);
	}
	
	private InfusionRecipe(Identifier id, Ingredient input, ItemStack output) {
		this.id = id;
		this.input = input;
		this.output = output;
		Arrays.fill(catalysts, Ingredient.EMPTY);
	}
	
	public int getInfusionTime() {
		return this.time;
	}

	@Override
	public boolean matches(InfusionRitual inv, World world) {
		boolean valid = this.input.test(inv.getStack(0));
		if (!valid) return false;
		for (int i = 0; i < 8; i++) {
			valid &= this.catalysts[i].test(inv.getStack(i + 1));
		}
		return valid;
	}

	@Override
	public ItemStack craft(InfusionRitual ritual) {
		return this.output.copy();
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(input);
		for (Ingredient catalyst : catalysts) {
			defaultedList.add(catalyst);
		}
		return defaultedList;
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
		return TYPE;
	}
	
	public static class Builder {
		private final static Builder INSTANCE = new Builder();
		
		public static Builder create(String id) {
			return create(BetterEnd.makeID(id));
		}
		
		public static Builder create(Identifier id) {
			INSTANCE.id = id;
			INSTANCE.input = null;
			INSTANCE.output = null;
			INSTANCE.time = 1;
			
			Arrays.fill(INSTANCE.catalysts, Ingredient.EMPTY);
			
			return INSTANCE;
		}
		
		private Identifier id;
		private Ingredient input;
		private ItemStack output;
		private int time = 1;
		private Ingredient[] catalysts = new Ingredient[8];
		
		private Builder() {
			Arrays.fill(catalysts, Ingredient.EMPTY);
		}
		
		public Builder setInput(ItemConvertible input) {
			this.input = Ingredient.ofItems(input);
			return this;
		}
		
		public Builder setOutput(ItemConvertible output) {
			this.output = new ItemStack(output);
			this.output.setCount(1);
			return this;
		}
		
		public Builder setTime(int time) {
			this.time = time;
			return this;
		}
		
		public Builder addCatalyst(int slot, ItemConvertible item) {
			if (slot > 7) return this;
			this.catalysts[slot] = Ingredient.ofItems(item);
			return this;
		}
		
		public void build() {
			if (input == null) {
				BetterEnd.LOGGER.warning("Input for Infusion recipe can't be 'null', recipe {} will be ignored!", id);
				return;
			}
			if (output == null) {
				BetterEnd.LOGGER.warning("Output for Infusion recipe can't be 'null', recipe {} will be ignored!", id);
				return;
			}
			InfusionRecipe recipe = new InfusionRecipe(id, input, output);
			recipe.time = time;
			int empty = 0;
			for (int i = 0; i < catalysts.length; i++) {
				if (catalysts[i].isEmpty()) empty++;
				else recipe.catalysts[i] = catalysts[i];
			}
			if (empty == catalysts.length) {
				BetterEnd.LOGGER.warning("At least one catalyst must be non empty, recipe {} will be ignored!", id);
				return;
			}
			EndRecipeManager.addRecipe(TYPE, recipe);
		}
	}
	
	public static class Serializer implements RecipeSerializer<InfusionRecipe> {
		@Override
		public InfusionRecipe read(Identifier id, JsonObject json) {
			InfusionRecipe recipe = new InfusionRecipe(id);
			recipe.input = Ingredient.fromJson(json.get("input"));
			JsonObject result = JsonHelper.getObject(json, "result");
			recipe.output = ItemUtil.fromJsonRecipe(result);
			if (recipe.output == null) {
				throw new IllegalStateException("Output item does not exists!");
			}
			recipe.time = JsonHelper.getInt(json, "time", 1);
			
			JsonObject catalysts = JsonHelper.asObject(json, "catalysts");
			ItemStack catalyst = ItemUtil.fromStackString(JsonHelper.getString(catalysts, "north", ""));
			recipe.catalysts[0] = Ingredient.ofStacks(Arrays.stream(new ItemStack[] { catalyst }));
			catalyst = ItemUtil.fromStackString(JsonHelper.getString(catalysts, "north_east", ""));
			recipe.catalysts[1] = Ingredient.ofStacks(Arrays.stream(new ItemStack[] { catalyst }));
			catalyst = ItemUtil.fromStackString(JsonHelper.getString(catalysts, "east", ""));
			recipe.catalysts[2] = Ingredient.ofStacks(Arrays.stream(new ItemStack[] { catalyst }));
			catalyst = ItemUtil.fromStackString(JsonHelper.getString(catalysts, "south_east", ""));
			recipe.catalysts[3] = Ingredient.ofStacks(Arrays.stream(new ItemStack[] { catalyst }));
			catalyst = ItemUtil.fromStackString(JsonHelper.getString(catalysts, "south", ""));
			recipe.catalysts[4] = Ingredient.ofStacks(Arrays.stream(new ItemStack[] { catalyst }));
			catalyst = ItemUtil.fromStackString(JsonHelper.getString(catalysts, "south_west", ""));
			recipe.catalysts[5] = Ingredient.ofStacks(Arrays.stream(new ItemStack[] { catalyst }));
			catalyst = ItemUtil.fromStackString(JsonHelper.getString(catalysts, "west", ""));
			recipe.catalysts[6] = Ingredient.ofStacks(Arrays.stream(new ItemStack[] { catalyst }));
			catalyst = ItemUtil.fromStackString(JsonHelper.getString(catalysts, "north_west", ""));
			recipe.catalysts[7] = Ingredient.ofStacks(Arrays.stream(new ItemStack[] { catalyst }));
			
			return recipe;
		}

		@Override
		public InfusionRecipe read(Identifier id, PacketByteBuf buffer) {
			InfusionRecipe recipe = new InfusionRecipe(id);
			recipe.input = Ingredient.fromPacket(buffer);
			recipe.output = buffer.readItemStack();
			recipe.time = buffer.readVarInt();
			for (int i = 0; i < 8; i++) {
				recipe.catalysts[i] = Ingredient.fromPacket(buffer);
			}
			return recipe;
		}

		@Override
		public void write(PacketByteBuf buffer, InfusionRecipe recipe) {
			recipe.input.write(buffer);
			buffer.writeItemStack(recipe.output);
			buffer.writeVarInt(recipe.time);
			for (int i = 0; i < 8; i++) {
				recipe.catalysts[i].write(buffer);
			}
		}
	}
}
