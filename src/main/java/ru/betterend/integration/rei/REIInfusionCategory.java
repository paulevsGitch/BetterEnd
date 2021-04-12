package ru.betterend.integration.rei;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.TransferRecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.entries.RecipeEntry;
import me.shedaniel.rei.gui.entries.SimpleRecipeEntry;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import ru.betterend.BetterEnd;
import ru.betterend.recipe.builders.InfusionRecipe;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.LangUtil;

public class REIInfusionCategory implements TransferRecipeCategory<REIInfusionDisplay> {
	
	private final static ResourceLocation BACKGROUND = BetterEnd.makeID("textures/gui/rei_infusion.png");

	@Override
	public @NotNull ResourceLocation getIdentifier() {
		return InfusionRecipe.ID;
	}

	@Override
	public @NotNull String getCategoryName() {
		return LangUtil.translate(EndBlocks.INFUSION_PEDESTAL.getDescriptionId());
	}
	
	@Override
	public @NotNull EntryStack getLogo() {
		return REIPlugin.INFUSION_RITUAL;
	}
	
	@Override
	public @NotNull RecipeEntry getSimpleRenderer(REIInfusionDisplay recipe) {
		return SimpleRecipeEntry.from(recipe.getInputEntries(), recipe.getResultingEntries());
	}
	
	@Override
	public @NotNull List<Widget> setupDisplay(REIInfusionDisplay display, Rectangle bounds) {
		Point centerPoint = new Point(bounds.getCenterX() - 34, bounds.getCenterY() - 2);
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		List<List<EntryStack>> inputEntries = display.getInputEntries();
		List<List<EntryStack>> outputEntries = display.getResultingEntries();
		widgets.add(Widgets.createTexturedWidget(BACKGROUND, bounds.x, bounds.y, 0, 0, 150, 104, 150, 104));
		widgets.add(Widgets.createSlot(centerPoint).entries(inputEntries.get(0)).disableBackground().markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x, centerPoint.y - 28)).entries(inputEntries.get(1)).disableBackground().markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x + 28, centerPoint.y)).entries(inputEntries.get(3)).disableBackground().markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x, centerPoint.y + 28)).entries(inputEntries.get(5)).disableBackground().markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x - 28, centerPoint.y)).entries(inputEntries.get(7)).disableBackground().markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x + 24, centerPoint.y - 24)).entries(inputEntries.get(2)).disableBackground().markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x + 24, centerPoint.y + 24)).entries(inputEntries.get(4)).disableBackground().markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x - 24, centerPoint.y + 24)).entries(inputEntries.get(6)).disableBackground().markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x - 24, centerPoint.y - 24)).entries(inputEntries.get(8)).disableBackground().markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x + 80, centerPoint.y)).entries(outputEntries.get(0)).disableBackground().markOutput());
		widgets.add(Widgets.createLabel(new Point(bounds.getMaxX() - 5, bounds.y + 6), new TranslatableComponent("category.rei.infusion.time&val", display.getInfusionTime()))
				.noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
		return widgets;
	}

	@Override
	public void renderRedSlots(PoseStack matrices, List<Widget> widgets, Rectangle bounds,
			REIInfusionDisplay display, IntList redSlots) {}
	
	@Override
	public int getDisplayHeight() {
		return 104;
	}
}
