package ru.betterend.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import ru.betterend.blocks.basis.BlockBase;

public class EnderBlock extends BlockBase {

	public EnderBlock() {
		super(FabricBlockSettings.of(Material.STONE, MaterialColor.field_25708).hardness(5F).resistance(6F)
				.requiresTool().sounds(SoundType.STONE));
	}

	@Environment(EnvType.CLIENT)
	public int getColor(BlockState state, BlockView world, BlockPos pos) {
		return 0xFF005548;
	}
}
