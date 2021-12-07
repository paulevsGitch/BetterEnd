package ru.betterend.integration.rei;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.Lists;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import ru.betterend.blocks.basis.EndAnvilBlock;

public class REIAnvilCategory implements DisplayCategory<REIAnvilDisplay> {
	private final EntryStack<?>[] ANVILS;
	
	REIAnvilCategory(EntryStack<?>[] anvils) {
		ANVILS = anvils;
	}
	
	@Override
	public CategoryIdentifier<REIAnvilDisplay> getCategoryIdentifier() {
		return REIPlugin.SMITHING;
	}
	
	@Override
	public @NotNull Component getTitle() {
		return new TranslatableComponent(Blocks.ANVIL.getDescriptionId());
	}
	
	@Override
	public @NotNull EntryStack<?> getIcon() {
		return ANVILS[0];
	}
	
	
	@Override
	public @NotNull List<Widget> setupDisplay(REIAnvilDisplay display, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.y + 10);
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		int x = startPoint.x + 10;
		int y = startPoint.y;
		widgets.add(Widgets.createResultSlotBackground(new Point(x + 61, y + 5)));
		List<EntryIngredient> inputEntries = display.getInputEntries();
		EntryIngredient materials = inputEntries.get(1);
		int anvilLevel = display.getAnvilLevel();
		List<EntryStack<?>> anvils = Arrays.stream(ANVILS).filter(anvil -> {
			Object value = anvil.getValue();
			if (value instanceof ItemStack) {
				value = ((ItemStack) value).getItem();
			}
			Block block = ((BlockItem) value).getBlock();
			if (block instanceof EndAnvilBlock) {
				return ((EndAnvilBlock) block).getCraftingLevel() >= anvilLevel;
			}
			return anvilLevel == 1;
		}).collect(Collectors.toList());
		widgets.add(Widgets.createArrow(new Point(x + 24, y + 4)));
		widgets.add(Widgets.createLabel(
			new Point(bounds.x + bounds.width - 7, bounds.y + bounds.height - 15),
			new TranslatableComponent("category.rei.damage.amount&dmg", display.getDamage())
		).noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
		widgets.add(Widgets.createSlot(new Point(x - 20, y + 4)).entries(materials).markInput());
		widgets.add(Widgets.createSlot(new Point(x + 1, y + 4)).entries(inputEntries.get(0)).markInput());
		widgets.add(Widgets.createSlot(new Point(x + 61, y + 5))
						   .entries(display.getOutputEntries().get(0))
						   .disableBackground()
						   .markOutput());
		widgets.add(Widgets.createSlot(new Point(x - 9, y + 25)).entries(anvils));
		
		return widgets;
	}

	//TODO: 1.18 REI, find replacement
	//@Override
	public void renderRedSlots(PoseStack matrices, List<Widget> widgets, Rectangle bounds, REIAnvilDisplay display, IntList redSlots) {
		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 27);
		matrices.pushPose();
		matrices.translate(0, 0, 400);
		if (redSlots.contains(0)) {
			GuiComponent.fill(
				matrices,
				startPoint.x - 20,
				startPoint.y + 3,
				startPoint.x - 20 + 16,
				startPoint.y + 3 + 16,
				1090453504
			);
			GuiComponent.fill(
				matrices,
				startPoint.x + 1,
				startPoint.y + 3,
				startPoint.x + 1 + 16,
				startPoint.y + 3 + 16,
				1090453504
			);
		}
		matrices.popPose();
	}
	
	@Override
	public int getDisplayHeight() {
		return 60;
	}
	
}
