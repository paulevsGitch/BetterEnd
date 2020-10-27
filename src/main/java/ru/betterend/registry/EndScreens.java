package ru.betterend.registry;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import ru.betterend.client.gui.EndStoneSmelterScreen;
import ru.betterend.client.gui.EndStoneSmelterScreenHandler;

public class EndScreens {
	public static void register() {
		ScreenRegistry.register(EndStoneSmelterScreenHandler.HANDLER_TYPE, EndStoneSmelterScreen::new);
	}
}
