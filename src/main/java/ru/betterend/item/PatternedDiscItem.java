package ru.betterend.item;

import net.minecraft.world.item.Item;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.RecordItem;
import ru.betterend.patterns.Patterned;
import ru.betterend.patterns.Patterns;

public class PatternedDiscItem extends RecordItem implements Patterned {
	public PatternedDiscItem(int comparatorOutput, SoundEvent sound, Item.Properties settings) {
		super(comparatorOutput, sound, settings);
	}

	@Override
	public String getModelPattern(String name) {
		return Patterns.createJson(Patterns.ITEM_GENERATED, name);
	}
}
