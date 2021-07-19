package ru.betterend.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluids;
import ru.bclib.interfaces.ItemModelProvider;
import ru.betterend.registry.EndItems;

public class EndBucketItem extends MobBucketItem implements ItemModelProvider {
	public EndBucketItem(EntityType<?> type) {
		super(type, Fluids.WATER, SoundEvents.BUCKET_EMPTY, EndItems.makeEndItemSettings().stacksTo(1));
	}
}
