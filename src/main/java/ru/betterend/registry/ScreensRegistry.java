package ru.betterend.registry;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import ru.betterend.client.gui.EndStoneSmelterScreen;
import ru.betterend.client.gui.EndStoneSmelterScreenHandler;

public class ScreensRegistry {
	public static void register() {
		ScreenRegistry.register(EndStoneSmelterScreenHandler.HANDLER_TYPE, EndStoneSmelterScreen::new);
	}
}
