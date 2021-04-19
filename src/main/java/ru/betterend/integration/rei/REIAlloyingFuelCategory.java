package ru.betterend.integration.rei;

import java.text.DecimalFormat;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Slot;
import me.shedaniel.rei.api.widgets.Tooltip;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.entries.RecipeEntry;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class REIAlloyingFuelCategory implements RecipeCategory<REIAlloyingFuelDisplay> {
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

	@Override
	public @NotNull ResourceLocation getIdentifier() {
		return REIPlugin.ALLOYING_FUEL;
	}

	@Override
	public @NotNull String getCategoryName() {
		return I18n.get("category.rei.fuel");
	}

	@Override
	public int getDisplayHeight() {
		return 49;
	}

	@Override
	public @NotNull EntryStack getLogo() {
		return EntryStack.create(Items.COAL);
	}

	@Override
	public @NotNull List<Widget> setupDisplay(REIAlloyingFuelDisplay recipeDisplay, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 17);
		String burnTime = DECIMAL_FORMAT.format(recipeDisplay.getFuelTime());
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createLabel(new Point(bounds.x + 26, bounds.getMaxY() - 15), new TranslatableComponent("category.rei.fuel.time", burnTime))
				.color(0xFF404040, 0xFFBBBBBB).noShadow().leftAligned());
		widgets.add(Widgets.createBurningFire(new Point(bounds.x + 6, startPoint.y + 1)).animationDurationTicks(recipeDisplay.getFuelTime()));
		widgets.add(Widgets.createSlot(new Point(bounds.x + 6, startPoint.y + 18)).entries(recipeDisplay.getInputEntries().get(0)).markInput());
		return widgets;
	}

	@Override
	public @NotNull RecipeEntry getSimpleRenderer(REIAlloyingFuelDisplay recipe) {
		Slot slot = Widgets.createSlot(new Point(0, 0)).entries(recipe.getInputEntries().get(0)).disableBackground().disableHighlight();
		String burnItems = DECIMAL_FORMAT.format(recipe.getFuelTime() / 200d);
		return new RecipeEntry() {
			private TranslatableComponent text = new TranslatableComponent("category.rei.fuel.time_short.items", burnItems);

			@Override
			public int getHeight() {
				return 22;
			}

			@Nullable
			@Override
			public Tooltip getTooltip(Point point) {
				if (slot.containsMouse(point))
					return slot.getCurrentTooltip(point);
				return null;
			}

			@Override
			public void render(PoseStack matrices, Rectangle bounds, int mouseX, int mouseY, float delta) {
				slot.setZ(getZ() + 50);
				slot.getBounds().setLocation(bounds.x + 4, bounds.y + 2);
				slot.render(matrices, mouseX, mouseY, delta);
				Minecraft.getInstance().font.drawShadow(matrices, text.getVisualOrderText(), bounds.x + 25, bounds.y + 8, -1);
			}
		};
	}
}
