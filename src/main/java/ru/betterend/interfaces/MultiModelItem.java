package ru.betterend.interfaces;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndItems;

public interface MultiModelItem {
	@Environment(EnvType.CLIENT)
	void registerModelPredicate();
	
	static void register() {
		EndItems.getModItems(BetterEnd.MOD_ID).forEach(item -> {
			if (item instanceof MultiModelItem) {
				((MultiModelItem) item).registerModelPredicate();
			}
		});
	}
}
