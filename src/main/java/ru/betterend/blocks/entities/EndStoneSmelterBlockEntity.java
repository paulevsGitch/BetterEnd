package ru.betterend.blocks.entities;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.LockableContainerBlockEntity;
import net.minecraft.world.entity.ExperienceOrbEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeFinder;
import net.minecraft.world.item.crafting.RecipeInputProvider;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeUnlocker;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.level.Level;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.EndStoneSmelter;
import ru.betterend.client.gui.EndStoneSmelterScreenHandler;
import ru.betterend.recipe.builders.AlloyingRecipe;
import ru.betterend.registry.EndBlockEntities;

public class EndStoneSmelterBlockEntity extends LockableContainerBlockEntity
		implements SidedInventory, RecipeUnlocker, RecipeInputProvider, Tickable {

	private static final int[] TOP_SLOTS = new int[] { 0, 1 };
	private static final int[] BOTTOM_SLOTS = new int[] { 2, 3 };
	private static final int[] SIDE_SLOTS = new int[] { 1, 2 };
	private static final Map<Item, Integer> AVAILABLE_FUELS = Maps.newHashMap();

	private final Object2IntOpenHashMap<ResourceLocation> recipesUsed;
	protected DefaultedList<ItemStack> inventory;
	protected final PropertyDelegate propertyDelegate;
	private Recipe<?> lastRecipe;
	private int smeltTimeTotal;
	private int smeltTime;
	private int burnTime;
	private int fuelTime;

	public EndStoneSmelterBlockEntity() {
		super(EndBlockEntities.END_STONE_SMELTER);
		this.inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
		this.recipesUsed = new Object2IntOpenHashMap<>();
		this.propertyDelegate = new PropertyDelegate() {
			public int get(int index) {
				switch (index) {
				case 0:
					return EndStoneSmelterBlockEntity.this.burnTime;
				case 1:
					return EndStoneSmelterBlockEntity.this.fuelTime;
				case 2:
					return EndStoneSmelterBlockEntity.this.smeltTime;
				case 3:
					return EndStoneSmelterBlockEntity.this.smeltTimeTotal;
				default:
					return 0;
				}
			}

			public void set(int index, int value) {
				switch (index) {
				case 0:
					EndStoneSmelterBlockEntity.this.burnTime = value;
					break;
				case 1:
					EndStoneSmelterBlockEntity.this.fuelTime = value;
					break;
				case 2:
					EndStoneSmelterBlockEntity.this.smeltTime = value;
					break;
				case 3:
					EndStoneSmelterBlockEntity.this.smeltTimeTotal = value;
				}
			}

			public int size() {
				return 4;
			}
		};
	}

	private boolean isBurning() {
		return this.burnTime > 0;
	}

	@Override
	public int size() {
		return this.inventory.size();
	}

	@Override
	public boolean isEmpty() {
		Iterator<ItemStack> iterator = this.inventory.iterator();
		ItemStack itemStack;
		do {
			if (!iterator.hasNext()) {
				return true;
			}
			itemStack = iterator.next();
		} while (itemStack.isEmpty());

		return false;
	}

	@Override
	public ItemStack getStack(int slot) {
		return this.inventory.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return Inventories.splitStack(this.inventory, slot, amount);
	}

	@Override
	public ItemStack removeStack(int slot) {
		return Inventories.removeStack(this.inventory, slot);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		ItemStack itemStack = this.inventory.get(slot);
		boolean stackValid = !stack.isEmpty() && stack.isItemEqualIgnoreDamage(itemStack)
				&& ItemStack.areTagsEqual(stack, itemStack);
		this.inventory.set(slot, stack);
		if (stack.getCount() > getMaxCountPerStack()) {
			stack.setCount(getMaxCountPerStack());
		}
		if ((slot == 0 || slot == 1) && !stackValid) {
			this.smeltTimeTotal = this.getSmeltTime();
			this.smeltTime = 0;
			this.markDirty();
		}
	}

	protected int getSmeltTime() {
		assert this.world != null;
		int smeltTime = this.world.getRecipeManager().getFirstMatch(AlloyingRecipe.TYPE, this, world)
				.map(AlloyingRecipe::getSmeltTime).orElse(0);
		if (smeltTime == 0) {
			smeltTime = this.world.getRecipeManager().getFirstMatch(RecipeType.BLASTING, this, world)
					.map(BlastingRecipe::getCookTime).orElse(200);
			smeltTime /= 1.5;
		}
		return smeltTime;
	}

	public void dropExperience(Player player) {
		assert world != null;
		List<Recipe<?>> list = Lists.newArrayList();
		for (Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
			world.getRecipeManager().get(entry.getKey()).ifPresent((recipe) -> {
				list.add(recipe);
				if (recipe instanceof AlloyingRecipe) {
					AlloyingRecipe alloying = (AlloyingRecipe) recipe;
					this.dropExperience(player.world, player.getPos(), entry.getIntValue(), alloying.getExperience());
				} else {
					BlastingRecipe blasting = (BlastingRecipe) recipe;
					this.dropExperience(player.world, player.getPos(), entry.getIntValue(), blasting.getExperience());
				}
			});
		}
		player.unlockRecipes(list);
		this.recipesUsed.clear();
	}

	private void dropExperience(Level world, Vec3d vec3d, int i, float f) {
		int j = Mth.floor(i * f);
		float g = Mth.fractionalPart(i * f);
		if (g != 0.0F && Math.random() < g) {
			j++;
		}

		while (j > 0) {
			int k = ExperienceOrbEntity.roundToOrbSize(j);
			j -= k;
			world.spawnEntity(new ExperienceOrbEntity(world, vec3d.x, vec3d.y, vec3d.z, k));
		}
	}

	@Override
	public boolean canPlayerUse(Player player) {
		assert this.world != null;
		if (this.world.getBlockEntity(this.pos) != this) {
			return false;
		}
		return player.squaredDistanceTo(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D,
				this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	@Override
	protected Text getContainerName() {
		return new TranslatableText(String.format("block.%s.%s", BetterEnd.MOD_ID, EndStoneSmelter.ID));
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new EndStoneSmelterScreenHandler(syncId, playerInventory, this, propertyDelegate);
	}

	@Override
	public void tick() {
		boolean initialBurning = this.isBurning();
		if (initialBurning) {
			this.burnTime--;
		}

		boolean burning = this.isBurning();
		assert this.world != null;
		if (!this.world.isClientSide) {
			ItemStack fuel = this.inventory.get(2);
			if (!burning && (fuel.isEmpty() || inventory.get(0).isEmpty() && inventory.get(1).isEmpty())) {
				if (smeltTime > 0) {
					this.smeltTime = Mth.clamp(smeltTime - 2, 0, smeltTimeTotal);
				}
			} else {
				Recipe<?> recipe = this.world.getRecipeManager().getFirstMatch(AlloyingRecipe.TYPE, this, world)
						.orElse(null);
				if (recipe == null) {
					recipe = this.world.getRecipeManager().getFirstMatch(RecipeType.BLASTING, this, world).orElse(null);
				}
				boolean accepted = this.canAcceptRecipeOutput(recipe);
				if (!burning && accepted) {
					this.burnTime = this.getFuelTime(fuel);
					this.fuelTime = this.burnTime;
					burning = this.isBurning();
					if (burning) {
						if (!fuel.isEmpty()) {
							Item item = fuel.getItem();
							fuel.decrement(1);
							if (fuel.isEmpty()) {
								Item remainFuel = item.getRecipeRemainder();
								this.inventory.set(2, remainFuel == null ? ItemStack.EMPTY : new ItemStack(remainFuel));
							}
						}
						this.markDirty();
					}
				}

				if (burning && accepted) {
					this.smeltTime++;
					if (smeltTime == smeltTimeTotal) {
						this.smeltTime = 0;
						this.smeltTimeTotal = this.getSmeltTime();
						this.craftRecipe(recipe);
						this.markDirty();
					}
				} else {
					this.smeltTime = 0;
				}
			}

			if (initialBurning != burning) {
				this.world.setBlockAndUpdate(pos, world.getBlockState(pos).with(EndStoneSmelter.LIT, burning), 3);
				this.markDirty();
			}
		}
	}

	protected boolean canAcceptRecipeOutput(Recipe<?> recipe) {
		if (recipe == null)
			return false;
		boolean validInput;
		if (recipe instanceof AlloyingRecipe) {
			validInput = !inventory.get(0).isEmpty() && !inventory.get(1).isEmpty();
		} else {
			validInput = !inventory.get(0).isEmpty() || !inventory.get(1).isEmpty();
		}
		if (validInput) {
			ItemStack result = recipe.getOutput();
			if (result.isEmpty()) {
				return false;
			}
			ItemStack output = this.inventory.get(3);
			int outCount = output.getCount();
			int total = outCount + result.getCount();
			if (output.isEmpty()) {
				return true;
			}
			if (!output.isItemEqualIgnoreDamage(result)) {
				return false;
			}
			if (outCount < this.getMaxCountPerStack() && outCount < output.getMaxCount()) {
				return this.getMaxCountPerStack() >= total;
			}
			return output.getCount() < result.getMaxCount();
		}
		return false;
	}

	private void craftRecipe(Recipe<?> recipe) {
		if (recipe == null || !canAcceptRecipeOutput(recipe))
			return;

		ItemStack result = recipe.getOutput();
		ItemStack output = this.inventory.get(3);
		if (output.isEmpty()) {
			this.inventory.set(3, result.copy());
		} else if (output.getItem() == result.getItem()) {
			output.increment(result.getCount());
		}

		assert this.world != null;
		if (!this.world.isClientSide) {
			this.setLastRecipe(recipe);
		}

		if (recipe instanceof AlloyingRecipe) {
			this.inventory.get(0).decrement(1);
			this.inventory.get(1).decrement(1);
		} else {
			if (!this.inventory.get(0).isEmpty()) {
				this.inventory.get(0).decrement(1);
			} else {
				this.inventory.get(1).decrement(1);
			}
		}
	}

	@Override
	public void provideRecipeInputs(RecipeFinder finder) {
		for (ItemStack itemStack : this.inventory) {
			finder.addItem(itemStack);
		}
	}

	@Override
	public void setLastRecipe(Recipe<?> recipe) {
		if (recipe != null) {
			ResourceLocation recipeId = recipe.getId();
			this.recipesUsed.addTo(recipeId, 1);
			this.lastRecipe = recipe;
		}
	}

	@Override
	public Recipe<?> getLastRecipe() {
		return this.lastRecipe;
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		if (side == Direction.DOWN) {
			return BOTTOM_SLOTS;
		}
		return side == Direction.UP ? TOP_SLOTS : SIDE_SLOTS;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		return this.isValid(slot, stack);
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		if (dir == Direction.DOWN && slot == 2) {
			return stack.getItem() == Items.BUCKET;
		}
		return true;
	}

	protected int getFuelTime(ItemStack fuel) {
		if (fuel.isEmpty()) {
			return 0;
		}
		Item item = fuel.getItem();
		return AVAILABLE_FUELS.getOrDefault(item, getFabricFuel(fuel));
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.inventory = DefaultedList.ofSize(size(), ItemStack.EMPTY);
		Inventories.fromTag(tag, inventory);
		this.burnTime = tag.getShort("BurnTime");
		this.fuelTime = tag.getShort("FuelTime");
		this.smeltTime = tag.getShort("SmeltTime");
		this.smeltTimeTotal = tag.getShort("SmeltTimeTotal");
		CompoundTag compoundTag = tag.getCompound("RecipesUsed");
		for (String id : compoundTag.getKeys()) {
			this.recipesUsed.put(new ResourceLocation(id), compoundTag.getInt(id));
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putShort("BurnTime", (short) burnTime);
		tag.putShort("FuelTime", (short) fuelTime);
		tag.putShort("SmeltTime", (short) smeltTime);
		tag.putShort("SmeltTimeTotal", (short) smeltTimeTotal);
		Inventories.toTag(tag, inventory);
		CompoundTag usedRecipes = new CompoundTag();
		this.recipesUsed.forEach((identifier, integer) -> usedRecipes.putInt(identifier.toString(), integer));
		tag.put("RecipesUsed", usedRecipes);

		return tag;
	}

	public boolean isValid(int slot, ItemStack stack) {
		if (slot == 3) {
			return false;
		} else if (slot != 2) {
			return true;
		}
		ItemStack itemStack = this.inventory.get(2);
		return canUseAsFuel(stack) || stack.getItem() == Items.BUCKET && itemStack.getItem() != Items.BUCKET;
	}

	public static boolean canUseAsFuel(ItemStack stack) {
		return AVAILABLE_FUELS.containsKey(stack.getItem()) || getFabricFuel(stack) > 2000;
	}

	public static void registerFuel(ItemLike fuel, int time) {
		AVAILABLE_FUELS.put(fuel.asItem(), time);
	}

	public static Map<Item, Integer> availableFuels() {
		return AVAILABLE_FUELS;
	}

	private static int getFabricFuel(ItemStack stack) {
		Integer ticks = FuelRegistry.INSTANCE.get(stack.getItem());
		return ticks == null ? 0 : ticks;
	}

	static {
		AbstractFurnaceBlockEntity.createFuelTimeMap().forEach((item, time) -> {
			if (time >= 2000) {
				registerFuel(item, time);
			}
		});
	}
}
