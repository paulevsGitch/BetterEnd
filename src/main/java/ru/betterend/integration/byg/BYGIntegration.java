package ru.betterend.integration.byg;

import ru.betterend.integration.ModIntegration;

public class BYGIntegration extends ModIntegration {
	public BYGIntegration() {
		super("byg");
	}

	@Override
	public void register() {
		BYGBiomes.register();
	}
}
