package ru.betterend.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.level.ServerPlayer;

public final class PlayerAdvancementsEvents {
	
	public static Event<AdvancementComplete> PLAYER_ADVENCEMENT_COMPLETE = EventFactory.createArrayBacked(AdvancementComplete.class, callbacks -> (player, advancement, criterionName) -> {
		for (AdvancementComplete event : callbacks) {
			event.onAdvancementComplete(player, advancement, criterionName);
		}
	});
	
	public interface AdvancementComplete {
		void onAdvancementComplete(ServerPlayer player, Advancement advancement, String criterionName);
	}
}
