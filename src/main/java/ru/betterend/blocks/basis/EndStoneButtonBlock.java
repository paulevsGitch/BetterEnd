package ru.betterend.blocks.basis;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;

public class EndStoneButtonBlock extends EndButtonBlock {

	public EndStoneButtonBlock(Block source) {
		super(source, FabricBlockSettings.copyOf(source).noOcclusion(), false);
	}

	@Override
	protected SoundEvent getSound(boolean clicked) {
		return clicked ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
	}
}
