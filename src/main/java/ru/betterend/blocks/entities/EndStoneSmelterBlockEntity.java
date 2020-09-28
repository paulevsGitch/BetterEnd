package ru.betterend.blocks.entities;

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import ru.betterend.BetterEnd;

public class EndStoneSmelterBlockEntity extends LockableContainerBlockEntity implements SidedInventory, RecipeUnlocker, RecipeInputProvider, Tickable {

	private static final int[] TOP_SLOTS = new int[] { 0, 1 };
	private static final int[] BOTTOM_SLOTS = new int[] { 2, 3 };
	private static final int[] SIDE_SLOTS = new int[] { 3 };
	protected DefaultedList<ItemStack> inventory;
	protected final PropertyDelegate propertyDelegate;
	private Map<Item, Integer> availableFuels = Maps.newHashMap();
	private int burnTime;
	private int fuelTime;
	private int smeltTime;
	private int smeltTimeTotal;
	
	protected EndStoneSmelterBlockEntity(BlockEntityType<?> blockEntityType) {
		super(blockEntityType);
		this.inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
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
		//TODO
		return 0;
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
		return new TranslatableText(String.format("block.%s.end_stone_smelter", BetterEnd.MOD_ID));
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void provideRecipeInputs(RecipeFinder finder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLastRecipe(Recipe<?> recipe) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Recipe<?> getLastRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		// TODO Auto-generated method stub
		return false;
	}

	protected int getFuelTime(ItemStack fuel) {
		if (fuel.isEmpty()) {
			return 0;
		} else {
			Item item = fuel.getItem();
			return this.availableFuels.getOrDefault(item, 0);
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
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putShort("BurnTime", (short)this.burnTime);
		tag.putShort("SmeltTime", (short)this.smeltTime);
		tag.putShort("SmeltTimeTotal", (short)this.smeltTimeTotal);
		Inventories.toTag(tag, this.inventory);
		return tag;
	}
}
