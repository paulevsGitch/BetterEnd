package ru.betterend.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import ru.bclib.blocks.BaseBlock;

public class EnderBlock extends BaseBlock {
	
	public EnderBlock() {
		super(FabricBlockSettings.of(Material.STONE, MaterialColor.WARPED_WART_BLOCK)
								 .hardness(5F)
								 .resistance(6F)
								 .requiresCorrectToolForDrops()
								 .sound(SoundType.STONE));
	}
	
	@Environment(EnvType.CLIENT)
	public int getColor(BlockState state, BlockGetter world, BlockPos pos) {
		return 0xFF005548;
	}
}
