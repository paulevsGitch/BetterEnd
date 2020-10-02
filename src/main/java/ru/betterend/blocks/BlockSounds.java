package ru.betterend.blocks;

import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;

public class BlockSounds {
	public static final BlockSoundGroup TERRAIN_SOUND = new BlockSoundGroup(1.0F, 1.0F,
			SoundEvents.BLOCK_STONE_BREAK,
			SoundEvents.BLOCK_WART_BLOCK_STEP,
			SoundEvents.BLOCK_STONE_PLACE,
			SoundEvents.BLOCK_STONE_HIT,
			SoundEvents.BLOCK_STONE_FALL);
}
