package ru.betterend.blocks.entities;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.EndStoneSmelter;
import ru.betterend.client.gui.EndStoneSmelterScreenHandler;
import ru.betterend.recipe.AlloyingRecipe;
import ru.betterend.registry.BlockEntityRegistry;

public class EndStoneSmelterBlockEntity extends LockableContainerBlockEntity implements SidedInventory, RecipeUnlocker, RecipeInputProvider, Tickable {

	private static final int[] TOP_SLOTS = new int[] { 0, 1 };
	private static final int[] BOTTOM_SLOTS = new int[] { 2, 3 };
	private static final int[] SIDE_SLOTS = new int[] { 3 };
	private static final Map<Item, Integer> availableFuels = Maps.newHashMap();
	
	private final Object2IntOpenHashMap<Identifier> recipesUsed;
	protected DefaultedList<ItemStack> inventory;
	protected final PropertyDelegate propertyDelegate;
	private int burnTime;
	private int fuelTime;
	private int smeltTime;
	private int smeltTimeTotal;
	
	public EndStoneSmelterBlockEntity() {
		super(BlockEntityRegistry.END_STONE_SMELTER);
		this.inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
		this.recipesUsed = new Object2IntOpenHashMap<Identifier>();
	    this.propertyDelegate = new PropertyDelegate() {
	    	public int get(int index) {
	    		switch(index) {
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
	    		switch(index) {
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
		//TODO
		ItemStack itemStack = this.inventory.get(slot);
		boolean stackValid = !stack.isEmpty() && stack.isItemEqualIgnoreDamage(itemStack) && ItemStack.areTagsEqual(stack, itemStack);
		this.inventory.set(slot, stack);
		if (stack.getCount() > this.getMaxCountPerStack()) {
			stack.setCount(this.getMaxCountPerStack());
		}
		if (slot == 0 && !stackValid) {
			this.smeltTimeTotal = this.getSmeltTime();
			this.smeltTime = 0;
			this.markDirty();
		}
	}
	
	protected int getSmeltTime() {
		return this.world.getRecipeManager().getFirstMatch(AlloyingRecipe.TYPE, this, world)
											.map(AlloyingRecipe::getSmeltTime).orElse(350);
	}
	
	public void dropExperience(PlayerEntity player) {
		List<Recipe<?>> list = Lists.newArrayList();
		ObjectIterator<Entry<Identifier>> usedRecipes = this.recipesUsed.object2IntEntrySet().iterator();
		while(usedRecipes.hasNext()) {
			Entry<Identifier> entry = usedRecipes.next();
			world.getRecipeManager().get(entry.getKey()).ifPresent((recipe) -> {
				list.add(recipe);
				AlloyingRecipe alloying = (AlloyingRecipe) recipe;
				this.dropExperience(player.world, player.getPos(), entry.getIntValue(), alloying.getExperience());
			});
		}
		player.unlockRecipes(list);
		this.recipesUsed.clear();
	}
	
	private void dropExperience(World world, Vec3d vec3d, int i, float f) {
		int j = MathHelper.floor(i * f);
		float g = MathHelper.fractionalPart(i * f);
		if (g != 0.0F && Math.random() < g) {
			j++;
		}

		while(j > 0) {
			int k = ExperienceOrbEntity.roundToOrbSize(j);
			j -= k;
			world.spawnEntity(new ExperienceOrbEntity(world, vec3d.x, vec3d.y, vec3d.z, k));
		}
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		if (this.world.getBlockEntity(this.pos) != this) {
			return false;
		} else {
			return player.squaredDistanceTo(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
		}
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
		// TODO Auto-generated method stub
	}

	@Override
	public void provideRecipeInputs(RecipeFinder finder) {
		Iterator<ItemStack> inventory = this.inventory.iterator();
		while(inventory.hasNext()) {
			ItemStack itemStack = inventory.next();
			finder.addItem(itemStack);
		}
	}

	@Override
	public void setLastRecipe(Recipe<?> recipe) {
		if (recipe != null) {
			Identifier recipeId = recipe.getId();
			this.recipesUsed.addTo(recipeId, 1);
		}
	}

	@Override
	public Recipe<?> getLastRecipe() {
		return null;
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		if (side == Direction.DOWN) {
			return BOTTOM_SLOTS;
		} else {
			return side == Direction.UP ? TOP_SLOTS : SIDE_SLOTS;
		}
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		return this.isValid(slot, stack);
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		if (dir == Direction.DOWN && slot == 2) {
			if (stack.getItem() != Items.BUCKET) {
				return false;
			}
		}
		return true;
	}

	protected int getFuelTime(ItemStack fuel) {
		if (fuel.isEmpty()) {
			return 0;
		} else {
			Item item = fuel.getItem();
			return availableFuels.getOrDefault(item, 0);
		}
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		Inventories.fromTag(tag, this.inventory);
		this.burnTime = tag.getShort("BurnTime");
		this.smeltTime = tag.getShort("SmeltTime");
		this.smeltTimeTotal = tag.getShort("SmeltTimeTotal");
		this.fuelTime = this.getFuelTime(this.inventory.get(2));
		CompoundTag compoundTag = tag.getCompound("RecipesUsed");
		Iterator<String> recipes = compoundTag.getKeys().iterator();
		while(recipes.hasNext()) {
			String id = recipes.next();
			this.recipesUsed.put(new Identifier(id), compoundTag.getInt(id));
		}
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putShort("BurnTime", (short)this.burnTime);
		tag.putShort("SmeltTime", (short)this.smeltTime);
		tag.putShort("SmeltTimeTotal", (short)this.smeltTimeTotal);
		Inventories.toTag(tag, this.inventory);
		CompoundTag usedRecipes = new CompoundTag();
		this.recipesUsed.forEach((identifier, integer) -> {
			usedRecipes.putInt(identifier.toString(), integer);
		});
		tag.put("RecipesUsed", usedRecipes);
		
		return tag;
	}
	
	public boolean isValid(int slot, ItemStack stack) {
		if (slot == 3) {
			return false;
		} else if (slot != 0 || slot != 1) {
			return true;
		} else {
			ItemStack itemStack = this.inventory.get(2);
			return canUseAsFuel(stack) || stack.getItem() == Items.BUCKET && itemStack.getItem() != Items.BUCKET;
		}
	}

	public static boolean canUseAsFuel(ItemStack stack) {
		return availableFuels.containsKey(stack.getItem());
	}
}
