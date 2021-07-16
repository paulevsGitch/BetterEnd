package ru.betterend.tab;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import ru.bclib.items.BaseAnvilItem;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

import java.util.stream.Collectors;

public class CreativeTabs {
	public static final CreativeModeTab TAB_BLOCKS;
	public static final CreativeModeTab TAB_ITEMS;
	
	static {
		TAB_BLOCKS = FabricItemGroupBuilder.create(BetterEnd.makeID("end_blocks")).icon(() -> new ItemStack(EndBlocks.END_MYCELIUM))
				.appendItems(stacks -> stacks.addAll(EndBlocks.getModBlocks().stream().map(ItemStack::new).collect(Collectors.toList())))
				.build();
		TAB_ITEMS = FabricItemGroupBuilder.create(BetterEnd.makeID("end_items")).icon(() -> new ItemStack(EndItems.ETERNAL_CRYSTAL))
				.appendItems(stacks -> stacks.addAll(EndItems.getModItems().stream().map(ItemStack::new).collect(Collectors.toList())))
				.build();
	}
}
