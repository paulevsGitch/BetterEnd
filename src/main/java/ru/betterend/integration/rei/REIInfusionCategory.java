package ru.betterend.integration.rei;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.Lists;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndBlocks;

public class REIInfusionCategory implements DisplayCategory<REIInfusionDisplay> {
	
	private final static ResourceLocation BACKGROUND = BetterEnd.makeID("textures/gui/rei_infusion.png");
	private final EntryStack ICON;
	
	REIInfusionCategory(EntryStack icon) {
		ICON = icon;
	}
	
	@Override
	public @NotNull CategoryIdentifier getCategoryIdentifier() {
		return REIPlugin.INFUSION;
	}
	
	@Override
	public @NotNull Component getTitle() {
		return new TranslatableComponent(EndBlocks.INFUSION_PEDESTAL.getDescriptionId());
	}
	
	@Override
	public @NotNull EntryStack getIcon() {
		return ICON;
	}
	
	@Override
	public @NotNull List<Widget> setupDisplay(REIInfusionDisplay display, Rectangle bounds) {
		Point centerPoint = new Point(bounds.getCenterX() - 34, bounds.getCenterY() - 2);
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		List<EntryIngredient> inputEntries = display.getInputEntries();
		List<EntryIngredient> outputEntries = display.getOutputEntries();
		if (inputEntries.size() < 9) {
			List<EntryIngredient> newList = new ArrayList<EntryIngredient>(9);
			newList.addAll(inputEntries);
			for (int i = inputEntries.size(); i < 9; i++) {
				newList.add(EntryIngredient.empty());
			}
			inputEntries = newList;
		}
		widgets.add(Widgets.createTexturedWidget(BACKGROUND, bounds.x, bounds.y, 0, 0, 150, 104, 150, 104));
		widgets.add(Widgets.createSlot(centerPoint).entries(inputEntries.get(0)).disableBackground().markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x, centerPoint.y - 28))
						   .entries(inputEntries.get(1))
						   .disableBackground()
						   .markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x + 28, centerPoint.y))
						   .entries(inputEntries.get(3))
						   .disableBackground()
						   .markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x, centerPoint.y + 28))
						   .entries(inputEntries.get(5))
						   .disableBackground()
						   .markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x - 28, centerPoint.y))
						   .entries(inputEntries.get(7))
						   .disableBackground()
						   .markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x + 24, centerPoint.y - 24))
						   .entries(inputEntries.get(2))
						   .disableBackground()
						   .markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x + 24, centerPoint.y + 24))
						   .entries(inputEntries.get(4))
						   .disableBackground()
						   .markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x - 24, centerPoint.y + 24))
						   .entries(inputEntries.get(6))
						   .disableBackground()
						   .markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x - 24, centerPoint.y - 24))
						   .entries(inputEntries.get(8))
						   .disableBackground()
						   .markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x + 80, centerPoint.y))
						   .entries(outputEntries.get(0))
						   .disableBackground()
						   .markOutput());
		widgets.add(Widgets.createLabel(
			new Point(bounds.getMaxX() - 5, bounds.y + 6),
			new TranslatableComponent("category.rei.infusion.time&val", display.getInfusionTime())
		).noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
		return widgets;
	}
	
	@Override
	public int getDisplayHeight() {
		return 104;
	}
}
