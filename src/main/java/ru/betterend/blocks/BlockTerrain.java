package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;

public class BlockTerrain extends BlockBase {
	public static final BlockSoundGroup TERRAIN_SOUND = new BlockSoundGroup(1.0F, 1.0F,
			SoundEvents.BLOCK_STONE_BREAK,
			SoundEvents.BLOCK_WART_BLOCK_STEP,
			SoundEvents.BLOCK_STONE_PLACE,
			SoundEvents.BLOCK_STONE_HIT,
			SoundEvents.BLOCK_STONE_FALL);
	
	public BlockTerrain(MaterialColor color) {
		super(FabricBlockSettings.copyOf(Blocks.END_STONE).materialColor(color).sounds(TERRAIN_SOUND));
	}
}
