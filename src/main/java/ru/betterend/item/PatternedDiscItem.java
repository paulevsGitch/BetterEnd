package ru.betterend.item;

import net.minecraft.world.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;
import ru.betterend.patterns.Patterned;
import ru.betterend.patterns.Patterns;

public class PatternedDiscItem extends MusicDiscItem implements Patterned {
	public PatternedDiscItem(int comparatorOutput, SoundEvent sound, Properties settings) {
		super(comparatorOutput, sound, settings);
	}

	@Override
	public String getModelPattern(String name) {
		return Patterns.createJson(Patterns.ITEM_GENERATED, name);
	}
}
