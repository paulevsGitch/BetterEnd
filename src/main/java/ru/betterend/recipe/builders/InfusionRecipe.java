package ru.betterend.recipe.builders;

import java.util.Arrays;

import com.google.gson.JsonObject;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.level.Level;
import ru.betterend.BetterEnd;
import ru.betterend.config.Configs;
import ru.betterend.interfaces.BetterEndRecipe;
import ru.betterend.recipe.EndRecipeManager;
import ru.betterend.rituals.InfusionRitual;
import ru.betterend.util.ItemUtil;

public class InfusionRecipe implements Recipe<InfusionRitual>, BetterEndRecipe {

	public final static String GROUP = "infusion";
	public final static RecipeType<InfusionRecipe> TYPE = EndRecipeManager.registerType(GROUP);
	public final static Serializer SERIALIZER = EndRecipeManager.registerSerializer(GROUP, new Serializer());
	public final static ResourceLocation ID = BetterEnd.makeID(GROUP);

	private final ResourceLocation id;
	private Ingredient input;
	private ItemStack output;
	private int time = 1;
	private Ingredient[] catalysts = new Ingredient[8];
	private String group;

	private InfusionRecipe(ResourceLocation id) {
		this(id, null, null);
	}

	private InfusionRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
		this.id = id;
		this.input = input;
		this.output = output;
		Arrays.fill(catalysts, Ingredient.EMPTY);
	}

	public int getInfusionTime() {
		return this.time;
	}

	@Override
	public boolean matches(InfusionRitual inv, Level world) {
		boolean valid = this.input.test(inv.getStack(0));
		if (!valid)
			return false;
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
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public String getGroup() {
		return this.group;
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
		private static boolean exist;

		public static Builder create(String id) {
			return create(BetterEnd.makeID(id));
		}

		public static Builder create(ResourceLocation id) {
			INSTANCE.id = id;
			INSTANCE.input = null;
			INSTANCE.output = null;
			INSTANCE.time = 1;
			exist = Configs.RECIPE_CONFIG.getBoolean("infusion", id.getPath(), true);

			Arrays.fill(INSTANCE.catalysts, Ingredient.EMPTY);

			return INSTANCE;
		}

		private ResourceLocation id;
		private Ingredient input;
		private ItemStack output;
		private String group;
		private int time = 1;
		private Ingredient[] catalysts = new Ingredient[8];

		private Builder() {
			Arrays.fill(catalysts, Ingredient.EMPTY);
		}

		public Builder setGroup(String group) {
			this.group = group;
			return this;
		}

		public Builder setInput(ItemLike input) {
			this.input = Ingredient.of(input);
			return this;
		}

		public Builder setOutput(ItemLike output) {
			this.output = new ItemStack(output);
			this.output.setCount(1);
			return this;
		}

		public Builder setOutput(ItemStack output) {
			this.output = output;
			this.output.setCount(1);
			return this;
		}

		public Builder setTime(int time) {
			this.time = time;
			return this;
		}

		public Builder addCatalyst(int slot, ItemLike... items) {
			if (slot > 7)
				return this;
			this.catalysts[slot] = Ingredient.of(items);
			return this;
		}

		public void build() {
			if (exist) {
				if (input == null) {
					BetterEnd.LOGGER.warning("Input for Infusion recipe can't be 'null', recipe {} will be ignored!",
							id);
					return;
				}
				if (output == null) {
					BetterEnd.LOGGER.warning("Output for Infusion recipe can't be 'null', recipe {} will be ignored!",
							id);
					return;
				}
				InfusionRecipe recipe = new InfusionRecipe(id, input, output);
				recipe.group = group != null ? group : GROUP;
				recipe.time = time;
				int empty = 0;
				for (int i = 0; i < catalysts.length; i++) {
					if (catalysts[i].isEmpty())
						empty++;
					else
						recipe.catalysts[i] = catalysts[i];
				}
				if (empty == catalysts.length) {
					BetterEnd.LOGGER.warning("At least one catalyst must be non empty, recipe {} will be ignored!", id);
					return;
				}
				EndRecipeManager.addRecipe(TYPE, recipe);
			}
		}
	}

	public static class Serializer implements RecipeSerializer<InfusionRecipe> {
		@Override
		public InfusionRecipe read(ResourceLocation id, JsonObject json) {
			InfusionRecipe recipe = new InfusionRecipe(id);
			recipe.input = Ingredient.fromJson(json.get("input"));
			JsonObject result = JsonHelper.getObject(json, "result");
			recipe.output = ItemUtil.fromJsonRecipe(result);
			if (recipe.output == null) {
				throw new IllegalStateException("Output item does not exists!");
			}
			recipe.group = JsonHelper.getString(json, "group", GROUP);
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
		public InfusionRecipe read(ResourceLocation id, PacketByteBuf buffer) {
			InfusionRecipe recipe = new InfusionRecipe(id);
			recipe.input = Ingredient.fromPacket(buffer);
			recipe.output = buffer.readItemStack();
			recipe.group = buffer.readString();
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
			buffer.writeString(recipe.group);
			buffer.writeVarInt(recipe.time);
			for (int i = 0; i < 8; i++) {
				recipe.catalysts[i].write(buffer);
			}
		}
	}
}
