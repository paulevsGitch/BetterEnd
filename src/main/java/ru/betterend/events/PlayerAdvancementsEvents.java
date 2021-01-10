package ru.betterend.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancement.Advancement;
import net.minecraft.server.network.ServerPlayerEntity;

public final class PlayerAdvancementsEvents {
	
	public static Event<AdvancementComplete> PLAYER_ADVENCEMENT_COMPLETE = EventFactory.createArrayBacked(AdvancementComplete.class, callbacks -> (player, advancement, criterionName) -> {
		for (AdvancementComplete event : callbacks) {
			event.onAdvancementComplete(player, advancement, criterionName);
		}
	});
	
	public interface AdvancementComplete {
		void onAdvancementComplete(ServerPlayerEntity player, Advancement advancement, String criterionName);
	}
}
