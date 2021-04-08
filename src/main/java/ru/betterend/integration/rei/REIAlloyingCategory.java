package ru.betterend.integration.rei;

import java.text.DecimalFormat;
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
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.resources.ResourceLocation;
import ru.betterend.recipe.builders.AlloyingRecipe;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.LangUtil;

public class REIAlloyingCategory implements TransferRecipeCategory<REIAlloyingDisplay> {

	@Override
	public @NotNull ResourceLocation getIdentifier() {
		return AlloyingRecipe.ID;
	}

	@Override
	public @NotNull String getCategoryName() {
		return LangUtil.translate(EndBlocks.END_STONE_SMELTER.getTranslationKey());
	}

	@Override
	public @NotNull EntryStack getLogo() {
		return REIPlugin.END_STONE_SMELTER;
	}

	@Override
	public @NotNull List<Widget> setupDisplay(REIAlloyingDisplay display, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.y + 10);
		double smeltTime = display.getSmeltTime();
		DecimalFormat df = new DecimalFormat("###.##");
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 9)));
		widgets.add(
				Widgets.createBurningFire(new Point(startPoint.x - 9, startPoint.y + 20)).animationDurationMS(10000));
		widgets.add(Widgets
				.createLabel(new Point(bounds.x + bounds.width - 5, bounds.y + 5), new TranslatableText(
						"category.rei.cooking.time&xp", df.format(display.getXp()), df.format(smeltTime / 20D)))
				.noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
		widgets.add(
				Widgets.createArrow(new Point(startPoint.x + 24, startPoint.y + 8)).animationDurationTicks(smeltTime));
		List<List<EntryStack>> inputEntries = display.getInputEntries();
		widgets.add(Widgets.createSlot(new Point(startPoint.x - 20, startPoint.y + 1)).entries(inputEntries.get(0))
				.markInput());
		if (inputEntries.size() > 1) {
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 1)).entries(inputEntries.get(1))
					.markInput());
		} else {
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 1)).entries(Lists.newArrayList())
					.markInput());
		}
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 9))
				.entries(display.getResultingEntries().get(0)).disableBackground().markOutput());
		return widgets;
	}

	@Override
	public void renderRedSlots(MatrixStack matrices, List<Widget> widgets, Rectangle bounds, REIAlloyingDisplay display,
			IntList redSlots) {
		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 27);
		matrices.push();
		matrices.translate(0, 0, 400);
		if (redSlots.contains(0)) {
			DrawableHelper.fill(matrices, startPoint.x - 20, startPoint.y + 1, startPoint.x - 20 + 16,
					startPoint.y + 1 + 16, 1090453504);
			DrawableHelper.fill(matrices, startPoint.x + 1, startPoint.y + 1, startPoint.x + 1 + 16,
					startPoint.y + 1 + 16, 1090453504);
		}
		matrices.pop();
	}

	@Override
	public @NotNull RecipeEntry getSimpleRenderer(REIAlloyingDisplay recipe) {
		return SimpleRecipeEntry.from(recipe.getInputEntries(), recipe.getResultingEntries());
	}

	@Override
	public int getDisplayHeight() {
		return 49;
	}
}
