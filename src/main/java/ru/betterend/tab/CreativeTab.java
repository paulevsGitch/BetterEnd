package ru.betterend.tab;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class CreativeTab {
	public static final ItemGroup TAB_ITEMS;
	public static final ItemGroup TAB_BLOCKS;

	static {
		TAB_ITEMS = FabricItemGroupBuilder.create(BetterEnd.makeID("end_items"))
				.icon(() -> new ItemStack(EndItems.ETERNAL_CRYSTAL)).appendItems(stacks -> {
					for (Item i : EndItems.getModItems()) {
						stacks.add(new ItemStack(i));
					}
				}).build();
		TAB_BLOCKS = FabricItemGroupBuilder.create(BetterEnd.makeID("end_blocks"))
				.icon(() -> new ItemStack(EndBlocks.END_MYCELIUM)).appendItems(stacks -> {
					for (Item i : EndItems.getModBlocks()) {
						stacks.add(new ItemStack(i));
					}
				}).build();
		if (BetterEnd.isDevEnvironment()) {
			System.out.println("CREATIVE_TAB_ITEMS: " + TAB_ITEMS.getName());
			System.out.println("CREATIVE_TAB_BLOCKS: " + TAB_BLOCKS.getName());
		}
	}
}
