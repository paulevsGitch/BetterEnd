package ru.betterend.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.FishBucketItem;
import net.minecraft.world.level.material.Fluids;
import ru.betterend.client.models.ItemModelProvider;
import ru.betterend.client.models.Patterns;
import ru.betterend.registry.EndItems;

import java.util.Optional;

public class EndBucketItem extends FishBucketItem implements ItemModelProvider {
	public EndBucketItem(EntityType<?> type) {
		super(type, Fluids.WATER, EndItems.makeItemSettings().stacksTo(1));
	}
}
