package ru.betterend.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.EndStoneSmelter;
import ru.betterend.blocks.entities.EndStoneSmelterBlockEntity;
import ru.betterend.recipe.AlloyingRecipe;

public class EndStoneSmelterScreenHandler extends AbstractRecipeScreenHandler<Inventory> {

	public final static ScreenHandlerType<EndStoneSmelterScreenHandler> HANDLER_TYPE = ScreenHandlerRegistry.registerSimple(
			new Identifier(BetterEnd.MOD_ID, EndStoneSmelter.ID), EndStoneSmelterScreenHandler::new);
	
	private final Inventory inventory;
	private final PropertyDelegate propertyDelegate;
	protected final World world;
	
	public EndStoneSmelterScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new SimpleInventory(4), new ArrayPropertyDelegate(4));
	}
	
	public EndStoneSmelterScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
		super(HANDLER_TYPE, syncId);
		this.inventory = inventory;
		this.propertyDelegate = propertyDelegate;
		this.world = playerInventory.player.world;
		
		this.addProperties(propertyDelegate);
		this.addSlot(new Slot(inventory, 0, 52, 17));
		this.addSlot(new Slot(inventory, 1, 77, 17));
		this.addSlot(new SmelterFuelSlot(this, inventory, 2, 56, 53));
		this.addSlot(new SmelterOutputSlot(playerInventory.player, inventory, 3, 116, 35));

		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for(int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}  
	}

	@Override
	public void populateRecipeFinder(RecipeFinder finder) {
		if (inventory instanceof RecipeInputProvider) {
			((RecipeInputProvider) inventory).provideRecipeInputs(finder);
		}
	}

	@Override
	public void clearCraftingSlots() {
		this.inventory.clear();
	}

	@Override
	public boolean matches(Recipe<? super Inventory> recipe) {
		return recipe.matches(this.inventory, this.world);
	}

	@Override
	public int getCraftingResultSlotIndex() {
		return 3;
	}

	@Override
	public int getCraftingWidth() {
		return 2;
	}

	@Override
	public int getCraftingHeight() {
		return 1;
	}

	@Override
	public int getCraftingSlotCount() {
		return 4;
	}

	@Override
	public RecipeBookCategory getCategory() {
		return RecipeBookCategory.FURNACE;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}

	protected boolean isSmeltable(ItemStack itemStack) {
		return this.world.getRecipeManager().getFirstMatch(AlloyingRecipe.TYPE, new SimpleInventory(new ItemStack[]{itemStack}), this.world).isPresent();
	}

	protected boolean isFuel(ItemStack itemStack) {
		return EndStoneSmelterBlockEntity.canUseAsFuel(itemStack);
	}
	
	@Environment(EnvType.CLIENT)
	public int getSmeltProgress() {
		int time = this.propertyDelegate.get(2);
		int timeTotal = this.propertyDelegate.get(3);
		return timeTotal != 0 && time != 0 ? time * 24 / timeTotal : 0;
	}

	@Environment(EnvType.CLIENT)
	public int getFuelProgress() {
		int fuelTime = this.propertyDelegate.get(1);
		if (fuelTime == 0) {
			fuelTime = 200;
		}
		return this.propertyDelegate.get(0) * 13 / fuelTime;
	}

	@Environment(EnvType.CLIENT)
	public boolean isBurning() {
		return this.propertyDelegate.get(0) > 0;
	}
}
