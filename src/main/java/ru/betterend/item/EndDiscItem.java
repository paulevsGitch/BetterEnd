package ru.betterend.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.RecordItem;
import ru.bclib.client.models.ItemModelProvider;

public class EndDiscItem extends RecordItem implements ItemModelProvider {
	public EndDiscItem(int comparatorOutput, SoundEvent sound, Properties settings) {
		super(comparatorOutput, sound, settings);
	}
}
