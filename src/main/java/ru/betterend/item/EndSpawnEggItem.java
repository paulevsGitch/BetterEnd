package ru.betterend.item;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import ru.betterend.client.models.ItemModelProvider;
import ru.betterend.client.models.ModelsHelper;
import ru.betterend.client.models.Patterns;

import java.util.Optional;

public class EndSpawnEggItem extends SpawnEggItem implements ItemModelProvider {
	public EndSpawnEggItem(EntityType<?> type, int primaryColor, int secondaryColor, Properties settings) {
		super(type, primaryColor, secondaryColor, settings);
	}
	
	@Override
	public BlockModel getItemModel(ResourceLocation resourceLocation) {
		Optional<String> pattern = Patterns.createJson(Patterns.ITEM_SPAWN_EGG, resourceLocation.getPath());
		return ModelsHelper.fromPattern(pattern);
	}
}
