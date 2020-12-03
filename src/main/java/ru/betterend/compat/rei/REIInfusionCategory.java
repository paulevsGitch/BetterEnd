package ru.betterend.compat.rei;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.TransferRecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.entries.RecipeEntry;
import me.shedaniel.rei.gui.entries.SimpleRecipeEntry;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;
import ru.betterend.recipe.builders.InfusionRecipe;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.LangUtil;

public class REIInfusionCategory implements TransferRecipeCategory<REIInfusionDisplay> {
	
	private final static Identifier COMPASS_IMG = BetterEnd.makeID("textures/gui/compass.png");

	@Override
	public @NotNull Identifier getIdentifier() {
		return InfusionRecipe.ID;
	}

	@Override
	public @NotNull String getCategoryName() {
		return LangUtil.translate(EndBlocks.INFUSION_PEDESTAL.getTranslationKey());
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
		Point centerPoint = new Point(bounds.getCenterX() - 34, bounds.getCenterY() - 6);
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		List<List<EntryStack>> inputEntries = display.getInputEntries();
		List<List<EntryStack>> outputEntries = display.getResultingEntries();
		widgets.add(Widgets.createArrow(new Point(centerPoint.x + 48, centerPoint.y)));
		widgets.add(Widgets.createTexturedWidget(COMPASS_IMG, centerPoint.x - 40, centerPoint.y - 40, 0.0F, 0.0F, 96, 96, 512, 512, 512, 512));
		widgets.add(Widgets.createSlot(centerPoint).entries(inputEntries.get(0)).markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x, centerPoint.y - 28)).entries(inputEntries.get(1)).markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x + 28, centerPoint.y)).entries(inputEntries.get(3)).markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x, centerPoint.y + 28)).entries(inputEntries.get(5)).markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x - 28, centerPoint.y)).entries(inputEntries.get(7)).markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x + 24, centerPoint.y - 24)).entries(inputEntries.get(2)).markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x + 24, centerPoint.y + 24)).entries(inputEntries.get(4)).markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x - 24, centerPoint.y + 24)).entries(inputEntries.get(6)).markInput());
		widgets.add(Widgets.createSlot(new Point(centerPoint.x - 24, centerPoint.y - 24)).entries(inputEntries.get(8)).markInput());
		widgets.add(Widgets.createResultSlotBackground(new Point(centerPoint.x + 80, centerPoint.y)));
		widgets.add(Widgets.createSlot(new Point(centerPoint.x + 80, centerPoint.y)).entries(outputEntries.get(0)).disableBackground().markOutput());
		widgets.add(Widgets.createLabel(new Point(bounds.getMaxX() - 5, bounds.y + 6), new TranslatableText("category.rei.infusion.time&val", display.getInfusionTime()))
				.noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
		return widgets;
	}

	@Override
	public void renderRedSlots(MatrixStack matrices, List<Widget> widgets, Rectangle bounds,
			REIInfusionDisplay display, IntList redSlots) {}
	
	@Override
	public int getDisplayHeight() {
		return 104;
	}
}
