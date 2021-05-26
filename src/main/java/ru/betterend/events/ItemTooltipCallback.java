package ru.betterend.events;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

@Environment(EnvType.CLIENT)
public interface ItemTooltipCallback {
	/**
	 * Fired after the game has appended all base tooltip lines to the list.
	 */
	Event<ItemTooltipCallback> EVENT = EventFactory.createArrayBacked(ItemTooltipCallback.class, callbacks -> (player, stack, context, lines) -> {
		for (ItemTooltipCallback callback : callbacks) {
			callback.getTooltip(player, stack, context, lines);
		}
	});

	/**
	 * Called when an item stack's tooltip is rendered. Text added to {@code lines} will be
	 * rendered with the tooltip.
	 *
	 * @param lines the list containing the lines of text displayed on the stack's tooltip
	 */
	void getTooltip(Player player, ItemStack stack, TooltipFlag context, List<Component> lines);
}
