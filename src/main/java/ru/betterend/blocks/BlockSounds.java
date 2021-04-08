package ru.betterend.blocks;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.sounds.SoundEvents;

public class BlockSounds {
	public static final SoundType TERRAIN_SOUND = new SoundType(1.0F, 1.0F, SoundEvents.BLOCK_STONE_BREAK,
			SoundEvents.BLOCK_WART_BLOCK_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT,
			SoundEvents.BLOCK_STONE_FALL);
}
