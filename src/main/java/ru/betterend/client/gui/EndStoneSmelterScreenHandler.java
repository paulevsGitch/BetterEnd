package ru.betterend.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.EndStoneSmelter;
import ru.betterend.blocks.entities.EndStoneSmelterBlockEntity;
import ru.betterend.client.gui.slot.SmelterFuelSlot;
import ru.betterend.client.gui.slot.SmelterOutputSlot;
import ru.betterend.recipe.builders.AlloyingRecipe;

public class EndStoneSmelterScreenHandler extends RecipeBookMenu<Container> {

	public final static MenuType<EndStoneSmelterScreenHandler> HANDLER_TYPE = ScreenHandlerRegistry.registerSimple(
			BetterEnd.makeID(EndStoneSmelter.ID), EndStoneSmelterScreenHandler::new);
	
	private final Container inventory;
	private final ContainerData propertyDelegate;
	protected final Level world;
	
	public EndStoneSmelterScreenHandler(int syncId, Inventory playerInventory) {
		this(syncId, playerInventory, new SimpleContainer(4), new SimpleContainerData(4));
	}
	
	public EndStoneSmelterScreenHandler(int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
		super(HANDLER_TYPE, syncId);
		this.inventory = inventory;
		this.propertyDelegate = propertyDelegate;
		this.world = playerInventory.player.level;
		
		addDataSlots(propertyDelegate);
		addSlot(new Slot(inventory, 0, 45, 17));
		addSlot(new Slot(inventory, 1, 67, 17));
		addSlot(new SmelterFuelSlot(this, inventory, 2, 56, 53));
		addSlot(new SmelterOutputSlot(playerInventory.player, inventory, 3, 129, 35));

		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for(int i = 0; i < 9; ++i) {
			addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	}
	
	@Override
	public MenuType<?> getType() {
		return HANDLER_TYPE;
	}

	@Override
	public void fillCraftSlotsStackedContents(StackedContents finder) {
		if (inventory instanceof StackedContentsCompatible) {
			((StackedContentsCompatible) inventory).fillStackedContents(finder);
		}
	}

	@Override
	public void clearCraftingContent() {
		inventory.clearContent();
	}

	@Override
	public boolean recipeMatches(Recipe<? super Container> recipe) {
		return recipe.matches(inventory, world);
	}

	@Override
	public int getResultSlotIndex() {
		return 3;
	}

	@Override
	public int getGridWidth() {
		return 2;
	}

	@Override
	public int getGridHeight() {
		return 1;
	}

	@Override
	public int getSize() {
		return 4;
	}

	@Override
	public RecipeBookType getRecipeBookType() {
		return RecipeBookType.BLAST_FURNACE;
	}

	@Override
	public boolean stillValid(Player player) {
		return inventory.stillValid(player);
	}

	protected boolean isSmeltable(ItemStack itemStack) {
		return world.getRecipeManager().getRecipeFor(AlloyingRecipe.TYPE, new SimpleContainer(itemStack), world).isPresent();
	}

	public boolean isFuel(ItemStack itemStack) {
		return EndStoneSmelterBlockEntity.canUseAsFuel(itemStack);
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		Slot slot = slots.get(index);
		if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;

		ItemStack slotStack = slot.getItem();
		ItemStack itemStack = slotStack.copy();
		if (index == 3) {
			if (!moveItemStackTo(slotStack, 4, 40, true)) {
				return ItemStack.EMPTY;
			}
			slot.onQuickCraft(slotStack, itemStack);
		} else if (index != 2 && index != 1 && index != 0) {
			if (isSmeltable(slotStack)) {
				if (!moveItemStackTo(slotStack, 0, 2, false)) {
					return ItemStack.EMPTY;
				}
			} else if (isFuel(slotStack)) {
				if (!moveItemStackTo(slotStack, 2, 3, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index < 31) {
				if (!moveItemStackTo(slotStack, 31, 40, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index < 40 && !moveItemStackTo(slotStack, 4, 31, false)) {
				return ItemStack.EMPTY;
			}
		} else if (!moveItemStackTo(slotStack, 4, 40, false)) {
			return ItemStack.EMPTY;
		}

		if (slotStack.isEmpty()) {
			slot.set(ItemStack.EMPTY);
		} else {
			slot.setChanged();
		}

		if (slotStack.getCount() == itemStack.getCount()) {
			return ItemStack.EMPTY;
		}

		slot.onTake(player, slotStack);

		return itemStack;
	}
	
	@Environment(EnvType.CLIENT)
	public int getSmeltProgress() {
		int time = propertyDelegate.get(2);
		int timeTotal = propertyDelegate.get(3);
		return timeTotal != 0 && time != 0 ? time * 24 / timeTotal : 0;
	}

	@Environment(EnvType.CLIENT)
	public int getFuelProgress() {
		int fuelTime = propertyDelegate.get(1);
		if (fuelTime == 0) {
			fuelTime = 200;
		}
		return propertyDelegate.get(0) * 13 / fuelTime;
	}

	@Environment(EnvType.CLIENT)
	public boolean isBurning() {
		return propertyDelegate.get(0) > 0;
	}
}
