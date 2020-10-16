package ru.betterend.recipe.builders;

import com.google.gson.JsonObject;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import ru.betterend.BetterEnd;
import ru.betterend.recipe.EndRecipeManager;
import ru.betterend.registry.ItemTagRegistry;
import ru.betterend.util.RecipeHelper;

public class AnvilSmithingRecipe implements Recipe<Inventory> {
	
	public final static String GROUP = "smithing";
	public final static RecipeType<AnvilSmithingRecipe> TYPE = EndRecipeManager.registerType(GROUP);
	public final static Serializer SERIALIZER = EndRecipeManager.registerSerializer(GROUP, new Serializer());
	public final static Identifier ID = BetterEnd.makeID(GROUP);
	
	private final Identifier id;
	private final Ingredient input;
	private final ItemStack output;
	private final int damage;
	private final int level;
	
	public AnvilSmithingRecipe(Identifier identifier, Ingredient input, ItemStack output, int level, int damage) {
		this.id = identifier;
		this.input = input;
		this.output = output;
		this.level = level;
		this.damage = damage;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public ItemStack getOutput() {
		return this.output;
	}
	
	@Override
	public boolean matches(Inventory craftingInventory, World world) {
		return this.matches(craftingInventory);
	}
	
	@Override
	public ItemStack craft(Inventory craftingInventory) {
		return this.output.copy();
	}
	
	public ItemStack craft(Inventory craftingInventory, PlayerEntity player) {
		if (!player.isCreative()) {
			ItemStack hammer = craftingInventory.getStack(0);
			int damage = hammer.getDamage() + this.damage;
			if (damage >= hammer.getMaxDamage()) return ItemStack.EMPTY;
			hammer.damage(this.damage, player, entity -> {
				entity.sendEquipmentBreakStatus(null);
			});
		}
		return this.craft(craftingInventory);
	}
	
	public boolean matches(Inventory craftingInventory) {
		ItemStack hammer = craftingInventory.getStack(0);
		if (hammer.isEmpty() || !ItemTagRegistry.HAMMERS.contains(hammer.getItem())) {
			return false;
		}
		int level = ((ToolItem) hammer.getItem()).getMaterial().getMiningLevel();
		return level >= this.level && this.input.test(craftingInventory.getStack(1));
	}
	
	public int getDamage() {
		return this.damage;
	}

	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(Ingredient.ofStacks(ItemTagRegistry.HAMMERS.values().stream().filter(hammer -> {
			return ((ToolItem) hammer).getMaterial().getMiningLevel() >= level;
		}).map(ItemStack::new)));
		defaultedList.add(input);
		
		return defaultedList;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public Identifier getId() {
		return this.id;
	}

	@Override
	public RecipeType<?> getType() {
		return TYPE;
	}
	
	@Override
	public boolean isIgnoredInRecipeBook() {
		return true;
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
			INSTANCE.level = 1;
			INSTANCE.damage = 1;
			
			return INSTANCE;
		}
		
		private Identifier id;
		private Ingredient input;
		private ItemStack output;
		private int level = 1;
		private int damage = 1;
		private boolean alright = true;
		
		private Builder() {}
		
		public Builder setInput(ItemConvertible... inputItems) {
			for (ItemConvertible item : inputItems) {
				this.alright &= RecipeHelper.exists(item);
			}
			this.setInput(Ingredient.ofItems(inputItems));
			return this;
		}
		
		public Builder setInput(Tag<Item> inputTag) {
			this.setInput(Ingredient.fromTag(inputTag));
			return this;
		}
		
		public Builder setInput(Ingredient ingredient) {
			this.input = ingredient;
			return this;
		}
		
		public Builder setOutput(ItemConvertible output, int amount) {
			this.alright &= RecipeHelper.exists(output);
			this.output = new ItemStack(output, amount);
			return this;
		}
		
		public Builder setLevel(int level) {
			this.level = level;
			return this;
		}
		
		public Builder setDamage(int damage) {
			this.damage = damage;
			return this;
		}
		
		public void build() {
			if (input == null) {
				BetterEnd.LOGGER.warning("Input for Smithing recipe can't be 'null', recipe {} will be ignored!", id);
				return;
			}
			if(output == null) {
				BetterEnd.LOGGER.warning("Output for Smithing recipe can't be 'null', recipe {} will be ignored!", id);
				return;
			}
			if (EndRecipeManager.getRecipe(TYPE, id) != null) {
				BetterEnd.LOGGER.warning("Can't add Smithing recipe! Id {} already exists!", id);
				return;
			}
			if (!alright) {
				BetterEnd.LOGGER.debug("Can't add Smithing recipe {}! Ingeredient or output not exists.", id);
				return;
			}
			EndRecipeManager.addRecipe(TYPE, new AnvilSmithingRecipe(id, input, output, level, damage));
		}
	}

	public static class Serializer implements RecipeSerializer<AnvilSmithingRecipe> {
		@Override
		public AnvilSmithingRecipe read(Identifier id, JsonObject json) {
			Ingredient input = Ingredient.fromJson(JsonHelper.getObject(json, "input"));
			String resultStr = JsonHelper.getString(json, "result");
			Identifier resultId = new Identifier(resultStr);
			ItemStack output = new ItemStack(Registry.ITEM.getOrEmpty(resultId).orElseThrow(() -> {
				return new IllegalStateException("Item: " + resultStr + " does not exists!");
			}));
			int level = JsonHelper.getInt(json, "level", 1);
			int damage = JsonHelper.getInt(json, "damage", 1);
			
			return new AnvilSmithingRecipe(id, input, output, level, damage);
		}

		@Override
		public AnvilSmithingRecipe read(Identifier id, PacketByteBuf packetBuffer) {
			Ingredient input = Ingredient.fromPacket(packetBuffer);
			ItemStack output = packetBuffer.readItemStack();
			int level = packetBuffer.readVarInt();
			int damage = packetBuffer.readVarInt();
			
			return new AnvilSmithingRecipe(id, input, output, level, damage);
		}

		@Override
		public void write(PacketByteBuf packetBuffer, AnvilSmithingRecipe recipe) {
			recipe.input.write(packetBuffer);
			packetBuffer.writeItemStack(recipe.output);
			packetBuffer.writeVarInt(recipe.level);
			packetBuffer.writeVarInt(recipe.damage);
		}
	}
}
