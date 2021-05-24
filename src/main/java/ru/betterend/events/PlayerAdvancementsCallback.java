package ru.betterend.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.level.ServerPlayer;

public interface PlayerAdvancementsCallback {
	
	Event<PlayerAdvancementsCallback> PLAYER_ADVANCEMENT_COMPLETE = EventFactory.createArrayBacked(PlayerAdvancementsCallback.class, callbacks -> (player, advancement, criterionName) -> {
		for (PlayerAdvancementsCallback event : callbacks) {
			event.onAdvancementComplete(player, advancement, criterionName);
		}
	});

	void onAdvancementComplete(ServerPlayer player, Advancement advancement, String criterionName);
}
