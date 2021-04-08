package ru.betterend.tab;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class CreativeTabs {
	public static final CreativeModeTab TAB_BLOCKS;
	public static final CreativeModeTab TAB_ITEMS;

	static {
		TAB_BLOCKS = FabricItemGroupBuilder.create(BetterEnd.makeID("end_blocks"))
				.icon(() -> new ItemStack(EndBlocks.END_MYCELIUM)).appendItems(stacks -> {
					for (Item i : EndItems.getModBlocks()) {
						stacks.add(new ItemStack(i));
					}
				}).build();
		TAB_ITEMS = FabricItemGroupBuilder.create(BetterEnd.makeID("end_items"))
				.icon(() -> new ItemStack(EndItems.ETERNAL_CRYSTAL)).appendItems(stacks -> {
					for (Item i : EndItems.getModItems()) {
						stacks.add(new ItemStack(i));
					}
				}).build();
	}
}
