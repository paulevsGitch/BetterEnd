package ru.betterend.item;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import ru.betterend.client.models.ModelProvider;
import ru.betterend.client.models.Patterns;

public class EndSpawnEggItem extends SpawnEggItem implements ModelProvider {
	public EndSpawnEggItem(EntityType<?> type, int primaryColor, int secondaryColor, Properties settings) {
		super(type, primaryColor, secondaryColor, settings);
	}
	
	@Override
	public String getModelString(String name) {
		return Patterns.createJson(Patterns.ITEM_SPAWN_EGG, name);
	}

	@Override
	public BlockModel getModel(ResourceLocation resourceLocation) {
		String pattern = Patterns.createJson(Patterns.ITEM_SPAWN_EGG, resourceLocation.getPath());
		return BlockModel.fromString(pattern);
	}
}
