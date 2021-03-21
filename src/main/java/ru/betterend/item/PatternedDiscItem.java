package ru.betterend.item;

import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;
import ru.betterend.patterns.Patterned;
import ru.betterend.patterns.Patterns;

public class PatternedDiscItem extends MusicDiscItem implements Patterned {
	public PatternedDiscItem(int comparatorOutput, SoundEvent sound, Settings settings) {
		super(comparatorOutput, sound, settings);
	}

	@Override
	public String getModelPattern(String name) {
		return Patterns.createJson(Patterns.ITEM_GENERATED, name);
	}
}
