package ru.betterend.blocks;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;

public class BlockSounds {
	public static final SoundType TERRAIN_SOUND = new SoundType(1.0F, 1.0F,
			SoundEvents.STONE_BREAK,
			SoundEvents.WART_BLOCK_STEP,
			SoundEvents.STONE_PLACE,
			SoundEvents.STONE_HIT,
			SoundEvents.STONE_FALL);
}
