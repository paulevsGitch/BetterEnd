package ru.betterend.item;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.RecordItem;
import ru.betterend.patterns.ModelProvider;
import ru.betterend.patterns.Patterns;

public class ModelProviderDiscItem extends RecordItem implements ModelProvider {
	public ModelProviderDiscItem(int comparatorOutput, SoundEvent sound, Properties settings) {
		super(comparatorOutput, sound, settings);
	}

	@Override
	public String getModelString(String name) {
		return Patterns.createJson(Patterns.ITEM_GENERATED, name);
	}
}
