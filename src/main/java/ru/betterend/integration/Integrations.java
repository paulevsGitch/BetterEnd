package ru.betterend.integration;

import java.util.List;

import com.google.common.collect.Lists;

import ru.betterend.integration.byg.BYGIntegration;

public class Integrations {
	public static final List<ModIntegration> INTEGRATIONS = Lists.newArrayList();
	public static final ModIntegration BYG = register(new BYGIntegration());
	public static final ModIntegration NOURISH = register(new NourishIntegration());
	//public static final ModIntegration EXTRA_PIECES = register(new ExtraPiecesIntegration());
	//public static final ModIntegration ADORN = register(new AdornIntegration());
	
	public static void register() {
		INTEGRATIONS.forEach((integration) -> {
			if (integration.modIsInstalled()) {
				integration.register();
			}
		});
	}
	
	public static void addBiomes() {
		INTEGRATIONS.forEach((integration) -> {
			if (integration.modIsInstalled()) {
				integration.addBiomes();
			}
		});
	}
	
	private static ModIntegration register(ModIntegration integration) {
		INTEGRATIONS.add(integration);
		return integration;
	}
}
