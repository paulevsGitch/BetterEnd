package ru.betterend.integration;

import java.lang.reflect.Method;

import ru.betterend.BetterEnd;

public class AdornIntegration extends ModIntegration {
	public AdornIntegration() {
		super("adorn");
	}

	@Override
	public void register() {
		Class<?> adornBlockBuilder = getClass("juuxel.adorn.api.block.AdornBlockBuilder");
		Class<?> blockVariantWood = getClass("juuxel.adorn.api.block.BlockVariant$Wood");
		Class<?> blockVariant = getClass("juuxel.adorn.api.block.BlockVariant");
		
		Object testVariant = newInstance(blockVariantWood, BetterEnd.MOD_ID + "/mossy_glowshroom");
		Method create = getMethod(adornBlockBuilder, "create", blockVariant);
		Object builder = executeMethod(adornBlockBuilder, create, testVariant);
		getAndExecuteRuntime(builder, "withEverything");
		getAndExecuteRuntime(builder, "registerIn", BetterEnd.MOD_ID);
	}

	@Override
	public void addBiomes() {}
}
