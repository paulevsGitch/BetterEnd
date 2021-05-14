package ru.betterend.item;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.world.item.Item;
import ru.betterend.patterns.ModelProvider;
import ru.betterend.patterns.Patterns;

public class ModelProviderItem extends Item implements ModelProvider {
	public ModelProviderItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public String getModelString(String name) {
		return Patterns.createItemGenerated(name);
	}

	@Override
	public BlockModel getModel() {
		return null;
	}
}
