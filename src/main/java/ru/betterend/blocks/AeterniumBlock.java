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

public class AeterniumBlock extends BaseBlock {
	
	public AeterniumBlock() {
		super(FabricBlockSettings.of(Material.METAL, MaterialColor.COLOR_GRAY)
								 .hardness(65F)
								 .resistance(1200F)
								 .requiresCorrectToolForDrops()
								 .sound(SoundType.NETHERITE_BLOCK));
	}
	
	@Environment(EnvType.CLIENT)
	public int getColor(BlockState state, BlockGetter world, BlockPos pos) {
		return 0xFF657A7A;
	}
}
