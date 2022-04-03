package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.interfaces.tools.AddMineableAxe;

public class HydraluxPetalBlock extends BaseBlock implements AddMineableAxe {
	public HydraluxPetalBlock() {
		this(
			FabricBlockSettings
				.of(Material.PLANT)
				.hardness(1)
				.resistance(1)
				.mapColor(MaterialColor.PODZOL)
				.sound(SoundType.WART_BLOCK)
		);
	}
	
	public HydraluxPetalBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public void fallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float f) {
	}
}
